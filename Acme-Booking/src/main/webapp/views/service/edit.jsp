<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('OWNER')">
	<jstl:if test="${service.id == 0 }">
		<h1><spring:message	code="service.create" /><jstl:out value="${service.room.name }"/></h1>
	</jstl:if>
	<jstl:if test="${service.id != 0 }">
		<h1><spring:message	code="service.title.edit" /><jstl:out value="${service.room.name }"/></h1>
	</jstl:if>
	<form:form modelAttribute="service" action="service/edit.do" id="form">
	
		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="service.legend" />
			</legend>
			<br/>
			<form:hidden path="id" />
			<form:hidden path="room" />
			<acme:textarea code="service.description" path="description" cols="71px" rows="4"/><br/> <br/>
			<acme:textbox code="service.price" path="price" size="20px"/><br/> 
			
		</fieldset>
		<br><br>
		
		<acme:submit code="mp.save" name="save" />&nbsp;
		<acme:cancel url="service/list.do?range=mineA" code="mp.cancel" />
		<br />
	</form:form>
</security:authorize>