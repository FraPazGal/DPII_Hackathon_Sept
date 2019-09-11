<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('OWNER')">
	<jstl:if test="${booking.id != 0 }">
		<h1>
			<spring:message code="booking.manage" />
		</h1>
	</jstl:if>
	<form:form modelAttribute="booking" action="booking/edit.do" id="form">

		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="booking.legend" />
			</legend>
			<br />

			<form:hidden path="id" />
			<form:hidden path="room" />

			<acme:textarea code="booking.rejectionReason" path="rejectionReason"
				cols="71px" rows="4" />
			<br />
		</fieldset>
		<br>
		<br>

		<acme:submit code="booking.accept" name="accepted" />&nbsp;
		<acme:submit code="booking.reject" name="rejected" />&nbsp;
		<acme:cancel url="booking/list.do" code="mp.cancel" />
		<br />
	</form:form>
</security:authorize>
<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${booking.id == 0 }">
		<h1>
			<spring:message code="booking.create" />
		</h1>
	</jstl:if>
	<jstl:if test="${booking.id != 0 }">
		<h1>
			<spring:message code="booking.title.edit" />
		</h1>
	</jstl:if>
	<form:form modelAttribute="booking" action="booking/edit.do" id="form">

		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="booking.legend" />
			</legend>
			<br />

			<form:hidden path="id" />
			<form:hidden path="room" />

			<acme:textbox code="booking.title" path="title" size="100px" />
			<br /> <br />
			<acme:textarea code="booking.bookingReason" path="bookingReason"
				cols="71px" rows="4" />
			<br />
			<acme:textbox code="booking.expectedAttendance"
				path="expectedAttendance" size="100px" />
			<br /> <br />
			<acme:textbox code="booking.duration" path="duration" size="20px" /> <spring:message code="booking.inhours" />
			<br /> <br />
			<acme:textbox code="booking.reservationDate" path="reservationDate"
				size="20px" placeholder="date.placeholder" />
			<br /> <strong><spring:message code="booking.services" /></strong><br>

			<jstl:if test="${not empty services}">
				<form:select path="services">
					<form:options items="${services}" itemLabel="name" itemValue="id" />
				</form:select>
				<form:errors cssClass="error" path="services" />
				<br />
				<br />
				<h2>
					<strong><spring:message code="booking.room.services" /></strong>
				</h2>
				<display:table style="width: 80%" class="displaytag" name="services"
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

		</fieldset>
		<br>
		<br>
		<input type="submit" name="save"
			value="<spring:message code="mp.save"/>"
			onclick="return confirm('<spring:message code="booking.confirm.save"/>')" />
		<acme:cancel url="booking/list.do" code="mp.cancel" />
		<br />
	</form:form>
</security:authorize>