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
		
				<li><a href="room/list.do"><spring:message code="room.list" /></a></li>
			
				<li><a href="room/list.do?range=mineA"><spring:message
							code="room.owner.list.active" /></a></li>
							
				<li><a href="room/list.do?range=mineD"><spring:message
							code="room.owner.list.draft" /></a></li>
							
				<li><a href="room/list.do?range=mineI"><spring:message
							code="room.owner.list.inrev" /></a></li>
							
				<li><a href="room/list.do?range=mineO"><spring:message
							code="room.owner.list.out" /></a></li>
							
				<li><a href="room/list.do?range=mineR"><spring:message
							code="room.owner.list.rejected" /></a></li>
			
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
				<li><a href="room/list.do"><spring:message
								code="room.list" /></a></li>
								
				<li><a href="room/list.do?range=toAssign"><spring:message
								code="room.admin.list.toassign" /></a></li>
								
				<li><a href="room/list.do?range=toReview"><spring:message
								code="room.admin.list.toreview" /></a></li>
								
				<li><a class="fNiv"><spring:message
						code="room.admin.list.reviewed" /></a>
				<ul>
					<li><a href="room/list.do?range=accepted"><spring:message
								code="room.admin.list.accepted" /></a></li>
								
					<li><a href="room/list.do?range=rejected"><spring:message
								code="room.admin.list.rejected" /></a></li>

				</ul></li>
				
		</security:authorize>
	</ul>
</div>
