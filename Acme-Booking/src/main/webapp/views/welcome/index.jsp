<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<security:authorize access="isAuthenticated()">
	<jstl:if test="${breachNotif ne null }">
		<jstl:if test="${pageContext.response.locale.language == 'es'}">
			<h1>
				<strong style="color: red;"><jstl:out value="${breachNotif.get('Espa�ol')}"/></strong>
			</h1>
		</jstl:if>
		<jstl:if test="${pageContext.response.locale.language == 'en'}">
			<h1>
				<strong style="color: red;"><jstl:out value="${breachNotif.get('English')}"/></strong>
			</h1>
		</jstl:if>
	</jstl:if>
</security:authorize>

<jstl:if test="${pageContext.response.locale.language == 'es'}">
	<h3>
		<jstl:out value="${welcomeMsg.get('Espa�ol')}"/>
	</h3>
</jstl:if>
<jstl:if test="${pageContext.response.locale.language == 'en'}">
	<h3>
		<jstl:out value="${welcomeMsg.get('English')}"/>
	</h3>
</jstl:if>

<p>
	<spring:message code="welcome.greeting.prefix" />
	<jstl:out value="${name}"/>!
</p>

<p>
	<spring:message code="welcome.greeting.current.time" />
	<jstl:out value="${moment}"/>
</p>
