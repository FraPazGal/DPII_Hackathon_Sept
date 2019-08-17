<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="permitAll">
	<!-- Actor Attributes -->
	<h1><spring:message	code="actor.view" /></h1>
	<fieldset style="width: 35%">
		<legend style="font-size: 21px">
			<spring:message code="actor.personalData" />
		</legend>

		<div style="float: left;">
		<br>
			<div>
				<strong><spring:message code="actor.name" />: </strong>
				<jstl:out value="${owner.name}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="actor.middleName" />: </strong>
				<jstl:out value="${owner.middleName}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="actor.surname" />: </strong>
				<jstl:out value="${owner.surname}" />
			</div>

			<br />

			<div>
				<strong><spring:message code="actor.email" />: </strong>
				<jstl:out value="${owner.email}" />
			</div>

			<br />

			<div>
				<strong><spring:message code="actor.phone" />: </strong>
				<jstl:out value="${owner.phoneNumber}" />
			</div>

			<br />

			<div>
				<strong><spring:message code="actor.address" />: </strong>
				<jstl:out value="${owner.address}" />
			</div>

		</div>

		<div style="float: right;">
			<img style="width: 200px; height: 200px" src="${owner.photo}"
				alt="User photo">
		</div>

	</fieldset>
	<br>
	<jstl:if test="${isPrincipal }">
		<input type="button" name="edit" value="<spring:message code="mp.edit" />" onclick="javascript: relativeRedir('/owner/edit.do')" />&nbsp;
	</jstl:if>
	<input type="button" name="back" value="<spring:message code="mp.back" />" onclick="window.history.back()" />&nbsp;
	
</security:authorize>