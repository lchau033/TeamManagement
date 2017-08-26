/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TeamManagement;

import CourseManagement.Course;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import UserManagement.User;
import java.util.List;

/**
 *
 * @author Lia
 */
public class TeamManagementFacade {
    private DBTeam database = new DBTeam();
    
    public String nameIsAvailable(EntityManager em, String name, String courseCode){
       return database.nameIsAvailable(em, name, courseCode);
    }
    
    public Team createTeam(EntityManager em, UserTransaction utx,Course course,String name, List<User> members,Long userId, String status){
        Team t = new Team(course,name,members,userId,status);
        database.createTeam(em, utx, t);
        return t;
    }
    
    public void mergeTeam(EntityManager em, UserTransaction utx, Team t){
        database.mergeTeam(em,utx,t);
    }
    
    public List<Team> getTeams(EntityManager em, Long userId){
        return database.getTeams(em,userId);
    }
    
    public User getLeader(EntityManager em, Team team){
        return database.getLeader(em,team);
    }
}
