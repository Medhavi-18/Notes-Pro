package com.example.notespro;

import com.google.firebase.Timestamp;

// FIREBASE CLOUD STORAGE FOR SAVING NOTE
public class Note {
    String title;
    String content;
    Timestamp timestamp;

    public Note() {
        // Default constructor required for Firebase
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
