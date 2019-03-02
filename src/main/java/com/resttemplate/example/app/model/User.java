package com.resttemplate.example.app.model;

public class User {

	private String username;
	private String emailid;
	private String contact;
	
	public User() {}
	
	public User(String username,String emailid,String contact) {
		this.username=username;
		this.emailid=emailid;
		this.contact=contact;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
}
