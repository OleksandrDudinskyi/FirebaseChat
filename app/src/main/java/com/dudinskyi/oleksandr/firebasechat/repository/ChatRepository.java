package com.dudinskyi.oleksandr.firebasechat.repository;

import com.dudinskyi.oleksandr.firebasechat.helpders.RxFirebase;
import com.dudinskyi.oleksandr.firebasechat.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import rx.Observable;

public class ChatRepository {

    private static final int MESSAGE_COUNT = 20;

    public Observable<List<Message>> loadMessages(String chatId, String messageKey) {
        Query chatQuery = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId)
                .orderByKey()
                .limitToLast(MESSAGE_COUNT + 1)
                .endAt(messageKey);
        if (null != messageKey) {
            chatQuery = chatQuery
                    .endAt(messageKey);
        }
        return RxFirebase.observeOnce(chatQuery)
                .filter(DataSnapshot::exists)
                .flatMapIterable(DataSnapshot::getChildren)
                .map(dataSnapshot -> dataSnapshot.getValue(Message.class))
                .toList();
    }
}
