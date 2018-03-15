package com.trustline.ripple.web;

import java.io.IOException;
import java.util.Date;
import java.util.Stack;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.trustline.ripple.config.Configuration;
import com.trustline.ripple.dto.MoneyTransfer;
import com.trustline.ripple.dto.Payment;
import com.trustline.ripple.service.TrustlineService;
import com.trustline.ripple.util.RestClient;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin()
public class TrustlineRestContorller {

	@Autowired
	Configuration configuration;
	
	@Autowired
	TrustlineService trustlineService;
	
	private static final Logger log = LoggerFactory.getLogger(TrustlineRestContorller.class);
	
	public static final String PAYMENT = "/pay/{payment}";
	private static final String BALANCE = "/balance";
	private static final String HISTORY = "/history";
	private static final String DEPOSIT = "/deposit";
	private static final String NAME = "/name";
	
	@PostConstruct
	private void init() throws JsonParseException, JsonMappingException, IOException{
		log.info("Welcome to the Trustline");
		log.info(configuration.getMyName() + ", your Trustline balance is: " + trustlineService.balance());
	}

	/**
	 * Transfer payment to partner
	 */
	@RequestMapping(value = PAYMENT, method = RequestMethod.POST)
	@ApiOperation(value = "Pay the Partner", notes = "Pay the partner.")
	public String payment(@PathVariable double payment) throws JsonParseException, JsonMappingException, IOException {
		String responseMessage = trustlineService.pay(payment);

		return responseMessage;
	}

	/**
	 * Deposit payment from partner
	 */
	@RequestMapping(value = DEPOSIT, method = RequestMethod.POST)
	@ApiOperation(value = "Deposit from Partner", notes = "Deposit from partner.")
	public String deposit(@RequestBody MoneyTransfer moneyTransfer) throws JsonParseException, JsonMappingException, IOException {
		return trustlineService.deposit(moneyTransfer);
	}

	/**
	 * your balance
	 */
	@RequestMapping(value = BALANCE, method = RequestMethod.GET)
	@ApiOperation(value = "Get Balance", notes = "Get Balance")
	public double balance() throws JsonParseException, JsonMappingException, IOException {
		return trustlineService.balance();
	}
	
	/**
	 * ledger all payment
	 */
	@RequestMapping(value = HISTORY, method = RequestMethod.GET)
	@ApiOperation(value = "Get Ledger Statement", notes = "Get Ledger Statement")
	public Stack<Payment> statment() throws JsonParseException, JsonMappingException, IOException {
		return trustlineService.statment();
	}
	
	/**
	 * provide my name 
	 */
	@RequestMapping(value = NAME, method = RequestMethod.GET)
	@ApiOperation(value = "Get My Name", notes = "Get My Name")
	public String myName() throws JsonParseException, JsonMappingException, IOException {
		String s = trustlineService.myName() ;
		return "My name is " + s;
	}
	
}