package com.trustline.ripple.unit.test;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;
import java.util.Stack;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import com.trustline.ripple.config.Configuration;
import com.trustline.ripple.dto.MoneyTransfer;
import com.trustline.ripple.dto.Payment;
import com.trustline.ripple.service.TrustlineService;
import com.trustline.ripple.util.RestClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrustLineApplicationTests {

	@Mock
	private RestClient restClient;

	@Mock
	private TrustlineService trustlineService;

	@Mock
	private Configuration configuration;
	
	@BeforeMethod
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		
		ReflectionTestUtils.setField(trustlineService, "restClient", restClient);
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testPay() throws JsonParseException, JsonMappingException, IOException {

		ResponseEntity<String> responseEntity = new ResponseEntity<String>("1111", HttpStatus.OK);

		when(restClient.callPost(Matchers.anyString(), Matchers.anyString())).thenReturn(responseEntity);
		when(configuration.getMyName()).thenReturn("Alice");

		trustlineService.pay(10.0);
	}

	@Test
	public void testDeposit() throws JsonParseException, JsonMappingException, IOException {
		MoneyTransfer moneyTransfer = new MoneyTransfer(10.0, "1111", new Date());

		when(configuration.getMyName()).thenReturn("Alice");

		trustlineService.deposit(moneyTransfer);
	}

	@Test
	public void testBalance() throws JsonParseException, JsonMappingException, IOException {

		Double resp = trustlineService.balance();

		Assert.assertEquals(resp, 0.0);
	}

}
