package com.dudinskyi.oleksandr.firebasechat.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {
    public String id;
    public String userId;
    public String content;
    public Long timestamp;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public void setId(String id) {
        this.id = id;
    }
}
