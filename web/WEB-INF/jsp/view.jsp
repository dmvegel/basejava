<%@ page import="com.javaops.webapp.model.CompanySection" %>
<%@ page import="com.javaops.webapp.model.ListSection" %>
<%@ page import="com.javaops.webapp.model.SectionType" %>
<%@ page import="com.javaops.webapp.model.TextSection" %>
<%@ page import="com.javaops.webapp.web.Action" %>
<%@ page import="com.javaops.webapp.util.HtmlHelper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=${Action.EDIT}"><img
            src="img/pencil.png"></a></h2>
    <c:forEach var="contactEntry" items="${resume.contacts}">
        <jsp:useBean id="contactEntry"
                     type="java.util.Map.Entry<com.javaops.webapp.model.ContactType, java.lang.String>"/>
        <div><%=contactEntry.getKey().convertToString(contactEntry.getValue())%>
        </div>
        <br/>
    </c:forEach>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <c:set var="sectionType" value="${sectionEntry.key}"/>
        <c:set var="sectionValue" value="${sectionEntry.value}"/>
        <jsp:useBean id="sectionValue" type="com.javaops.webapp.model.Section"/>
        <h3>${sectionType.title}</h3>
        <c:if test="${sectionType.equals(SectionType.PERSONAL) || sectionType.equals(SectionType.OBJECTIVE)}">
            <div><%=((TextSection) sectionValue).getText()%>
            </div>
        </c:if>
        <c:if test="${sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATIONS)}">
            <div>
                <ul>
                    <c:forEach var="text" items="<%=((ListSection) sectionValue).getTexts()%>">
                        <li>${text}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <c:if test="${sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)}">
            <div>
                <c:forEach var="block" items="<%=((CompanySection) sectionValue).getBlocks()%>">
                    <c:choose>
                        <c:when test="${empty block.url}">
                            <div>${block.title}</div>
                        </c:when>
                        <c:otherwise>
                            <div><a href="${block.url}">${block.title}</a></div>
                        </c:otherwise>
                    </c:choose>
                    <br/>
                    <c:forEach var="period" items="${block.periods}">
                        <div>${HtmlHelper.convertDate(period.start)} - ${HtmlHelper.convertDate(period.end)}
                            <b>${period.title}</b>
                        </div>
                        <br/>
                        <c:choose>
                            <c:when test="${not empty period.text}">
                                <div>${period.text}</div>
                                <br/>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    <br/>
                </c:forEach>
            </div>
        </c:if>
        <br/>
    </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>