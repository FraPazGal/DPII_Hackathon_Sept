<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<security:authorize access="permitAll">
<jstl:choose>
	<jstl:when test="${range eq null }">
		<h1><spring:message	code="room.list" /></h1>
	</jstl:when>
	<jstl:when test="${range == 'mineD' }">
		<h1><spring:message	code="room.room.list.draft" /></h1>
	</jstl:when>
	<jstl:when test="${range == 'mineO' }">
		<h1><spring:message	code="room.room.list.out" /></h1>
	</jstl:when>
	<jstl:otherwise>
		<h1><spring:message	code="room.room.list.active" /></h1>
	</jstl:otherwise>
</jstl:choose>

<jstl:choose>
	<jstl:when test="${isPrincipal}">
	
		<display:table class="displaytag" name="rooms" pagesize="5" 
			requestURI="room/list.do" id="room" style="width:80%">

			<display:column titleKey="room.title" sortable="true">
				<jstl:out value="${room.title}" />
			</display:column>
			
			<display:column titleKey="room.ticker" sortable="true">
				<jstl:out value="${room.ticker}" />
			</display:column>
			
			<display:column titleKey="room.address" sortable="true">
				<jstl:out value="${room.address}" />
			</display:column>
			
			<display:column titleKey="room.pricePerHour" sortable="true">
				<jstl:out value="${room.pricePerHour}" /> &#8364;
			</display:column>
			
			<display:column titleKey="room.capacity" sortable="true">
				<jstl:out value="${room.capacity}" />
			</display:column>
			
			<display:column titleKey="room.openingHour" sortable="true">
				<jstl:out value="${room.openingHour}" />
			</display:column>
			
			<display:column titleKey="room.closingHour" sortable="true">
				<jstl:out value="${room.closingHour}" />
			</display:column>
			
			<display:column>
				<a href="room/display.do?roomId=${room.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
			
			<display:column>
				<jstl:if test="${!room.isOutOfService}">
					<a href="room/edit.do?roomId=${room.id}"> <spring:message
							code="mp.edit" />
					</a>
				</jstl:if>
			</display:column>
			
			<display:column>
				<jstl:if test="${!room.isOutOfService and !room.isDraft}">
					<a id="decomission"
						href="room/action.do?action=decomission&roomId=${room.id}">
						<spring:message code="room.decomission" />
					</a>
				</jstl:if>
			</display:column>
			<jstl:if test="${room.isDraft}">
				<display:column>
					<a href="room/delete.do?roomId=${room.id}" 
						onclick="return confirm('<spring:message code="room.confirm.delete"/>')">
						<spring:message code="mp.delete" />
					</a>
				</display:column>
			</jstl:if>
		</display:table>
		
		<input type="button"
			onclick="redirect: location.href = 'room/create.do';"
			value="<spring:message code='room.create' />" />
			
	</jstl:when>
	<jstl:otherwise>
		<display:table class="displaytag" name="rooms" pagesize="5" 
			requestURI="room/list.do" id="room">

			<display:column titleKey="room.title" sortable="true">
				<jstl:out value="${room.title}" />
			</display:column>
			
			<display:column titleKey="room.ticker" sortable="true">
				<jstl:out value="${room.ticker}" />
			</display:column>
			
			<display:column titleKey="room.address" sortable="true">
				<jstl:out value="${room.address}" />
			</display:column>
			
			<display:column titleKey="room.pricePerHour" sortable="true">
				<jstl:out value="${room.pricePerHour}" /> &#8364;
			</display:column>
			
			<display:column titleKey="room.capacity" sortable="true">
				<jstl:out value="${room.capacity}" />
			</display:column>
			
			<display:column titleKey="room.openingHour" sortable="true">
				<jstl:out value="${room.openingHour}" />
			</display:column>
			
			<display:column titleKey="room.closingHour" sortable="true">
				<jstl:out value="${room.closingHour}" />
			</display:column>
			
			<display:column>
				<a href="room/display.do?roomId=${room.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
			
			<display:column>
				<a href="owner/display.do?id=${room.owner.id}"> <spring:message
						code="room.owner" />
				</a>
			</display:column>
			
			<security:authorize access="hasRole('CUSTOMER')">
				<display:column>
					<a href="booking/create.do?roomId=${room.id}"> <spring:message
							code="room.booking" />
					</a>
				</display:column>
			</security:authorize>
		</display:table>
	</jstl:otherwise>
</jstl:choose>
</security:authorize>