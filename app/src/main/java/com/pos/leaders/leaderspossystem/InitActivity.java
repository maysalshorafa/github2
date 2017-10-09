package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Backup.Backup;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InitActivity extends AppCompatActivity {

    Button btnNewPOS, btnRestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_init);




        btnNewPOS = (Button) findViewById(R.id.initActivity_btNewPOS);
        btnRestore = (Button) findViewById(R.id.initActivity_btRestore);
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restore(v);
            }
        });
        btnNewPOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupNewPOS(v);
            }
        });
    }

    public void restore(View view){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.restore))
                .setMessage(getString(R.string.do_you_want_to_restore_the_system))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        fileIntent.setType("file/*");
                        try {
                            startActivityForResult(fileIntent, CONSTANT.COM_POS_LEADERS_LEADPOS_RESTORE_CHOSEFILE);
                        } catch (ActivityNotFoundException e) {
                            Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void setupNewPOS(View view){
        Intent intent = new Intent(this, SetupNewPOSOnlineActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setMessage(getString(R.string.wait_for_finish));
        if (requestCode == CONSTANT.COM_POS_LEADERS_LEADPOS_RESTORE_CHOSEFILE) {
            if (resultCode == RESULT_OK) {
                progressDialog.show();
                File file=new File(data.getData().getPath());
                if (file.getName() == Backup.FULL_BACKUP_FILE_NAME || true) {
                    if (Backup.decBackupDB(file)) {
                        Toast.makeText(this,getString(R.string.success_to_restore),Toast.LENGTH_SHORT);
                        Util.isFirstLaunch(this, true);
                        finish();
                    }
                    else {
                        Toast.makeText(this, getString(R.string.error_on_restore_the_system), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, getString(R.string.file_does_not_valid), Toast.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
