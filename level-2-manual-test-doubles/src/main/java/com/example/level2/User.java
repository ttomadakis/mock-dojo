package com.example.level2;

/**
 * Simple model class representing a user in the system.
 */
public class User {
    private String id;
    private String username;
    private String email;
    private boolean active;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor with ID and username
     *
     * @param id User identifier
     * @param username User's username
     */
    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.active = true; // Default to active
    }

    /**
     * Constructor with all fields
     *
     * @param id User identifier
     * @param username User's username
     * @param email User's email address
     */
    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = true; // Default to active
    }

    /**
     * Constructor with all fields including active status
     *
     * @param id User identifier
     * @param username User's username
     * @param email User's email address
     * @param active Whether the user is active
     */
    public User(String id, String username, String email, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
} 