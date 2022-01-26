package ru.job4j.dream.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostServletTest {

    @Test
    public void whenCreatePost() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(new Post(0, "New post"));
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(res.getOutputStream()).thenReturn(mock(ServletOutputStream.class));
        new PostServlet().doPost(req, res);
        Post post = DbStore.instOf().findPostById(1);
        assertThat(post, notNullValue());
    }

    @Test
    public void whenEditPost() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        DbStore.instOf().savePost(new Post(0, "New post"));
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(new Post(1, "Updated post"));
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(res.getOutputStream()).thenReturn(mock(ServletOutputStream.class));
        new PostServlet().doPost(req, res);
        Post post = DbStore.instOf().findPostById(1);
        assertThat(post.getName(), is("Updated post"));
    }
}