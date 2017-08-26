/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseManagement;

import TeamManagement.Team;
import UserManagement.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class DBCourse {
    
    
    public void addCourse(EntityManager em, UserTransaction utx, Course course){
        Query query = em.createQuery(
                "SELECT c FROM Course c "
                        + "WHERE c.courseCode = :courseCode"
        );
        query.setParameter("courseCode", course.getCourseCode());
        ArrayList<Course> courses = (ArrayList<Course>) performQuery(query);
        if(courses == null){
            String msg = "null courses";
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
            this.persist(em,utx,course);
        }
    }
    
    public List<User> getAllStudents(EntityManager em, Course c){
        String courseCode = c.getCourseCode();
        Query query = em.createQuery(
                "SELECT s FROM User s "
                        + "JOIN s.course c "
                        + "WHERE c.courseCode = :courseCode"
        );
        query.setParameter("courseCode", courseCode);
        ArrayList<User> students = (ArrayList<User>) performQuery(query);
        return students;
    }
    
    public List<Course> getCourses(EntityManager em, User u){
        Query query = null;
        if(u == null){
            return null;
        }
        else if (u.getResponsibility().equals(User.STUDENT)){
            query = em.createQuery(
                    "SELECT c FROM Course c "
                            + "JOIN c.students s "
                            + "WHERE s.userId = :userId"
            );
        }
        else{
            query = em.createQuery(
                    "SELECT c FROM Course c "
                            + "JOIN c.prof p "
                            + "WHERE p.userId = :userId"
            );
        }
        query.setParameter("userId", u.getUserId());
        ArrayList<Course> courses = (ArrayList<Course>) performQuery(query);
        return courses;
    }
    
    public Course getCourse(EntityManager em, String courseCode){
        Query query = em.createQuery(
                "SELECT c FROM Course c "
                        + "WHERE c.courseCode = :courseCode"
        );
        query.setParameter("courseCode", courseCode);
        ArrayList<Course> courses = (ArrayList<Course>) performQuery(query);
        if(courses == null){
            return null;
        }
        return courses.get(0);
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

    void mergeCourse(EntityManager em, UserTransaction utx, Course c) {
        this.merge(em, utx,c);
    }

    public void persist(EntityManager em, UserTransaction utx,Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    public void merge(EntityManager em, UserTransaction utx,Object object) {
        try {
            utx.begin();
            em.merge(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    public boolean alreadyInTeam(EntityManager em, String courseCode, Long userId){
         Query query = em.createQuery(
                "SELECT u FROM Course c "
                        + "JOIN c.teams t "
                        + "JOIN t.user u "
                        + "WHERE u.userId = :userId AND c.courseCode = :courseCode"
            );
         query.setParameter("userId", userId);
         query.setParameter("courseCode", courseCode);
         ArrayList<User> users = (ArrayList<User>) performQuery(query);
         return users != null;
    }

    public User findUserInCourse(EntityManager em,String firstName,String lastName, String courseCode) {
        Query query= em.createQuery(
                "SELECT u FROM Course c "
                        + "JOIN c.students u "
                        + "WHERE c.courseCode = :courseCode AND u.firstName = :firstName AND u.lastName = :lastName"
        );
        query.setParameter("courseCode", courseCode);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        ArrayList<User> users = (ArrayList<User>) performQuery(query);
        if(users == null){
            return null;
        }
        return users.get(0);
    }

    public List<Team> getIncompleteTeams(EntityManager em, String courseCode) {
       Query query = em.createQuery(
               "SELECT t FROM Course c "
                       + "JOIN c.teams t "
                       + "WHERE t.status = :incomplete AND c.courseCode = :courseCode"
       );
       query.setParameter("incomplete", Team.INCOMPLETE);
       query.setParameter("courseCode", courseCode);
       return (ArrayList<Team>) performQuery(query);
    }
    
    public List<User> getMembers(EntityManager em, String courseCode, String name){
        Query query = em.createQuery(
                "SELECT u FROM Course c "
                        + "JOIN c.teams t "
                        + "JOIN t.user u "
                        + "WHERE c.courseCode = :courseCode AND t.name = :name"
        );
        query.setParameter("courseCode", courseCode);
        query.setParameter("name", name);
        return (ArrayList<User>) performQuery(query);
    }
    
    public List<User> getCandidates(EntityManager em, String courseCode, String name){
        Query query = em.createQuery(
                "SELECT u FROM Course c "
                        + "JOIN c.students u "
                        + "JOIN u.incompleteTeam t "
                        + "WHERE c.courseCode = :courseCode AND t.name = :name"
        );
        query.setParameter("courseCode", courseCode);
        query.setParameter("name", name);
        return (ArrayList<User>) performQuery(query);
    }
    
    public boolean isLeader(EntityManager em, String courseCode, Long userId){
        Query query = em.createQuery(
                "SELECT u FROM Course c "
                        + "JOIN c.teams t "
                        + "JOIN t.user u "
                        + "WHERE c.courseCode = :courseCode AND t.idLeader = :userId AND u.userId = :userId"
        );
        query.setParameter("courseCode",courseCode);
        query.setParameter("userId", userId);
        return performQuery(query) != null;
    }
    
    public List<User> getCanadidates(EntityManager em, String courseCode, Long userId){
        Query query = em.createQuery(
                "SELECT u FROM Course c "
                        + "JOIN c.students u "
                        + "JOIN u.incompleteTeam t "
                        + "WHERE c.courseCode = :courseCode AND u.userId <> :userId AND t.idLeader = :userId"
        );
        query.setParameter("courseCode", courseCode);
        query.setParameter("userId", userId);
        return (ArrayList<User>) performQuery(query);
    }
    
    public List<User> getMembers(EntityManager em, String courseCode, Long userId){
        Query query = em.createQuery(
            "SELECT u FROM Course c "
                    + "JOIN c.teams t "
                    + "JOIN t.user u "
                    + "WHERE c.courseCode = :courseCode AND t.idLeader = :userId"
        );
        query.setParameter("courseCode", courseCode);
        query.setParameter("userId", userId);
        return (ArrayList<User>) performQuery(query);
    }
    
    public Team getTeam(EntityManager em, String courseCode, Long userId){
        Query query = em.createQuery(
                "SELECT t FROM Course c "
                        + "JOIN c.teams t "
                        + "JOIN t.user u "
                        + "WHERE c.courseCode = :courseCode AND u.userId = :userId"
        );
        query.setParameter("courseCode", courseCode);
        query.setParameter("userId", userId);
        ArrayList<Team> teams = (ArrayList<Team>) performQuery(query);
        if(teams == null){
            return null;
        }
        return teams.get(0);
    }
    
    public List<Team> getIncompleteTeams(EntityManager em, String courseCode, Long userId){
        Query query = em.createQuery(
                "SELECT t FROM Course c "
                        + "JOIN c.students s "
                        + "JOIN s.incompleteTeam t "
                        + "WHERE c.courseCode = :courseCode AND s.userId = :userId"
        );
        query.setParameter("courseCode", courseCode);
        query.setParameter("userId", userId);
        return (ArrayList<Team>) performQuery(query);
    }
    
    public List<Team> getTeams(EntityManager em, String courseCode){
        Query query = em.createQuery(
                "SELECT t FROM Course c "
                        + "JOIN c.teams t "
                        + "WHERE c.courseCode = :courseCode"
        );
        query.setParameter("courseCode", courseCode);
        return (ArrayList<Team>) performQuery(query);
    }
    

    
    
}
