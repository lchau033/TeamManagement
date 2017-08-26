/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lia
 */
@Named(value = "setUpParametersBean")
@SessionScoped
public class SetUpParametersBean implements Serializable {
    private String courseCode;
    private String name;
    private String description;
    private int minStudents;
    private int maxStudents;
    private Date deadline;
    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    
    /**
     * Creates a new instance of SetUpPatametersBean
     */
    public SetUpParametersBean() {
    }
    
    public String getCourseCode(){
        return this.courseCode;
    }
    
    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public int getMinStudents(){
        return this.minStudents;
    }
    
    public void setMinStudents(int min){
        this.minStudents = min;
    }
    
    public int getMaxStudents(){
        return this.maxStudents;
    }
    
    public void setMaxStudents(int max){
        this.maxStudents = max;
    }
    
    public Date getDeadline(){
        return this.deadline;
    }
    
    public void setDeadline(Date deadline){
        this.deadline = deadline;
    }
    
    public String getUserId(){
        Long userId = TeamManagementSystem.getUserId();
        if(userId == null){
            return "";
        }
        return userId.toString();
    }
    
    public String doUpdateCourse(){
        TeamManagementSystem.doUpdateCourse(em,utx,courseCode,name,description,minStudents,maxStudents,deadline);
        this.courseCode=null;
        this.name=null;
        this.deadline=null;
        this.maxStudents=0;
        this.minStudents=0;
        this.description=null;
        return "instructorPage";
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
    
    public static void courseCreatedMsg(String name){
        String msg =  name + " Created Successfully";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
        FacesContext.getCurrentInstance().getExternalContext()
             .getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }
}
