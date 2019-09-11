<%--
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<h1>
	<spring:message code="administrator.list.suspicious.actors" />
</h1>
<!-- Tabla de actores sospechosos -->
<display:table pagesize="5" class="displaytag" name="actors"
	requestURI="config/admin/listSA.do" id="actors">

	<jstl:if test="${not empty actors}">
		<jstl:choose>
			<jstl:when test="${actors.userAccount.isBanned }">
				<spring:message var="status" code="actor.banned" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message var="status" code="actor.active" />
			</jstl:otherwise>
		</jstl:choose>
	</jstl:if>

	<display:column property="userAccount.username"
		titleKey="actor.username" sortable="true" />

	<display:column titleKey="actor.status" sortable="true">
		<jstl:out value="${status}" />
	</display:column>

	<display:column property="surname" titleKey="actor.surname"
		sortable="true" />

	<display:column property="name" titleKey="actor.name" sortable="true" />

	<display:column property="email" titleKey="actor.email" />

	<display:column property="userAccount.authorities"
		titleKey="actor.type" />

	<display:column>
		<jstl:choose>
			<jstl:when test="${!actors.userAccount.isBanned}">
				<jstl:if test="${actors.isSpammer}">
					<input type="button" name="ban"
						value="<spring:message code="actor.ban" />"
						onclick="redirect: location.href = 'config/admin/ban.do?actorId=${actors.id}';" />
				</jstl:if>
			</jstl:when>
			<jstl:when test="${actors.userAccount.isBanned}">
				<input type="button" name="unban"
					value="<spring:message code="actor.unban" />"
					onclick="redirect: location.href = 'config/admin/unban.do?actorId=${actors.id}';" />
			</jstl:when>
		</jstl:choose>
	</display:column>
</display:table>
