package com.trustline.ripple.unit.test;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.trustline.ripple.config.Configuration;
import com.trustline.ripple.dto.MoneyTransfer;
import com.trustline.ripple.service.TrustlineService;
import com.trustline.ripple.util.RestClient;

@Test
public class TrustLineApplicationTests {

	@Mock
	private RestClient restClient;

	private TrustlineService trustlineService = new TrustlineService();

	@Mock
	private Configuration configuration;
	
	@BeforeMethod
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		
		ReflectionTestUtils.setField(trustlineService, "restClient", restClient);
		ReflectionTestUtils.setField(trustlineService, "configuration", configuration);
	}

	@Test
	public void contextLoads() {
	}

	@Test(priority = 1)
	public void testPay() throws JsonParseException, JsonMappingException, IOException {

		ResponseEntity<String> responseEntity = new ResponseEntity<String>("1111", HttpStatus.OK);

		when(restClient.callPost(Matchers.anyString(), Matchers.anyString())).thenReturn(responseEntity);
		when(configuration.getMyName()).thenReturn("Alice");
		when(configuration.getPartnerName()).thenReturn("Bob");

		String msg = trustlineService.pay(10.0);
		Assert.assertNotEquals(msg, null);
		Assert.assertEquals(trustlineService.balance(), -10.0);
	}

	@Test(priority = 2)
	public void testDeposit() throws JsonParseException, JsonMappingException, IOException {
		MoneyTransfer moneyTransfer = new MoneyTransfer(20.0, "1111", new Date());

		when(configuration.getMyName()).thenReturn("Alice");

		String msg = trustlineService.deposit(moneyTransfer);
		
		Assert.assertNotEquals(msg, null);
		Assert.assertEquals(trustlineService.balance(), 10.0);
	}


}
