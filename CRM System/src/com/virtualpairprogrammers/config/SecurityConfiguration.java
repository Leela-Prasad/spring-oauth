package com.virtualpairprogrammers.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class SecurityConfiguration {
	
	@Configuration
	/*This EnableWebSecurity will gives some defaults(like xml namespaces) which is not given in xml version
	  i.e., autowiring beans by default like approvalstore,oauthtokenentrypoint, expressionhandler etc.*/
	@EnableWebSecurity
	public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		public void configure(WebSecurity web) {
			//<http pattern="/css/**" security="none" />
			web.ignoring()
				.antMatchers("/css/**");
		}
		
		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			/*<authentication-manager>
				<authentication-provider>
					<user-service>			
						<user name="rac" password="secret" authorities="ROLE_CRM_USER"/>
					</user-service>
				</authentication-provider>
			</authentication-manager>*/
			auth.inMemoryAuthentication()
					.withUser("rac")
					.password("secret")
					.roles("CRM_USER");
		}
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			/*<http pattern="/**">
				<intercept-url pattern="/login.jsp" access="permitAll()"/>
				<intercept-url pattern="/login" access="permitAll()"/>
				<intercept-url pattern="/**" access="hasRole('ROLE_CRM_USER')"/>
				<form-login login-page="/login.jsp"
							authentication-failure-url="/login.jsp?error=1"
							login-processing-url="/login"/>
							
				<logout logout-success-url="/website/all-customers.html"/>
									
				<csrf/> 		
			</http>*/
			http.antMatcher("/**").authorizeRequests()
									.anyRequest()
									.hasRole("CRM_USER")
									
									.and()
									
									.formLogin()
										.loginPage("/login.jsp")
										.failureUrl("/login.jsp?error=1")
										.loginProcessingUrl("/login")
										.permitAll() //permitAll will make login.jsp, /login urls open to all users.
			
									.and()
									
									.logout()
										.logoutSuccessUrl("/website/all-customers.html")
										
									.and()
									
									.csrf() //csrf is enabled by default, so it will be ok if you dont write this line 
									;
		}
	}
	
	@Configuration
	@EnableAuthorizationServer
	public static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
		
		/*<oauth2:authorization-server client-details-service-ref="clientDetailsService" user-approval-handler-ref="userApprovalHandler">
	   		<oauth2:authorization-code/>
	   		<oauth2:client-credentials/>
	   	</oauth2:authorization-server>  
		
		<oauth2:client-details-service id="clientDetailsService">
			<oauth2:client client-id="mailmonkey"
							secret="somesecretkey"
							authorities="ROLE_CLIENT"
							scope="read,write"
							authorized-grant-types="authorization_code"/>
							
			<oauth2:client client-id="trusted"
							secret="trustedsecret"
							authorities="ROLE_TRUSTED"
							scope="trusted"
							authorized-grant-types="client_credentials"
							/>				
		</oauth2:client-details-service>
		
		<beans:bean id="clientDetailsUserService" class="org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService">
	 		<beans:constructor-arg ref="clientDetailsService" />
	 	</beans:bean>
	 	
		<authentication-manager id="oauthTokenEndpointAuthManager" >
			<authentication-provider user-service-ref="clientDetailsUserService">
			</authentication-provider>
		</authentication-manager>
		
		<http pattern="/oauth/token" create-session="stateless" authentication-manager-ref="oauthTokenEndpointAuthManager">
			<intercept-url pattern="/oauth/token" access="hasRole('ROLE_CLIENT') or hasRole('ROLE_TRUSTED')" />
			<http-basic/>
		
			<csrf disabled="true"/>
		</http>
	
		<beans:bean id="approvalStore" class="org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore" />
		<beans:bean id="userApprovalHandler" class="org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler" autowire="byType" />
		*/
	
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
					.withClient("mailmonkey")
					.secret("somesecretkey")
					.authorities("CLIENT")
					.scopes("read","write")
					.authorizedGrantTypes("authorization_code")
					
					.and()
					
					.withClient("trusted")
					.secret("trustedsecret")
					.authorities("TRUSTED")
					.scopes("trusted")
					.authorizedGrantTypes("client_credentials")
					;
		}
		
	}
	
	@Configuration
	@EnableResourceServer
	public static class ResourceServerConfig extends ResourceServerConfigurerAdapter {
		
		/*<beans:bean id="oauthTokenEntry" class="org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint"/>
			
			<oauth2:resource-server id="oauthFilter"/>

			<beans:bean id="expressionHandler" class="org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler" />
			
			<http pattern="/rest/customers" create-session="stateless" entry-point-ref="oauthTokenEntry">
				<intercept-url pattern="/rest/customers" method="POST" access="#oauth2.hasScope('write') or #oauth2.hasScope('trusted')"/>
				<intercept-url pattern="/rest/customers" access="#oauth2.hasScope('read') or #oauth2.hasScope('write') or #oauth2.hasScope('trusted')"/>
				
				<custom-filter ref="oauthFilter" before="PRE_AUTH_FILTER"/>
				
				<expression-handler ref="expressionHandler"/>
				
				<csrf disabled="true"/>
			</http>
		*/
		public void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/rest/**").authorizeRequests()
										.antMatchers(HttpMethod.POST,"/rest/customers")
											.access("#oauth2.hasScope('write') or #oauth2.hasScope('trusted')")
										.antMatchers("/rest/customers")
											.access("#oauth2.hasScope('read') or #oauth2.hasScope('write') or #oauth2.hasScope('trusted')")
										
										.and()
										
										.csrf().disable()
										.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
										;
										
		}
	}
	
}
