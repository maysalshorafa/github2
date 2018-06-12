package com.pos.leaders.leaderspossystem.Printer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import POSSDK.POSSDK;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

public class TestPrintInfo {
	
	private static final String LOG_TAG = "SNBC_POS";
	
	//Returned Value
	public static final int POS_SUCCESS=1000;		//success	
	public static final int ERR_PROCESSING = 1001;	//processing error
	public static final int ERR_PARAM = 1002;		//parameter error
	private int error_code = 0;
	
	// ---------------------------------------------------------
	// Print mode options.
	private static final int PRINT_MODE_STANDARD = 0;
	private static final int PRINT_MODE_PAGE = 1;
//	private int printMode = PRINT_MODE_STANDARD;
	
	//----------------------------------------------------------
	// thread flag
	private boolean ThreadFlg1 = false;
	private boolean ThreadFlg2 = false;
	private POSSDK thread_sdk = null;

	// ---------------------------------------------------------
	// Font type options.
	private static final int POS_FONT_TYPE_STANDARD   = 0x00;
	private static final int POS_FONT_TYPE_COMPRESSED = 0x01;
	private static final int POS_FONT_TYPE_CHINESE    = 0x03;


	// ---------------------------------------------------------
	// Font style options.
	private static final int POS_FONT_STYLE_NORMAL            =   0x00;
	private static final int POS_FONT_STYLE_BOLD              =   0x08;
	private static final int POS_FONT_STYLE_THIN_UNDERLINE    =   0x80;
	private static final int POS_FONT_STYLE_UPSIDEDOWN        =  0x200;
	private static final int POS_FONT_STYLE_REVERSE           =  0x400;

	// Specify the area direction of paper or lable.
	private static final int POS_AREA_LEFT_TO_RIGHT = 0x00;

	// ---------------------------------------------------------
	// Cut mode options.
	private static final int POS_CUT_MODE_FULL				= 0x41;

	// ---------------------------------------------------------
	// Mode options of printing bit image in RAM or Flash.
	private static final int POS_BITMAP_PRINT_NORMAL        = 0x00;


	// ---------------------------------------------------------
	// Barcode HRI's position.
	private static final int POS_HRI_POSITION_BOTH  = 0x03;


