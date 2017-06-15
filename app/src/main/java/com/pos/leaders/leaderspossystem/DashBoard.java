package com.pos.leaders.leaderspossystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Dash_bord_adapter;
import com.pos.leaders.leaderspossystem.Reports.UserAttendanceReport;

public class DashBoard extends AppCompatActivity {
    GridView grid;
    String[] dashbord_text = {
            "Main Screen",
            "Report",
            "Product",
            "Department",
            "Users",
            "Offers",
            "BackUp",
            "Tax",
            "Hours Of Work",

    } ;
    int[] imageId = {
            R.drawable.home,
            R.drawable.reports,
            R.drawable.products,
            R.drawable.departments,
            R.drawable.users,
            R.drawable.offers,
            R.drawable.backup,
            R.drawable.tax,
            R.drawable.hours


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Dash_bord_adapter adapter = new Dash_bord_adapter(DashBoard.this, dashbord_text, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id){
                // Send intent to SingleViewActivity
                onClickedImage(position);

               // Intent i = new Intent(getApplicationContext(), SingleViewActivity.class);
                // Pass image index
               // i.putExtra("id", position);
                //startActivity(i);
            }
        });

    }



    public void onClickedImage(int pos){
        Intent i;
    switch (pos){

        case 0://1.popUp selection 2.Main activity
            AlertDialog alertDialog = new AlertDialog.Builder(DashBoard.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Did you create A report?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), UserAttendanceReport.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
             break;
        case 1:
            i = new Intent(getApplicationContext(), ReportsManagementActivity.class);
            startActivity(i);

            break;
        case 2:
            i = new Intent(getApplicationContext(), ProductCatalogActivity.class);
            startActivity(i);
            break;
        case 3:
            i = new Intent(getApplicationContext(), DepartmentActivity.class);
            startActivity(i);
            break;
        case 4:
            i = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivity(i);
            break;
        case 5:
          i = new Intent(getApplicationContext(), OfferActivity.class);
            startActivity(i);
            break;
        case 6:
           i = new Intent(getApplicationContext(), BackupActivity.class);
            startActivity(i);
            break;
        case 7:

            break;
        case 8:

        break;

        default:break;





    }





    }
}
