<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<h1><spring:message	code="category.title.edit" /></h1>
<form:form action="category/edit.do" modelAttribute="category" id="form">
	
	<fieldset style="width: 25%">
		<legend style="font-size: 21px">
			<spring:message code="category.legend" />
		</legend>
		<br/>
		<form:hidden path="id" />
	
		<strong><spring:message code="category.title.es" /></strong><br>
		<input type="text" name="nameES" id="nameES" value="${nameES}" size="50px"
			placeholder="<spring:message code='category.edit.placeholder.es' />">
		<br/><br />
	
		<strong><spring:message code="category.title.en" /></strong><br>
		<input type="text" name="nameEN" id="nameEN" value="${nameEN}" size="50px"
			placeholder="<spring:message code='category.edit.placeholder.en' />">
		<br>
		<form:errors cssClass="error" path="title" /><br>
	
		<jstl:if test="${category.id == 0 }">
			<jstl:choose>
				<jstl:when test="${pageContext.response.locale.language == 'es'}">
					<strong><form:label path="parentCategory"><spring:message code="category.parentCategory" /></form:label></strong><br>
					<form:select path="parentCategory" style="width:200px;">
						<jstl:forEach var="parentCategory" items="${categories}">
							<form:option value="${parentCategory}" label="${parentCategory.title.get('Español')}" />
						</jstl:forEach>
					</form:select><br>
				</jstl:when>
				<jstl:otherwise>
					<strong><form:label path="parentCategory"><spring:message code="category.parentCategory" /></form:label></strong><br>
					<form:select path="parentCategory" style="width:200px;">
					<jstl:set var="lol" value="<spring:message code='category.no.parent' />"/>
						<jstl:forEach var="parentCategory" items="${categories}">
							<form:option value="${parentCategory}" label="${parentCategory.title.get('English')}" />
						</jstl:forEach>
					</form:select><br>
				</jstl:otherwise>
			</jstl:choose><br>
		</jstl:if>
		
		</fieldset>
		<br><br>
		<acme:submit code="mp.save" name="save"/>&nbsp;
		<acme:cancel code="mp.cancel" url="category/list.do"/><br/>
</form:form>
