package com.dudinskyi.oleksandr.firebasechat.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dudinskyi.oleksandr.firebasechat.R;
import com.dudinskyi.oleksandr.firebasechat.model.Message;

public class ChatActivity extends AppCompatActivity implements ChatView {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_activity);
    }

    @Override
    public void addMessage(Message message) {

    }
}
