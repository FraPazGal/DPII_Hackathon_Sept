<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${(messageForm.id == 0) && (broadcast)}">

		<h3>
			<spring:message code="message.broadcast" />
		</h3>
		<form:form action="message/administrator/broadcast.do"
			modelAttribute="messageForm">

			<form:hidden path="id" />

			<form:label path="subject">
				<spring:message code="message.subject" />:
			</form:label>
			<form:input path="subject" />
			<form:errors cssClass="error" path="subject" />
			<br>
			<br>

			<form:label path="body">
				<spring:message code="message.body" />:
	</form:label>
			<form:textarea path="body" />
			<form:errors cssClass="error" path="body" />
			<br>
			<br>

			<form:label path="priority">
				<spring:message code="message.priority" />:
	</form:label>
			<form:select path="priority">
				<option value="HIGH">
					<spring:message code="message.priority.high" />
				</option>
				<option value="NEUTRAL">
					<spring:message code="message.priority.neutral" />
				</option>
				<option value="LOW">
					<spring:message code="message.priority.low" />
				</option>

			</form:select>
			<form:errors cssClass="error" path="priority" />
			<br />
			<br />
			<input type="submit" name="saveBroadcast"
				value="<spring:message code="message.broadcast"/>" />&nbsp;
		
			<input type="button" name="cancel"
				value="<spring:message code="message.cancel" />"
				onclick="javascript: relativeRedir('/messagebox/list.do');" />

			<br />

		</form:form>

	</jstl:if>

</security:authorize>