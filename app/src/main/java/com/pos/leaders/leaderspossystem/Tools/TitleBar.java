package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by KARAM on 11/10/2017.
 */

public class TitleBar {
    private static android.support.v7.app.ActionBar actionBar;
    private static ImageView ivInternet;
    private static ImageView ivSync ;
    public static void setTitleBar(final AppCompatActivity context) {
        final ViewGroup actionBarLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.title_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Set up your ActionBar
        actionBar = context.getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        context.getSupportActionBar().setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBarLayout.setLayoutParams(params);
        actionBar.setCustomView(actionBarLayout);
        long date;
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // You customization
        final int actionBarColor = context.getResources().getColor(R.color.primaryColor);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final TextView actionBarTitle = (TextView) context.findViewById(R.id.date);
        actionBarTitle.setText(format.format(ca.getTime()));
        final TextView actionBarSent = (TextView) context.findViewById(R.id.posID);
        actionBarSent.setText("POSID  "+ SESSION.POS_ID_NUMBER);


        final TextView actionBarStaff = (TextView) context.findViewById(R.id.userName);
        actionBarStaff.setText(SESSION._USER.getFullName());

        final TextView actionBarLocations = (TextView) context.findViewById(R.id.userPermtions);
        actionBarLocations.setText(" "+SESSION._USER.getPermtionName());


        ivInternet = (ImageView) context.findViewById(R.id.titleBar_ivInternetStatus);
        ivInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SyncMessage.isConnected(context)) {
                    SESSION.internetStatus = InternetStatus.CONNECTED;
                } else {
                    SESSION.internetStatus = InternetStatus.ERROR;
                }
                refreshStatus(context);
            }
        });


        ivSync = (ImageView) context.findViewById(R.id.titleBar_ivSync);
        ivSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isSyncServiceRunning(context)){
                    SESSION.syncStatus = SyncStatus.ENABLED;
                } else {
                    SESSION.syncStatus = SyncStatus.DISABLED;
                }
                refreshStatus(context);
            }
        });

        refreshStatus(context);
    }


    private static void refreshStatus(Context context){
        switch (SESSION.internetStatus) {
            case CONNECTED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivInternet.setImageDrawable(context.getResources().getDrawable(R.drawable.internet_connected, context.getTheme()));
                } else {
                    ivInternet.setImageDrawable(context.getResources().getDrawable(R.drawable.internet_connected));
                }
                break;
            case ERROR:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivInternet.setImageDrawable(context.getResources().getDrawable(R.drawable.internet_error, context.getTheme()));
                } else {
                    ivInternet.setImageDrawable(context.getResources().getDrawable(R.drawable.internet_error));
                }
                break;
        }
        switch (SESSION.syncStatus) {
            case ENABLED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivSync.setImageDrawable(context.getResources().getDrawable(R.drawable.cloud_sync, context.getTheme()));
                } else {
                    ivSync.setImageDrawable(context.getResources().getDrawable(R.drawable.cloud_sync));
                }
                break;
            case DISABLED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivSync.setImageDrawable(context.getResources().getDrawable(R.drawable.cloud_sync_error, context.getTheme()));
                } else {
                    ivSync.setImageDrawable(context.getResources().getDrawable(R.drawable.cloud_sync_error));
                }
                break;
        }
    }
}
