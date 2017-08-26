/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserManagement;

import TeamManagement.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Lia
 */
public class DBUser {

    public void persist(EntityManager em, UserTransaction utx, Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    public void merge(EntityManager em, UserTransaction utx, Object object) {
        try {
            utx.begin();
            em.merge(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    public void newUser(EntityManager em, UserTransaction utx, User u){
        this.persist(em, utx, u);
    }
    
    public User findUser(EntityManager em,Long userId){
        Query query = em.createQuery(
                "SELECT u FROM User u "
                        + "WHERE u.userId = :userId"
        );
        query.setParameter("userId", userId);
        ArrayList<User> users = (ArrayList<User>) performQuery(query);
        if(users == null){
            return null;
        }
        return users.get(0);
    }
    
    public void createUser(EntityManager em, UserTransaction utx, User u){
        this.persist(em, utx, u);
    }
    
    public User findUser(EntityManager em,Long userId, String password){
        Query query = em.createQuery(
                "SELECT u FROM User u "
                        + "WHERE u.userId = :userId AND u.password = :password"
        );
        query.setParameter("userId", userId);
        query.setParameter("password", password);
        ArrayList<User> users = (ArrayList<User>) performQuery(query);
        if(users == null){
            return null;
        }
        return users.get(0);
    }
    
    private static List performQuery(final Query query) {
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } 
        ArrayList<Object> results = new ArrayList<>();
        results.addAll(resultList);
        return results;
    }
    
    
    public void mergeUser(EntityManager em, UserTransaction utx,User u){
        this.merge(em, utx, u);
    }
    
    public List<Team> getIncompleteTeams(EntityManager em, Long userId){
       Query query = em.createQuery(
               "SELECT t FROM User u "
                       + "JOIN u.incompleteTeam t "
                       + "WHERE u.userId = :userId"
       );
       query.setParameter("userId", userId);
       return (ArrayList<Team>) performQuery(query);
    }
    
}
