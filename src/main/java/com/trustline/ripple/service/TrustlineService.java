package com.trustline.ripple.service;

import java.io.IOException;
import java.util.Date;
import java.util.Stack;
import java.util.UUID;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.trustline.ripple.config.Configuration;
import com.trustline.ripple.dto.MoneyTransfer;
import com.trustline.ripple.dto.Payment;
import com.trustline.ripple.util.RestClient;

@Service
public class TrustlineService {
	
	@Autowired
	Configuration configuration;
	
	@Autowired
	RestClient restClient;
	
	private Stack<Payment> ledger = new Stack<Payment>();

	private static String unprocessedPayment = ": payment is not processed";
	
	ObjectMapper mapper = new ObjectMapper();
	
	public static final String PAYMENT = "/pay/{payment}";
	private static final String BALANCE = "/balance";
	private static final String HISTORY = "/history";
	private static final String DEPOSIT = "/deposit";
	private static final String NAME = "/name";
	
	private static final Logger log = LoggerFactory.getLogger(TrustlineService.class);

	public String pay(double payment) throws JsonParseException, JsonMappingException, IOException {
		UUID confirmationNumber = UUID.randomUUID();

		// deposit payment to partner

		MoneyTransfer moneyTransfer = new MoneyTransfer(payment, confirmationNumber.toString(), new Date());
		log.info("Paying " + payment + " to " + configuration.getPartnerName());
		ResponseEntity<String> response = restClient.callPost(configuration.getPartnerUrl() + DEPOSIT,
				mapper.writeValueAsString(moneyTransfer));
		String responseMessage;
		if (response.getStatusCode() == HttpStatus.OK) {
			addPaymentToLedger(-payment, confirmationNumber.toString(), response.getBody());
			log.info("Paid");
			responseMessage = confirmationNumber.toString();
			log.info(configuration.getMyName() + ", your Trustline balance is: " + getLastBalance());
		} else
			responseMessage = response.getStatusCode() + unprocessedPayment;

		return responseMessage;
	}

	private void addPaymentToLedger(double payment, String confirmationNumber, String confirmationNumberBeneficiar) {
		ledger.add(new Payment(confirmationNumber, confirmationNumberBeneficiar, payment, getLastBalance() + payment, new Date()));
	}

	private double getLastBalance() {
		double balance = ledger.isEmpty() ? 0.0 : ledger.peek().getBalance();
		return balance;
	}
	
	/**
	 * Deposit payment from partner
	 */
	public String deposit(MoneyTransfer moneyTransfer) throws JsonParseException, JsonMappingException, IOException {
		UUID confirmationNumber = UUID.randomUUID();
		addPaymentToLedger(moneyTransfer.getPayment(), confirmationNumber.toString(), moneyTransfer.getConfirmationNumber());
		log.info(configuration.getMyName() + ", you were paid  " + moneyTransfer.getPayment());
		log.info(configuration.getMyName() + ", your Trustline balance is: " + getLastBalance());
		return confirmationNumber.toString();
	}
	
	/**
	 * user balance
	 */
	public double balance() throws JsonParseException, JsonMappingException, IOException {
		return getLastBalance();
	}
	
	/**
	 * ledger all payment
	 */
	public Stack<Payment> statment() throws JsonParseException, JsonMappingException, IOException {
		return ledger;
	}
	
	/**
	 * user Name 
	 */
	public String myName() throws JsonParseException, JsonMappingException, IOException {
		String s = configuration.getMyName() ;
		return "My name is " + s;
	}
}
