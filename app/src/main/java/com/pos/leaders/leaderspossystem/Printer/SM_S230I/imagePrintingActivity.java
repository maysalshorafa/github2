/*package com.pos.leaders.leaderspossystem.Printer.SM_S230I;

import java.util.Locale;

import com.StarMicronics.StarIOSDK.PrinterFunctions.RasterCommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class imagePrintingActivity extends Activity implements OnItemSelectedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printingimage);

		Spinner spinner_Image = (Spinner) findViewById(R.id.spinner_Image);
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this, R.layout.spinner, new String[] { "image1", "image2", "image3", "image4" });
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner_Image.setAdapter(ad);
		spinner_Image.setOnItemSelectedListener(this);

		String[] paper_width_array;
		if (PrinterTypeActivity.getPortSettings().toUpperCase(Locale.US).contains("PORTABLE")) {
			paper_width_array = new String[] { "2inch", "3inch", "4inch" };
		} else {
			paper_width_array = new String[] { "3inch", "4inch" };
		}
		Spinner spinner_paper_width = (Spinner) findViewById(R.id.spinner_paper_width);
		ArrayAdapter<String> ad_paper_width = new ArrayAdapter<String>(this, R.layout.spinner, paper_width_array);
		spinner_paper_width.setAdapter(ad_paper_width);
		ad_paper_width.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		CheckBox checkbox_bitImage_CompressAPI = (CheckBox) findViewById(R.id.checkbox_bitImage_CompressAPI);
		CheckBox checkbox_bitImage_pageMode = (CheckBox) findViewById(R.id.checkbox_bitImage_pageMode);

		if (PrinterTypeActivity.getPortSettings().toUpperCase(Locale.US).contains("ESCPOS")) {
			//
		} else {
			TextView textView_paper_width = (TextView) findViewById(R.id.textView_paper_width);
			checkbox_bitImage_pageMode.setVisibility(View.GONE);
			textView_paper_width.setVisibility(View.GONE);
		}

		checkbox_bitImage_CompressAPI.setChecked(true);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		ImageView imageView_Image = (ImageView) findViewById(R.id.imageView_Image);
		switch (arg2) {
		case 0:
			imageView_Image.setImageResource(R.drawable.image1);
			break;
		case 1:
			imageView_Image.setImageResource(R.drawable.image2);
			break;
		case 2:
			imageView_Image.setImageResource(R.drawable.image3);
			break;
		case 3:
			imageView_Image.setImageResource(R.drawable.image4);
			break;
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		//
	}

	public void PrintText(View view) {
		if (!checkClick.isClickEvent()) {
			return;
		}

		Spinner spinner_Image = (Spinner) findViewById(R.id.spinner_Image);
		int source = R.drawable.image1;
		switch (spinner_Image.getSelectedItemPosition()) {
		case 0:
			source = R.drawable.image1;
			break;
		case 1:
			source = R.drawable.image2;
			break;
		case 2:
			source = R.drawable.image3;
			break;
		case 3:
			source = R.drawable.image4;
			break;
		}
		getResources();

		Spinner spinner_paper_width = (Spinner) findViewById(R.id.spinner_paper_width);
		int paperWidth = 576;
		if (spinner_paper_width.getSelectedItem().toString().equals("2inch") ) {
			paperWidth = 384; // 2inch (384 dot)
		} else if (spinner_paper_width.getSelectedItem().toString().equals("3inch") ) {
			paperWidth = 576; // 3inch (576 dot)
		} else if (spinner_paper_width.getSelectedItem().toString().equals("4inch") ) {
			paperWidth = 832; // 4inch (832 dot)
		}

		boolean compressionEnable = false;
		boolean pageModeEnable = false;

		CheckBox checkbox_bitImage_CompressAPI = (CheckBox) findViewById(R.id.checkbox_bitImage_CompressAPI);
		if (checkbox_bitImage_CompressAPI.isChecked() == true) {
			compressionEnable = true;
		}

		CheckBox checkbox_bitImage_pageMode = (CheckBox) findViewById(R.id.checkbox_bitImage_pageMode);
		if (checkbox_bitImage_pageMode.isChecked() == true) {
			pageModeEnable = true;
		}

		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		if (portSettings.toUpperCase(Locale.US).contains("PORTABLE") && portSettings.toUpperCase(Locale.US).contains("ESCPOS")) {
			MiniPrinterFunctions.PrintBitmapImage(this, portName, portSettings, getResources(), source, paperWidth, compressionEnable, pageModeEnable);
		} else {
			RasterCommand rasterType = RasterCommand.Standard;
			if (portSettings.toUpperCase(Locale.US).contains("PORTABLE")) {
				rasterType = RasterCommand.Graphics;
			}
			PrinterFunctions.PrintBitmapImage(this, portName, portSettings, getResources(), source, paperWidth, compressionEnable, rasterType);
		}
	}

}
*/