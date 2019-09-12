
<%--
 * login.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<h1>
	<spring:message code="finder.title" />
</h1>
<spring:message code="date.dateFormat" var="format" />
<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${empty anon }">

		<form:form action="finder/customer/search.do" modelAttribute="finder"
			id="form">

			<form:hidden path="id" />
			<acme:textbox code="finder.keyWord" path="keyWord" size="50px" />
			<br />
			<br />
			<acme:textbox code="finder.service" path="service" size="50px" />
			<br />
			<br />

			<acme:textbox code="finder.maximumFee" path="maximumFee" size="50px"
				placeholder="price.placeholder" />
			<br />
			<br />
			<acme:textbox code="finder.capacity" path="capacity" size="50px" />
			<br />
			<br />
			<form:label path="category">
				<spring:message code="finder.category" />:</form:label>
			<form:select path="category">
				<form:option label="----" value="" />
				<jstl:forEach var="cat" items="${categories}">
					<jstl:if test="${pageContext.response.locale.language == 'es'}">
						<form:option value="${cat.title.get('Español')}" label="${cat.title.get('Español')}" />
					</jstl:if>
					<jstl:if test="${pageContext.response.locale.language == 'en'}">
						<form:option value="${cat.title.get('Español')}" label="${cat.title.get('English')}" />
					</jstl:if>
				</jstl:forEach>
			</form:select>
			<form:errors path="category" cssClass="error" />
			<br />
			<br>

			<input type="submit" name="save" id="save"
				value="<spring:message code="finder.showResults" />" />
		
	&#160;
		<jstl:if test="${finder.id!=0}">
				<input type="submit" name="delete" id="delete"
					value='<spring:message code="mp.clear"/>' />
			</jstl:if>

		</form:form>

		<security:authorize access="permitAll">
			<jstl:choose>
				<jstl:when test="${range eq null }">
					<h1>
						<spring:message code="room.list" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineD' }">
					<h1>
						<spring:message code="room.owner.list.draft" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineO' }">
					<h1>
						<spring:message code="room.owner.list.out" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineI' }">
					<h1>
						<spring:message code="room.owner.list.inrev" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineR' }">
					<h1>
						<spring:message code="room.owner.list.rejected" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineA' }">
					<h1>
						<spring:message code="room.owner.list.active" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'toReview' }">
					<h1>
						<spring:message code="room.admin.list.toreview" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'toAssign' }">
					<h1>
						<spring:message code="room.admin.list.toassign" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'accepted' }">
					<h1>
						<spring:message code="room.admin.list.accepted" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'rejected' }">
					<h1>
						<spring:message code="room.admin.list.rejected" />
					</h1>
				</jstl:when>
			</jstl:choose>

			<jstl:choose>
				<jstl:when test="${isPrincipal == 'OWNER'}">

					<display:table class="displaytag" name="rooms" pagesize="5"
						requestURI="finder/customer/list.do" id="room" style="width:80%">

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
						<jstl:if test="${room.status == 'DRAFT'}">
							<display:column>
								<a href="room/edit.do?roomId=${room.id}"> <spring:message
										code="mp.edit" />
								</a>
							</display:column>
							<display:column>
								<a href="room/delete.do?roomId=${room.id}"
									onclick="return confirm('<spring:message code="room.confirm.delete"/>')">
									<spring:message code="mp.delete" />
								</a>
							</display:column>
						</jstl:if>
						<jstl:if test="${ room.status == 'ACTIVE'}">
							<display:column>
								<a href="room/editA.do?roomId=${room.id}"> <spring:message
										code="mp.edit" />
								</a>
							</display:column>
							<display:column>
								<a id="decomission"
									href="room/action.do?action=decommission&roomId=${room.id}">
									<spring:message code="room.decommission" />
								</a>
							</display:column>
						</jstl:if>
					</display:table>

					<jstl:if test="${range == 'mineA' or range == 'mineD'}">
						<jstl:if test="${empty rooms}">
							<br>
							<br>
						</jstl:if>
						<input type="button"
							onclick="redirect: location.href = 'room/create.do';"
							value="<spring:message code='room.create' />" />
					</jstl:if>

				</jstl:when>
				<jstl:when test="${isPrincipal == 'ADMIN'}">

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
						<jstl:if test="${room.status == 'REVISION-PENDING'}">
							<jstl:if test="${room.administrator eq null}">
								<display:column>
									<a href="room/action.do?action=assign&roomId=${room.id}"> <spring:message
											code="room.assign" />
									</a>
								</display:column>
							</jstl:if>
							<jstl:if test="${room.administrator ne null}">
								<display:column>
									<a href="room/action.do?action=accept&roomId=${room.id}"> <spring:message
											code="room.accept" />
									</a>
								</display:column>

								<display:column>
									<a href="room/action.do?action=reject&roomId=${room.id}"> <spring:message
											code="room.reject" />
									</a>
								</display:column>
							</jstl:if>
						</jstl:if>
					</display:table>

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
							<a href="owner/display.do?ownerId=${room.owner.id}"> <spring:message
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
	</jstl:if>
