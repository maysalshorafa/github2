package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.ClubCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.List;

public class ClubManagementActivity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;

    List<Club> groups;
    ClubAdapter groupAdapter;
    GridView gvGroup;
    Button btAddGroup, btCancel;
    public static int Club_Management_View=0;
    public  static int  Club_Management_Edit=0;
    Club group;

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
                Intent intent = new Intent(ClubManagementActivity.this, Coustmer_Group.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


        groupAdapter = new ClubAdapter(this);
        groupAdapter.open();
        groups = groupAdapter.getAllGroup();
        group = null;

        final ClubCatalogGridViewAdapter adapter = new ClubCatalogGridViewAdapter(this, groups);
        gvGroup.setAdapter(adapter);
        gvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Club_Management_Edit=0;
                Club_Management_View=0;
                final String[] items = {
                        getString(R.string.view),
                        getString(R.string.edit),
                        getString(R.string.delete),
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ClubManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                Club_Management_View=9;
                                intent = new Intent(ClubManagementActivity.this, Coustmer_Group.class);
                                intent.putExtra("usedPointId", groups.get(position).getClubId());


                                startActivity(intent);
                            case 1:
                                Club_Management_Edit=10;
                                intent = new Intent(ClubManagementActivity.this, Coustmer_Group.class);
                                intent.putExtra("usedPointId", groups.get(position).getClubId());
                                startActivity(intent);
                                break;

                            case 2:
                                new AlertDialog.Builder(ClubManagementActivity.this)
                                        .setTitle(getString(R.string.delete)+" "+getString(R.string.club))
                                        .setMessage(getString(R.string.delete_club_message))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                groupAdapter.deleteEntry(groups.get(position).getClubId());
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