	/**
	 * Name��TestPrintText
	 * 
	 * Function��test print text 
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK Variable
	 * @param printMode[in]��print mode
	 * @param txtbuf[in]��print data
	 * @param DataLength[in]: the length of the print data
     * @param FontType[in]��  font type
     * @param FontStyle[in]: font style
     * @param Alignment[in]��the alignment type
     * @param HorStartingPosition[in]: starting horizontal position
     * @param VerStartingPosition[in]:starting vertical position
     * @param LineHeight[in]: line height
     * @param HorizontalTimes[in]��horizontal number of Times
     * @param VerticalTimes[in]��vertical number of Times
	 * 						
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestPrintText(POSSDK pos_sdk,int printMode,String txtbuf,int DataLength,int FontType, int FontStyle,
			int Alignment,int HorStartingPosition,int VerStartingPosition,int LineHeight,int HorizontalTimes,int VerticalTimes) 
	{ 
		
		//**********************************************************************************************
		//Download file test
//		String str = "/data/bmp/BTP-R980.JK";
//		error_code = pos_sdk.systemDownloadFile(str,5000000);
//		if(error_code == POS_SUCCESS){
//			System.out.println("Download file success!");
//			return error_code;
//		}else{
//			System.out.println("Download file fail!");
//		}
		
		//***********************************************************************************************
		//print variety of data test 
//		int count = 0;
//		String str = "/data/bmp/record_data.txt";
//		while(true){
//			count++;
//			error_code = pos_sdk.systemDownloadFile(str,20000);
//			if(error_code == POS_SUCCESS){
//				System.out.println("Download file success!");
//			}else
//			{
//				System.out.println("Download file fail!");
//				return error_code;
//			}
//			 try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			 if(count > 1000){
//				 break;
//			 }
//		}

		
		//***********************************************************************************************
		//Multithreaded test 
//		thread_sdk = pos_sdk;
//		thread1 tr1 = new thread1();
//		thread2 tr2 = new thread2();
//		long tick,totulTick;
//		tick = System.currentTimeMillis();
//		totulTick = tick+2000;
//		if(ThreadFlg1 == false){
//			tr1.start();//Start a thread 1 
//		}
//		if(ThreadFlg2 == false){
//			tr2.start();//Start a thread 2ss
//		}
//		while(true){
//			tr1.printText();
//			tr2.printText();
//			tick = System.currentTimeMillis();
//			if(tick > totulTick){
//				tr1.stop();
//				tr2.stop();
//				break;
//			}
//		}
		
		
		if(printMode == PRINT_MODE_PAGE)
		{
			//set print area
			error_code = pos_sdk.pageModeSetPrintArea(0, 0, 640, 100, 0);
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
			//set print position
			error_code = pos_sdk.pageModeSetStartingPosition(HorStartingPosition,VerStartingPosition); 
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
		}else{
			//set the alignment type
			error_code = pos_sdk.textStandardModeAlignment(Alignment);
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}	
		}
		
		//set the horizontal and vertical motion units 
		error_code = pos_sdk.systemSetMotionUnit(100, 100);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
	
		//set line height
		error_code = pos_sdk.textSetLineHeight(LineHeight);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		
		//set character font 
		error_code = pos_sdk.textSelectFont(FontType,FontStyle);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		
		//set character size
		error_code = pos_sdk.textSelectFontMagnifyTimes(HorizontalTimes,VerticalTimes);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		
		//print text
		try {
			byte []send_buf = txtbuf.getBytes("GB18030");
			error_code = pos_sdk.textPrint(send_buf, send_buf.length);
			send_buf = null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		
		//feed line
		error_code = pos_sdk.systemFeedLine(1);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		
		//entry page mode
		if(printMode == PRINT_MODE_PAGE)
		{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//*****************************************************************************************
			//clear buffer 
			error_code = pos_sdk.pageModeClearBuffer();    			
		}
		return error_code;
	}
	
	/**
	 * Name��TestPrintBar
	 * 
	 * Function��test print BarCode
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
	 * @param pszBuffer[in]��print data of barcode
	 * @param DataLength[in]��the length of print data
     * @param nType[in]��  barcode type
     * @param nWidthX[in]: barcode module width [2 6]
     * @param nHeight[in]: barcode height[1 255]
     * @param nHriFontType[in]:Font Type [0 1]
     * @param nHriFontPosition[in]: Hri Font Position [0 3]
	 * 						
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestPrintBar(POSSDK pos_sdk,int printMode,String pszBuffer,int DataLength,int nType, int nWidthX,
			int nHeight, int nHriFontType, int nHriFontPosition){
			
			if(printMode == PRINT_MODE_PAGE)
			{
				//set print area
				error_code = pos_sdk.pageModeSetPrintArea(0, 0, 640, 200, 0);
				if(error_code != POS_SUCCESS)
				{
					return error_code;
				}
    			//set print position
    			error_code = pos_sdk.pageModeSetStartingPosition(20,100);
				if(error_code != POS_SUCCESS)
				{
					return error_code;
				}
			}
			error_code = pos_sdk.barcodePrint1Dimension(pszBuffer, DataLength,nType, nWidthX, nHeight, nHriFontType, nHriFontPosition);
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
			error_code = pos_sdk.systemFeedLine(1);
    		if(printMode == PRINT_MODE_PAGE)
    		{
        		//******************************************************************************************
        		//print in page mode
    			error_code = pos_sdk.pageModePrint();
        		
    			//*****************************************************************************************
    			//clear buffer in page mode
    			error_code = pos_sdk.pageModeClearBuffer();   			
    		}
    		return error_code;
	}

	
	/**
	 * Name��TestPrintPDF417
	 * 
	 * Function��test print PDF417
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
	 * @param pszBuffer[in]: print data
	 * @param DataLength[in]: the length of print data
	 * @param AppearanceToHeight[in]��Appearance to height[1-10]
     * @param AppearanceToWidth[in]�� Appearance to width[1-100]
     * @param RowsNumber[in]��the number of rows[3-90]
     * @param ColumnsNumber[in]��the number of columns[1-30]
     * @param Xsize[in]��[1-7]
     * @param LineHeight[in]��line height[2-25]
	 * @param nCorrectGrade Correction Grade[0-8]
	 * 
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 *     
	 */
	public int TestPrintPDF417(POSSDK pos_sdk,int printMode,String pszBuffer,int DataLength,
			int AppearanceToHeight,int AppearanceToWidth,int RowsNumber,
			int ColumnsNumber,int Xsize, int LineHeight,int nCorrectGrade) {
		if(printMode == PRINT_MODE_PAGE)
		{
			//set print area
			error_code = pos_sdk.pageModeSetPrintArea(0, 0, 640, 300, 0);
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
			//set print position
			error_code = pos_sdk.pageModeSetStartingPosition(20,100); 
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
		}
		error_code = pos_sdk.barcodePrintPDF417(pszBuffer, DataLength, AppearanceToHeight, AppearanceToWidth, RowsNumber, ColumnsNumber, Xsize, LineHeight, nCorrectGrade);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		error_code = pos_sdk.systemFeedLine(1);
		if(printMode == PRINT_MODE_PAGE)
		{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//******************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer();   		
		}
		return error_code;
	}

