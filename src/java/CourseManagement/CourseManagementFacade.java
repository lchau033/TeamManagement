/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseManagement;

import TeamManagement.Team;
import UserManagement.User;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 *
 * @author Lia
 */
public class CourseManagementFacade {
    private static DBCourse database = new DBCourse(); 
    
    public Course createCourse(EntityManager em,String courseCode){
        Course c = database.getCourse(em, courseCode);
        if(c == null){
            c = new Course(courseCode);
        }
        return c;
    }
    
    public List<User> getAllStudents(EntityManager em, Course c){
        List<User> students = database.getAllStudents(em, c);
        return students;
    }

    public void mergeCourse(EntityManager em, UserTransaction utx,Course c) {
        database.mergeCourse(em, utx,c);
    }
    
    public List<Course> getCourses(EntityManager em, User u){
        List<Course> courses = database.getCourses(em,u);
        return courses;
    }
    
    public Course getCourse(EntityManager em, String courseCode){
        Course c = database.getCourse(em, courseCode);
        return c;
    }
    
    public boolean alreadyInTeam(EntityManager em, String courseCode, Long userId){
        return database.alreadyInTeam(em,courseCode, userId);
    }
    
    public User findUserInCourse(EntityManager em,String firstName, String lastName, String courseCode){
        return database.findUserInCourse(em,firstName,lastName,courseCode);
    }

    public List<Team> getIncompleteTeams(EntityManager em,String courseCode) {
        return database.getIncompleteTeams(em,courseCode);
    }
    
    public List<User> getMembers(EntityManager em, String courseCode, String name){
        return database.getMembers(em,courseCode,name);
    }
    
    public List<User> getCandidates(EntityManager em, String courseCode, String name){
        return database.getCandidates(em,courseCode,name);
    }
    
    public boolean isLeader(EntityManager em, String courseCode, Long userId){
        return database.isLeader(em,courseCode,userId);
    }
    
    public List<User> getCandidates(EntityManager em, String courseCode, Long userId){
        return database.getCanadidates(em, courseCode, userId);
    }
    
    public List<User> getMembers(EntityManager em, String courseCode, Long userId){
        return database.getMembers(em, courseCode, userId);
    }
    
    public Team getTeam(EntityManager em, String courseCode, Long userId){
        return database.getTeam(em,courseCode, userId);
    }
    
    public List<Team> getIncompleteTeams(EntityManager em, String courseCode, Long userId){
        return database.getIncompleteTeams(em, courseCode,userId);
    }
    
    public List<Team> getTeams(EntityManager em, String courseCode){
        return database.getTeams(em,courseCode);
    }
    
    public Course createCourse(String courseCode,String name,String description,int minStudents,
            int maxStudents,User user,Date deadline){
      Course c = new Course(courseCode,name,description,minStudents,maxStudents,user,deadline);
      return c;
    }
    
}
