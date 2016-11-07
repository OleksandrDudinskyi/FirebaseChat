package com.dudinskyi.oleksandr.firebasechat.presenter;

import android.util.Log;

import com.dudinskyi.oleksandr.firebasechat.repository.ChatListRepository;
import com.dudinskyi.oleksandr.firebasechat.view.ChatListView;

import rx.subscriptions.CompositeSubscription;

public class ChatListPresenter extends Presenter<ChatListView> {
    private String userId;
    private CompositeSubscription compositeSubscription;
    private ChatListRepository chatListRepository;

    public ChatListPresenter(String userId) {
        this.userId = userId;
        compositeSubscription = new CompositeSubscription();
        chatListRepository = new ChatListRepository(userId);
    }

    public void init() {
        compositeSubscription.add(chatListRepository.queryMessages()
                .subscribe(lastMessage -> {
                    queryUserData(lastMessage.userId);
                    view.addMessage(lastMessage);
                }, throwable -> Log.e("ChatListPresenter", "Get chat list error: ", throwable)));


        compositeSubscription.add(chatListRepository.isMessageListEmpty()
                .subscribe(emptyStatus -> {
                    if (emptyStatus) {
                        view.showEmptyPlaceHolder();
                    }
                }, throwable -> Log.e("ChatListPresenter", "Get chat list is empty status error: ", throwable)));


    }

    private void queryUserData(String userId) {
        compositeSubscription.add(chatListRepository.queryUserData(userId)
                .subscribe(userData -> {
                    view.addUser(userData);
                }, throwable -> Log.e("ChatListPresenter", "Get User data error: ", throwable)));
    }

    public void destroy() {
        compositeSubscription.unsubscribe();
    }

}
