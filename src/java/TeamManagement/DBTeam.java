/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TeamManagement;

import UserManagement.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Lia
 */
public class DBTeam {

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
    
    public String nameIsAvailable(EntityManager em,String name,String courseCode){
        Query query = em.createQuery(
                "SELECT t FROM Team t "
                        + "JOIN t.course c "
                        + "WHERE t.name = :name AND c.courseCode = :courseCode"
        );
        query.setParameter("name", name);
        query.setParameter("courseCode", courseCode);
        ArrayList<Team> teams = (ArrayList<Team>) performQuery(query);
        if(teams == null){
            return name;
        }
        return null;
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
    
    public void createTeam(EntityManager em, UserTransaction utx, Team t){
        this.persist(em, utx, t);
    }
    
    public void mergeTeam(EntityManager em, UserTransaction utx, Team t){
        this.merge(em, utx, t);
    }
    
    public List<Team> getTeams(EntityManager em, Long userId){
        Query query = em.createQuery(
                "SELECT t FROM Team t "
                        + "JOIN t.user u "
                        + "WHERE u.userId = :userId"
        );
        query.setParameter("userId", userId);
        return (ArrayList<Team>) performQuery(query);
    }
    
    public User getLeader(EntityManager em, Team t){
       if(t == null){
           return null;
       }
       Query query = em.createQuery(
               "SELECT u FROM Team t "
                       + "JOIN t.user u "
                       + "WHERE t.name = :name AND u.userId = :idLeader"
       );
       query.setParameter("name", t.getName());
       query.setParameter("idLeader", t.getIdLeader());
       List<User> leaders = (ArrayList<User>) performQuery(query);
       if(leaders == null){
           return null;
       }
       return leaders.get(0);
    }
}
