package main;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import exceptions.CustomExceptionHandler;
import resources.CustomerCollectionRepresentation;


public class RestClient 
{
	public static void main(String[] args) throws IOException
	{
		RestTemplate template = new RestTemplate();		
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		template.setErrorHandler(new CustomExceptionHandler(template));

		String originalUserPass = "rac:secret";
		String encodedUserPass = new String(Base64.encodeBase64(originalUserPass.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedUserPass);
		HttpEntity<CustomerCollectionRepresentation> entity = new HttpEntity<>(headers);
		
		//CustomerCollectionRepresentation allCustomers = template.getForObject("http://localhost:8080/crm/rest/customers?first=1&last=2", CustomerCollectionRepresentation.class);
		 
		ResponseEntity<CustomerCollectionRepresentation> responseEntity = template.exchange("http://localhost:8080/crm/rest/customers?first=1&last=2", HttpMethod.GET, entity, CustomerCollectionRepresentation.class);
		CustomerCollectionRepresentation allCustomers = responseEntity.getBody();
		
		Link link = allCustomers.getLink("next");
		System.out.println("the next page will be at " + link);
				
		System.out.println(allCustomers);
		
		
		//second request
		System.out.println("Second Request :" + template.getForObject("http://localhost:8080/crm/rest/customers?first=1&last=2", CustomerCollectionRepresentation.class));
	}
}