	/**
	 * Name��TestPrintQR
	 * 
	 * Function�� test print QR
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK Variable
	 * @param printMode[in]��print mode
     * @param pszBuffer[in]��print data
     * @param DataLength[in]�� the length of print data
     * @param nOrgx[in]�� the starting point distance from the left border 
     * @param iWeigth[in]��module width[1 10]��
     * @param iSymbolType[in]��symbol type[1,2]
	 * @param iLanguageMode[in]��language mode 0��Chinese��1��Japanese
	 * 					
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestPrintQR(POSSDK pos_sdk,int printMode,String pszBuffer,int DataLength,int nOrgx,int iWeigth,int iSymbolType,int iLanguageMode) {
		if(printMode == PRINT_MODE_PAGE)
		{
			//set print area
			error_code = pos_sdk.pageModeSetPrintArea(0, 0, 640, 300, 0);
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
			//set print position
			error_code = pos_sdk.pageModeSetStartingPosition(20,100); 
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
		}
		error_code = pos_sdk.barcodePrintQR(pszBuffer,DataLength, nOrgx, iWeigth, iSymbolType, iLanguageMode);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		error_code = pos_sdk.systemFeedLine(2);
		if(printMode == PRINT_MODE_PAGE)
		{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//*****************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer();     			
		}
		return error_code;

	}// end of function TestPrintQR
	
	/**
	 * Name��TestPrintGS1
	 * 
	 * Function��test print GS1
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
	 * @param pszBuffer[in]��print data
	 * @param DataLength[in]: the length of the print data
     * @param BarcodeType[in]��Barcode Type[1-7]
     * @param BasicElementWidth[in]��Basic element width[1-6]
     * @param BarcodeHeight[in]��Barcode height[2-250]
     * @param BasicElementHeight[in]�� Basic element height[1-10]
     * @param SeparatorHeight[in]:Separator height[1-10]
     * @param SegmentHeight[in]:Segment height[2-20]
     * @param HRI[in]��HRI(Human Readable Interpretation) character[1-4]
     * @param AI[in]��whether to use AI 0-no use, 1-use
	 * 						
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestPrintGS1(POSSDK pos_sdk,int printMode,String pszBuffer,int DataLength,int BarcodeType,int BasicElementWidth,int BarcodeHeight,
			int BasicElementHeight,int SeparatorHeight,int SegmentHeight,int HRI,int AI){
		if(printMode == PRINT_MODE_PAGE)
		{
			//set print area
			error_code = pos_sdk.pageModeSetPrintArea(0, 0, 640, 300, 0);
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
			//set print position
			error_code = pos_sdk.pageModeSetStartingPosition(20,100); 
			if(error_code != POS_SUCCESS)
			{
				return error_code;
			}
		}
		error_code = pos_sdk.barcodePrintGS1DataBar(pszBuffer, DataLength, BarcodeType, 
				BasicElementWidth, BarcodeHeight, BasicElementHeight, SeparatorHeight, SegmentHeight, HRI, AI);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		
		error_code = pos_sdk.systemFeedLine(1);
		if(printMode == PRINT_MODE_PAGE)
		{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//*****************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer(); 	
		}
		return error_code;
	}
	/**
	 * Name��TestFeedLine
	 * 
	 * Function�� feed line
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
	 * 					
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestFeedLine(POSSDK pos_sdk,int printMode)
	{
		
		if(printMode == PRINT_MODE_PAGE)
		{ 
			error_code = pos_sdk.systemFeedLine(1);
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//*****************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer(); 
		}
		else
		{
			error_code = pos_sdk.systemFeedLine(3);
		}
		return error_code;
	}
	
	/**
	 * Name��TestCutPaper
	 * 
	 * Function��cut paper
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
	 * 					
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestCutPaper(POSSDK pos_sdk,int printMode)
	{
		if(printMode == PRINT_MODE_PAGE)
		{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			error_code = pos_sdk.systemCutPaper(66, 0);	
			
			//*****************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer(); 			
		}else{
			error_code = pos_sdk.systemCutPaper(66, 0);	
		}
		return error_code;
	}

	/**
	 * Name��TestPrintBitmap
	 * 
	 * Function��Test print bitmap
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
     * @param img_name[in]��image name
     * @param img_type[in]�� print type 0��RAM��1��FALSH��
	 * 					
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestPrintBitmap(POSSDK pos_sdk,int printMode,String img_name,int img_type) {

			FileInputStream temp_stream = null;
			Bitmap image;
			final int PrinterWidth = 640;
			int image_size = 0;
			byte pzsCommand[] = {0x1C,0x71,0x00};
			
			if(printMode == PRINT_MODE_PAGE)
			{
    			//*****************************************************************************************
    			//set print area
    			error_code = pos_sdk.pageModeSetPrintArea(0,0,640,500,0);
    			if(error_code !=POS_SUCCESS)
    			{
    				return error_code;
    			}
    			
    			//set print position
    			error_code = pos_sdk.pageModeSetStartingPosition(20,200);  
    			if(error_code !=POS_SUCCESS)
    			{
    				return error_code;
    			}
			}

			//*****************************************************************************************
			//download bitmap to RAM and print
			if(img_type == 0)
			{
				//*****************************************************************************************
				//read bitmap data
				try {
					temp_stream = new FileInputStream(img_name);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(temp_stream == null){
					return ERR_PROCESSING;
				}
				
				image = BitmapFactory.decodeStream(temp_stream);
				if(image == null){
					return ERR_PROCESSING;
				}
				

				//download bitmap
    			error_code = pos_sdk.imageDownloadToPrinterRAM(2, image, PrinterWidth);
    			if(error_code !=POS_SUCCESS)
    			{
    				return error_code;
    			}
    			
    			//print bitmap
    			error_code = pos_sdk.imageRAMPrint(2,0);
    			if(error_code !=POS_SUCCESS)
    			{
    				return error_code;
    			}			
			}
			//*********************************************************************************************
			//download bitmap to flash and print
			else if(img_type == 1)
			{
				//download bitmap
				String sigPaths[] = img_name.split("@");
//				String sigPaths[] = str_name.split("@");
				int image_num = sigPaths.length;
				Bitmap cg_image[] = new Bitmap[image_num];
				int i = 0;
				
				//*******************************************************************************************
				//read bitmap data
				for(i = 0; i < image_num; i++)
				{
					try {
						temp_stream = new FileInputStream(sigPaths[i]);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					if(temp_stream == null)
					{
						return ERR_PROCESSING;
					}
					//Application of memory can not be more than 32M 
					cg_image[i] = BitmapFactory.decodeStream(temp_stream);
					if(cg_image[i] == null){
						temp_stream = null;
						return ERR_PROCESSING;
					}
					temp_stream = null;
				}
				
				error_code = pos_sdk.imageDownloadToPrinterFlash(image_num, cg_image, PrinterWidth);
				if(error_code != POS_SUCCESS){
					pos_sdk.pos_command.WriteBuffer(pzsCommand, 0, pzsCommand.length, 5000);//Empties the buffer  of FALSH
					return error_code;
				}
				
				//print bitmap
				for(i = 0; i < image_num;i++)
				{
					error_code = pos_sdk.imageFlashPrint(i+1, 0);
					if(error_code != POS_SUCCESS)
					{
						pos_sdk.pos_command.WriteBuffer(pzsCommand, 0, pzsCommand.length, 5000);//Empties the buffer  of FALSH
						return error_code;
					}	
				}
				//destroy bitmap buffer
				for(i = 0; i < image_num; i++)
				{
					cg_image[i].recycle();
				}
			}
		if(printMode == PRINT_MODE_PAGE)
		{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//*****************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer();   			
		}
		temp_stream = null;
		pos_sdk.pos_command.WriteBuffer(pzsCommand, 0, pzsCommand.length, 5000);//Empties the buffer  of FALSH
		return error_code;
	}
	
	/**
	 * Name��TestUserDefinedCharacter
	 * 
	 * Function�� test Employee-Defined Character
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
	 * 					
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestUserDefinedCharacter(POSSDK pos_sdk,int printMode)
	{
		String str = "0123456789";
		String path =  
				"/data/bmp/u1.bmp" + "@" + 
			    "/data/bmp/u2.bmp" + "@" + 
			    "/data/bmp/u3.bmp";
		
		FileInputStream temp_stream = null;
		String sigPaths[] = path.split("@");
		int image_num = sigPaths.length;
		Bitmap cg_image[] = new Bitmap[image_num];
		int i = 0;
		
		if(printMode == PRINT_MODE_PAGE)
		{
			//*****************************************************************************************
			//set print area
			error_code = pos_sdk.pageModeSetPrintArea(0,0,640,500,0);
			if(error_code !=POS_SUCCESS)
			{
				return error_code;
			}
			
			//set print position
			error_code = pos_sdk.pageModeSetStartingPosition(20,200);  
			if(error_code !=POS_SUCCESS)
			{
				return error_code;
			}
		}
		
		//*******************************************************************************************
		//read bitmap data
		for(i = 0; i < image_num; i++)
		{
			try {
				temp_stream = new FileInputStream(sigPaths[i]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(temp_stream == null)
			{
				return ERR_PROCESSING;
			}
			cg_image[i] = BitmapFactory.decodeStream(temp_stream);
			temp_stream = null;
		}
		
		
		//Choose Font Employee Defined
		error_code = pos_sdk.textUserDefinedCharacterEnable(1);
		if(error_code != POS_SUCCESS){
			return error_code;
		}
		
		error_code = pos_sdk.textUserDefinedCharacterDefine(3, 12, 48, 50, cg_image);
		if(error_code != POS_SUCCESS){
			return error_code;	
		}
		
		error_code = pos_sdk.textSelectFontMagnifyTimes(2,2);
		
		//print text data
		error_code = pos_sdk.textPrint(str.getBytes(), str.getBytes().length);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}
		//feed line
		error_code = pos_sdk.systemFeedLine(1);
		
		//Cancel Font Employee Defined of CharCode
		error_code = pos_sdk.textUserDefinedCharacterCancel(48);
		error_code = pos_sdk.textUserDefinedCharacterCancel(49);
		error_code = pos_sdk.textUserDefinedCharacterCancel(50);
		
		if(printMode == PRINT_MODE_PAGE)
		{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//*****************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer();   			
		}
		return error_code;
	}
	
	/**
	 * Name��TestRasterBitmap
	 * 
	 * Function��Test Raster Bitmap
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param printMode[in]��print mode
	 * @param str_data[in]��print data
	 * @param paintSize[in]��paint size
	 * @param bold[in]��font bold of character
	 * 					
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int TestRasterBitmap(POSSDK pos_sdk,int printMode,String str_data,int paintSize,int bold)
	{
		final int PrinterWidth = 640;
		if(printMode == PRINT_MODE_PAGE)
		{
    		//*****************************************************************************************
    		//set print area
    		error_code = pos_sdk.pageModeSetPrintArea(0,0,640,500,0);
    		if(error_code != POS_SUCCESS)
    		{
    			return error_code;
    		}
    		//*****************************************************************************************
    		//set print position
    		error_code = pos_sdk.pageModeSetStartingPosition(20,200);  
    		if(error_code != POS_SUCCESS)
    		{
    			return error_code;
    		}
		}
	
		Bitmap c_image = null;
		
		//set print position
		pos_sdk.standardModeSetStartingPosition(10);
		
		//create raster bitmap
		c_image = pos_sdk.imageCreateRasterBitmap(str_data,paintSize,bold);
		error_code = pos_sdk.imageStandardModeRasterPrint(c_image,PrinterWidth);
		if(error_code != POS_SUCCESS)
		{
			return error_code;
		}	
		
    	if(printMode == PRINT_MODE_PAGE)
    	{
    		//******************************************************************************************
    		//print in page mode
			error_code = pos_sdk.pageModePrint();
    		
			//*****************************************************************************************
			//clear buffer in page mode
			error_code = pos_sdk.pageModeClearBuffer(); 		
    	}
			
		
		return error_code;
	}
	/**
	 * Name��POSNETQueryStatus
	 * 
	 * Function�� Get Printer state
	 * 
	 * Parameter��
	 * @param pos_sdk[in]��SDK variable
	 * @param pStatus[out]��state data
	 * 					
	 * Return��
	 * @return SUCCESS��POS_SUCCESS��FAIL��ERR_PROCESSING,ERR_PARAM
	 */
	public int POSNETQueryStatus(POSSDK pos_sdk,byte[] pStatus) {

		int result = POS_SUCCESS;
		int data_size = 0;
		byte[] recbuf = new byte[64];// accept buffer
		
		//Get firmware version 
//		byte pszCommand[] = {0x1D,(byte) 0x99,0x42,0x45,(byte) 0x92,(byte) 0x9A,0x35};
//		pos_sdk.pos_command.WriteBuffer(pszCommand, 0, pszCommand.length, 10000);
//		data_size = pos_sdk.pos_command.ReadBuffer(recbuf, 0, 25, 10000);
//		Log.d(LOG_TAG, "POS_ReadPort---enter,parameter:" + pos_sdk.pos_command.byte2hex(recbuf)+"--"+data_size);
//		if(data_size != -1){
//			return POS_SUCCESS;
//		}
		
		//Query Status
		//data_size = pos_sdk.systemQueryStatus(recbuf, 4, MainActivity.port_type);
		
//		Log.d(LOG_TAG, "POS_ReadPort---enter,parameter:" + pos_sdk.pos_command.byte2hex(recbuf)+"--"+data_size);
//		if(MainActivity.port_type != MainActivity.BLUETOOTHPORT)
//		{
			if ((recbuf[0] & 0x04) == 0x04) {
				// Drawer open/close signal is HIGH (connector pin 3).
				pStatus[0] |= 0x01;
			} else {
				pStatus[0] &= 0xFE;
			}

			if ((recbuf[0] & 0x08) == 0x08) {
				// Printer is Off-line.
				pStatus[0] |= 0x02;
			} else {
				pStatus[0] &= 0xFD;
			}

			if ((recbuf[0] & 0x20) == 0x20) {
				// Cover is open.
				pStatus[0] |= 0x04;
			} else {
				pStatus[0] &= 0xFB;
			}

			if ((recbuf[0] & 0x40) == 0x40) {
				// Paper is being fed by the FEED button.
				pStatus[0] |= 0x08;
			} else {
				pStatus[0] &= 0xF7;
			}

			if ((recbuf[1] & 0x40) == 0x40) {
				// Error occurs.
				pStatus[0] |= 0x10;
			} else {
				pStatus[0] &= 0xEF;
			}

			if ((recbuf[1] & 0x08) == 0x08) {
				// Auto-cutter error occurs.
				pStatus[0] |= 0x20;
			} else {
				pStatus[0] &= 0xDF;
			}

			if ((recbuf[2] & 0x03) == 0x03) {
				// Paper near-end is detected by the paper roll near-end sensor.
				pStatus[0] |= 0x40;
			} else {
				pStatus[0] &= 0xBF;
			}

			if ((recbuf[2] & 0x0C) == 0x0C) {
				// Paper roll end detected by paper roll sensor.
				pStatus[0] |= 0x80;
			} else {
				pStatus[0] &= 0x7F;
			}	
//		}
//		else //real-time status
//		{
//			if ((recbuf[0] & 0x04) == 0x04) {
//				// Drawer open/close signal is HIGH (connector pin 3).
//				pStatus[0] |= 0x01;
//			} else {
//				pStatus[0] &= 0xFE;
//			}
//
//			if ((recbuf[0] & 0x08) == 0x08) {
//				// Printer is Off-line.
//				pStatus[0] |= 0x02;
//			} else {
//				pStatus[0] &= 0xFD;
//			}
//
//			if ((recbuf[1] & 0x04) == 0x04) {
//				// Cover is open.
//				pStatus[0] |= 0x04;
//			} else {
//				pStatus[0] &= 0xFB;
//			}
//
//			if ((recbuf[1] & 0x08) == 0x08) {
//				// Paper is being fed by the FEED button.
//				pStatus[0] |= 0x08;
//			} else {
//				pStatus[0] &= 0xF7;
//			}
//
//			if ((recbuf[1] & 0x40) == 0x40) {
//				// Error occurs.
//				pStatus[0] |= 0x10;
//			} else {
//				pStatus[0] &= 0xEF;
//			}
//
//			if ((recbuf[2] & 0x08) == 0x08) {
//				// Auto-cutter error occurs.
//				pStatus[0] |= 0x20;
//			} else {
//				pStatus[0] &= 0xDF;
//			}
//
//			if ((recbuf[3] & 0x04) == 0x04 || (recbuf[3] & 0x08) == 0x08) {
//				// Paper near-end is detected by the paper roll near-end sensor.
//				pStatus[0] |= 0x40;
//			} else {
//				pStatus[0] &= 0xBF;
//			}
//
//			if ((recbuf[3] & 0x20) == 0x20 || (recbuf[3] & 0x40) == 0x40) {
//				// Paper roll end detected by paper roll sensor.
//				pStatus[0] |= 0x80;
//			} else {
//				pStatus[0] &= 0x7F;
//			}
//			
//		}

		return result;
	}
	
	//create thread1
	private class thread1 extends Thread{
		private boolean isread = false;
		String str = "1111111111";
		@Override
		public void run() {
			super.run();
			ThreadFlg1 = true;
			isread = true;
			ThreadFlg1 = false;
		}
		public void printText(){
			thread_sdk.textPrint(str.getBytes(), str.getBytes().length);
		}
	}
	
	//create thread2
	private class thread2 extends Thread{
		private boolean isread = false;
		String str ="2222222222";
		@Override
		public void run() {
			super.run();
			ThreadFlg2 = true;
			isread = true;
			ThreadFlg2 = false;
		}
		public void printText(){
			thread_sdk.textPrint(str.getBytes(), str.getBytes().length);
		}
	}
}
