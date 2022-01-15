package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (DbStore.instOf().findUserByEmail(email) != null) {
            req.setAttribute("error", "Email занят");
            req.getRequestDispatcher("reg.jsp").forward(req, res);
        } else {
            DbStore.instOf().saveUser(new User(name, email, password));
            HttpSession session = req.getSession();
            session.setAttribute("user", DbStore.instOf().findUserByEmail(email));
            res.sendRedirect(req.getContextPath() + "/post.do");
        }
    }
}
