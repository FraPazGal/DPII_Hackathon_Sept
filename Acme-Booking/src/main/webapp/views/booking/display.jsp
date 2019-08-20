<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasAnyRole('OWNER','CUSTOMER')">

	<h1>
		<spring:message code="booking.legend" />
		<i><jstl:out value="${booking.title}" /></i>
	</h1>
	<fieldset style="width: 30%">
		<legend style="font-size: 21px">
			<spring:message code="booking.details" />
		</legend>
		<spring:message code="date.dateFormat" var="format" />
		<div style="float: left;">
			<br>
			<div>
				<strong><spring:message code="booking.bookingReason" />: </strong><br>
				<br>
				<jstl:out value="${booking.bookingReason}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="booking.rejectionReason" />:
				</strong><br> <br>
				<jstl:out value="${booking.rejectionReason}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="booking.expectedAttendance" />:
				</strong><br> <br>
				<jstl:out value="${booking.expectedAttendance}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="booking.duration" />: </strong><br>
				<br>
				<jstl:out value="${booking.duration}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="booking.reservationPrice" />:
				</strong><br> <br>
				<jstl:out value="${booking.reservationPrice}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="booking.status" />: </strong><br>
				<br>
				<jstl:if test="${booking.status=='PENDING'}">
					<spring:message code="booking.status.pending" />
				</jstl:if>
				<jstl:if test="${booking.status=='ACCEPTED'}">
					<spring:message code="booking.status.accepted" />
				</jstl:if>
				<jstl:if test="${booking.status=='REJECTED'}">
					<spring:message code="booking.status.rejected" />
				</jstl:if>
			</div>
			<br />

			<div>
				<strong><spring:message code="booking.requestedMoment" />:
				</strong><br> <br>
				<jstl:out value="${booking.requestedMoment}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="booking.reservationDate" />:
				</strong><br> <br>
				<jstl:out value="${booking.reservationDate}" />
			</div>
			<br />
		</div>
	</fieldset>

	<jstl:if test="${not empty services}">
		<h2>
			<strong><spring:message code="room.services" /></strong>
		</h2>
		<display:table style="width: 80%" class="displaytag" name="services"
			pagesize="5" requestURI="${requestURI}" id="service">

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

		<acme:cancel url="booking/list.do" code="mp.cancel" />

</security:authorize>
