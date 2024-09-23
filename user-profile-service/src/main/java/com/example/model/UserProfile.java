package com.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_profiles")
public class UserProfile {
    @Id
    private String id;
    private String name;
    private String cancuoc;
    private String mst;
    private String email;
    private String address;
    private String phone;
    private String bank;
    private String bank_name;
    private String username;
    private String password;
    private boolean admin;
    public String getUsername() {
		return username;
	}
    public String getPassword() {
		return password;
	}
   
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCancuoc() {
		return cancuoc;
	}
	public void setCancuoc(String cancuoc) {
		this.cancuoc = cancuoc;
	}
	public String getMst() {
		return mst;
	}
	public void setMst(String mst) {
		this.mst = mst;
	}
	public String getEmail() {
		return email;
	}
	public void setUsername(String user) {
		this.username = user;
	}
	public void setPassword(String pass) {
		this.password = pass;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public boolean getAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}