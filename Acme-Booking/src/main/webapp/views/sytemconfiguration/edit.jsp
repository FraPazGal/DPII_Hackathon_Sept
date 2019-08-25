<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('ADMIN')">
	<script type='text/javascript'>
		function addFields() {
			// Container <div> where dynamic content will be placed
			var container = document.getElementById("container");
			// Create an <input> element, set its type and name attributes
			var input = document.createElement("input");
			input.type = "text";
			input.name = "makers";
			container.appendChild(input);
		}
		function addFields2() {
			// Container <div> where dynamic content will be placed
			var container = document.getElementById("container2");
			// Create an <input> element, set its type and name attributes
			var input = document.createElement("input");
			input.type = "text";
			input.name = "spamWords";
			container.appendChild(input);
		}
	</script>
	<br>
	<fieldset>
		<legend style="font-size: 21px">
			<spring:message code="sysconfig.edit" />
		</legend>
		<br>

		<form:form action="config/admin/edit.do"
			modelAttribute="systemConfiguration" id="form">

			<form:hidden path="id" />
			<form:hidden path="version" />

			<acme:textbox code="config.sysname" path="systemName" />
			<br>
			<acme:textbox code="config.banner" path="banner" />
			<br>
			<acme:textbox code="config.countryCode" path="countryCode" />
			<br>
			<acme:textbox code="config.cache" path="timeResultsCached" />
			<br>
			<acme:textbox code="config.maxResults" path="maxResults" />
			<br>

			<acme:textbox code="config.VATTax" path="VATTax" />
			<br>
			<p>
				<spring:message code="config.makers" />
				:
				<button type="button" onClick="addFields()">
					<spring:message code="config.add" />
				</button>
			<div id="container"></div>

			<jstl:forEach items="${systemConfiguration.makers}" var="item">
				<input name="makers" value="${item }" />
			</jstl:forEach>
			<form:errors path="makers" cssClass="error" />
			<br>
			<br>

			<p>
				<spring:message code="config.spamWords" />
				:
				<button type="button" onClick="addFields2()">
					<spring:message code="config.add" />
				</button>
			<div id="container2"></div>

			<jstl:forEach items="${systemConfiguration.spamWords}" var="item">
				<input name="spamWords" value="${item }" />
			</jstl:forEach>
			<form:errors path="spamWords" cssClass="error" />
			<br>
			<br>

			<strong><spring:message code="welcome.es" /></strong>
			<br>
			<input type="text" name="welcomeES" id="welcomeES" size="150%"
				value="${welcome.get('Español')}"
				placeholder="<spring:message code='sysconfig.welcome.message.es' />">

			<br>
			<br>
			<strong><spring:message code="welcome.en" /></strong>
			<br>
			<input type="text" name="welcomeEN" id="welcomeEN" size="150%"
				value="${welcome.get('English')}"
				placeholder="<spring:message code='sysconfig.welcome.message.en' />">

			<br>
			<br>
			<form:errors cssClass="error" path="welcomeMessage" />
			<br />
			<strong><spring:message code="breach.es" /></strong>
			<br>
			<input type="text" name="breachES" id="breachES" size="150%"
				value="${breach.get('Español')}"
				placeholder="<spring:message code='sysconfig.breach.message.es' />">

			<br>
			<br>
			<strong><spring:message code="breach.en" /></strong>
			<br>
			<input type="text" name="breachEN" id="breachEN" size="150%"
				value="${breach.get('English')}"
				placeholder="<spring:message code='sysconfig.breach.message.en' />">

			<br>
			<br>
			<form:errors cssClass="error" path="breachNotification" />

			<form:errors cssClass="error" code="${message}" />
			<acme:submit code="mp.save" name="save" />
			<acme:cancel code="mp.cancel" url="config/admin/display.do" />
		</form:form>
	</fieldset>
	<br>

</security:authorize>
