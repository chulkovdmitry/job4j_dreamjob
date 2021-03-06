package ru.job4j.dream.servlet;

import ru.job4j.dream.config.Config;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class RemoveServlet extends HttpServlet {
    String imagePath = Config.getConfig().getProperty("path.images");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("candidates", DbStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        DbStore.instOf().deleteCandidate (Integer.parseInt(req.getParameter("id")));
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
        File file = new File(imagePath + req.getParameter("id") + ".jpg");
        file.delete();
    }
}
