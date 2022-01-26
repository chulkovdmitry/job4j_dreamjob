<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Post" %>
<%@ page import="ru.job4j.dream.store.DbStore" %>
<%@ page import="java.time.LocalDateTime" %>
<!doctype html>
<jsp:include page="../header.jsp" />
<body>
<div class="container">
    <jsp:include page="../navbar.jsp" />
    <%
        String id = request.getParameter("id");
        Post post = new Post(0, "");
        if (id != null) {
            post = DbStore.instOf().findPostById(Integer.parseInt(id));
        }
    %>
    <div class="row pt-3">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новая вакансия
                <% } else { %>
                Редактирование вакансии
                <% } %>
            </div>
            <div class="card-body">
                <form id="form" action="<%=request.getContextPath()%>/posts.do?id=<%=post.getId()%>" method="post">
                    <div id="error" class="alert alert-danger" style="display: none">Не удалось сохранить вакансию</div>
                    <input type="hidden" id="id" value="<%=post.getId()%>">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" name="name" value="<%=post.getName()%>" id="name">
                        <small class="form-text text-muted" style="color: red!important; display: none;">Заполните поле</small>
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script>
    $(document).ready(function () {
        $("#form").submit(function (e) {
            e.preventDefault();
            const url = `http://${location.host}/dreamjob/posts.do`;
            const nameInput = $("#name");
            if (nameInput.val() === "") {
                nameInput.addClass("is-invalid");
                nameInput.next().show();
            }
            if (nameInput.val() !== "") {
                $.ajax({
                    type: 'POST',
                    url: url,
                    data: JSON.stringify({
                        id: $("#id").val(),
                        name: nameInput.val(),
                    }),
                    dataType: 'json'
                }).done(function (data) {
                    window.location.replace(url);
                }).fail(function (err) {
                    console.log(err);
                    $("#error").show();
                });
            }
        });
    });
</script>
</body>
</html>