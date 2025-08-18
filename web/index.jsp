<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Resumes</title>
</head>
<body>
<table>
    <tr>
        <th>UUID</th>
        <th>fullName</th>
    </tr>
    <c:forEach var="resume" items="${resumes}">
        <tr>
            <td>${resume.uuid}</td>
            <td>${resume.fullName}</td>
        </tr>
    </c:forEach>

</table>
</body>
</html>