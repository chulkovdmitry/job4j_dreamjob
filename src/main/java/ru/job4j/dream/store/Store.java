package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;


import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    void savePost(Post post);

    Post findPostById(int id);

    Collection<Candidate> findAllCandidates();

    void saveCandidate(Candidate candidate);

    Candidate findCandidateById(int id);

    void deleteCandidate(int id);

    Collection<User> findAllUsers();

    void saveUser(User user);

    void deleteUser(int id);

    User findByIdUser(int id);
}