/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Lia
 */
@Named(value = "joinTeamBean")
@SessionScoped
public class JoinTeamBean implements Serializable {

    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    /**
     * Creates a new instance of JoinTeamBean
     */
    public JoinTeamBean() {
    }
    
    public boolean studentInTeam(){
        return TeamManagementSystem.studentInTeam(em);
    }
    
    public List<Team> getIncompleteTeams(){
        return TeamManagementSystem.getInCompleteTeams(em);
    }
    
    public String getTeamMembers(Team team){
        return TeamManagementSystem.getMembers(em,team);
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
    
    public void setSelectedIncompleteTeams(List<Team> teams){
        TeamManagementSystem.setSelectedIncompleteTeams(teams);
    }
    
    public String addStudentToSelectedTeams(){
        TeamManagementSystem.addStudentToSelectedTeams(em,utx);
        return "coursePage";
    }
    
    public List<Team> getSelectedIncompleTeams(){
        return TeamManagementSystem.getSelectedIncompleteTeams();
    }
    
    public void addSelectedTeam(SelectEvent event){
        TeamManagementSystem.addSelectedTeam((Team) event.getObject());
    }
    
    public void removeSelectedTeam(UnselectEvent event){
        TeamManagementSystem.removeSelectedTeam((Team) event.getObject());
    }
    
}
