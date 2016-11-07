package com.dudinskyi.oleksandr.firebasechat.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHolder {
    DatabaseReference ref;
    ValueEventListener valueEventListener;

    public FirebaseHolder(DatabaseReference ref, ValueEventListener valueEventListener) {
        this.ref = ref;
        this.valueEventListener = valueEventListener;
    }

    public void addListener() {
        ref.addValueEventListener(valueEventListener);
    }

    public void removeListener() {
        ref.removeEventListener(valueEventListener);
    }
}
