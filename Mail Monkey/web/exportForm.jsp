<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

	<head>
   		<title>Export customer to CRM</title>
   		<link href="<c:url value="/css/styles.css"/>" rel="Stylesheet" type="text/css"/>   	
   </head>

	<body>
		  <h1>Export Data to CRM</h1>
	
		  <form:form commandName="customer">
	         <p><label>Company Name (*)</label><form:input path="companyName"/>	  
	                                  <form:errors path="companyName" cssClass="error"/></p>  
	                    
	         <p><label>Customer Id (*)</label> <form:input path="customerId"/>
	         		                    <form:errors path="customerId" cssClass="error"/></p>  
	         		         	
	         <p><label>Email (*)</label><form:input path="email"/>	               
	         	                       <form:errors path="email" cssClass="error"/></p>
	         	                       
	         <p><label>Notes</label><form:input path="notes"/>
	         		                    <form:errors path="notes" cssClass="error"/></p>  

	         <p><label>Telephone</label><form:input path="telephone"/>
	         		                    <form:errors path="telephone" cssClass="error"/>  </p>
	         

			 <p>Submitting this form will export the above customer into the CRM system. Note: you must allow write access to CRM for this to work.</p>
	
	         <input type="submit" value="Export to CaRM"/>
	      </form:form>		   	  
      </div>            
  	  <jsp:include page="/footer.jsp"/> 												 	
	</body>
	
</html>
