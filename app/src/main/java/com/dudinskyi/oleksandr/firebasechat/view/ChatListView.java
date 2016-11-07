package com.dudinskyi.oleksandr.firebasechat.view;

import com.dudinskyi.oleksandr.firebasechat.model.LastMessage;
import com.dudinskyi.oleksandr.firebasechat.model.User;

public interface ChatListView extends View {
    void addMessage(LastMessage lastMessage);

    void addUser(User user);

    void showEmptyPlaceHolder();
}
