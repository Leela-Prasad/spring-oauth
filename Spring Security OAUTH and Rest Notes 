Oauth is a standard defined by RFC and it defines a kind of framework to enable one web service to share data to another web service or websites in a secure way.

Oauth Versions:
Oath 1.0 - introduced in 2010
Oauth 2.0 - introduced in 2012

In Oauth 1 the messages that are sent are encrypted using a signing mechanism and its developers responsibility to encrypt this messages. Because of this oauth 1.0 is difficult to implement

In Oauth 2 we don’t have signing mechanism and we will be sending messages from one system to another in clear text, we will encrypt the traffic using SSL/TLS, so if you are using Oauth2 you should use TLS.

Here as a case study we are having 2 system 
1. CRM system 
2. Mail Monkey - a mailing system for the new letters for CRM users
To send the new letters we need to get users email ids from CRM system this is where we are using oauth to import user email list.

Goal of the case study:
we will allow Resource provider(CRM system) to give access to set of resources.
The client application will not use username and password.

Oauth Actors:
1. Resource Provider (CRM System) - because it is providing rest resource to import user email list.
Resource provider is an application(usually a web service) that is storing a shared data.

2. client application (Mail Monkey System) - 
Client application is an application that is requesting for the shared data.

3. Resource owner:
It is the person who actually owns the data on the resource provider.
They will have login to both client application and resource provider.
example: email list rest resource is actually protected by username/password so the owner of that credentials is Resource owner, it might be system generic credentials or user credentials(in case of Facebook,twitter applications).

4. Access Tokens:
It is access tokens of oauth users to allow client application(Mail Monkey) to access resources on the resource(rest contact list resource) provider(CRM system).

5. Grant Types(Flow):
It is a series of operations/messages that are sent from resource owner to client application and from the client application to the resource provider.
This series of operations should be happen in a certain order which is called Grant type.

Oauth has different Grant types(flows) to support different types devices and different types of requirements you project have.

Full Oauth process is called as Authorization code grant type.
In Authorization code grant type the resource provider wants to know
a) is the user a genuine resource owner?
b) what resources does the resource owner want to expose to the client application?
c) is the client application valid - are they who they say they are?

These 3 steps we will call as Authorization legs
Leg1:
1. In leg1 user will log into the client aplication(mail monkey) and click that import users button then the mail monkey will make a request to CRM system to start the oauth process.
GET /authorize?client_id=MailMonkey&redirect_uri=/mailmonkey/import

2. Now the CRM system will send its login page to the actual user once they are logged in correctly then the Resource Provider confirms user who they say they are

3. Now after authentication, CRM system sends another request which have information about the resource that client application(Mail Monkey) is requesting.
like “The application Mail Monkey wants to Read your customers Yes or no?”
If the user send Yes then CRM system will send a redirect request to Mail Monkey system

HTTP 302 Redirect: /mailmonkey/import?code=A2F4

The code in the url is not the access token it is the “Authorisation code”
This code signifies that leg1 is completed and it unlocks leg2.

Leg2:
To verify the client application
it needs to provide
client Id : mailmonkey
client secret : somesecretkey



