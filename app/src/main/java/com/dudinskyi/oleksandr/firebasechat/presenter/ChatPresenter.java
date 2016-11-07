package com.dudinskyi.oleksandr.firebasechat.presenter;

import com.dudinskyi.oleksandr.firebasechat.model.Message;
import com.dudinskyi.oleksandr.firebasechat.view.ChatView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatPresenter extends Presenter<ChatView> {
    public static final String USER_MESSAGE_KEY = "chat";
    private ChildEventListener childEventListener;
    private DatabaseReference ref;
    private String chatId;

    public ChatPresenter(String roomId) {
        this.chatId = roomId;
    }

    public void init() {
        ref = FirebaseDatabase.getInstance().getReference().child(USER_MESSAGE_KEY).child(chatId);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setId(dataSnapshot.getKey());
                view.addMessage(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addChildEventListener(childEventListener);
    }


    public void destroy() {
        ref.removeEventListener(childEventListener);
    }

}
