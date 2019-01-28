package main;
import java.io.IOException;

import org.springframework.hateoas.Link;
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

		CustomerCollectionRepresentation allCustomers = template.getForObject("http://localhost:8080/crm/rest/customers?first=1&last=2", CustomerCollectionRepresentation.class);
		
		Link link = allCustomers.getLink("next");
		System.out.println("the next page will be at " + link);
				
		System.out.println(allCustomers);
	}
}
