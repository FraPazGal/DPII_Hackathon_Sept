<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<security:authorize access="hasRole('ADMIN')">
	<table class="displayStyle">

		<tr>
			<td><strong> <spring:message code="config.sysname" />
					:
			</strong></td>
			<td><jstl:out value="${config.systemName}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.banner" /> :
			</strong></td>
			<td><a href="<jstl:out
						value='${config.banner}' />">
					<jstl:out value='${config.banner}' />
			</a></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.countryCode" />
					:
			</strong></td>
			<td><jstl:out value="${config.countryCode}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.cache" /> :
			</strong></td>
			<td><jstl:out value="${config.timeResultsCached}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.maxResults" />
					:
			</strong></td>
			<td><jstl:out value="${config.maxResults}"></jstl:out></td>
		</tr>

	</table>

	<jstl:if test="${not empty welcome}">
		<table class="displayStyle">

			<tr>
				<td><strong><spring:message code="welcome.es" /></strong></td>
				<td><jstl:out value="${welcome.get('Espa�ol')}" /></td>
			</tr>

			<tr>
				<td><strong><spring:message code="welcome.en" /></strong></td>
				<td><jstl:out value="${welcome.get('English')}" /></td>
			</tr>

		</table>
	</jstl:if>
	
	<jstl:if test="${not empty breach}">
		<table class="displayStyle">

			<tr>
				<td><strong><spring:message code="breach.es" /></strong></td>
				<td><jstl:out value="${breach.get('Espa�ol')}" /></td>
			</tr>

			<tr>
				<td><strong><spring:message code="breach.en" /></strong></td>
				<td><jstl:out value="${breach.get('English')}" /></td>
			</tr>

		</table>
	</jstl:if>

	<input type="button"
		onclick="redirect: location.href = 'config/admin/edit.do';"
		value="<spring:message code='sysconfig.edit' />" />

</security:authorize>