<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="permitAll">

	<h1><jstl:out value="${service.name}" /></h1>
	<fieldset style="width: 30%">
		<legend style="font-size: 21px">
			<spring:message code="service.details" />
		</legend>
		<div style="float: left;">
		<br>
			<div>
				<strong><spring:message code="service.description" />: </strong><br><br>
				<jstl:out value="${service.description}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="service.price" />: </strong>
				<jstl:out value="${service.price}" /> &#8364;
			</div>
			<br />
			
		</div>
	</fieldset>
	<br>
	
	<jstl:if test="${isPrincipal }">
		<input type="button" name="back"
			value="<spring:message code="mp.edit" />"
			onclick="redirect: location.href = 'service/edit.do?serviceId=${service.room.id}';" />
	</jstl:if>
		
	<input type="button" name="back"
		value="<spring:message code="mp.back" />"
		onclick="window.history.back()" />

</security:authorize>