examples:
CRM registered clients
Mailmonkey[somesecretkey]
Microsoft Outlook[sdfhpi3urhf]
Facebook[kdjfh873posk[-]
Linkedin[akufhvalkj21398ul]

This client id and client secret has to generated on the CRM system and stores on the mail monkey site so that they can make request in leg2 using these details.

In Leg2 mail monkey will make a rest call with client id and client secret to verify client who they say they are 
POST /token?clientid=mailmonkey&clientsecret=somesecretkey&code=A2F4

Then CRM will validate client id and client secret and send the access token to access the shared resource.

Leg3:
In leg3 client application(mail monkey) will send a request to shared resource with access token 
GET /rest/customers?access_token=asifhoqsj7

now the mail monkey will get the details and it will served to the actual user.
** for the next requests leg1 and leg2 will be skipped and leg3 will be executed until access_token is valid
Generally we should make access_token to be valid for a short duration like 1 hour so that even though it is comprised we don’t have a risk.


Code changes for every Leg


Leg1:
In Leg1 we have validate Resource owner(i.e., actual user) and get approval for the resources that he is giving access to Client application(i.e., mail monkey)

crm-security-config.xml

<!-- This block is to setup authorizaiton and token endpoints i.e., /oauth/authorize  and  /oauth/token -->
<oauth2:authorization-server client-details-service-ref="clientDetailsService">
	<!-- This is to tell that we are doing Oauth full Authentication Grant type -->
	<oauth2:authorization-code/>
</oauth2:authorization-server>  

<!-- This block is responsible for storing list of registered clients who want to do oauth authentication full Grant type for veryfing client what they are saying. This block is an inmemory store for development puropses, we can use a jdbc store where all the registered clients are stored, for this we have to use JdbcClientDetailsService-->

<!-- This is something similar to the authentication-manager where users who are doing are authenticated -->
<oauth2:client-details-service id="clientDetailsService">
	<oauth2:client client-id="mailmonkey"
 		secret="somesecretkey"
 		authorities="ROLE_CLIENT"
 		scope="read"
 		authorized-grant-types="authorization_code"/>
</oauth2:client-details-service>

To authenticate against datastore:
<bean id="clientDetailsUserService" class="org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService">
	<constructor-arg ref="myClientDetails" />
</bean> 

<bean class="org.springframework.security.oauth2.provider.client.JdbcClientDetailsService" id="myClientDetails">
	<constructor-arg index="0">
         	<!— This is your jdbc datasource, i.e. db details —>
		<ref bean="dataSource" />
	</constructor-arg>
</bean>

<http pattern="/**">
	<!-- *** Here we have secure /oauth/authorize so that it will produce a login to the resource owner -->	
	<intercept-url pattern="/oauth/**" access="hasRole('ROLE_CRM_USER')"/>
</http>

Mail Monkey - newLetter.jsp
<form action="http://localhost:8080/crm/oauth/authorize" >
	<p>Client Id<input type="text" name="client_id" value="mailmonkey" /></p>
	<p>Redirect URI<input type="text" name="redirect_uri" value="http://localhost:8080/mailmonkey/import.html" /></p>
	<!-- In response_type="code"  code represents full OAuth Authentication Grant Type-->
	<p>Authorization Grant type <input type="text" name="response_type" value="code" /></p>
	<p>Scope<input type="text" name="scope" value="read" />
	<p><input type="Submit" /></p>
</form>


Leg2:
crm-security-config.xml
<http pattern="/oauth/token" create-session="stateless" authentication-manager-ref="oauthTokenEndpointAuthManager">
	<intercept-url pattern="/oauth/token" access="hasRole('ROLE_CLIENT')" />
	<http-basic/>

	<!-- oauth is not working if we don't disable csrf, which is ok as rest endpoints doesn't need csrf because csrf is required if we want session but rest is stateless, where we will send credentials for every request-->
	<csrf disabled="true"/>
</http>

<authentication-manager id="oauthTokenEndpointAuthManager" >
	<authentication-provider user-service-ref="clientDetailsUserService"></authentication-provider>
</authentication-manager>

<beans:bean id="clientDetailsUserService" class="org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService">
	<beans:constructor-arg ref="clientDetailsService" />
</beans:bean>


NewsLetterController.java
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
			+"&redirect_uri=http://localhost:8080/mailmonkey/import.html"  
			+"&grant_type=authorization_code";

	ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, entity, String.class);
	System.out.println(response.getBody());
	return null;
	}


Leg3:

<http pattern="/rest/customers" create-session="stateless" entry-point-ref="oauthTokenEntry">
<!--  Here the access must be one who have oauth access token so the we have validate based on the scope, here we have used spring expression language #oauth2, inorder to enable this SEL we have wire another bean called expression handler-->
	<intercept-url pattern="/rest/customers" access="#oauth2.hasScope('read')"/>

<!-- By default oauth filter is not added in the default filter chain so we need to add this manually by referencing resource-server where existing tokens are avialable and this filter must be added in a specific position in the filter chain i.e., before PRE_AUTH_FILTER-->
	
	<custom-filter ref="oauthFilter" before="PRE_AUTH_FILTER"/>

	<expression-handler ref="expressionHandler"/>

<!-- Here we need to enable token authentication using oauth so we have to disable http basic entry point and plug in token authentication entry point, unfortunately we dont have any tag to plug in but we can write a bean to this-->
<!-- <http-basic /> -->
</http>

<!-- This bean is used to disable default http basic and enable token authentication -->
<beans:bean id="oauthTokenEntry" class="org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint"/>

<!-- By default oauth filter is not added in the default filter chain so we need to add this manually this oauth2:resource-server filter will autheticate tokens with the existing tokens in the memory. -->
<oauth2:resource-server id="oauthFilter"/>

<!--  This bean is used to enable Spring Expression Language i.e., #oauth2-->
<beans:bean id="expressionHandler" class="org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler" />


