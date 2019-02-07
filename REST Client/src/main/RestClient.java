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
		
		// This property is used to tell java virtual machine to trust the certificate.
		// This is only required if the certificate is self signed, if it is signed by CA
		// then rest endpoint will automatically invoke
		System.setProperty("javax.net.ssl.trustStore", "./client.keystore");
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setClientId("trusted");
		resource.setClientSecret("trustedsecret");
		resource.setGrantType("client_credentials");
		resource.setAccessTokenUri("https://localhost:8443/crm/oauth/token");
		OAuth2RestTemplate oauthTemplate = new OAuth2RestTemplate(resource);
		
		CustomerCollectionRepresentation customers = oauthTemplate.getForObject("https://localhost:8443/crm/rest/customers", CustomerCollectionRepresentation.class);
		System.out.println(customers);
		
	}
}
