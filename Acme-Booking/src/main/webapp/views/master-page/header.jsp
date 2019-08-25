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
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme-Booking Co., Inc."
		style="margin-bottom: 0.5em; max-height: 150px;" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->


		<security:authorize access="permitAll">
			<li><a class="fNiv"><spring:message code="master.page.room" /></a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="!hasAnyRole('OWNER','ADMIN')">
						<li><a href="room/list.do"><spring:message
									code="master.page.room.list" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('OWNER')">
						<li><a href="room/list.do"><spring:message
									code="room.list" /></a></li>
						<li><a href="room/list.do?range=mineA"><spring:message
									code="room.owner.list.owner" /></a></li>
						<li><a href="room/create.do"><spring:message
									code="master.page.room.new" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('ADMIN')">
						<li><a href="room/list.do"><spring:message
									code="room.list" /></a></li>
						<li><a href="room/list.do?range=toReview"><spring:message
									code="room.owner.list.owner" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('CUSTOMER')">
						<li><a href="finder/customer/list.do"><spring:message
									code="master.page.finder" /></a></li>
					</security:authorize>

					<li><a href="finder/anon/search.do"><spring:message
								code="master.page.finder.search" /></a></li>

				</ul></li>


		</security:authorize>
		<security:authorize access="hasAnyRole('OWNER','CUSTOMER')">
			<li><a class="fNiv"><spring:message
						code="master.page.booking" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="booking/list.do"><spring:message
								code="master.page.booking.list" /></a></li>
				</ul></li>

		</security:authorize>
		<security:authorize access="hasRole('ADMIN')">
			<!-- Register admin -->
			<li><a class="fNiv"><spring:message
						code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/register.do"><spring:message
								code="master.page.register.admin" /></a></li>
				</ul></li>

		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"><spring:message
						code="master.page.singup" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="customer/register.do"><spring:message
								code="master.page.register.customer" /></a></li>
					<li><a href="owner/register.do"><spring:message
								code="master.page.register.owner" /></a></li>
				</ul></li>

			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
		</security:authorize>

		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.categories" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="category/list.do"><spring:message
								code="master.page.categories.list" /></a></li>
					<li><a href="category/create.do"><spring:message
								code="master.page.categories.new" /></a></li>
				</ul></li>
			<li><a href="statistics/display.do"><spring:message
						code="master.page.administrator.statistics" /></a></li>

			<li><a href="config/admin/display.do"><spring:message
						code="master.page.system" /></a></li>
			<li><a href="config/admin/listSA.do"><spring:message
						code="master.page.list.suspicious" /></a></li>
		</security:authorize>
		<security:authorize access="isAuthenticated()">
			<!-- Register admin -->
			<li><a class="fNiv"><spring:message
						code="master.page.messages" /></a>
				<ul>
					<li class="arrow"></li>

					<li><a href="messagebox/list.do"><spring:message
								code="master.page.messages.myBoxes" /></a></li>
					<li><a href="messagebox/create.do"><spring:message
								code="master.page.messages.newbox" /></a></li>
					<li><a href="message/actor/create.do"><spring:message
								code="master.page.messages.new" /></a></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="message/administrator/broadcast.do"><spring:message
									code="master.page.messages.broadcsat" /></a></li>
					</security:authorize>

				</ul></li>

		</security:authorize>
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="administrator/display.do"><spring:message
									code="master.page.actor.view" /></a></li>
						<li><a href="administrator/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="administrator/export.do"><spring:message
									code="master.page.export" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('CUSTOMER')">
						<li><a href="customer/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="customer/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="customer/export.do"><spring:message
									code="master.page.export" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('OWNER')">
						<li><a href="owner/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="owner/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="owner/export.do"><spring:message
									code="master.page.export" /></a></li>
					</security:authorize>

					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
	</ul>
</div>

<div style="float: right;">

	<a href="?language=en"><img style="width: 20px; height: 15px"
		src="https://upload.wikimedia.org/wikipedia/en/thumb/a/ae/Flag_of_the_United_Kingdom.svg/1280px-Flag_of_the_United_Kingdom.svg.png"
		alt="EN"></a> <span>|</span> <a href="?language=es"><img
		style="width: 20px; height: 15px;"
		src="http://www.ahb.es/m/100150RES.jpg" alt="ES"></a>
</div>

<security:authorize access="isAuthenticated()">
	<jstl:if test="${breachNotification ne null }">
		<jstl:if test="${pageContext.response.locale.language == 'es'}">
			<h2>
				<strong style="color: red;"><jstl:out
						value="${breachNotification.get('Español')}" /></strong>
			</h2>
		</jstl:if>
		<jstl:if test="${pageContext.response.locale.language == 'en'}">
			<h2>
				<strong style="color: red;"> <jstl:out
						value="${breachNotification.get('English')}" /></strong>
			</h2>
		</jstl:if>
	</jstl:if>
</security:authorize>

