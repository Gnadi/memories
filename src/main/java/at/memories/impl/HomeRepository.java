package at.memories.impl;

import at.memories.dao.HomeDao;
import at.memories.model.Home;
import at.memories.model.Post;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

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

    @Override
    public Home findHomebyUser(Long user) {
        return em.createQuery("SELECT home FROM Home home WHERE home.userId=:user", Home.class)
                .setParameter("user", user).getSingleResult();
    }

    @Override
    @Transactional
    public List<Post> getPostsByPage(int pageNumber, int pageSize, long homeId) {
            TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE p.home.id =:homeId ORDER BY p.id", Post.class)
                    .setParameter("homeId", homeId)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize);

            return query.getResultList();
    }

    @Override
    @Transactional
    public long getTotalPostCount(long homeId) {
            return em.createQuery("SELECT COUNT(p) FROM Post p WHERE p.home.id =:homeId", Long.class)
                    .setParameter("homeId", homeId).getSingleResult();
    }
}
