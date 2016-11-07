package com.dudinskyi.oleksandr.firebasechat.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LastMessage {
    public String id;
    public String userId;
    public String lastReadMessageId;
    public String lastReadMessageContent;
    public String lastReadMessageTimestamp;

    public LastMessage() {
        // Default constructor required for calls to DataSnapshot.getValue(LastMessage.class)
    }

    public void setId(String id) {
        this.id = id;
    }
}
