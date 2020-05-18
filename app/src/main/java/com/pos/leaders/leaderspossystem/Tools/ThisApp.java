package com.pos.leaders.leaderspossystem.Tools;

/**
 * Created by Win8.1 on 5/13/2020.
 */


        import android.app.Activity;
        import android.app.Application;
        import android.content.Context;


public class ThisApp extends Application {

    private static Context mContext = null;

    private static Activity mCurrentActivity = null;

    @Override
    public void onCreate() {

        super.onCreate();

        mContext = getApplicationContext();

    }

    public static Context getContext() {

        return mContext;
    }

    public static Activity getCurrentActivity() {

        return mCurrentActivity;

    }

    public static void setCurrentActivity(Activity mCurrentActivity) {

        ThisApp.mCurrentActivity = mCurrentActivity;

    }
}
