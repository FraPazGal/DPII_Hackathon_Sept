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

<script>
	function addFieldsPhotos() {
		// Container <div> where dynamic content will be placed
		var container = document.getElementById("container");
		// Create an <input> element, set its type and name attributes
		var input = document.createElement("input");
		input.type = "text";
		input.name = "photos";
		container.appendChild(input);
	}
</script>

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
		<acme:textbox code="room.title" path="title" size="100px" /><br/>
		<acme:textarea code="room.description" path="description" cols="71px" rows="4"/><br/> 
		<acme:textbox code="room.address" path="address" size="100px" /><br/> 
		<acme:textbox code="room.capacity" path="capacity" size="20px"/><br/> 
		<acme:textbox code="room.pricePerHour" path="pricePerHour" size="20px" placeholder="price.placeholder"/><br/> 
		<acme:textbox code="room.openingHour" path="openingHour" size="20px" placeholder="hour.placeholder"/><br/> 
		<acme:textbox code="room.closingHour" path="closingHour" size="20px" placeholder="hour.placeholder"/><br/> 
		<acme:textarea code="room.scheduleDetails" path="scheduleDetails" cols="71px" rows="4"/><br/> 
		<acme:textbox code="room.proveOfOwnership" path="proveOfOwnership" size="100px" /><br/>
		
		<strong><spring:message code="room.categories" /></strong><br>
		<jstl:choose>
			<jstl:when test="${pageContext.response.locale.language == 'es'}">
				<form:select path="categories" name="categoryArray" style="width:200px;" multiple="multiple">
					<jstl:forEach var="category" items="${categories}">
						<option value="${category.id}">
							<jstl:out value="${category.title.get('Español')}" />
						</option>
					</jstl:forEach>
				</form:select><br>
			</jstl:when>
			<jstl:otherwise>
				<form:select path="categories" name="categoryArray" style="width:200px;" multiple="multiple">
					<jstl:forEach var="category" items="${categories}">
						<option value="${category.id}">
							<jstl:out value="${category.title.get('English')}" />
						</option>
					</jstl:forEach>
				</form:select><br>
			</jstl:otherwise>
		</jstl:choose>
		<form:errors cssClass="error" path="categories" /><br>
		
		<strong><spring:message code="room.photos" /></strong><br>
		<button type="button" onClick="addFieldsPhotos()" >
			<spring:message code="room.add" />
		</button>
		<div id="container"></div>
		<jstl:forEach items="${room.photos}" var="photo">
			<input name="photos" value="${photo}" />
		</jstl:forEach>
		<form:errors path="photos" cssClass="error" />
		<br>
	
	</fieldset>
	<br>
	
	<acme:submit code="mp.save" name="save" />&nbsp;
	<acme:submit code="mp.saveFinal" name="saveFinal" />&nbsp;
	<acme:cancel url="room/list.do?range=mineA" code="mp.cancel" />
	<br />
</form:form>
