<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<h1><spring:message code="room.legend" /> <i><jstl:out value="${room.title}" /></i></h1>
<fieldset style="width: 30%">
<legend style="font-size: 21px">
	<spring:message code="room.details" />
</legend>
<spring:message code="date.dateFormat" var="format" /> 
<div style="float: left;">
<br>
	<div>
		<strong><spring:message code="room.description" />: </strong><br><br>
		<jstl:out value="${room.description}" />
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.address" />: </strong>
		<jstl:out value="${room.address}" />
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.capacity" />: </strong>
		<jstl:out value="${room.capacity}" />
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.pricePerHour" />: </strong>
		<jstl:out value="${room.pricePerHour}" /> &#8364;
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.openingHour" />: </strong>
		<jstl:out value="${room.openingHour}" />
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.closingHour" />: </strong>
		<jstl:out value="${room.closingHour}" />
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.scheduleDetails" />: </strong>
		<jstl:out value="${room.scheduleDetails}" />
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.ticker" />: </strong>
		<jstl:out value="${room.ticker}" />
	</div>
	<br />
	
	<div>
		<strong><spring:message code="room.proveOfOwnership" />: </strong>
		<a href="<jstl:out value="${room.proveOfOwnership}" />"><jstl:out value="${room.proveOfOwnership}" /></a>
	</div>
	<br />
	
	<jstl:if test="${not empty room.categories}">
		<jstl:choose>
			<jstl:when test="${pageContext.response.locale.language == 'es'}">
				<tr>
					<td><strong> <spring:message code="room.categories" /> : </strong></td>
					<td>
						|| 
						<jstl:forEach var="category" items="${room.categories}">
							<jstl:if test="${category.title.get('Español') ne 'CATEGORIA'}">
								<jstl:out value="${category.parentCategory.title.get('Español')}" /> -> 
							</jstl:if>
							<i><jstl:out value="${category.title.get('Español')}" /></i> || 
						</jstl:forEach>
					</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr>
					<td><strong> <spring:message code="room.categories" /> : </strong></td>
					<td>
						|| 
						<jstl:forEach var="category" items="${room.categories}">
							<jstl:if test="${category.title.get('English') ne 'CATEGORY'}">
								<jstl:out value="${category.parentCategory.title.get('English')}" /> -> 
							</jstl:if>
							<i><jstl:out value="${category.title.get('English')}" /></i> || 
						</jstl:forEach>
					</td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
	</jstl:if>
	<jstl:if test="${isPrincipal }">
	
		<jstl:if test="${room.status == 'DRAFT'}">
			<spring:message var="status" code='room.status.draft' />
		</jstl:if>
		<jstl:if test="${room.status == 'REVISION-PENDING'}">
			<spring:message var="status" code='room.status.inrevision' />
		</jstl:if>
		<jstl:if test="${room.status == 'ACTIVE'}">
			<spring:message var="status" code='room.status.active' />
		</jstl:if>
		<jstl:if test="${room.status == 'OUT-OF-SERVICE'}">
			<spring:message var="status" code='room.status.outofservice' />
		</jstl:if>
		<jstl:if test="${room.status == 'REJECTED'}">
			<spring:message var="status" code='room.status.rejected' />
		</jstl:if>
	
		<div>
			<br>
			<strong><spring:message code="room.status" />: </strong>
			<jstl:out value="${status}" />
		</div>
		<br />
	</jstl:if>
	</div>
	
	<jstl:if test="${not empty room.photos}">
		<div>
			<strong> <spring:message code="room.photos" /> : </strong><br><br>
			<jstl:forEach items="${photos}" var="photo">
				<img src="${photo}" alt="photo" style="margin-bottom: 0.5em; max-height: 150px;" /><br>
			</jstl:forEach>
		</div>
	</jstl:if>
	
</fieldset>
<br>
<jstl:if test="${not empty services}">
	<h2>
		<strong><spring:message code="room.services" /></strong>
	</h2>
	<display:table style="width: 80%" class="displaytag"
		name="services" pagesize="5"
		requestURI="${requestURI}"
		id="service">
		
		<display:column titleKey="service.name">
			<jstl:out value="${service.name}" />
		</display:column>	
		
		<display:column titleKey="service.price">
			<jstl:out value="${service.price}" /> &#8364;
		</display:column>
		
		<display:column>
			<a href="service/display.do?serviceId=${service.id}"> <spring:message
				code="service.details" />
			</a>
		</display:column>
		
		<jstl:if test="${isPrincipal }">
			<jstl:if test="${room.status == 'DRAFT'}">
				<display:column>
					<a href="service/edit.do?serviceId=${service.id}"> <spring:message
						code="service.edit" />
					</a>
				</display:column>
				<display:column>
					<a href="service/delete.do?serviceId=${service.id}"
						onclick="return confirm('<spring:message code="service.confirm.delete"/>')"> 
						<spring:message	code="service.delete" />
					</a>
				</display:column>
			</jstl:if>
			<jstl:if test="${room.status == 'ACTIVE'}">
				<display:column>
					<a href="service/decommission.do?serviceId=${service.id}"
						onclick="return confirm('<spring:message code="service.confirm.decommission"/>')"> 
						<spring:message code="service.decommission" />
					</a>
				</display:column>
			</jstl:if>
		</jstl:if>
	</display:table>
</jstl:if>
<jstl:if test="${room.status == 'DRAFT' or room.status == 'ACTIVE' and isPrincipal}">
	<input type="button"
		onclick="redirect: location.href = 'service/create.do?roomId=${room.id}';"
		value="<spring:message code='service.create' />" /><br>
	<br>
</jstl:if>
<input type="button" name="back"
	value="<spring:message code="mp.back" />"
	onclick="redirect: location.href = 'room/list.do'" />
