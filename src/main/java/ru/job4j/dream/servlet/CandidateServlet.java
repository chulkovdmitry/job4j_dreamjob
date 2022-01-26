package ru.job4j.dream.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.dream.config.Config;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CandidateServlet extends HttpServlet {

    private final static String CANDIDATES_PATH = "/candidates.do";
    private final static String CANDIDATE_DELETE_PATH = "/candidate/delete.do";
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (CANDIDATES_PATH.equals(req.getServletPath())) {
            req.setAttribute("candidates", DbStore.instOf().findAllCandidates());
            req.getRequestDispatcher("candidates.jsp").forward(req, res);
        }
        if (CANDIDATE_DELETE_PATH.equals(req.getServletPath())) {
            String id = req.getParameter("id");
            DbStore.instOf().deleteCandidate(Integer.parseInt(id));
            String imagesFolder = Config.getConfig().getProperty("path.images");
            if (!"".equals(imagesFolder)) {
                for (File file : new File(imagesFolder).listFiles()) {
                    if (file.getName().contains(id)) {
                        file.delete();
                        break;
                    }
                }
            }
            res.sendRedirect(req.getContextPath() + CANDIDATES_PATH);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Candidate candidate = GSON.fromJson(req.getReader(), Candidate.class);
        DbStore.instOf().saveCandidate(candidate);
        res.setContentType("application/json; charset=utf-8");
        OutputStream output = res.getOutputStream();
        String json = GSON.toJson(candidate);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}