package org.example.coursework.entities;

public class User {
    private String name;
    private String password;
    private String role;

    public User(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }
    public String getUsername() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
