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
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<com.javaops.webapp.model.SectionType, com.javaops.webapp.model.Section>"/>
        <div><%=HtmlHelper.sectionToHtml(sectionEntry.getKey(), sectionEntry.getValue())%>
        </div>
        <br/>
    </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>