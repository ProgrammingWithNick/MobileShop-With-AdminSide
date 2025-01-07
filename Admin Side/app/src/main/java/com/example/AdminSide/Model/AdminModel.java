package com.example.AdminSide.Model;

public class AdminModel {
    private String name;
    private String email;
    private String password;

    // No-argument constructor required for Firebase
    public AdminModel() {
    }

    // Constructor with parameters for easier instantiation
    public AdminModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for email
    public String getEmail() {  // Corrected capitalization here
        return email;
    }

    public void setEmail(String email) {  // Corrected capitalization here
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {  // Corrected capitalization here
        return password;
    }

    public void setPassword(String password) {  // Corrected capitalization here
        this.password = password;
    }
}
