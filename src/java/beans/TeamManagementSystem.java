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
import java.util.List;
import CourseManagement.Course;
import CourseManagement.CourseManagementFacade;
import TeamManagement.Team;
import TeamManagement.TeamManagementFacade;
import UserManagement.User;
import UserManagement.UserManagementFacade;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Lia
 */
@Named(value = "teamManagementSystem")
@SessionScoped
public class TeamManagementSystem implements Serializable {
    private static CourseManagementFacade cMFc = new CourseManagementFacade();
    private static UserManagementFacade uMFc = new UserManagementFacade();
    private static TeamManagementFacade tMFc = new TeamManagementFacade();
    private static User user;
    private static List<Course> courses;
    private static boolean logIn;
    private static boolean createUser;
    private static Course course;
    private static List<User> members = new ArrayList<User>();
    private static List<Team> selectedIncompleteTeams;
    private static List<User> selectedCandidates;
    private static int size;
    private static int index;
    private static Team team;
    private static List<Team> teams;
    private static String leaderName;

    public static void removeSelectedStudent(User user) {
        selectedCandidates.remove(user);
        size--;
    }
    /**
     * Creates a new instance of TeamManagementSystem
     */
    public TeamManagementSystem() {
    }
    
    public static int getIndex(){
        return index;
    }
    
    public static void setIndex(int index1){
        index = index1;
    }
    
    public static Course addCourse(EntityManager em, UserTransaction utx,String courseCode){
        Course c = cMFc.createCourse(em,courseCode);
        if(courses == null){
            courses = new ArrayList<Course> ();
        }
        courses.add(c);
        return c;
    }
    
