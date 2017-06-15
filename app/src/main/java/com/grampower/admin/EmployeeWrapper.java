package com.grampower.admin;

/**
 * Created by samdroid on 29/5/17.
 */

public class EmployeeWrapper {

    String email,name, profileUrl, morningStatus,eveningStatus;

    public EmployeeWrapper(String email, String name, String profileUrl) {
        this.email = email;
        this.name = name;
        this.profileUrl=profileUrl;
    }

    public EmployeeWrapper(String email, String name, String profileUrl, String morningStatus, String eveningStatus) {
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.morningStatus = morningStatus;
        this.eveningStatus = eveningStatus;
    }

    public String getMorningStatus() {
        return morningStatus;
    }

    public void setMorningStatus(String morningStatus) {
        this.morningStatus = morningStatus;
    }

    public String getEveningStatus() {
        return eveningStatus;
    }

    public void setEveningStatus(String eveningStatus) {
        this.eveningStatus = eveningStatus;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
