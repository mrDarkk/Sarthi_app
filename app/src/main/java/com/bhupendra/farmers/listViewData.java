package com.bhupendra.farmers;


import com.google.firebase.firestore.GeoPoint;

public class listViewData {

    public String executive_email;
    public String farmer_name;
    public String issue;
    public String farmer_contact_no;
    public String location;

    public listViewData(String executive_email, String farmer_name, String issue, String phone, String location) {
        this.executive_email = executive_email;
        this.farmer_name = farmer_name;
        this.issue = issue;
        this.farmer_contact_no = phone;
        this.location = location;
    }
}
