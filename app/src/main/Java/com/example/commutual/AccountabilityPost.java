package com.example.commutual;

public class AccountabilityPost {

    private String postName;
    private String postDescription;
    private String username;

    AccountabilityPost(String mPostName, String mPostDescription, String mUsername) {
        this.postName = mPostName;
        this.postDescription = mPostDescription;
        this.username = mUsername;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
