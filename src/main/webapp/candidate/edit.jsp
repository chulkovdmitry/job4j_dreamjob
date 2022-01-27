<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.store.DbStore" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<!doctype html>
<html lang="en">
<jsp:include page="../header.jsp" />
<body>
<div class="container">
    <jsp:include page="../navbar.jsp" />
    <%
        String id = request.getParameter("id");
        Candidate candidate = new Candidate(0, "");
        if (id != null) {
            candidate = DbStore.instOf().findCandidateById(Integer.parseInt(id));
        }
    %>
    <div class="row pt-3">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат
                <% } else { %>
                Редактирование кандидата
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post" id="form">
                    <div id="error" class="alert alert-danger" style="display: none">Не удалось сохранить кандидата</div>
                    <input type="hidden" id="id" value="<%=candidate.getId()%>">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" name="name" value="<%=candidate.getName()%>" id="name">
                        <small class="form-text text-muted" style="color: red!important; display: none;">Заполните поле</small>
                    </div>
                    <input type="hidden" id="cityId" value="<%=candidate.getCityId()%>">
                    <div class="form-group">
                        <label>Город</label>
                        <select class="form-control" id="cities">
                            <option></option>
                        </select>
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
        $.ajax({
            type: 'GET',
            url: 'http://${location.host}/dreamjob/cities',
            dataType: 'json'
        }).done(function (data) {
            for (let city of data) {
                $('#cities').append(`<option value="${city.id}">${city.name}</option>`)
            }
            const cityId = $("#cityId").val();
            if (cityId !== "") {
                $(`#cities option[value='${cityId}']`).prop('selected', true)
            }
        }).fail(function (err) {
            console.log(err);
        });
        $("#form").submit(function (e) {
            e.preventDefault();
            const url = `http://${location.host}/dreamjob/candidates.do`;
            const nameInput = $("#name");
            const cityOption = $("#cities option:selected");
            if (nameInput.val() === "") {
                nameInput.addClass("is-invalid");
                nameInput.next().show();
            }
            if (cityOption.val() === "") {
                cityOption.parent().addClass("is-invalid");
                cityOption.parent().next().show();
            }
            if (nameInput.val() !== "" && cityOption.val() !== "") {
                $.ajax({
                    type: 'POST',
                    url: url,
                    data: JSON.stringify({
                        id: $("#id").val(),
                        name: nameInput.val(),
                        cityId: cityOption.val()
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