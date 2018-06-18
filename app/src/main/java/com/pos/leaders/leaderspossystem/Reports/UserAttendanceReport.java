package com.pos.leaders.leaderspossystem.Reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.EmployeeAttendanceReportListViewAdapter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by KARAM on 30/10/2016.
 */

public class UserAttendanceReport extends AppCompatActivity {

	TextView etFromDate, etToDate;
	TextView tvTotalHour;
	ListView lvReport;
	Button print;
	ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
	List<ScheduleWorkers> scheduleWorkersList, _scheduleWorkersList;
	Date from, to;
	int mYear, mMonth, mDay;
	long userID;
	private static final int DIALOG_FROM_DATE = 825;
	private static final int DIALOG_TO_DATE = 324;
	EmployeeAttendanceReportListViewAdapter adapter;
	Bitmap page=null ;
	public static final String SAMPLE_FILE = "randompdf.pdf";
	ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.attendance_report_user);
		TitleBar.setTitleBar(this);
		etFromDate = (TextView) findViewById(R.id.userAttendanceReport_ETFrom);
		etToDate = (TextView) findViewById(R.id.userAttendanceReport_ETTo);
		tvTotalHour = (TextView) findViewById(R.id.userAttendanceReport_TVTotalHourlyWork);
		lvReport = (ListView) findViewById(R.id.userAttendanceReport_LVReport);
		print=(Button)findViewById(R.id.attendance_report_print);
		print.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					PrintTools pt=new PrintTools(UserAttendanceReport.this);

				for (int i= 1;i<bitmapList.size(); i++) {
					Log.d("bitmapsize",bitmapList.size()+"");
					pt.PrintReport(bitmapList.get(i));

				}
			}
		});
		//region Date
		Date d = new Date();
		Log.i("current date", d.toString());
		//Mon Oct 24 08:45:01 GMT 2016
		//("yyyy-mm-dd hh:mm:ss")
		mYear = Integer.parseInt(d.toString().split(" ")[5]);
		mMonth = d.getMonth();
		mDay = Integer.parseInt(d.toString().split(" ")[2]);
