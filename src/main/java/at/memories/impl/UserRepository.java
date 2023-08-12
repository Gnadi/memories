package at.memories.impl;

import at.memories.dao.UserDao;
import at.memories.model.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
@ApplicationScoped
public class UserRepository implements UserDao {

    @Inject
    EntityManager em;

    @Transactional
    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Transactional
    @Override
    public User findUserByUsername(String username) {
        return em.createQuery("FROM User u WHERE u.username=:username", User.class)
                .setParameter("username",username)
                .getSingleResult();
    }

    @Override
    public User findUserById(Long userId) {
        return em.createQuery("FROM User u WHERE u.id=:userId", User.class)
                .setParameter("userId",userId)
                .getSingleResult();
    }

    @Transactional
    @Override
    public boolean userExists(String username) {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
    }

}
