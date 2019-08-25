<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>




<h1><spring:message code="administrator.statistics" /></h1>	
<table class="displayStyle" style="width:800px; line-height: 30px; font-size: 18px; " >

	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsBookingsPerRoom" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsBookingsPerRoom[0] ne null}">
			<tr>
				<td style=""><spring:message code="administrator.statsBookingsPerRoom.max" /></td>
				<td> ${statsBookingsPerRoom[0]}</td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsBookingsPerRoom.min" /></td>
				<td> ${statsBookingsPerRoom[1]}</td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsBookingsPerRoom.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsBookingsPerRoom[2]}"/></td>
			</tr>
			
			<tr>
				
				<td><spring:message code="administrator.statsBookingsPerRoom.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsBookingsPerRoom[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsServicesPerRoom" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsServicesPerRoom[0] ne null}">
			<tr>
				<td style=""><spring:message code="administrator.statsServicesPerRoom.max" /></td>
				<td> ${statsServicesPerRoom[0]}</td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsServicesPerRoom.min" /></td>
				<td> ${statsServicesPerRoom[1]}</td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsServicesPerRoom.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsServicesPerRoom[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsServicesPerRoom.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsServicesPerRoom[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsPricePerRoom" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsPricePerRoom[0] ne null}">
			<tr>
				<td style=""><spring:message code="administrator.statsPricePerRoom.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsPricePerRoom[0]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsPricePerRoom.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsPricePerRoom[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsPricePerRoom.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsPricePerRoom[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsPricePerRoom.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsPricePerRoom[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>

	<tr><td><br></td></tr>
	
	<jstl:choose>
		<jstl:when test="${ratioRevisionPendingByFinalRooms ne null}">
			<tr>
				<td style=""><strong><spring:message code="administrator.ratioRevisionPendingByFinalRooms" /></strong></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${ratioRevisionPendingByFinalRooms}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<jstl:choose>
		<jstl:when test="${ratioAcceptedByFinalRooms ne null}">
			<tr>
				<td style=""><strong><spring:message code="administrator.ratioAcceptedByFinalRooms" /></strong></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${ratioAcceptedByFinalRooms}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<jstl:choose>
		<jstl:when test="${ratioRejectedByFinalRooms ne null}">
			<tr>
				<td style=""><strong><spring:message code="administrator.ratioRejectedByFinalRooms" /></strong></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${ratioRejectedByFinalRooms}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<jstl:choose>
		<jstl:when test="${ratioRoomsOutOfService ne null}">
			<tr>
				<td style=""><strong><spring:message code="administrator.ratioRoomsOutOfService" /></strong></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${ratioRoomsOutOfService}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<jstl:choose>
		<jstl:when test="${topCategoryByRooms ne null}">
			<tr>
				<td style=""><strong><spring:message code="administrator.topCategoryByRooms" /></strong></td>
				<td> 
					<jstl:choose>
						<jstl:when test="${pageContext.response.locale.language == 'es'}">
							<jstl:out value="${topCategoryByRooms.title.get('Español')}" />
						</jstl:when>
						<jstl:otherwise>
							<jstl:out value="${topCategoryByRooms.title.get('English')}" />
						</jstl:otherwise>
					</jstl:choose>
				</td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsFinder" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsFinder[0] ne null}">
			<tr>
				<td style=""><spring:message code="administrator.statsFinder.max" /></td>
				<td> ${statsFinder[0]}</td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsFinder.min" /></td>
				<td> ${statsFinder[1]}</td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsFinder.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsFinder[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsFinder.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsFinder[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<jstl:choose>
		<jstl:when test="${ratioFindersEmpty ne null}">
			<tr>
				<td style=""><strong><spring:message code="administrator.ratioFindersEmpty" /></strong></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${ratioFindersEmpty}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
</table>
