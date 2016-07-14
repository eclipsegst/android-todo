package com.zhaolongzhong.todo.main;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zz on 6/9/16.
 */

public class TodoApplication extends Application {
    private static final String TAG = TodoApplication.class.getSimpleName();

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.getDefaultInstance();
    }

    public static Application getApplication() {
        return application;
    }
}
