<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<h3>
	<b><spring:message code="message.subject" />: </b>
	<jstl:out value="${messageO.subject}" />
</h3>
<jstl:if test="${not empty messageCode}">
	<h4>
		<spring:message code="${messageCode}" />
	</h4>
</jstl:if>

<b><spring:message code="message.actor.sender" />: </b>
<jstl:out value="${messageO.sender.userAccount.username}" />
<br />

<b><spring:message code="message.actor.receiver" />: </b>
<jstl:out value="${messageO.receiver.userAccount.username}" />
<br />

<b><spring:message code="message.sendTime" />: </b>
<jstl:out value="${messageO.sentMoment}" />
<br />
<b><spring:message code="message.priority" />: </b>
<jstl:if test="${messageO.priority=='HIGH'}">
	<spring:message code="message.priority.high" />
</jstl:if>
<jstl:if test="${messageO.priority=='NEUTRAL'}">
	<spring:message code="message.priority.neutral" />
</jstl:if>
<jstl:if test="${messageO.priority=='LOW'}">
<spring:message code="message.priority.low" />
</jstl:if>
<br />

<b><spring:message code="message.body" />: </b>
<jstl:out value="${messageO.body}" />
<br />

<button onClick="window.location.href='messagebox/list.do'">
	<spring:message code="message.cancel" />
</button>