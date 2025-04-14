package com.example.jobportalapp.Model;

import java.io.Serializable;

public class Model implements Serializable {

    private static final long serialVersionUID = 1L;  // Optional, but recommended for version control

    private String id;
    private String mail;
    private String name;
    private String profilePic;
    private String role;
    private String aboutJob;
    private String additionalInfo;
    private String adminId;
    private String userName;  // This is the username field
    private String resumeLink;
    private String userId;
    private String companyName;
    private String jobLastDate;
    private String jobSalary;
    private String jobStartDate;
    private String jobTitle;
    private String skillsRequired;
    private String totalOpenings;

    // Default constructor (required for Firebase)
    public Model() {}

    // Parameterized constructor
    public Model(String id, String mail, String name, String profilePic, String role,
                 String aboutJob, String additionalInfo, String adminId, String userName,
                 String resumeLink, String userId, String companyName, String jobLastDate,
                 String jobSalary, String jobStartDate, String jobTitle,
                 String skillsRequired, String totalOpenings) {
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.profilePic = profilePic;
        this.role = role;
        this.aboutJob = aboutJob;
        this.additionalInfo = additionalInfo;
        this.adminId = adminId;
        this.userName = userName;  // Initialize username
        this.resumeLink = resumeLink;
        this.userId = userId;
        this.companyName = companyName;
        this.jobLastDate = jobLastDate;
        this.jobSalary = jobSalary;
        this.jobStartDate = jobStartDate;
        this.jobTitle = jobTitle;
        this.skillsRequired = skillsRequired;
        this.totalOpenings = totalOpenings;
    }

    // Getter and Setter methods for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAboutJob() {
        return aboutJob;
    }

    public void setAboutJob(String aboutJob) {
        this.aboutJob = aboutJob;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserName() {
        return userName;  // Getter for username
    }

    public void setUserName(String userName) {
        this.userName = userName;  // Setter for username
    }

    public String getResumeLink() {
        return resumeLink;
    }

    public void setResumeLink(String resumeLink) {
        this.resumeLink = resumeLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobLastDate() {
        return jobLastDate;
    }

    public void setJobLastDate(String jobLastDate) {
        this.jobLastDate = jobLastDate;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(String jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getTotalOpenings() {
        return totalOpenings;
    }

    public void setTotalOpenings(String totalOpenings) {
        this.totalOpenings = totalOpenings;
    }
}
