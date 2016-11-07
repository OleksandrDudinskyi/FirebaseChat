package com.dudinskyi.oleksandr.firebasechat.view;

import com.dudinskyi.oleksandr.firebasechat.model.Message;

public interface ChatView extends View {
    void addMessage(Message message);
}
