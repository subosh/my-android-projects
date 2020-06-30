package com.example.subosh.restauranttrack.admincontent;

public class AdminInformation {
    private String adminemail;
    private String adminphone;
    private String date;
    private String downloadpath;
    private String adminname;
    AdminInformation(){

    }

    public AdminInformation(String adminemail, String adminphone, String date, String downloadpath, String adminname) {
        this.adminemail = adminemail;
        this.adminphone = adminphone;
        this.date = date;
        this.downloadpath = downloadpath;
        this.adminname = adminname;
    }

    public String getAdminemail() {
        return adminemail;
    }

    public void setAdminemail(String adminemail) {
        this.adminemail = adminemail;
    }

    public String getAdminphone() {
        return adminphone;
    }

    public void setAdminphone(String adminphone) {
        this.adminphone = adminphone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadpath() {
        return downloadpath;
    }

    public void setDownloadpath(String downloadpath) {
        this.downloadpath = downloadpath;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }
}
