package com.finalProject.travelTogether.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.finalProject.travelTogether.MyApplication;
import com.finalProject.travelTogether.feed.BaseActivity;
import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    ModelFirebase modelFirebase = new ModelFirebase();

    private Model() {
        postListLoadingState.setValue(PostListLoadingState.loaded);
    }

    /* Posts */

    public enum PostListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<PostListLoadingState>();

    public LiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }

    MutableLiveData<List<PostAndUser>> postsList = new MutableLiveData<List<PostAndUser>>();
    MutableLiveData<List<PostAndUser>> userPostsList = new MutableLiveData<List<PostAndUser>>();

    public LiveData<List<PostAndUser>> getAll() {
        if (postsList.getValue() == null) {
            refreshPostList();
        }
        return postsList;
    }

    public LiveData<List<PostAndUser>> getUserPosts() {
        if (userPostsList.getValue() == null) {
            refreshPostList();
        }
        return userPostsList;
    }

    public void refreshPostList() {
        postListLoadingState.setValue(PostListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("postListLoadingState", 0);

        executor.execute(() -> {
            List<PostAndUser> posList = AppLocalDb.db.postDao().getAll();
            postsList.postValue(posList);
        });

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllPosts(lastUpdateDate, new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> list) {
                // add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        for (Post post : list) {
                            AppLocalDb.db.postDao().insertAll(post);
                            if (lud < post.getUpdateDate()) {
                                lud = post.getUpdateDate();
                            }
                        }
                        // update last local update date
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("PostsLastUpdateDate", lud)
                                .commit();

                        //return all data to caller
                        List<PostAndUser> posList = AppLocalDb.db.postDao().getAll();
                        postsList.postValue(posList);
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        List<PostAndUser> usList=new ArrayList<>();
                        for(PostAndUser postAndUser : posList){
                            if(postAndUser.post.getAuthorEmailAddress().equals(email)){
                                usList.add(postAndUser);
                            }
                        }
                        userPostsList.postValue(usList);
                        postListLoadingState.postValue(PostListLoadingState.loaded);
                    }
                });
            }
        });
    }

    public interface AddPostListener {
        void onComplete();
    }

    public void addPost(Post post, AddPostListener listener) {
        modelFirebase.addPost(post, () -> {
            listener.onComplete();
            refreshPostList();
        });
    }

    public interface GetPostById {
        void onComplete(Post post);
    }

    public Post getPostById(String postId, GetPostById listener) {
        modelFirebase.getPostById(postId, listener);
        return null;
    }

    public void editPost(Post post) {
        executor.execute(() -> {
            AppLocalDb.db.postDao().deleteByPostId(post.id);//Delete post from rom
        });
        addPost(post,()->{});
    }

    /* Firebase Storage */

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener,int type) {
        modelFirebase.saveImage(imageBitmap, imageName, listener,type);
    }

    /* Authentication */

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    /* Users */

    MutableLiveData<List<User>> usersList = new MutableLiveData<List<User>>();
    MutableLiveData<User> user = new MutableLiveData<User>();

    public LiveData<List<User>> getAllUsers() {
        if (usersList.getValue() == null) {
            refreshUserList();
        }
        return usersList;
    }

    public void refreshUserList() {

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("userListLoadingState", 0);

        executor.execute(() -> {
            List<User> usList = AppLocalDb.db.userDao().getAll();
            usersList.postValue(usList);
        });

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllUsers(lastUpdateDate, new ModelFirebase.GetAllUsersListener() {
            @Override
            public void onComplete(List<User> list) {
                // add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        for (User user : list) {
                            AppLocalDb.db.userDao().insertAll(user);
                            if (lud < user.getUpdateDate()) {
                                lud = user.getUpdateDate();
                            }
                        }
                        // update last local update date
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("UsersLastUpdateDate", lud)
                                .commit();

                        //return all data to caller
                        List<User> usList = AppLocalDb.db.userDao().getAll();
                        usersList.postValue(usList);
                    }
                });
            }
        });
    }

    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(User user, AddUserListener listener) {
        modelFirebase.addUser(user, () -> {
            listener.onComplete();
        });
    }

    public MutableLiveData<User> getCurrentUser() {
        executor.execute(() -> {
            User result = AppLocalDb.db.userDao().getUserByEmailAddress(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            user.postValue(result);
        });
        return user;
    }
}