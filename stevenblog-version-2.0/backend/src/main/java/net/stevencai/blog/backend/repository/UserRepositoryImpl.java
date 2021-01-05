package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Repository
public class UserRepositoryImpl implements UserRepositoryExtension {
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void lockAccount(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Session session = entityManager.unwrap(Session.class);
        Query<?> query = session
                .createQuery("update User u  set u.isAccountLocked = true where u.username=:username");
        query.setParameter("username", username);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public void unlockAccount(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Session session = entityManager.unwrap(Session.class);
        Query<?> query = session
                .createQuery("update User u  set u.isAccountLocked = false where u.username=:username");
        query.setParameter("username", username);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public void enableAccount(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Session session = entityManager.unwrap(Session.class);
        Query<?> query = session
                .createQuery("update User u  set u.enabled = true where u.username=:username");
        query.setParameter("username", username);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }
}