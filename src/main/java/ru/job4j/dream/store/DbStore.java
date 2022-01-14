package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class DbStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOG = LoggerFactory.getLogger(DbStore.class.getName());

    private DbStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        DbStore.class.getClassLoader()
                                .getResourceAsStream("db.properties")
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new DbStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to execute findAllPosts", e);
        }
        return posts;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public Post findPostById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to find Post", e);
        }
        return null;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from candidate")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    candidates.add(new Candidate(resultSet.getInt("id"),
                            resultSet.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to execute findAllCandidates", e);
        }
        return candidates;
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    @Override
    public Candidate findCandidateById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to find Candidate", e);
        }
        return null;
    }

    @Override
    public void deleteCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.info("Unable to delete Candidate", e);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to create Post", e);
        }
        return post;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE post SET name = ? where id = ?")) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.info("Unable to update Post", e);
        }
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to create Candidate", e);
        }
        return candidate;
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = ? where id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.info("Unable to update Candidate", e);
        }
    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(new User(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("public Collection<User> findAllUsers()", e);
        }
        return users;
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() == 0) {
            createUser(user);
        } else {
            updateUser(user);
        }
    }

    private User createUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO users (name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("private User createUser(User user)", e);
        }
        return user;
    }

    private void updateUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE users SET name = ?, email = ?, password = ?, where id = ?")) {
            ps.setString(1, user.getName());
            ps.setInt(2, user.getId());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("private void updateUser(User user)", e);
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("public void deleteUser(int id)", e);
        }
    }

    @Override
    public User findByIdUser(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new User(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("public User findByIdUser(int id)", e);
        }
        return null;
    }

}
