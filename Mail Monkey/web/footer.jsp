<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="footer">	
	<div id="brand">
		<img id="monkey" src="<c:url value="/images/monkey.png"/>"/>	<span id="slogan"><span id="sloganemph">MailMonkey</span> - More Ham, Less Spam.</span>
	</div>
	
	<div id="links">
			<sec:authorize access="isAuthenticated()">				
				<a href='<c:url value="/build-newsletter.html"/>'>Build Newsletter</a>
			</sec:authorize>
			
			<sec:authorize access="isAnonymous()">									
				<a href='<c:url value="login.jsp"/>'>Login</a>
			</sec:authorize>
			
			<sec:authorize access="isAuthenticated()">
				<!-- a post form is required to prevent CSRF attacks. -->									
				<c:url value="/logout" var="logoutUrl"/>
					<div id="logout">
						<form:form method="post" action="${logoutUrl}">
							<input type="submit" value="Logout"/>
							<sec:csrfInput />	   					
						</form:form>
					</div>				
			</sec:authorize>
	</div>	
	
</div>