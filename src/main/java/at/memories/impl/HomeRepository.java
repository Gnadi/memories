package at.memories.impl;

import at.memories.dao.HomeDao;
import at.memories.model.Home;
import at.memories.model.Post;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped

public class HomeRepository implements HomeDao {
    @Inject
    EntityManager em;

    @Override
    public void addHome(Home home) {
        em.persist(home);
    }

    @Override
    public void addPost(Post post) {
        em.persist(post);
    }
}
