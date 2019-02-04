package com.virtualpairprogrammers.webcontrollers;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualpairprogrammers.rest.representations.CustomerClientVersion;
import com.virtualpairprogrammers.rest.representations.CustomerCollectionRepresentation;

@Controller
public class NewsletterController 
{	
	@Autowired
	private OAuth2RestTemplate oauthTemplate;
	
	@RequestMapping("/build-newsletter")
	public ModelAndView displayAllCustomersOnWebPage()
	{
		return new ModelAndView("/newsletter.jsp");
	}
	
	@RequestMapping("/importFromCRM.html")
	public ModelAndView secondVersion() {
		CustomerCollectionRepresentation customers = oauthTemplate.getForObject("http://localhost:8080/crm/rest/customers", CustomerCollectionRepresentation.class);
		return new ModelAndView("/importedContacts.jsp","customers",customers.getCustomers());
	}
	
	@RequestMapping(value="/exportToCRM.html", method=RequestMethod.GET)
	public ModelAndView show()
	{
		CustomerClientVersion newCustomer = new CustomerClientVersion();
		newCustomer.setCompanyName("Virtual Pair Programmers");
		newCustomer.setCustomerId("VPP-1001");
		newCustomer.setEmail("richard@capabilitytm.com");
		newCustomer.setTelephone("6323003");
		newCustomer.setNotes("This has been prepopulated, for convenience!");
		return new ModelAndView("/exportForm.jsp", "customer", newCustomer);
	}	

	@RequestMapping(value="/exportToCRM.html", method=RequestMethod.POST)
	public ModelAndView processForm(@Valid CustomerClientVersion customer, Errors result)
	{
		if (result.hasErrors())
		{
			return new ModelAndView("/exportForm.jsp", "customer", customer);
		}
		String url = "http://localhost:8080/crm/rest/customers";
		
		// call the external CRM system and then redirect to show all contacts page.
		oauthTemplate.postForLocation(url, customer);
		return new ModelAndView("redirect:/importFromCRM.html");
	}

	
	
	//@RequestMapping("/import.html")
	public ModelAndView firstVersion(@RequestParam String code) throws JsonParseException, JsonMappingException, IOException {
		RestTemplate template = new RestTemplate();
		
		String credentials = "mailmonkey:somesecretkey";
		String encodedCredentials = Base64.encodeBase64String(credentials.getBytes());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		HttpEntity entity = new HttpEntity<>(headers);
		
		//It is recommended to pass authorization code which unlocks
		//leg2 in headers instead of headers so when the ssl url 
		//decrypts at server side it will not be visible in the url.
		String url ="http://localhost:8080/crm/oauth/token"
				    + "?code=" + code
				    +"&redirect_uri=http://localhost:8080/mailmonkey/import.html"  //not sure why we need to give redirect uri
				    +"&grant_type=authorization_code";
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, entity, String.class);

		System.out.println(response.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String,String> oauthTokenMap = mapper.readValue(response.getBody(), Map.class);
		String oauthToken = oauthTokenMap.get("access_token");
		
		// It is always recommended to pass access_token in headers so that 
		// it will not be visible in the url when ssl url decrypts at service side
		// so that hacker will not see the access_token
		/*String urlleg3 = "http://localhost:8080/crm/rest/customers"
				         + "?access_token=" + oauthToken;

		CustomerCollectionRepresentation customers = template.getForObject(urlleg3, CustomerCollectionRepresentation.class);
		return new ModelAndView("/importedContacts.jsp","customers",customers.getCustomers());
		*/
		HttpHeaders headersleg3 = new HttpHeaders();
		headersleg3.add("Authorization", "Bearer " + oauthToken);
		
		HttpEntity entityleg3 = new HttpEntity<>(headersleg3);
		
		String urlleg3 = "http://localhost:8080/crm/rest/customers";
		ResponseEntity<CustomerCollectionRepresentation> customers = template.exchange(urlleg3, HttpMethod.GET, entityleg3, CustomerCollectionRepresentation.class);
		
		return new ModelAndView("/importedContacts.jsp","customers",customers.getBody().getCustomers());
	}
}