NewsletterController.java
public ModelAndView firstVersion(@RequestParam String code) throws JsonParseException, JsonMappingException, IOException {
	ObjectMapper mapper = new ObjectMapper();
	Map<String,String> oauthTokenMap = mapper.readValue(response.getBody(),Map.class);
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




OauthRestTemplate for leg2 and leg3:

mail-monkey-security-config.xml:

we have to define a oauth rest template bean with the configuration to which endpoints it need to invoke.
<oauth2:rest-template id="oauthTemplate" resource="crmResource"></oauth2:rest-template>

<!— This will make /oauth/token post request —>
<oauth2:resource id="crmResource" client-id="mailmonkey"
				client-secret="somesecretkey"
				type="authorization_code"
	user-authorization-uri="http://localhost:8080/crm/oauth/authorize"
	access-token-uri="http://localhost:8080/crm/oauth/token"
	pre-established-redirect-uri="http://localhost:8080/mailmonkey/import.html"
	/>

Here we need to define user-authorization-uri as we are overriding the default way of oauth invoke.

NewsletterController.java
CustomerCollectionRepresentation customers = oauthTemplate.getForObject("http://localhost:8080/crm/rest/customers", CustomerCollectionRepresentation.class);
		return new ModelAndView("/importedContacts.jsp","customers",customers.getCustomers());
					 

Oauth configuration to automatically do leg1:

<http pattern=“/**”>
<!-- This filter will kick in when an exception is thrown that client app mail monkey does have leg1 code to kick in leg2, so this filter will do leg1 process automatically and avoides a get request to /oauth/authorize manually  and this will also make redirect uri to be redundent as it will redirect to the same page where client is in-->	
	<custom-filter ref="leg1Filter" after="EXCEPTION_TRANSLATION_FILTER"/>		
</http>

<!-- This will avoid a get request to the /oauth/authorize request -->
<oauth2:client id="leg1Filter"/>	




Other Grant types:
1. Implicit Grant type.
In this Grant Type we will skip leg2(i.e., client will not verified)
leg1 url will be
/authorize?response_type=“token”&client_id=“MailMonkey”&redirect_uri=/mailMonkey/import
Here response_type is “token”

as a result the CRM system will return with redirect
HTTP 302 Redirect :/mailmonkey/import?token={token}

Disadvantages:
Hackers can try with user credentials directly
token is exposed in the url.


2. Client Credentials:
In this Grant type we will skip leg1(i.e., resource owner will not be verified)



SSL /TLS in OAuth:
1. All intercept-url tags should be added with a attribute requires-channel="https"
2. Add a port mapping if you are using non standard ports (i.e., http(8080), https(8443)) in spring security config file.
In mail monkey project 
<port-mappings>
	<port-mapping http="8180" https="8543"/>
</port-mappings>

3. Generate keystone file and place that tomcat home directory
   keytool -genkey -alias mm -keyalg RSA -keystore mm.keystore 

4. Uncomment TLS Configuration.
<Connector port="8543" protocol="HTTP/1.1" SSLEnabled="true"
		maxThreads="150" scheme="https" secure="true"
		clientAuth="false" sslProtocol="TLS"
		keystoreFile="mm.keystore" keystorePass="password"/>

5. Because of tomcat configuration browser will automatically redirect from 
a) 8080 to 8443
b) 8180 to 8543
but rest endpoints will not do that so we need to change the protocol (from http to https) and port from (8080 to 8443)

6. After this rest endpoints will be invoked with https protocol/scheme 
If we are doing a call to the url in https if it is a certificate signed by CA then browser will show the green padlock else it will be in red with a strike out and ask us whether to proceed to this unprotected site.(here it means browser doesn’t know who signed this certificate but the transmission will be done in encryption)

similarly when rest call is happened to https endpoint it will invoke the target url if server is running with a valid certificate signed by CA, else it will throw an exception complaining about the certificate which is not trusted.
Error:
javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target

To avoid this error we have to build a trust store for CRM site

keystone (i.e., crm.keystore) will contain both public and private key so this confidential
To build a truststore we have to take out public key from keystore file.


Generating Truststore
Generate certificate file(.cer) either from
1. Browser
2. command line
   keytool -export -alias crm -keystore crm.keystore > crm_publickey_certificate.cer

Now generate the trust store with this public certificate
Note: truststore will contain only the public key of the target system.

keytool -import -alias crm -file crm_publickey_certificate.cer -keystore client.keystore



You can very well see the contents of the .keystore files with below commands
keytool -list -keystore crm.keystore

Leelas-Air:tomcat Leela$ keytool -list -keystore crm.keystore 
Enter keystore password:  
Keystore type: jks
Keystore provider: SUN

Your keystore contains 1 entry
crm, 6 Feb, 2019, PrivateKeyEntry, 

Certificate fingerprint (SHA1): 05:22:1E:BC:5D:E5:60:6F:26:99:3A:CA:17:B7:EB:B1:3A:B6:F4:D8

Here fingerprint is CA signed private key.


contents of newly created trust store will be 
keytool -list -keystore client.keystore 

Enter keystore password:  
Keystore type: jks
Keystore provider: SUN

Your keystore contains 1 entry

crm, 6 Feb, 2019, trustedCertEntry, 
Certificate fingerprint (SHA1): 05:22:1E:BC:5D:E5:60:6F:26:99:3A:CA:17:B7:EB:B1:3A:B6:F4:D8

Truststore will contain public certificate of target system
and it is used to tell virtual machine to trust this certificate.
that is the reason it is called as trust store.

7. If you are running in linux then set the below parameter as a VM argument.
   javax.net.ssl.trustStore=/Users/Leela/Desktop/spring-oauth/REST-Client/client.keystore

for this we have to create setenv.sh in tomcat_home/bin folder
and add below line
CATALINA_OPTS="${CATALINA_OPTS} -Djavax.net.ssl.trustStore=/Users/Leela/Desktop/spring-oauth/REST-Client/client.keystore"


***** This truststore is only needed when your target system certificate is not signed by CA.(i.e., self signed)
