package at.memories.impl;

import at.memories.dao.UserDao;
import at.memories.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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

}
