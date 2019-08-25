<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<security:authorize access="hasAnyRole('OWNER','CUSTOMER')">

	<div>
		<jstl:if test="${not empty messageCode}">
			<h4>
				<spring:message code="${messageCode}" />
			</h4>
		</jstl:if>
		<h3>
			<spring:message code="booking.list" />
		</h3>
		<display:table name="bookings" id="row" requestURI="${requestURI}"
			pagesize="5">

			<display:column titleKey="booking.title">
				<jstl:out value="${row.title}"></jstl:out>
			</display:column>


			<display:column titleKey="booking.status">
				<jstl:if test="${row.status=='PENDING'}">
					<spring:message code="booking.status.pending" />
				</jstl:if>
				<jstl:if test="${row.status=='ACCEPTED'}">
					<spring:message code="booking.status.accepted" />
				</jstl:if>
				<jstl:if test="${row.status=='REJECTED'}">
					<spring:message code="booking.status.rejected" />
				</jstl:if>
			</display:column>

			<display:column titleKey="booking.reservationDate">
				<jstl:out value="${row.reservationDate}"></jstl:out>
			</display:column>

			<display:column titleKey="booking.duration">
				<jstl:out value="${row.duration}"></jstl:out>
			</display:column>

			<display:column titleKey="booking.reservationPrice">
				<jstl:out value="${row.reservationPrice}"></jstl:out>
			</display:column>

			<display:column titleKey="booking.display">
				<button
					onClick="window.location.href='booking/display.do?Id=${row.id}'">
					<spring:message code="booking.display" />
				</button>
			</display:column>
			<security:authorize access="hasRole('OWNER')">
				<display:column>
				<jstl:if test="${row.status == 'PENDING' }">
					<button
						onClick="window.location.href='booking/edit.do?Id=${row.id}'">
						<spring:message code="booking.manage" />
					</button>
					</jstl:if>
				</display:column>
			</security:authorize>

		</display:table>
		<br />
	</div>
</security:authorize>