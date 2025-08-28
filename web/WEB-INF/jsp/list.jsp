<%@ page import="com.javaops.webapp.web.Action" %>
<%@ page import="com.javaops.webapp.model.ContactType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Resumes</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table>
        <tr>
            <th>Имя</th>
            <th>EMAIL</th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.javaops.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=${Action.VIEW}">${resume.fullName}</a></td>
                <td><%=ContactType.EMAIL.convertToString(resume.getContacts().get(ContactType.EMAIL))%>
                </td>
                <td><a href="resume?uuid=${resume.uuid}&action=${Action.DELETE}"><img src="img/delete.png"></a>
                </td>
                <td><a href="resume?uuid=${resume.uuid}&action=${Action.EDIT}"><img src="img/pencil.png"></a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <form action="resume" method="get">
        <input type="hidden" name="action" value="${Action.CREATE}">
        <button type="submit">Создать резюме</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>