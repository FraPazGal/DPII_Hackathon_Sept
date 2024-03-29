<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


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
			<jstl:out value="${admin.name}" />
		</div><br />

		<jstl:if test="${admin.middleName ne null}">
			<div>	
				<strong><spring:message code="actor.middleName" />: </strong>	
				<jstl:out value="${admin.middleName}" />	
			</div><br />
		</jstl:if>
		
		<div>
			<strong><spring:message code="actor.surname" />: </strong>
			<jstl:out value="${admin.surname}" />
		</div><br />

		<div>
			<strong><spring:message code="actor.email" />: </strong>
			<jstl:out value="${admin.email}" />
		</div><br />

		<div>
			<strong><spring:message code="actor.phone" />: </strong>
			<jstl:out value="${admin.phoneNumber}" />
		</div><br />

		<div>
			<strong><spring:message code="actor.address" />: </strong>
			<jstl:out value="${admin.address}" />
		</div>

	</div>

	<div style="float: right;">
		<img style="width: 200px; height: 200px" src="${admin.photo}"
			alt="User photo">
	</div>

</fieldset>
<br>
<input type="button" name="edit" value="<spring:message code="mp.edit" />" onclick="javascript: relativeRedir('/administrator/edit.do')" />&nbsp;	
<input type="button" name="back" value="<spring:message code="mp.back" />" onclick="window.history.back()" />&nbsp;
<br>