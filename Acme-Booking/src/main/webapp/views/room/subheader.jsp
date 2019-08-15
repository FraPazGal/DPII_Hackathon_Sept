<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<ul id="jSubMenu">
		
		<security:authorize access="hasRole('OWNER')">
				<li><a href="room/list.do"><spring:message
								code="room.list" /></a></li>
			
				<li><a class="fNiv"><spring:message
						code="room.room.list.owner" /></a>
				<ul>
					<li><a href="room/list.do?range=mineD"><spring:message
								code="room.room.list.draft" /></a></li>
								
					<li><a href="room/list.do?range=mineA"><spring:message
								code="room.room.list.active" /></a></li>
								
					<li><a href="room/list.do?range=mineO"><spring:message
								code="room.room.list.decomissioned" /></a></li>

				</ul></li>
				
		</security:authorize>
	</ul>
</div>
