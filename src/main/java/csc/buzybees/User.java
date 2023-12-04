/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc.buzybees;

import java.util.ArrayList;
import java.util.List;

/**
 * This class includes all member variables and methods to make up a User
 *
 * @author Luke Green
 */
public class User {
    
    // Member variables
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String jobPosition;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private List<Shift> schedule; // List to store the user's schedule
    
    
    /**
     * default constructor to  create an "empty" user
     */
    public User() {
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = "";
        this.jobPosition = "";
        this.streetAddress = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
        this.phoneNumber = "";
        this.email = "";
        this.username = "";
        this.password = "";
        this.schedule = new ArrayList<>();
    }
    
    /**
     * constructor that takes member variables as parameters
     * 
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param jobPosition
     * @param streetAddress
     * @param city
     * @param state
     * @param zipCode
     * @param phoneNumber
     * @param email
     * @param username
     * @param password 
     * @param schedule 
     */
    public User(String firstName, String lastName, String dateOfBirth, String jobPosition, String streetAddress,
                String city, String state, String zipCode, String phoneNumber, String email, String username,
                String password, List<Shift> schedule) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.jobPosition = jobPosition;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.schedule = schedule; // Initialize the schedule
    }
  

    //The following methods are get/set functions that read/alter User member variables easily

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void addShift(Shift shift) {
        if (this.schedule == null) {
            this.schedule = new ArrayList<>();
        }
        this.schedule.add(shift);
    }
    
    public List<Shift> getSchedule() {
        return schedule;
    }
    
    public void setSchedule(List<Shift> schedule) {
        this.schedule = schedule;
    }
}
