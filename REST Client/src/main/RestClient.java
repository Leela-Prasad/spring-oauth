package main;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import resources.CustomerCollectionRepresentation;


public class RestClient 
{
	public static void main(String[] args) throws IOException
	{
		/*RestTemplate template = new RestTemplate();
		
		String credentials = "trusted:trustedsecret";
		String encodedCredentials = Base64.encodeBase64String(credentials.getBytes());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "client_credentials");
		
		HttpEntity entity = new HttpEntity<>(params,headers);
		String url ="http://localhost:8080/crm/oauth/token";
		
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, entity, String.class);

		System.out.println(response.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String,String> oauthTokenMap = mapper.readValue(response.getBody(), Map.class);
		String oauthToken = oauthTokenMap.get("access_token");
		
		HttpHeaders headersleg3 = new HttpHeaders();
		headersleg3.add("Authorization", "Bearer " + oauthToken);
		
		HttpEntity entityleg3 = new HttpEntity<>(headersleg3);
		
		String urlleg3 = "http://localhost:8080/crm/rest/customers";
		ResponseEntity<CustomerCollectionRepresentation> customers = template.exchange(urlleg3, HttpMethod.GET, entityleg3, CustomerCollectionRepresentation.class);
		System.out.println(customers.getBody());*/
		
		
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setClientId("trusted");
		resource.setClientSecret("trustedsecret");
		resource.setGrantType("client_credentials");
		resource.setAccessTokenUri("http://localhost:8080/crm/oauth/token");
		OAuth2RestTemplate oauthTemplate = new OAuth2RestTemplate(resource);
		
		CustomerCollectionRepresentation customers = oauthTemplate.getForObject("http://localhost:8080/crm/rest/customers", CustomerCollectionRepresentation.class);
		System.out.println(customers);
		
	}
}
