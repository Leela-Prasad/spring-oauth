<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
	<head>
		<title>Mail Monkey Login</title>
		<link href="<c:url value="/css/styles.css"/>" rel="Stylesheet" type="text/css"/>		
	</head>
	
	<body>
		<h1><strong>MailMonkey Login</strong></h1>
	
		<p>You are now logging on to <em>MailMonkey</em></p>
	
		<div id="loginleft">
			<img id="monkey2" src="<c:url value="/images/monkey.png"/>"/>
			
			<c:if test="${param.error != null}">
				<p>Bad username/password</p>
			</c:if>			
		</div>	
	
		<c:url value="/login" var="loginUrl"/>
		
		<div id="loginright">	
			<p>As this site is not yet launched to the public, there is only one user - mm/mm</p>
		
			<form:form action="${loginUrl}" method="post">
				<p><label>MailMonkey username:</label> <input type="text" name="username" value="mm"/></p>
				<p><label>MailMonkey password:</label> <input type="text" name="password" value="mm"/></p>
				<p><input type="submit"/></p>
			</form:form>
		</div>

		<div style="clear: both;"></div>
			
		<jsp:include page="/footer.jsp"/>												 		
		
	</body>
</html>