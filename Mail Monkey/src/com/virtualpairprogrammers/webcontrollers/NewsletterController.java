package com.virtualpairprogrammers.webcontrollers;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewsletterController 
{	
	@RequestMapping("/build-newsletter")
	public ModelAndView displayAllCustomersOnWebPage()
	{
		return new ModelAndView("/newsletter.jsp");
	}
	
	@RequestMapping("/import.html")
	public ModelAndView firstVersion(@RequestParam String code) {
		RestTemplate template = new RestTemplate();
		
		String credentials = "mailmonkey:somesecretkey";
		String encodedCredentials = Base64.encodeBase64String(credentials.getBytes());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		HttpEntity entity = new HttpEntity<>(headers);
		
		String url ="http://localhost:8080/crm/oauth/token"
				    + "?code=" + code
				    +"&redirect_uri=http://localhost:8080/mailmonkey/import.html"  //not sure why we need to give redirect uri
				    +"&grant_type=authorization_code";
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, entity, String.class);
		System.out.println(response.getBody());
		return null;
	}
}
