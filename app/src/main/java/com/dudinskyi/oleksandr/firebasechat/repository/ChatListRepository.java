package com.dudinskyi.oleksandr.firebasechat.repository;

import com.dudinskyi.oleksandr.firebasechat.helpders.RxFirebase;
import com.dudinskyi.oleksandr.firebasechat.model.LastMessage;
import com.dudinskyi.oleksandr.firebasechat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import rx.Observable;

public class ChatListRepository {
    private static final String USERS_KEY = "users";
    private static final String DATA_KEY = "data";
    private static final String CHAT_KEY = "chat";
    private static final String UNREAD_MESSAGES = "unread-messages";
    private static final String READ = "read";
    private DatabaseReference chatRef;
    private String userId;

    public ChatListRepository(String userId) {
        this.userId = userId;
        this.chatRef = FirebaseDatabase.getInstance().getReference()
                .child(USERS_KEY).child(userId).child(CHAT_KEY);
    }

    public Observable<LastMessage> queryMessages() {
        return RxFirebase.observeChildren(chatRef)
                .filter(firebaseChildEvent -> firebaseChildEvent.eventType == RxFirebase.EventType.CHILD_ADDED)
                .map(firebaseChildEvent -> {
                    LastMessage roomMessage = firebaseChildEvent.snapshot.getValue(LastMessage.class);
                    roomMessage.setId(firebaseChildEvent.snapshot.getKey());
                    return roomMessage;
                });
    }

    public Observable<Boolean> isMessageListEmpty() {
        return RxFirebase.observeOnce(chatRef)
                .map(DataSnapshot::getChildrenCount)
                .filter(childCount -> childCount > 0)
                .isEmpty();
    }

    public Observable<User> queryUserData(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(USERS_KEY).child(userId).child(DATA_KEY);
        return RxFirebase.observe(ref)
                .filter(DataSnapshot::exists)
                .map(dataSnapshot -> {
                    User userData = dataSnapshot.getValue(User.class);
                    userData.id = dataSnapshot.getKey();
                    return userData;
                });
    }

    public Observable<Long> getUnreadMsgCount(String userId, String chatId) {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(USERS_KEY).child(userId).child(CHAT_KEY).child(chatId).child(UNREAD_MESSAGES);

        return RxFirebase.observe(query)
                .map(DataSnapshot::getChildrenCount);
    }

    public Observable<Long> getUnreadChatsCount(String userId) {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(USERS_KEY).child(userId).child(CHAT_KEY).orderByChild(READ).equalTo(false);

        return RxFirebase.observe(query)
                .map(DataSnapshot::getChildrenCount);
    }
}
