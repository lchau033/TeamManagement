/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseManagement;

import TeamManagement.Team;
import UserManagement.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.transaction.UserTransaction;

/**
 *
 * @author lchau033
 */
@Entity
@Table(name="CourseSEGINC")
public class Course implements Serializable {
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
    private String courseCode;
    private String name;
    private String description;
    private int minStudents;
    private int maxStudents;
    @Temporal(TemporalType.DATE)
    private Date deadline;
    @ManyToOne
    @JoinColumn(name="COURSE_PROF",
                referencedColumnName="userId")
    private User prof;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<Team> teams;
    
    @ManyToMany
    private Collection<User> students;
    
    public Course(){
    }
    
    public Course(String courseCode){
       this.courseCode = courseCode;
       teams = new ArrayList<Team>();
       students = new ArrayList<User>();
    }
    
    public Course(String courseCode,String name,String description,int minStudents,
            int maxStudents,User user,Date deadline){
        this.courseCode = courseCode;
        this.name = name;
        this.deadline = deadline;
        this.description = description;
        this.maxStudents = maxStudents;
        this.minStudents = minStudents;
        this.prof = user;
        this.teams = new ArrayList<Team>();
        this.students = new ArrayList<User>();
    }
    
    public String getCourseCode() {
        return this.courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.courseCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getMinStudents(){
        return this.minStudents;
    }
    
    public void setMinStudents(int minStudents){
        this.minStudents = minStudents;
    }
    
     public int getMaxStudents(){
        return this.maxStudents;
    }
    
    public void setMaxStudents(int maxStudents){
        this.maxStudents = maxStudents;
    }
    
    public Date getDeadline(){
        return this.deadline;
    }
    
    public void setDeadline(Date deadline){
        this.deadline = deadline;
    }
    
    public User getProf(){
        return this.prof;
    }
    
    public void setProf(User prof){
        this.prof = prof;
    }
    
    public void addStudent(User student){
        this.students.add(student);
    }
    
    public void setStudents(List<User> user){
        if(user != null && !user.isEmpty()){
            students = user;
        }    
    }
    
    public List<Team> getTeams(){
        return (List<Team>) this.teams;
    }
    
    public void setTeams(List<Team> teams){
        this.teams = teams;
    }
   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseCode != null ? courseCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.courseCode == null && other.courseCode != null) || (this.courseCode != null && !this.courseCode.equals(other.courseCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.Course[ courseCode=" + courseCode + " ]";
    }
    
}