    public static void newStudent(EntityManager em, UserTransaction utx,String firstName, String lastName, String email,String password, String program, Long userId){
        User u = uMFc.newStudent(firstName, lastName,email, password, program, userId);
        Iterator<Course> it = courses.iterator();
        while(it.hasNext()){
            Course c = it.next();
            List<User> students = cMFc.getAllStudents(em, c);
            c.setStudents(students);
            c.addStudent(u);
            cMFc.mergeCourse(em, utx,c);
        }
        u.setCourse(courses);
        uMFc.mergeUser(em,utx,u);
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
    
    public static void newInstructor(EntityManager em, UserTransaction utx, String firstName, String lastName, String email,String password,Long userId){
        User u = uMFc.newInstructor(em,utx,firstName, lastName,email, password, userId);
    }
    
    public static void findUser(EntityManager em, Long userId, String password){
        uMFc = new UserManagementFacade();
        cMFc = new CourseManagementFacade();
        tMFc = new TeamManagementFacade();
        user = uMFc.findUser(em,userId, password);
        index = -1;
    }
    
    public static User findUser(EntityManager em,Long userId){
        User u = uMFc.findUser(em,userId);
        return u;
    }
    
    public static void createTeam(EntityManager em, UserTransaction utx, String name){
       boolean alreadyInTeam = false;
       Iterator<User> it = null;
       name = tMFc.nameIsAvailable(em,name,course.getCourseCode());
       if(members == null){
           members = new ArrayList<User>();
       }
       int numberMembers = 0;
       if(name == null){
           TeamManagementSystem.teamNameErrorMsg();
           index = 0;
       }
       else{
           members.add(user);
           numberMembers = members.size();
           it = members.iterator();
           alreadyInTeam = false;
           while(it.hasNext() && !alreadyInTeam){
               User u = it.next();
               alreadyInTeam = cMFc.alreadyInTeam(em,course.getCourseCode(),u.getUserId());
           }
           if(alreadyInTeam){
                TeamManagementSystem.alreadyInTeamErrorMsg();
                index = 0;
           }
           else if(numberMembers > course.getMaxStudents()){
               TeamManagementSystem.tooManyMembersErrorMsg(course.getMaxStudents());
               index = 0;
           }
           else{
               String status = Team.COMPLETE;
               if(numberMembers < course.getMinStudents()){
                   status = Team.INCOMPLETE;
               }
               Team t = tMFc.createTeam(em,utx,course, name,members, user.getUserId(), status);
               it = members.iterator();
               while(it.hasNext()){
                   User u = it.next();
                   u.addTeam(t);
                   uMFc.mergeUser(em, utx, u);
               }
               TeamManagementSystem.teamCreatedMsg(t.getName());
               index = -1;
           }
       }
      members = null;
    }
    
    public static void tooManyMembersErrorMsg(int maxStudents){
        CreateTeamBean.tooManyMembersErrorMsg(maxStudents);
    }
     
    public static void alreadyInTeamErrorMsg(){
        CreateTeamBean.alreadyInTeamErrorMsg();
    }
    
    public static void teamNameErrorMsg(){
        CreateTeamBean.teamNameErrorMsg();
    }
    
    public static void showMessageLogIn(){
    }
    
    public static List<Course> getCourses(){
        return courses;
    }
    
    public static List<Course> fetchCourses(EntityManager em){
        courses = cMFc.getCourses(em, user);
        return courses;
    }
    
    public static void logIn(){
        logIn = true;
        createUser = false;
    }
    
    public static boolean showLogIn(){
        return logIn;
    }
    
    public static void registerUser(){
        createUser = true;
        logIn = false;
    }
    
    public static boolean showRegisterUser(){
        return createUser;
    }
    
    public static void fetchCourse(EntityManager em, String courseCode){
        course = cMFc.getCourse(em, courseCode);
        team = cMFc.getTeam(em, courseCode, user.getUserId());
        index = -1;
    }
    
    public static Course getCourse(){
        return course;
    }
    
    public static boolean showCourseOptions(){
        Date today = new Date();
        if(course == null){
            return false;
        }
        return course.getProf()!= null && course.getDeadline().compareTo(today) >= 0;
    }
    
    public static String getResponsibility(){
        if(user == null || user.getResponsibility() == null){
            return null;
        }
        return user.getResponsibility();
    } 
    
    public static boolean showCourseOptionsErrorMsg(){
        Date today = new Date();
        if(course == null){
            return false;
        }
        return course.getProf()== null || course.getDeadline().compareTo(today) < 0;
    }
    
    public static void addStudent(EntityManager em, String firstName, String lastName){
        if(members == null){
            members = new ArrayList<User>();
        }
        User member = cMFc.findUserInCourse(em, firstName,lastName, course.getCourseCode());
        if(member != null){
            members.add(member);
        }
    }
    
    public static List<User> getMembers(){
        return members;
    }
    
    public static String getMembers(EntityManager em, Team team){
        if(team == null || course == null){
            return "";
        }
        members = cMFc.getMembers(em,course.getCourseCode(), team.getName());
        if(members == null){
            members = new ArrayList<User>();
        }
        Iterator<User> it = members.iterator();
        StringBuffer nameMembers = new StringBuffer();
        while(it.hasNext()){
            User u = it.next();
            if(u.getUserId().equals(team.getIdLeader())){
                leaderName = u.getFirstName()+" " + u.getLastName();
            }
            if(it.hasNext()){
                nameMembers.append(u.getFirstName() + " " + u.getLastName() + ", ");
            }
            else{
                nameMembers.append(u.getFirstName() + " " + u.getLastName());
            }
        }
        members = null;
        return nameMembers.toString();
    }
    
    public static void teamCreatedMsg(String teamName){
        CreateTeamBean.teamCreatedMsg(teamName);
    }
    
    public static boolean showStudentList(){
        return members != null && !members.isEmpty();
    }
    
    public static boolean studentInTeam(EntityManager em){
        return cMFc.alreadyInTeam(em, course.getCourseCode(), user.getUserId());
    }
    
    public static List<Team> getInCompleteTeams(EntityManager em){
        return cMFc.getIncompleteTeams(em,course.getCourseCode());
    }
    
    public static void setSelectedIncompleteTeams(List<Team> teams){
        selectedIncompleteTeams = teams;
    }
    
    public static void addStudentToSelectedTeams(EntityManager em, UserTransaction utx){
        if(selectedIncompleteTeams != null){
            Iterator<Team> it = selectedIncompleteTeams.iterator();
            List<User> candidates = new ArrayList<User>();
            List<Team> incompleteTeamsUser = uMFc.getIncompleteTeams(em,user.getUserId());
            List<Team> teams = tMFc.getTeams(em,user.getUserId());
            if(incompleteTeamsUser == null){
                incompleteTeamsUser = new ArrayList<Team>();
            }
            if(teams == null){
                teams = new ArrayList<Team>();
            }
            user.setCourse(courses);
            user.setIncompleteTeam(incompleteTeamsUser);
            user.setTeams(teams);
            while(it.hasNext()){
                Team t = it.next();
                members = cMFc.getMembers(em, course.getCourseCode(), t.getName());
                candidates = cMFc.getCandidates(em, course.getCourseCode(),t.getName());
                t.setUser(members);
                t.setCandidates(candidates);
                user.addIncompleteTeam(t);
                uMFc.mergeUser(em, utx, user);
                tMFc.mergeTeam(em,utx,t);
            }
            uMFc.mergeUser(em, utx, user);
        }
    }
    
    public static List<Team> getSelectedIncompleteTeams(){
        return selectedIncompleteTeams;
    }
    
    public static void addSelectedTeam(Team team){
        if(selectedIncompleteTeams == null){
            selectedIncompleteTeams = new ArrayList<Team> ();
        }
        selectedIncompleteTeams.add(team);
    }
    
    public static void removeSelectedTeam(Team team){
        selectedIncompleteTeams.remove(team);
    }
    
    public static boolean isLeader(EntityManager em){
        return cMFc.isLeader(em,course.getCourseCode(),user.getUserId());
    }
    
    public static List<User> getCandidates(EntityManager em){
        return cMFc.getCandidates(em, course.getCourseCode(), user.getUserId());
    }
    
    public static void addSelectedStudent(EntityManager em,User student){
        if(selectedCandidates == null){
            selectedCandidates = new ArrayList<User> ();
            size = cMFc.getMembers(em, course.getCourseCode(), user.getUserId()).size();
        }
        if(size + 1 > course.getMaxStudents()){
            TeamManagementSystem.notEnoughSpaceMsg(course.getMaxStudents());
        }
        else{
            selectedCandidates.add(student);
            size++;
        }
    }
    
    public static void notEnoughSpaceMsg(int max){
        AcceptNewStudentsBean.notEnoughSpaceMsg(max);
    }
    public static void acceptStudents(EntityManager em, UserTransaction utx){
        Iterator<User> it = null;
        if(size > course.getMaxStudents()){
            TeamManagementSystem.notEnoughSpaceMsg(size);
        }
        else if(selectedCandidates != null){
            Team t = cMFc.getTeam(em,course.getCourseCode(),user.getUserId());
            members = cMFc.getMembers(em, course.getCourseCode(), t.getName());
            t.setUser(members);
            if(size >= course.getMinStudents()){
                t.setStatus(Team.COMPLETE);
            }
            it = selectedCandidates.iterator();
            while(it.hasNext()){
                User u = it.next();
                List<Team> teams = tMFc.getTeams(em, u.getUserId());
                if(teams != null){
                    u.setTeams(teams);
                }
                List<Course> classes = cMFc.getCourses(em, u);
                u.setCourse(classes);
                List<Team> incompleteTeam = uMFc.getIncompleteTeams(em, u.getUserId());
                List<Team> incompleteTeamCourse = cMFc.getIncompleteTeams(em, course.getCourseCode(), u.getUserId());
                Iterator<Team> it2 = incompleteTeamCourse.iterator();
                while(it2.hasNext()){
                    Team team = it2.next();
                    incompleteTeam.remove(team);
                }
                u.setIncompleteTeam(incompleteTeam);
                members.add(u);
                u.addTeam(t);
                uMFc.mergeUser(em, utx, u);
            }
            tMFc.mergeTeam(em, utx, t);
        }
    }
    
    public static void logOut(){
        user = null;
        courses = null;
        course = null;
        cMFc = new CourseManagementFacade();
        uMFc = new UserManagementFacade();
        tMFc = new TeamManagementFacade();
        logIn = false;
        createUser = false;
        members = null;
        selectedIncompleteTeams = null;
        selectedCandidates = null;
        size = 0;
    }
    
    public static String getTeamName(){
        if(team == null){
            return "";
        }
        return team.getName();
    }
    
    public static String getMembers(EntityManager em){
        return TeamManagementSystem.getMembers(em,team);
    }
    
    public static String getLeader(EntityManager em){
       return leaderName;
    }
    
    public static String getTeamStatus(){
        if(team == null){
            return "";
        }
        return team.getStatus();
    }
    
    public static Long getUserId(){
        return user.getUserId();
    }
    
    public static void doUpdateCourse(EntityManager em, UserTransaction utx, String courseCode, String name,String description, int minStudents, int maxStudents, Date deadline){
        Course c = cMFc.getCourse(em, courseCode);
        if(c != null){
            List<User> students = cMFc.getAllStudents(em, c);
            List<Team> teams = cMFc.getTeams(em,c.getCourseCode());
            c.setDeadline(deadline);
            c.setDescription(description);
            c.setMaxStudents(maxStudents);
            c.setMinStudents(minStudents);
            c.setName(name);
            c.setProf(user);
            c.setStudents(students);
            c.setTeams(teams);
        }
        else{
            c = cMFc.createCourse(courseCode,name,description,minStudents,maxStudents,user,deadline);
        }
        cMFc.mergeCourse(em, utx, c);
        SetUpParametersBean.courseCreatedMsg(c.getName());
        index = -1;
    }
    
    public static boolean showCourseTeams(EntityManager em, String courseCode){
        boolean found = false;
    int index;
    teams = new ArrayList<Team>();
    for(index=0; index<courses.size() && !found; index++){
       if (courses.get(index).getCourseCode().equals(courseCode)){
           found = true;
       }
    }
    if (found){
        teams = cMFc.getTeams(em, courseCode);
        if(teams==null){
            teams = new ArrayList<Team>();
        }
    }
    return true;
    }
    
    public static List<Team> getTeams(){
        return teams;
    }
    
    public static Team getTeam(EntityManager em){
        if(course == null){
            return null;
        }
        team = cMFc.getTeam(em, course.getCourseCode(), user.getUserId());
        return team;
    }
    
    public static Date getDateTeam(){
        return team.getCreation();
    }
}