/*
		etStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_ID);
			}
		});

		etEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_ID2);
			}
		});
		//endregion
*/

		etFromDate.setText(DateConverter.getBeforeMonth().split(" ")[0]);
		from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
		etFromDate.setFocusable(false);
		etFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_FROM_DATE);
			}
		});
		etToDate.setText(DateConverter.currentDateTime().split(" ")[0]);
		to = DateConverter.stringToDate(DateConverter.currentDateTime());
		etToDate.setFocusable(false);
		etToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_TO_DATE);
			}
		});


		scheduleWorkersDBAdapter = new ScheduleWorkersDBAdapter(this);
		scheduleWorkersDBAdapter.open();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userID = extras.getLong("employeeId");
		}
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_user_attendence_report, lvReport, false);
		lvReport.addHeaderView(header, null, false);
		setReportDate();
	}

	private void setReportDate() {
		scheduleWorkersList = new ArrayList<ScheduleWorkers>();

		_scheduleWorkersList = scheduleWorkersDBAdapter.getAllUserScheduleWorkBtweenToDate(userID,from,to);
		Log.d("scedule list",_scheduleWorkersList.toString());


		long r=0,h=0,m=0,s=0;
		for (ScheduleWorkers sw : _scheduleWorkersList) {
				scheduleWorkersList.add(sw);
			if(sw.getExitTime()>0&&sw.getStartTime()>0) {
				r += DateConverter.getDateDiff(new Date(sw.getStartTime()), new Date(sw.getExitTime()), TimeUnit.MILLISECONDS);
			}
		}

		h=r/(1000*60*60);
		m=((r-(h*1000*60*60))/(1000*60));
		s=(r-(m*1000*60)-(h*1000*60*60))/(1000);

		tvTotalHour.setText(String.format("%02d:%02d:%02d",h,m,s));

		adapter = new EmployeeAttendanceReportListViewAdapter(this, R.layout.list_adapter_row_attendance, _scheduleWorkersList);
		lvReport.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		try {
			PdfUA pdfUA = new PdfUA();

			pdfUA.printUserReport(getApplicationContext(),_scheduleWorkersList,userID,from,to);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try
		{
			File path = new File( Environment.getExternalStorageDirectory(), getPackageName() );
			File file = new File(path,SAMPLE_FILE);
			RandomAccessFile f = new RandomAccessFile(file, "r");
			byte[] data = new byte[(int)f.length()];
			f.readFully(data);
			pdfLoadImages(data);
			//pdfLoadImages1(data);
		}
		catch(Exception ignored)
		{
		}
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_FROM_DATE) {
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, onFromDateSetListener, Integer.parseInt(from.toString().split(" ")[5]), from.getMonth(),  Integer.parseInt(from.toString().split(" ")[2]));
			//datePickerDialog.getDatePicker().setMaxDate(to.getTime());
			datePickerDialog.getDatePicker().setCalendarViewShown(false);
			return datePickerDialog;
		} else if (id == DIALOG_TO_DATE) {
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, onToDateSetListener, Integer.parseInt(to.toString().split(" ")[5]), to.getMonth(),  Integer.parseInt(to.toString().split(" ")[2]));
			//datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
			//datePickerDialog.getDatePicker().setMinDate(from.getTime());
			datePickerDialog.getDatePicker().setCalendarViewShown(false);
			return datePickerDialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener onFromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
			mYear = view.getYear();
			mMonth = view.getMonth()+1;
			mDay = view.getDayOfMonth();
			String s = mYear + "-" + mMonth+ "-" + mDay;
			etFromDate.setText(s);
			SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
			String currentDateTimeString = df2.format(new Date());
			from = DateConverter.stringToDate(s + " "+currentDateTimeString);
			setReportDate();
		}
	};
	private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
			mYear = view.getYear();
			mMonth = view.getMonth()+1;
			mDay = view.getDayOfMonth();
			String s = mYear + "-" + mMonth+ "-" + mDay;
			etToDate.setText(s);
			SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
			String currentDateTimeString = df2.format(new Date());
			to = DateConverter.stringToDate(s + " "+currentDateTimeString);
			setReportDate();
		}
	};

	private void pdfLoadImages(final byte[] data) {
		bitmapList=new ArrayList<Bitmap>();
		try {
			// run async
			new AsyncTask<Void, Void, String>() {
				// create and show a progress dialog
				ProgressDialog progressDialog = ProgressDialog.show(UserAttendanceReport.this, "", "Opening...");

				@Override
				protected void onPostExecute(String html) {
					//after async close progress dialog
					progressDialog.dismiss();
					//load the html in the webview
			//	wv1.loadDataWithBaseURL("", html, "randompdf/html", "UTF-8", "");
				}

				@Override
				protected String doInBackground(Void... params) {
					try {
						//create pdf document object from bytes
						ByteBuffer bb = ByteBuffer.NEW(data);
						PDFFile pdf = new PDFFile(bb);
						//Get the first page from the pdf doc
						PDFPage PDFpage = pdf.getPage(1, true);
						//create a scaling value according to the WebView Width
						final float scale = 800 / PDFpage.getWidth() * 0.80f;
						//convert the page into a bitmap with a scaling value
						page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
						//save the bitmap to a byte array
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						page.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byte[] byteArray = stream.toByteArray();
						stream.reset();
						//convert the byte array to a base64 string
						String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
						//create the html + add the first image to the html
						String html = "<!DOCTYPE html><html><body bgcolor=\"#ffffff\"><img src=\"data:image/png;base64," + base64 + "\" hspace=328 vspace=4><br>";
						//loop though the rest of the pages and repeat the above
						for (int i = 0; i <= pdf.getNumPages(); i++) {
							PDFpage = pdf.getPage(i, true);
							page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
							page.compress(Bitmap.CompressFormat.PNG, 100, stream);
						bitmapList.add(page);
							byteArray = stream.toByteArray();
							stream.reset();
							base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
							html += "<img src=\"data:image/png;base64," + base64 + "\" hspace=10 vspace=10><br>";

					}
                     Log.d("bit",bitmapList.size()+"");
						stream.close();
						html += "</body></html>";
						return html;
					} catch (Exception e) {
						Log.d("error", e.toString());
					}
					return null;
				}
			}.execute();
			System.gc();// run GC
		} catch (Exception e) {
			Log.d("error", e.toString());
		}
	}
}
