package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.LogInActivity;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;
import com.pos.leaders.leaderspossystem.updater.AutoUpdateApk;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Timer;
import java.util.TimerTask;

import static com.pos.leaders.leaderspossystem.Tools.SendLog.sendLogFile;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by KARAM on 11/10/2017.
 */

public class TitleBar {
    private static android.support.v7.app.ActionBar actionBar;
    private static ImageView ivInternet;
    private static ImageView ivSync ;
    private static Timer timer = null;
    private static AutoUpdateApk aua = null;
    private static long lastUpdateCheck;
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
        if (SESSION._USER == null) {
            Intent intent = new Intent(context, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            SESSION._LogOut();
            context.startActivity(intent);
            //terminal stop
            //System.exit(0);
        }

        long date;
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // You customization
        final int actionBarColor = context.getResources().getColor(R.color.primaryColor);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final TextView actionBarTitle = (TextView) context.findViewById(R.id.date);
        actionBarTitle.setText(format.format(ca.getTime()));
        final TextView actionBarSent = (TextView) context.findViewById(R.id.posID);
        actionBarSent.setText("POSID  "+ SETTINGS.posID);



        final TextView actionBarStaff = (TextView) context.findViewById(R.id.userName);
        actionBarStaff.setText(SESSION._USER.getFullName());

        final TextView actionBarLocations = (TextView) context.findViewById(R.id.userPermtions);
      //  actionBarLocations.setText(" "+SESSION._USER.getPermtionName());


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
                    Toast.makeText(context, "Reconnect, please wait...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, SyncMessage.class);
                    intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, SETTINGS.BO_SERVER_URL);
                    context.startService(intent);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!Util.isSyncServiceRunning(context)) {
                        SESSION.syncStatus = SyncStatus.DISABLED;
                    }

                    SESSION.syncStatus = SyncStatus.ENABLED;
                }
                refreshStatus(context);
            }
        });
        ivSync.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (Util.isSyncServiceRunning(context)) {
                    Util.killSyncService(context);
                    SESSION.syncStatus = SyncStatus.DISABLED;
                    refreshStatus(context);
                    Toast.makeText(context, "Connection closed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        refreshStatus(context);


      if(!SETTINGS.timerState){

          SETTINGS.timerState = true;
          timer  = new Timer();
          timer.schedule(new LoggingTask(), 10800000, 10800000);

      }
        if(aua==null){
            aua = new AutoUpdateApk(context);
            aua.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object data) {
                    if( ((String)data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_GOT_UPDATE) ) {
                        android.util.Log.i("AutoUpdateApkActivity", "Have just received update!");
                    }
                    if( ((String)data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_HAVE_UPDATE) ) {
                        android.util.Log.i("AutoUpdateApkActivity", "There's an update available!");
                    }
                }
            });
            lastUpdateCheck=new Date().getTime();
        } else if(new Date().getTime()-lastUpdateCheck>(1000)*(60)*(3)){
            aua.checkUpdatesManually();
            lastUpdateCheck=new Date().getTime();
        }

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

class LoggingTask extends TimerTask{

    @Override
    public void run() {
        sendLogFile();
    }


}
