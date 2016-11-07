package com.dudinskyi.oleksandr.firebasechat.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dudinskyi.oleksandr.firebasechat.R;
import com.dudinskyi.oleksandr.firebasechat.model.LastMessage;
import com.dudinskyi.oleksandr.firebasechat.model.User;

public class ChatListActivity extends AppCompatActivity implements ChatListView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_activity);
    }

    @Override
    public void addMessage(LastMessage lastMessage) {

    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public void showEmptyPlaceHolder() {

    }
}
