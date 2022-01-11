package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.Candidate;

public class MainStore {
    public static void main(String[] args) {
        Store store = DbStore.instOf();
        store.savePost(new Post(0, "Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        store.saveCandidate(new Candidate(0, "Junior Java"));
        store.saveCandidate(new Candidate(0, "Middle Java"));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
    }
}