</security:authorize>

<security:authorize access="permitAll">
	<jstl:if test="${! empty anon }">


		<form>
			<b><spring:message code="finder.enterKey" />&#160;</b> <input
				id="test" type="text" name="keyWord" size="20" />

			<script>
				var keyWord = "";
				document.getElementById("test").value = keyWord;
			</script>

			<input type="submit" value="Search" name="submit" />

		</form>

		<br>
		<br>

		<security:authorize access="permitAll">
			<jstl:choose>
				<jstl:when test="${range eq null }">
					<h1>
						<spring:message code="room.list" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineD' }">
					<h1>
						<spring:message code="room.owner.list.draft" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineO' }">
					<h1>
						<spring:message code="room.owner.list.out" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineI' }">
					<h1>
						<spring:message code="room.owner.list.inrev" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineR' }">
					<h1>
						<spring:message code="room.owner.list.rejected" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'mineA' }">
					<h1>
						<spring:message code="room.owner.list.active" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'toReview' }">
					<h1>
						<spring:message code="room.admin.list.toreview" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'toAssign' }">
					<h1>
						<spring:message code="room.admin.list.toassign" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'accepted' }">
					<h1>
						<spring:message code="room.admin.list.accepted" />
					</h1>
				</jstl:when>
				<jstl:when test="${range == 'rejected' }">
					<h1>
						<spring:message code="room.admin.list.rejected" />
					</h1>
				</jstl:when>
			</jstl:choose>

			<jstl:choose>
				<jstl:when test="${isPrincipal == 'OWNER'}">

					<display:table class="displaytag" name="rooms" pagesize="5"
						requestURI="finder/anon/search.do" id="room" style="width:80%">

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
						<jstl:if test="${room.status == 'DRAFT'}">
							<display:column>
								<a href="room/edit.do?roomId=${room.id}"> <spring:message
										code="mp.edit" />
								</a>
							</display:column>
							<display:column>
								<a href="room/delete.do?roomId=${room.id}"
									onclick="return confirm('<spring:message code="room.confirm.delete"/>')">
									<spring:message code="mp.delete" />
								</a>
							</display:column>
						</jstl:if>
						<jstl:if test="${ room.status == 'ACTIVE'}">
							<display:column>
								<a href="room/editA.do?roomId=${room.id}"> <spring:message
										code="mp.edit" />
								</a>
							</display:column>
							<display:column>
								<a id="decomission"
									href="room/action.do?action=decommission&roomId=${room.id}">
									<spring:message code="room.decommission" />
								</a>
							</display:column>
						</jstl:if>
					</display:table>

					<jstl:if test="${range == 'mineA' or range == 'mineD'}">
						<jstl:if test="${empty rooms}">
							<br>
							<br>
						</jstl:if>
						<input type="button"
							onclick="redirect: location.href = 'room/create.do';"
							value="<spring:message code='room.create' />" />
					</jstl:if>

				</jstl:when>
				<jstl:when test="${isPrincipal == 'ADMIN'}">

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
						<jstl:if test="${room.status == 'REVISION-PENDING'}">
							<jstl:if test="${room.administrator eq null}">
								<display:column>
									<a href="room/action.do?action=assign&roomId=${room.id}"> <spring:message
											code="room.assign" />
									</a>
								</display:column>
							</jstl:if>
							<jstl:if test="${room.administrator ne null}">
								<display:column>
									<a href="room/action.do?action=accept&roomId=${room.id}"> <spring:message
											code="room.accept" />
									</a>
								</display:column>

								<display:column>
									<a href="room/action.do?action=reject&roomId=${room.id}"> <spring:message
											code="room.reject" />
									</a>
								</display:column>
							</jstl:if>
						</jstl:if>
					</display:table>

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
							<a href="owner/display.do?ownerId=${room.owner.id}"> <spring:message
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
	</jstl:if>

</security:authorize>