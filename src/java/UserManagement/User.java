/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import CourseManagement.Course;
import TeamManagement.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 *
 * @author lchau033
 */
@Entity
@Table(name="UserSEGINC")
public class User implements Serializable {
    public static final String INSTRUCTOR = "Instructor";
    public static final String STUDENT = "Student";
    
    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    private Long userId;
    private String email;
    private String  password;
    private String firstName;
    private String lastName;
    private String responsibility;
    private String program;
    
    @ManyToMany(mappedBy="user")
    private Collection<Team> team;
    
    @ManyToMany
    private Collection<Team> incompleteTeam;
    
    @ManyToMany(mappedBy="students")
    private Collection<Course> course;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prof" )
    private Collection<Course> courses;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPassword(){
        return this.password;
    }
    
     public void setPassword(String password){
         this.password = password;
     }
     
     public String getFirstName(){
         return this.firstName;
     }
     
     public void setFirstName(String firstName){
         this.firstName = firstName;
     }
     
     public String getLastName(){
         return this.lastName;
     }
     
     public void setLastName(String lastName){
         this.lastName = lastName;
     }
     
     public String getResponsibility(){
         return this.responsibility;
     }
     
     public void setResponsability(String responsibility){
         this.responsibility = responsibility;
     }
     
     public String getProgram(){
         return this.program;
     }
     
     public void setProgram(String program){
         this.program = program;
     }
     
     public Collection<Team> getTeams(){
         return this.team;
     }
     
     public void setTeams(List<Team> teams){
         this.team = teams;
     }
     
     public void addTeam(Team team){
         this.team.add(team);
     }
     
     
     public Collection<Course> getCourses(){
         return this.courses;
     }
     
     public void setCourses(Collection<Course> courses){
         this.courses = courses;
     }
     
     public void setCourse(List<Course> course){
         this.course = course;
     }
     
     public Collection<Course> getCourse(){
         return this.course;
     }
     
     public void addCourse(Course course){
         if(this.responsibility.equals(User.STUDENT)){
             this.course.add(course);
         }
         else{
             this.courses.add(course);
         }
     }
     
     public List<Team> getIncompleteTeam(){
         return (List<Team>) this.incompleteTeam;
     }
     
     public void setIncompleteTeam(List<Team> incompleteTeam){
         this.incompleteTeam = incompleteTeam;
     }
     
     public void addIncompleteTeam(Team t){
        this.incompleteTeam.add(t);
     }
     
     public User(){
     }
     
      public User(Long userId,String email, String password, String firstName,
        String lastName, String responsibility) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.responsibility = responsibility;
        
        if (responsibility.equals(User.STUDENT)){
            this.course = new ArrayList<Course>();
            this.team = new ArrayList<Team>();
            this.incompleteTeam = new ArrayList<Team>();
        }
        else{
            this.courses = new ArrayList<Course>();
        }
        
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.User[ userId=" + userId + " ]";
    }
    
}
