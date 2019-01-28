<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

	<head>
		<title>Newsletter Management</title>
		<link href="<c:url value="/css/styles.css"/>" rel="Stylesheet" type="text/css"/>				
	</head>

	<body>
		<h1>Newsletter Management</h1>
		
		<em>We have now, miraculously, got the contacts from CRM - and you never
			gave us your CRM password!</em>
		
		<table>
			<tr>
				<th>Id</th>
				<th>Company</th>
				<th>Email</th>
				<th>Phone</th>
			</tr>
			
			<c:forEach items="${customers}" var="customer">
				<tr>
					<td>${customer.customerId}</td>
					<td>${customer.companyName}</td>
					<td>${customer.email}</td>
					<td>${customer.telephone}</td>
				</tr>
			</c:forEach>
	 	<table>
	 										
		<jsp:include page="/footer.jsp"/>												 
	</body>
</html>