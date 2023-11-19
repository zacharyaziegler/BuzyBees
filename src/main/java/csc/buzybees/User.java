/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc.buzybees;

/**
 * This class includes all member variables and methods to make up a User
 *
 * @author Luke Green
 */
public class User {
    
    //bool to represent manager status of a suer
    //true means is a manager
    //false means is not a manager
    private boolean isManager;
    
    private String fName;
    
    private String lName;
    
    private String birthday;
    
    private String position;
    
    private String userId;

    /**
     * default constructor to  create an "empty" user
     */
    public User() {
        isManager = false;
        fName = "";
        lName = "";
        birthday = "";
        position = "";
        userId = "";
    }

    /**
     * constructor that takes member variables as parameters
     * 
     * @param isManager manager status of new User
     * @param fName first name of User
     * @param lName last name of User
     * @param birthday birthday of User
     * @param position job/position of User
     * @param userId unique ID to represent user 
     */
    public User(boolean isManager, String fName, String lName, String birthday, String position, String userId) {
        this.isManager = isManager;
        this.fName = fName;
        this.lName = lName;
        this.birthday = birthday;
        this.position = position;
        this.userId = userId;
    }

    //following methods are get/set functions to read/alter User member variables easily
    
    public boolean isIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
