/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TeamManagement;

import UserManagement.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import CourseManagement.Course;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lchau033
 */
@Entity
@Table(name="TeamSEGINC")
public class Team implements Serializable {
    public static final String COMPLETE = "complete";
    public static final String INCOMPLETE ="incomplete";
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
    private String name;
    private Long idLeader;
    private String status;
    @Temporal(TemporalType.DATE)
    private Date creation;
    
    @ManyToOne
    private Course course;
    
    @ManyToMany
    private Collection<User> user;
    
    @ManyToMany(mappedBy="incompleteTeam")
    private Collection<User> candidates;
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public Long getIdLeader(){
        return this.idLeader;
    }
    
    public void setIdLeader(Long idLeader){
        this.idLeader = idLeader;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public Date getCreation(){
        return this.creation;
    }
    
    public void setCreation(Date creation){
        this.creation = creation;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public Course getCourse(){
        return this.course;
    }
    
    public void setCourse(Course course){
        this.course = course;
    }
    
    
    
    public List<User> getUser(){
        return (List<User>) this.user;
    }
    
    public void setUser(List<User> user){
        this.user = user;
    }
    
    public List<User> getCandidates(){
        return (List<User>) this.candidates;
    }
    
    public void setCandidates(List<User> candidates){
        this.candidates = candidates;
    }
    
    public Team(){
    }
    
    public Team(Course course, String name, List<User> user,Long idLeader, String status){
        this.course = course;
        this.name = name;
        this.user = user;
        this.idLeader = idLeader;
        this.status = status;
        this.creation = new Date();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.Team[ teamId=" + name + " ]";
    }
    
}
