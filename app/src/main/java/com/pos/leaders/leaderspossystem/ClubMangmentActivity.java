package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupAdapter;
import com.pos.leaders.leaderspossystem.Models.Group;
import com.pos.leaders.leaderspossystem.Tools.ClubCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ClubMangmentActivity extends AppCompatActivity  {
    android.support.v7.app.ActionBar actionBar;

    List<Group> groups;
    GroupAdapter groupAdapter;
    GridView gvGroup;
    Button btAddGroup,btCancel;
    private static final int CHANGE_PASSWORD_DIALOG = 656;
    Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_club_mangment);


        TitleBar.setTitleBar(this);



        gvGroup = (GridView) findViewById(R.id.clubManagement_GVClub);
        btAddGroup = (Button) findViewById(R.id.clubManagement_BTNewClub);
        btCancel = (Button) findViewById(R.id.clubManagement_BTCancel);

        //region Buttons

        btAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClubMangmentActivity.this, Coustmer_Group.class);
                startActivity(intent);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });


        //endregion


        groupAdapter= new GroupAdapter(this);
        groupAdapter.open();
        groups = groupAdapter.getAllGroup();
        group = null;

        final ClubCatalogGridViewAdapter adapter = new ClubCatalogGridViewAdapter(this, groups);
        gvGroup.setAdapter(adapter);
        gvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String[] items = {
                        getString(R.string.view),
                        getString(R.string.edit),
                        getString(R.string.delete),
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ClubMangmentActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                intent = new Intent(ClubMangmentActivity.this, Coustmer_Group.class);
                                intent.putExtra("id", groups.get(position).getId());


                                startActivity(intent);
                            case 1:
                                intent = new Intent(ClubMangmentActivity.this, Coustmer_Group.class);
                                intent.putExtra("id", groups.get(position).getId());
                                startActivity(intent);
                                break;

                            case 2:
                                new AlertDialog.Builder(ClubMangmentActivity.this)
                                        .setTitle("Delete Club")
                                        .setMessage("Are you want to delete this Club?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                groupAdapter.deleteEntry(groups.get(position).getId());
                                                groups.remove(groups.get(position));
                                                gvGroup.setAdapter(adapter);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;

                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


}
