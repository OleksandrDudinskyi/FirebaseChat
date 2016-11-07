package com.dudinskyi.oleksandr.firebasechat.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String id;
    public String name;
    public String avatar;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
