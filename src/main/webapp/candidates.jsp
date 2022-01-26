<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<jsp:include page="header.jsp" />
<body>
<div class="container">
    <jsp:include page="navbar.jsp" />
    <div class="row pt-3">
        <div class="card" style="width: 100%">
            <div class="card-header">
                Кандидаты
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Имя</th>
                        <th scope="col">Фото</th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${candidates}" var="candidate">
                        <tr>
                            <td>
                                <a href='<c:url value="/candidate/edit.jsp?id=${candidate.id}"/>'>
                                    <i class="fa fa-edit mr-3"></i>
                                </a>
                                <c:out value="${candidate.name}"/>
                            </td>
                            <td>
                                <img src="<c:url value='/download?id=${candidate.id}'/>" width="100px" height="100px"/>
                            </td>
                            <td>
                                <a class="btn btn-primary btn-sm" href="<c:url value='/upload.jsp?id=${candidate.id}' />">Добавить фото</a>
                                <a class="btn btn-danger btn-sm" href="<c:url value='/candidate/delete.do?id=${candidate.id}'/>">Удалить кандидата</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>