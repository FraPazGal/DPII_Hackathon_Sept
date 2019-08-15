<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('OWNER')">
	<jstl:if test="${room.id == 0 }">
		<h1><spring:message	code="room.create" /></h1>
	</jstl:if>
	<jstl:if test="${room.id != 0 }">
		<h1><spring:message	code="room.title.edit" /></h1>
	</jstl:if>
	<form:form modelAttribute="room" action="room/edit.do" id="form">
	
		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="room.legend" />
			</legend>
			<br/>
	
			<form:hidden path="id" />
			<acme:textbox code="room.title" path="title" size="100px" /><br/> <br/>
			<acme:textarea code="room.description" path="description" cols="71px" rows="4"/><br/> <br/>
			<acme:textbox code="room.address" path="address" size="100px" /><br/> <br/>
			<acme:textbox code="room.capacity" path="capacity" size="20px"/><br/> <br/>
			<acme:textbox code="room.pricePerHour" path="pricePerHour" size="20px" placeholder="price.placeholder"/><br/> <br/>
			<acme:textbox code="room.openingHour" path="openingHour" size="20px" placeholder="hour.placeholder"/><br/> <br/>
			<acme:textbox code="room.closingHour" path="closingHour" size="20px" placeholder="hour.placeholder"/><br/> <br/>
			<acme:textarea code="room.scheduleDetails" path="scheduleDetails" cols="71px" rows="4"/><br/> <br/>
			
			<spring:message code="room.category" />
			<jstl:choose>
				<jstl:when test="${pageContext.response.locale.language == 'es'}">
					<form:select path="category" name="categoryArray" style="width:200px;">
						<jstl:forEach var="category" items="${categories}">
							<option value="${category.id}" >
								<jstl:out value="${category.title.get('Español')}" />
							</option>
						</jstl:forEach>
					</form:select><br><br>
				</jstl:when>
				<jstl:otherwise>
					<form:select path="category" name="categoryArray" style="width:200px;">
						<jstl:forEach var="category" items="${categories}">
							<option value="${category.id}" >
								<jstl:out value="${category.title.get('English')}" />
							</option>
						</jstl:forEach>
					</form:select><br><br>
				</jstl:otherwise>
			</jstl:choose>
			
		</fieldset>
		<br><br>
		
		<acme:submit code="mp.save" name="save" />&nbsp;
		<acme:cancel url="room/list.do?range=mineA" code="mp.cancel" />
		<br />
	</form:form>
</security:authorize>