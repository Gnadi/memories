package at.memories.impl;

import at.memories.dao.HomeDao;
import at.memories.model.Home;
import at.memories.model.Post;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

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

    @Override
    public Home findHomebyAdmin(Long admin) {
        return em.createQuery("SELECT home FROM Home home WHERE home.adminId=:admin", Home.class)
                .setParameter("admin", admin).getSingleResult();
    }
}
