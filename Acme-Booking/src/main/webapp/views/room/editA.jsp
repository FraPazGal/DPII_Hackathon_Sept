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
	<jstl:if test="${activeRoomForm.id != 0 }">
		<h1><spring:message	code="room.title.edit" /></h1>
	</jstl:if>
	<form:form modelAttribute="activeRoomForm" action="room/editA.do" id="form">
	
		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="room.legend" />
			</legend>
			<br/>
	
			<form:hidden path="id" />
			<acme:textbox code="room.openingHour" path="openingHour" size="20px" placeholder="hour.placeholder"/><br/>
			<acme:textbox code="room.closingHour" path="closingHour" size="20px" placeholder="hour.placeholder"/><br/>
			<acme:textarea code="room.scheduleDetails" path="scheduleDetails" cols="71px" rows="4"/><br/>
			
		</fieldset>
		<br><br>
		
		<acme:submit code="mp.save" name="save" />&nbsp;
		<acme:cancel url="room/list.do?range=mineA" code="mp.cancel" />
		<br />
	</form:form>
</security:authorize>