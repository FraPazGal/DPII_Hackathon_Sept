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

	<h1><spring:message code="room.legend" /> <i><jstl:out value="${room.title}" /></i></h1>
	<fieldset style="width: 30%">
	<legend style="font-size: 21px">
		<spring:message code="room.details" />
	</legend>
	<spring:message code="date.dateFormat" var="format" /> 
	<div style="float: left;">
	<br>
		<div>
			<strong><spring:message code="room.description" />: </strong><br><br>
			<jstl:out value="${room.description}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.address" />: </strong>
			<jstl:out value="${room.address}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.capacity" />: </strong>
			<jstl:out value="${room.capacity}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.pricePerHour" />: </strong>
			<jstl:out value="${room.pricePerHour}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.openingHour" />: </strong>
			<jstl:out value="${room.openingHour}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.closingHour" />: </strong>
			<jstl:out value="${room.closingHour}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.scheduleDetails" />: </strong>
			<jstl:out value="${room.scheduleDetails}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.ticker" />: </strong>
			<jstl:out value="${room.ticker}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="room.attachments" />: </strong>
			<jstl:out value="${room.attachments}" />
		</div>
		<br />
		
		<jstl:if test="${not empty room.photos}">
			<div>
				<strong><spring:message code="room.photos" />: </strong>
				<jstl:out value="${room.photos}" />
			</div>
		<br />
		</jstl:if>

		<jstl:choose>
			<jstl:when test="${pageContext.response.locale.language == 'es'}">
				<tr>
					<td><strong> <spring:message code="room.category" /> : </strong></td>
					<td><jstl:out value="${room.category.title.get('Español')}" /></td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr>
					<td><strong> <spring:message code="room.category" /> : </strong></td>
					<td><jstl:out value="${room.category.title.get('English')}" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
		
		<jstl:if test="${isPrincipal }">
		
			<jstl:if test="${room.visibility == 'DRAFT'}">
				<spring:message var="status" code='room.status.draft' />
			</jstl:if>
			<jstl:if test="${room.visibility == 'ACTIVE'}">
				<spring:message var="status" code='room.status.active' />
			</jstl:if>
			<jstl:if test="${room.visibility == 'OUTOFSERVICE'}">
				<spring:message var="status" code='room.status.outofservice' />
			</jstl:if>
		
			<div>
				<br>
				<strong><spring:message code="room.visibility" />: </strong>
				<jstl:out value="${status}" />
			</div>
			<br />
		</jstl:if>
		</div>
	</fieldset>

	<jstl:if test="${not empty services}">
		<h2>
			<strong><spring:message code="room.services" /></strong>
		</h2>
		<display:table style="width: 80%" class="displaytag"
			name="services" pagesize="5"
			requestURI="${requestURI}"
			id="service">
			
			<display:column titleKey="service.name">
				<jstl:out value="${service.name}" />
			</display:column>	
			
			<display:column titleKey="service.price">
				<jstl:out value="${service.price}" />
			</display:column>
			
			<display:column>
				<a href="service/display.do?serviceId=${service.id}"> <spring:message
					code="service.details" />
				</a>
			</display:column>
		</display:table>
	</jstl:if>
			
	<security:authorize access="isAuthenticated()">
		<br>
		<jstl:if test="${room.visibility == 'DRAFT'}">
			<input type="button"
				onclick="redirect: location.href = 'service/create.do?roomId=${room.id}';"
				value="<spring:message code='service.create' />" /><br>
		<br>
		</jstl:if>
	</security:authorize>
	
	<input type="button" name="back"
		value="<spring:message code="mp.back" />"
		onclick="window.history.back()" />

</security:authorize>
