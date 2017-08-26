/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserManagement;

import CourseManagement.Course;
import TeamManagement.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 *
 * @author Lia
 */
public class UserManagementFacade {
    private DBUser database = new DBUser();
    public UserManagementFacade(){
        
    }
    
    public User newStudent(String firstName, String lastName,String email, String password, String program, Long userId){
        User u = new User(userId,email, password, firstName,lastName, User.STUDENT);
        u.setProgram(program);
        return u;
    }
    
    public User newInstructor(EntityManager em, UserTransaction utx, String firstName, String lastName,String email, String password,Long userId){
        User u = new User(userId,email, password, firstName,lastName, User.INSTRUCTOR);
        database.createUser(em,utx,u);
        return u;
    }
    
    public void setCourse(User u,List<Course> courses){
        u.setCourse(courses);
    }

    public User findUser(EntityManager em, Long userId) {
        User u = database.findUser(em, userId);
        return u;
    }
    
    public User findUser(EntityManager em, Long userId, String password){
        User u = database.findUser(em, userId, password);
        return u;
    }
    
    public void mergeUser(EntityManager em, UserTransaction utx, User u){
        database.mergeUser(em,utx,u);
    }
    
    public List<Team> getIncompleteTeams(EntityManager em,Long userId){
        return database.getIncompleteTeams(em,userId);
    }
}
