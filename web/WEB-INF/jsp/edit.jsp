<%@ page import="com.javaops.webapp.model.SectionType" %>
<%@ page import="com.javaops.webapp.util.HtmlHelper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <jsp:useBean id="resume" type="com.javaops.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume?action=${param.action}" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}" required></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="${contactTypeValues}">
            <dl>
                <dt>${type.typeName}</dt>
                <dd><input type="text" name="${type.name()}" size=100 value="${resume.contacts[type]}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="sectionType" items="${sectionTypeValues}">
            <h3>${sectionType.title}</h3><br/>
            <c:choose>
                <c:when test="${sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)}">
                    <c:forEach var="block" items="${companyBlocks[sectionType]}" varStatus="blockCounter">
                        <input type="text" name="${sectionType.name()}title${blockCounter.index}" size=100
                               value="${block.title}" required><br/>
                        <input type="text" name="${sectionType.name()}url${blockCounter.index}" size=100
                               value="${block.url}"><br/>
                        <c:forEach var="period" items="${block.periods}" varStatus="counter">
                            <label for="start${counter.index}">Дата начала:</label>
                            <input type="month" id="start${counter.index}"
                                   name="${sectionType.name()}start${counter.index}"
                                   value="${HtmlHelper.convertDateToInputFormat(period.start)}" required>

                            <label for="end${counter.index}">Дата окончания:</label>
                            <input type="month" id="end${counter.index}"
                                   name="${sectionType.name()}end${counter.index}"
                                   value="${HtmlHelper.convertDateToInputFormat(period.end)}"><br/>

                            <input type="text" name="${sectionType.name()}periodTitle${counter.index}" size=100
                                   value="${period.title}" required><br/>
                            <textarea name="${sectionType.name()}periodText${counter.index}"
                                      class="section-text">${period.text}</textarea><br/>
                        </c:forEach>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                <textarea name="${sectionType.name()}"
                          class="section-text">${resume.sections[sectionType] != null ? HtmlHelper.convertSection(resume.sections[sectionType]) : ""}</textarea><br/>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
