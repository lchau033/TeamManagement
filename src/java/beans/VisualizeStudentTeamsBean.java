/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import CourseManagement.Course;
import TeamManagement.Team;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lia
 */
@Named(value = "visualizeStudentTeamsBean")
@SessionScoped
public class VisualizeStudentTeamsBean implements Serializable {
    private int tabNum;
    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    private String courseCode;
    /**
     * Creates a new instance of VisualizeStudentTeamsBean
     */
    public VisualizeStudentTeamsBean() {
    }
    
    public int getTabNum(){
        return tabNum;
    }
    
    public void setTabNum(int tabNum){
        this.tabNum = tabNum;
    }
    
    public String courseCode(){
        return this.courseCode;
    }
    
    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }
    
    public boolean showCourseTeams(Course c){
        return TeamManagementSystem.showCourseTeams(em,c.getCourseCode());
    }

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    public List<Team> getTeams(){
        return TeamManagementSystem.getTeams();
    }
    
}
