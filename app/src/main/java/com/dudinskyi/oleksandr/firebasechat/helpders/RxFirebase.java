package com.dudinskyi.oleksandr.firebasechat.helpders;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rx.Emitter;
import rx.Observable;
import rx.functions.Func1;

public class RxFirebase {
    private static final String PERMISSION_DENIED_MSG = "Permission denied";
    private static Func1<Observable<? extends Throwable>, Observable<?>> authenticationHandler = attempts ->
            attempts.flatMap(new Func1<Throwable, Observable<?>>() {
                @Override
                public Observable<?> call(Throwable throwable) {
                    Log.d("RxFirebase", throwable.getMessage());
                    if (PERMISSION_DENIED_MSG.equals(throwable.getMessage())) {
                        return authentication("<firebaseToken>")
                                .flatMap(firebaseUser -> {
                                    if (firebaseUser == null) {
                                        // developer add your flow to get new valid firebase token here
                                        return null;
                                    } else {
                                        return Observable.fromCallable(() -> firebaseUser);
                                    }
                                });
                    }
                    // Max retries hit. Just pass the error along.
                    return Observable.error(throwable);
                }
            });

    public static Observable<DataSnapshot> observe(final Query ref) {
        return Observable.<DataSnapshot>fromEmitter(emitter -> {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    emitter.onNext(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Turn the FirebaseError into a throwable to conform to the API
                    emitter.onError(new FirebaseException(error.getMessage()));
                }
            };
            ref.addValueEventListener(valueEventListener);
            emitter.setCancellation(() -> ref.removeEventListener(valueEventListener));
        }, Emitter.BackpressureMode.BUFFER).retryWhen(authenticationHandler);
    }

    public static Observable<DataSnapshot> observeWithAuthentication(final Query ref) {
        return observe(ref).retryWhen(authenticationHandler);
    }

    private static Observable<FirebaseUser> authentication(final String firebaseToken) {
        return Observable.fromEmitter(emitter -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (null != user) {
                emitter.onNext(user);
                emitter.onCompleted();
            } else if (!TextUtils.isEmpty(firebaseToken)) {
                OnCompleteListener<AuthResult> onCompleteListener = task -> {
                    AuthResult result = null;
                    try {
                        result = task.getResult();
                    } catch (Exception e) {
                        Log.e("RxFirebase", "GetResult() from Firebase sign return in error: ", e);
                    }
                    emitter.onNext(result == null ? null : result.getUser());
                    emitter.onCompleted();
                };
                FirebaseAuth.getInstance().signInWithCustomToken(firebaseToken)
                        .addOnCompleteListener(onCompleteListener);
                Log.d("RxFirebase", "signInWithCustomToken token: " + firebaseToken);
            } else {
                emitter.onNext(null);
                emitter.onCompleted();
            }
        }, Emitter.BackpressureMode.BUFFER);
    }

    public static Observable<DataSnapshot> observeOnce(final Query ref) {
        return Observable.fromEmitter(emitter -> {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    emitter.onNext(dataSnapshot);
                    emitter.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Turn the FirebaseError into a throwable to conform to the API
                    emitter.onError(new FirebaseException(error.getMessage()));
                }
            };
            ref.addListenerForSingleValueEvent(valueEventListener);
            emitter.setCancellation(() -> ref.removeEventListener(valueEventListener));
        }, Emitter.BackpressureMode.BUFFER);
    }

    public static Observable<FirebaseChildEvent> observeChildren(Query ref) {

        return Observable.fromEmitter(emitter -> {

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevName) {
                    emitter.onNext(new FirebaseChildEvent(dataSnapshot, EventType.CHILD_ADDED, prevName));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevName) {
                    emitter.onNext(new FirebaseChildEvent(dataSnapshot, EventType.CHILD_CHANGED, prevName));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    emitter.onNext(new FirebaseChildEvent(dataSnapshot, EventType.CHILD_REMOVED, null));
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevName) {
                    emitter.onNext(new FirebaseChildEvent(dataSnapshot, EventType.CHILD_MOVED, prevName));
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Turn the FirebaseError into a throwable to conform to the API
                    emitter.onError(new FirebaseException(error.getMessage()));
                }
            };

            ref.addChildEventListener(childEventListener);
            emitter.setCancellation(() -> ref.removeEventListener(childEventListener));

        }, Emitter.BackpressureMode.BUFFER);
    }

    public enum EventType {
        CHILD_ADDED, CHILD_CHANGED, CHILD_REMOVED, CHILD_MOVED
    }

    // Essentially a struct so that we can pass all children events through as a single object.
    public static class FirebaseChildEvent {
        public DataSnapshot snapshot;
        public EventType eventType;
        public String prevName;


        public FirebaseChildEvent(DataSnapshot snapshot, EventType eventType, String prevName) {
            this.snapshot = snapshot;
            this.eventType = eventType;
            this.prevName = prevName;
        }
    }

}
