package com.pos.leaders.leaderspossystem.Printer.SM_S230I;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.pos.leaders.leaderspossystem.Printer.SM_S230I.RasterDocument.RasPageEndMode;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.RasterDocument.RasSpeed;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.RasterDocument.RasTopMargin;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

public class PrinterFunctions {
	public enum NarrowWide {
		_2_6, _3_9, _4_12, _2_5, _3_8, _4_10, _2_4, _3_6, _4_8
	};

	public enum BarCodeOption {
		No_Added_Characters_With_Line_Feed, Adds_Characters_With_Line_Feed, No_Added_Characters_Without_Line_Feed, Adds_Characters_Without_Line_Feed
	}

	public enum Min_Mod_Size {
		_2_dots, _3_dots, _4_dots
	};

	public enum NarrowWideV2 {
		_2_5, _4_10, _6_15, _2_4, _4_8, _6_12, _2_6, _3_9, _4_12
	};

	public enum CorrectionLevelOption {
		Low, Middle, Q, High
	};

	public enum Model {
		Model1, Model2
	};

	public enum Limit {
		USE_LIMITS, USE_FIXED
	};

	public enum CutType {
		FULL_CUT, PARTIAL_CUT, FULL_CUT_FEED, PARTIAL_CUT_FEED
	};

	public enum Alignment {
		Left, Center, Right
	};

	public enum RasterCommand {
		Standard, Graphics
	};
	
	private static StarIOPort portForMoreThanOneFunction = null;
	
	private static int printableArea = 576; // for raster data

	/**
	 * This function is used to print a PDF417 barcode to standard Star POS printers
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param limit
	 *     Selection of the Method to use specifying the barcode size. This is either 0 or 1. 0 is Use Limit method and 1 is Use Fixed method. See section 3-122 of the manual (Rev 1.12).
	 * @param p1
	 *     The vertical proportion to use. The value changes with the limit select. See section 3-122 of the manual (Rev 1.12).
	 * @param p2
	 *     The horizontal proportion to use. The value changes with the limit select. See section 3-122 of the manual (Rev 1.12).
	 * @param securityLevel
	 *     This represents how well the barcode can be recovered if it is damaged. This value should be 0 to 8.
	 * @param xDirection
	 *     Specifies the X direction size. This value should be from 1 to 10. It is recommended that the value be 2 or less.
	 * @param aspectRatio
	 *     Specifies the ratio of the PDF417 barcode. This values should be from 1 to 10. It is recommended that this value be 2 or less.
	 * @param barcodeData
	 *     Specifies the characters in the PDF417 barcode.
	 */
	public static void PrintPDF417Code(Context context, String portName, String portSettings, Limit limit, byte p1, byte p2, byte securityLevel, byte xDirection, byte aspectRatio, byte[] barcodeData) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte[] setBarCodeSize = new byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x30, 0x00, 0x00, 0x00 };
		switch (limit) {
		case USE_LIMITS:
			setBarCodeSize[5] = 0;
			break;
		case USE_FIXED:
			setBarCodeSize[5] = 1;
			break;
		}

		setBarCodeSize[6] = p1;
		setBarCodeSize[7] = p2;
		commands.add(setBarCodeSize);

		commands.add(new byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x31, securityLevel });

		commands.add(new byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x32, xDirection });

		commands.add(new byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x33, aspectRatio });

		byte[] setBarcodeData = new byte[6 + barcodeData.length];
		setBarcodeData[0] = 0x1b;
		setBarcodeData[1] = 0x1d;
		setBarcodeData[2] = 0x78;
		setBarcodeData[3] = 0x44;
		setBarcodeData[4] = (byte) (barcodeData.length % 256);
		setBarcodeData[5] = (byte) (barcodeData.length / 256);
		System.arraycopy(barcodeData, 0, setBarcodeData, 6, barcodeData.length);
		commands.add(setBarcodeData);

		commands.add(new byte[] { 0x1b, 0x1d, 0x78, 0x50 });

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print a QR Code on standard Star POS printers
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param correctionLevel
	 *     The correction level for the QR Code. The correction level can be 7, 15, 25, or 30. See section 3-129 (Rev. 1.12).
	 * @param model
	 *     The model to use when printing the QR Code. See section 3-129 (Rev. 1.12).
	 * @param cellSize
	 *     The cell size of the QR Code. The value of this should be between 1 and 8. It is recommended that this value is 2 or less.
	 * @param barCodeData
	 *     Specifies the characters in the QR Code.
	 */
	public static void PrintQrCode(Context context, String portName, String portSettings, CorrectionLevelOption correctionLevel, Model model, byte cellSize, byte[] barCodeData) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte[] modelCommand = new byte[] { 0x1b, 0x1d, 0x79, 0x53, 0x30, 0x00 };
		switch (model) {
		case Model1:
			modelCommand[5] = 1;
			break;
		case Model2:
			modelCommand[5] = 2;
			break;
		}

		commands.add(modelCommand);

		byte[] correctionLevelCommand = new byte[] { 0x1b, 0x1d, 0x79, 0x53, 0x31, 0x00 };
		switch (correctionLevel) {
		case Low:
			correctionLevelCommand[5] = 0;
			break;
		case Middle:
			correctionLevelCommand[5] = 1;
			break;
		case Q:
			correctionLevelCommand[5] = 2;
			break;
		case High:
			correctionLevelCommand[5] = 3;
			break;
		}
		commands.add(correctionLevelCommand);

		commands.add(new byte[] { 0x1b, 0x1d, 0x79, 0x53, 0x32, cellSize });

		// Add BarCode data
		commands.add(new byte[] { 0x1b, 0x1d, 0x79, 0x44, 0x31, 0x00, (byte) (barCodeData.length % 256), (byte) (barCodeData.length / 256) });
		commands.add(barCodeData);
		commands.add(new byte[] { 0x1b, 0x1d, 0x79, 0x50 } );

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function opens the cash drawer connected to the printer This function just send the byte 0x07 to the printer which is the open cashdrawer command It is not possible that the OpenCashDraware and OpenCashDrawer2 are running at the same time.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 */
	public static void OpenCashDrawer(Context context, String portName, String portSettings) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		commands.add(new byte[] { 0x07 });

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function opens the cash drawer connected to the printer This function just send the byte 0x1a to the printer which is the open cashdrawer command The OpenCashDrawer2, delay time and power-on time is 200msec fixed. It is not possible that the OpenCashDraware and OpenCashDrawer2 are running at the same time.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 */
	public static void OpenCashDrawer2(Context context, String portName, String portSettings) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		commands.add(new byte[] { 0x1a });

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function checks the Firmware Informatin of the printer
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 */
	public static void CheckFirmwareVersion(Context context, String portName, String portSettings) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			Map<String, String> firmware = port.getFirmwareInformation();

			String modelName = firmware.get("ModelName");
			String firmwareVersion = firmware.get("FirmwareVersion");

			String message = "Model Name:" + modelName;
			message += "\nFirmware Version:" + firmwareVersion;

			Builder dialog = new Builder(context);
			dialog.setNegativeButton("OK", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Firmware Information");
			alert.setMessage(message);
			alert.setCancelable(false);
			alert.show();

		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("OK", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function checks the status of the printer
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param sensorActiveHigh
	 *     boolean variable to tell the sensor active of CashDrawer which is High
	 */
	public static void CheckStatus(Context context, String portName, String portSettings, boolean sensorActiveHigh) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			StarPrinterStatus status = port.retreiveStatus();

			if (status.offline == false) {
				String message = "Printer is online";

				if (status.compulsionSwitch == false) {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Close";
					} else {
						message += "\nCash Drawer: Open";
					}
				} else {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Open";
					} else {
						message += "\nCash Drawer: Close";
					}
				}

				Builder dialog = new Builder(context);
				dialog.setNegativeButton("OK", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			} else {
				String message = "Printer is offline";

				if (status.receiptPaperEmpty == true) {
					message += "\nPaper is empty";
				}

				if (status.coverOpen == true) {
					message += "\nCover is open";
				}

				if (status.compulsionSwitch == false) {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Close";
					} else {
						message += "\nCash Drawer: Open";
					}
				} else {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Open";
					} else {
						message += "\nCash Drawer: Close";
					}
				}

				Builder dialog = new Builder(context);
				dialog.setNegativeButton("OK", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			}

		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("OK", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function checks the status of the printer which does not have compulsion switch
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 */
	public static void CheckStatus(Context context, String portName, String portSettings) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			StarPrinterStatus status = port.retreiveStatus();

			if (status.offline == false) {
				String message = "Printer is online";

				Builder dialog = new Builder(context);
				dialog.setNegativeButton("OK", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			} else {
				String message = "Printer is offline";

				if (status.receiptPaperEmpty == true) {
					message += "\nPaper is empty";
				}

				if (status.coverOpen == true) {
					message += "\nCover is open";
				}

				Builder dialog = new Builder(context);
				dialog.setNegativeButton("OK", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			}

		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("OK", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function enable USB serial number of the printer
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (USB:)
	 * @param portSettings
	 *     Should be blank
	 */
	public static void EnableUSBSerialNumber(Context context, String portName, String portSettings, byte[] serialNumber) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			ArrayList<byte[]> setSerialNumCommand = new ArrayList<byte[]>();

			setSerialNumCommand.add(new byte[] { 0x1b, 0x23, 0x23, 0x57, 0x38, 0x2c });
			for (int i = 0; i < 8 - serialNumber.length; i++) {
				setSerialNumCommand.add(new byte[] { 0x30 });	// Fill in the top at "0" to be a total 8 digits.
			}
			setSerialNumCommand.add(serialNumber);
			setSerialNumCommand.add(new byte[] { 0x0a, 0x00 });

			byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(setSerialNumCommand);
			port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

			// Wait for 5 seconds until printer recover from software reset
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			byte[] mswenableCommand = new byte[] { 0x1b, 0x1d, 0x23, 0x2b, 0x43, 0x30, 0x30, 0x30, 0x32, 0x0a, 0x00, 0x1b, 0x1d, 0x23, 0x57, 0x30, 0x30, 0x30, 0x30, 0x30, 0x0a, 0x00 };

			port.writePort(mswenableCommand, 0, mswenableCommand.length);
			
			// Wait for 3 seconds until printer recover from software reset
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}

		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("OK", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function disable USB serial number of the printer
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (USB:)
	 * @param portSettings
	 *     Should be blank
	 */
	public static void DisableUSBSerialNumber(Context context, String portName, String portSettings) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			byte[] clearSerialNumCommand = new byte[] { 0x1b, 0x23, 0x23, 0x57, 0x38, 0x2c, '?', '?', '?', '?', '?', '?', '?', '?', 0x0a, 0x00 };

			port.writePort(clearSerialNumCommand, 0, clearSerialNumCommand.length);

			// Wait for 5 seconds until printer recover from software reset
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			byte[] mswedisableCommand = new byte[] { 0x1b, 0x1d, 0x23, 0x2d, 0x43, 0x30, 0x30, 0x30, 0x32, 0x0a, 0x00, 0x1b, 0x1d, 0x23, 0x57, 0x30, 0x30, 0x30, 0x30, 0x30, 0x0a, 0x00 };

			port.writePort(mswedisableCommand, 0, mswedisableCommand.length);
			
			// Wait for 3 seconds until printer recover from software reset
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}

		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("OK", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function is used to print barcodes in 39 format
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param barcodeData
	 *     These are the characters that will be printed in the barcode. The characters available for this bar code are listed in section 3-43 (Rev. 1.12).
	 * @param option
	 *     This tells the printer to put characters under the printed barcode or not. This may also be used to line feed after the barcode is printed.
	 * @param height
	 *     The height of the barcode. This is measured in pixels
	 * @param width
	 *     The width of the barcode. This value should be between 1 to 9. See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCode39(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, NarrowWide width) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte n1 = 0x34;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_6:
			n3 = 49;
			break;
		case _3_9:
			n3 = 50;
			break;
		case _4_12:
			n3 = 51;
			break;
		case _2_5:
			n3 = 52;
			break;
		case _3_8:
			n3 = 53;
			break;
		case _4_10:
			n3 = 54;
			break;
		case _2_4:
			n3 = 55;
			break;
		case _3_6:
			n3 = 56;
			break;
		case _4_8:
			n3 = 57;
			break;
		}
		byte n4 = height;
		byte[] command = new byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		commands.add(command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in 93 format
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param barcodeData
	 *     These are the characters that will be printed in the barcode. The characters available for this barcode are listed in section 3-43 (Rev. 1.12).
	 * @param option
	 *     This tells the printer to put characters under the printed barcode or not. This may also be used to line feed after the barcode is printed.
	 * @param height
	 *     The height of the barcode. This is measured in pixels
	 * @param width
	 *     This is the number of dots per module. This value should be between 1 to 3. See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCode93(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, Min_Mod_Size width) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte n1 = 0x37;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_dots:
			n3 = 49;
			break;
		case _3_dots:
			n3 = 50;
			break;
		case _4_dots:
			n3 = 51;
			break;
		}
		byte n4 = height;
		byte[] command = new byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		commands.add(command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in ITF format
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param barcodeData
	 *     These are the characters that will be printed in the barcode. The characters available for this barcode are listed in section 3-43 (Rev. 1.12).
	 * @param option
	 *     This tell the printer to put characters under the printed barcode or not. This may also be used to line feed after the barcode is printed.
	 * @param height
	 *     The height of the barcode. This is measured in pixels
	 * @param width
	 *     The width of the barcode. This value should be between 1 to 9. See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCodeITF(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, NarrowWideV2 width) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte n1 = 0x35;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_5:
			n3 = 49;
			break;
		case _4_10:
			n3 = 50;
			break;
		case _6_15:
			n3 = 51;
			break;
		case _2_4:
			n3 = 52;
			break;
		case _4_8:
			n3 = 53;
			break;
		case _6_12:
			n3 = 54;
			break;
		case _2_6:
			n3 = 55;
			break;
		case _3_9:
			n3 = 56;
			break;
		case _4_12:
			n3 = 57;
			break;
		}
		byte n4 = height;
		byte[] command = new byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		commands.add(command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in the 128 format
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param barcodeData
	 *     These are the characters that will be printed in the barcode. The characters available for this barcode are listed in section 3-43 (Rev. 1.12).
	 * @param option
	 *     This tell the printer to put characters under the printed barcode or not. This may also be used to line feed after the barcode is printed.
	 * @param height
	 *     The height of the barcode. This is measured in pixels
	 * @param width
	 *     This is the number of dots per module. This value should be between 1 to 3. See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCode128(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, Min_Mod_Size width) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte n1 = 0x36;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_dots:
			n3 = 49;
			break;
		case _3_dots:
			n3 = 50;
			break;
		case _4_dots:
			n3 = 51;
			break;
		}
		byte n4 = height;
		byte[] command = new byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		commands.add(command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function shows different cut patterns for Star POS printers.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param cuttype
	 *     The cut type to perform. The cut types are full cut, full cut with feed, partial cut, and partial cut with feed
	 */
	public static void performCut(Context context, String portName, String portSettings, CutType cuttype) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		byte[] autocutCommand = new byte[] { 0x1b, 0x64, 0x00 };
		switch (cuttype) {
		case FULL_CUT:
			autocutCommand[2] = 48;
			break;
		case PARTIAL_CUT:
			autocutCommand[2] = 49;
			break;
		case FULL_CUT_FEED:
			autocutCommand[2] = 50;
			break;
		case PARTIAL_CUT_FEED:
			autocutCommand[2] = 51;
			break;
		}

		commands.add(autocutCommand);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw text to the printer, showing how the text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param slashedZero
	 *     boolean variable to tell the printer to slash zeroes
	 * @param underline
	 *     boolean variable that tells the printer to underline the text
	 * @param invertColor
	 *     boolean variable that tells the printer to should invert text. All white space will become black but the characters will be left white.
	 * @param emphasized
	 *     boolean variable that tells the printer to should emphasize the printed text. This is somewhat like bold. It isn't as dark, but darker than regular characters.
	 * @param upperline
	 *     boolean variable that tells the printer to place a line above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *     boolean variable that tells the printer to print text upside down.
	 * @param heightExpansion
	 *     This integer tells the printer what the character height should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param widthExpansion
	 *     This integer tell the printer what the character width should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param leftMargin
	 *     Defines the left margin for text on Star portable printers. This number can be from 0 to 65536. However, remember how much space is available as the text can be pushed off the page.
	 * @param alignment
	 *     Defines the alignment of the text. The printers support left, right, and center justification.
	 * @param textData
	 *     The text to send to the printer.
	 * @param encode
	 *     Set encode for multi-byte character or blank for single byte character.
	 */
	public static void PrintText(Context context, String portName, String portSettings, boolean slashedZero, boolean underline, boolean invertColor, boolean emphasized, boolean upperline, boolean upsideDown, int heightExpansion, int widthExpansion, byte leftMargin, Alignment alignment, byte[] textData, String encode) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		commands.add(new byte[] { 0x1b, 0x40 }); // Initialization

		if (encode.startsWith("Shift_JIS")) {
			byte[] kanjiModeCommand = new byte[] { 0x1b, 0x71, 0x1b, 0x24, 0x31 }; // Shift-JIS Kanji Mode(Disable JIS(ESC q) + Enable Shift-JIS(ESC $ n))
			commands.add(kanjiModeCommand);
		} else if (encode.startsWith("ISO2022JP")) {
			byte[] kanjiModeCommand = new byte[] { 0x1b, 0x24, 0x30 }; // JIS Kanji Mode(Disable Shift-JIS(ESC $ n))
			commands.add(kanjiModeCommand);
		}

		byte[] slashedZeroCommand = new byte[] { 0x1b, 0x2f, 0x00 };
		if (slashedZero) {
			slashedZeroCommand[2] = 49;
		} else {
			slashedZeroCommand[2] = 48;
		}
		commands.add(slashedZeroCommand);

		byte[] underlineCommand = new byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		commands.add(underlineCommand);

		byte[] invertColorCommand = new byte[] { 0x1b, 0x00 };
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		commands.add(invertColorCommand);

		byte[] emphasizedPrinting = new byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		commands.add(emphasizedPrinting);

		byte[] upperLineCommand = new byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		commands.add(upperLineCommand);

		if (upsideDown) {
			commands.add(new byte[] { 0x0f });
		} else {
			commands.add(new byte[] { 0x12 });
		}

		byte[] characterExpansion = new byte[] { 0x1b, 0x69, 0x00, 0x00 };
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		commands.add(characterExpansion);

		commands.add(new byte[] { 0x1b, 0x6c, leftMargin });

		byte[] alignmentCommand = new byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		commands.add(alignmentCommand);

		// textData Encoding!!
		if (encode == "") {
			commands.add(textData);
		} else {
			String strData = new String(textData);
			byte[] rawData = null;
			try {
				if (encode.startsWith("Shift_JIS")) {
					rawData = strData.getBytes("Shift_JIS"); // Shift JIS code
				} else if (encode.startsWith("ISO2022JP")) {
					byte[] tempDataBytes  = strData.getBytes("ISO2022JP"); // JIS code;
					rawData = ReplaceCommand(tempDataBytes);				
				} else if (encode.startsWith("Big5")) {
					rawData = strData.getBytes("Big5"); // Traditional Chinese
				} else if (encode.startsWith("GB2312")) {
					rawData = strData.getBytes("GB2312"); // Simplified Chinese
				} else {
					rawData = strData.getBytes();
				}
			} catch (UnsupportedEncodingException e) {
				rawData = strData.getBytes();
			}
			commands.add(rawData);
		}

		commands.add(new byte[] { 0x0a });

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw text to the printer, showing how the text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param ambiguousCharacterSettings
	 *     boolean variable to tell the printer to ambiguous character settings
	 * @param slashedZero
	 *     boolean variable to tell the printer to slash zeroes
	 * @param underline
	 *     boolean variable that tells the printer to underline the text
	 * @param invertColor
	 *     boolean variable that tells the printer to should invert text. All white space will become black but the characters will be left white.
	 * @param emphasized
	 *     boolean variable that tells the printer to should emphasize the printed text. This is somewhat like bold. It isn't as dark, but darker than regular characters.
	 * @param upperline
	 *     boolean variable that tells the printer to place a line above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *     boolean variable that tells the printer to print text upside down.
	 * @param heightExpansion
	 *     This integer tells the printer what the character height should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param widthExpansion
	 *     This integer tell the printer what the character width should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param leftMargin
	 *     Defines the left margin for text on Star portable printers. This number can be from 0 to 65536. However, remember how much space is available as the text can be pushed off the page.
	 * @param alignment
	 *     Defines the alignment of the text. The printers support left, right, and center justification.
	 * @param textData
	 *     The text to send to the printer.
	 * @param encode
	 *     Set encode for multi-byte character or blank for single byte character.
	 */
	public static void PrintTextUTF8(Context context, String portName, String portSettings, boolean ambiguousCharacterSettings, boolean slashedZero, boolean underline, boolean invertColor, boolean emphasized, boolean upperline, boolean upsideDown, int heightExpansion, int widthExpansion, byte leftMargin, Alignment alignment, byte[] textData, String encode) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		commands.add(new byte[] { 0x1b, 0x40 }); // Initialization

		byte[] CodePageCommand = new byte[] { 0x1b, 0x1d, 0x74, (byte)0x80 }; // Code Page UTF-8
		commands.add(CodePageCommand);

		byte[] ambiguousCharacterSettingsCommand = new byte[] { 0x1b, 0x1d, 0x29, 0x55, 0x02, 0x00, 0x40, 0x00};
		if(ambiguousCharacterSettings)
		{
			ambiguousCharacterSettingsCommand[7] = 0x00;//Single-bytes character priority
		} else{
			ambiguousCharacterSettingsCommand[7] = 0x01;//Double-bytes character priority
		}
		commands.add(ambiguousCharacterSettingsCommand);
		
		byte[] slashedZeroCommand = new byte[] { 0x1b, 0x2f, 0x00 };
		if (slashedZero) {
			slashedZeroCommand[2] = 49;
		} else {
			slashedZeroCommand[2] = 48;
		}
		commands.add(slashedZeroCommand);

		byte[] underlineCommand = new byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		commands.add(underlineCommand);

		byte[] invertColorCommand = new byte[] { 0x1b, 0x00 };
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		commands.add(invertColorCommand);

		byte[] emphasizedPrinting = new byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		commands.add(emphasizedPrinting);

		byte[] upperLineCommand = new byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		commands.add(upperLineCommand);

		if (upsideDown) {
			commands.add(new byte[] { 0x0f });
		} else {
			commands.add(new byte[] { 0x12 });
		}

		byte[] characterExpansion = new byte[] { 0x1b, 0x69, 0x00, 0x00 };
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		commands.add(characterExpansion);

		commands.add(new byte[] { 0x1b, 0x6c, leftMargin });

		byte[] alignmentCommand = new byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		commands.add(alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			if (encode.startsWith("UTF-8")) {
				rawData = strData.getBytes("UTF-8"); // UTF-8 code
			} 
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}
		commands.add(rawData);

		commands.add(new byte[] { 0x0a });

		sendCommand(context, portName, portSettings, commands);
	}
	
	/**
	 * This function sends raw text to the printer, showing how the text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param slashedZero
	 *     boolean variable to tell the printer to slash zeroes
	 * @param underline
	 *     boolean variable that tells the printer to underline the text
	 * @param twoColor
	 *     boolean variable that tells the printer to should print red or black text.
	 * @param emphasized
	 *     boolean variable that tells the printer to should emphasize the printed text. This is somewhat like bold. It isn't as dark, but darker than regular characters.
	 * @param upperline
	 *     boolean variable that tells the printer to place a line above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *     boolean variable that tells the printer to print text upside down.
	 * @param heightExpansion
	 *     boolean variable that tells the printer to should expand double-tall printing.
	 * @param widthExpansion
	 *     boolean variable that tells the printer to should expand double-wide printing.
	 * @param leftMargin
	 *     Defines the left margin for text on Star portable printers. This number can be from 0 to 65536. However, remember how much space is available as the text can be pushed off the page.
	 * @param alignment
	 *     Defines the alignment of the text. The printers support left, right, and center justification.
	 * @param textData
	 *     The text to send to the printer.
	 * @param encode
	 *     Set encode for multi-byte character or blank for single byte character.
	 */
	public static void PrintTextbyDotPrinter(Context context, String portName, String portSettings, boolean slashedZero, boolean underline, boolean twoColor, boolean emphasized, boolean upperline, boolean upsideDown, boolean heightExpansion, boolean widthExpansion, byte leftMargin, Alignment alignment, byte[] textData, String encode) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		commands.add(new byte[] { 0x1b, 0x40 }); // Initialization

		if (encode.startsWith("Shift_JIS")) {
			commands.add(new byte[] { 0x1b, 0x71, 0x1b, 0x24, 0x31 }); // Shift-JIS Kanji Mode(Disable JIS(ESC q) + Enable Shift-JIS(ESC $ n))
		} else if (encode.startsWith("ISO2022JP")) {
			commands.add(new byte[] { 0x1b, 0x24, 0x30}); // JIS Kanji Mode(Disable Shift-JIS(ESC $ n)
		}

		byte[] slashedZeroCommand = new byte[] { 0x1b, 0x2f, 0x00 };
		if (slashedZero) {
			slashedZeroCommand[2] = 49;
		} else {
			slashedZeroCommand[2] = 48;
		}
		commands.add(slashedZeroCommand);

		byte[] underlineCommand = new byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		commands.add(underlineCommand);

		byte[] twoColorCommand = new byte[] { 0x1b, 0x00 };
		if (twoColor) {
			twoColorCommand[1] = 0x34;
		} else {
			twoColorCommand[1] = 0x35;
		}
		commands.add(twoColorCommand);

		byte[] emphasizedPrinting = new byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		commands.add(emphasizedPrinting);

		byte[] upperLineCommand = new byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		commands.add(upperLineCommand);

		if (upsideDown) {
			commands.add(new byte[] { 0x0f });
		} else {
			commands.add(new byte[] { 0x12 });
		}

		byte[] characterheightExpansion = new byte[] { 0x1b, 0x68, 0x00 };
		if (heightExpansion) {
			characterheightExpansion[2] = 49;
		} else {
			characterheightExpansion[2] = 48;
		}
		commands.add(characterheightExpansion);

		byte[] characterwidthExpansion = new byte[] { 0x1b, 0x57, 0x00 };
		if (widthExpansion) {
			characterwidthExpansion[2] = 49;
		} else {
			characterwidthExpansion[2] = 48;
		}
		commands.add(characterwidthExpansion);

		commands.add(new byte[] { 0x1b, 0x6c, leftMargin });

		byte[] alignmentCommand = new byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		commands.add(alignmentCommand);

		// textData Encoding!!
		if (encode == "") {
			commands.add(textData);
		} else {
			String strData = new String(textData);
			byte[] rawData = null;
			try {
				if (encode.startsWith("Shift_JIS")) {
					rawData = strData.getBytes("Shift_JIS"); // Shift JIS code
				} else if (encode.startsWith("ISO2022JP")) {				
					byte[] tempDataBytes  = strData.getBytes("ISO2022JP"); // JIS code;
					rawData = ReplaceCommand(tempDataBytes);					
				} else if (encode.startsWith("Big5")) {
					rawData = strData.getBytes("Big5"); // Traditional Chinese
				} else if (encode.startsWith("GB2312")) {
					rawData = strData.getBytes("GB2312"); // Simplified Chinese
				} else {
					rawData = strData.getBytes();
				}
			} catch (UnsupportedEncodingException e) {
				rawData = strData.getBytes();
			}
			commands.add(rawData);
		}

		commands.add(new byte[] { 0x0a });

		sendCommand(context, portName, portSettings, commands);
	}

	protected static byte[] ReplaceCommand(byte[] tempDataBytes) {

		byte[] buffer = new byte[tempDataBytes.length];
		int j = 0;
		
		byte[] specifyJISkanjiCharacterModeCommand = new byte[] {0x1b, 0x70};
		byte[] cancelJISkanjiCharacterModeCommand = new byte[] {0x1b, 0x71};
		
		//replace command
		//Because LF(0x0A) command is not performed.
		if(tempDataBytes.length > 0){
			for(int i=0; i<tempDataBytes.length; i++){
				if(tempDataBytes[i] == 0x1b){
					if(tempDataBytes[i+1] == 0x24){// Replace [0x1b 0x24 0x42] to "Specify JIS Kanji Character Mode" command
						buffer[j]   = specifyJISkanjiCharacterModeCommand[0];
						buffer[j+1] = specifyJISkanjiCharacterModeCommand[1];
						j += 2;
					}
					else if(tempDataBytes[i+1] == 0x28){//Replace [0x1b 0x28 0x42] to "Cancel JIS Kanji Character Mode" command
						buffer[j]   = cancelJISkanjiCharacterModeCommand[0];
						buffer[j+1] = cancelJISkanjiCharacterModeCommand[1];
						j += 2;
					}
					
					i += 2;
				}else{
					buffer[j] = tempDataBytes[i];
					j++;
				}
			}
		}
		
		//check 0x00 position
		int datalength = 0;
		for(int i=0; i< buffer.length; i++){
			if(buffer[i] == 0x00){
				datalength = i;
				break;
			}
		}
		
		//copy data
		if(datalength == 0){
			datalength = buffer.length;
		}
		byte[] data = new byte[datalength];		
		System.arraycopy(buffer, 0, data, 0, datalength);

		return data;
	}

	/**
	 * This function is used to print a Java bitmap directly to the printer. There are 2 ways a printer can print images: through raster commands or line mode commands This function uses raster commands to print an image. Raster is supported on the TSP100 and all Star Thermal POS printers. Line mode printing is not supported by the TSP100. There is no example of using this method in this sample.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param source
	 *     The bitmap to convert to Star Raster data
	 * @param maxWidth
	 *     The maximum width of the image to print. This is usually the page width of the printer. If the image exceeds the maximum width then the image is scaled down. The ratio is maintained.
	 */
	public static void PrintBitmap(Context context, String portName, String portSettings, Bitmap source, int maxWidth, boolean compressionEnable, RasterCommand rasterType) {
		try {
			ArrayList<byte[]> commands = new ArrayList<byte[]>();

			RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
			StarBitmap starbitmap = new StarBitmap(source, false, maxWidth);

			if (rasterType == RasterCommand.Standard) {
				commands.add(rasterDoc.BeginDocumentCommandData());

				commands.add(starbitmap.getImageRasterDataForPrinting_Standard(compressionEnable));

				commands.add(rasterDoc.EndDocumentCommandData());
			} else {
				commands.add(starbitmap.getImageRasterDataForPrinting_graphic(compressionEnable));
				commands.add(new byte[] { 0x1b, 0x64, 0x02 }); // Feed to cutter position
			}

			sendCommand(context, portName, portSettings, commands);
		} catch (OutOfMemoryError e) {
			throw e;
		}

	}

	/**
	 * This function is used to print a Java bitmap directly to the printer. There are 2 ways a printer can print images: through raster commands or line mode commands This function uses raster commands to print an image. Raster is supported on the TSP100 and all Star Thermal POS printers. Line mode printing is not supported by the TSP100. There is no example of using this method in this sample.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings
	 *     Should be blank
	 * @param res
	 *     The resources object containing the image data
	 * @param source
	 *     The resource id of the image data
	 * @param maxWidth
	 *     The maximum width of the image to print. This is usually the page width of the printer. If the image exceeds the maximum width then the image is scaled down. The ratio is maintained.
	 */
	public static void PrintBitmapImage(Context context, String portName, String portSettings, Resources res, int source, int maxWidth, boolean compressionEnable, RasterCommand rasterType) {
		ArrayList<byte[]> commands = new ArrayList<byte[]>();

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
		Bitmap bm = BitmapFactory.decodeResource(res, source);
		StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);

		if (rasterType == RasterCommand.Standard) {
			commands.add(rasterDoc.BeginDocumentCommandData());

			commands.add(starbitmap.getImageRasterDataForPrinting_Standard(compressionEnable));

			commands.add(rasterDoc.EndDocumentCommandData());
		} else {
			commands.add(starbitmap.getImageRasterDataForPrinting_graphic(compressionEnable));
			commands.add(new byte[] { 0x1b, 0x64, 0x02 }); // Feed to cutter position
		}

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function shows how to read the MSR data(credit card) of a portable printer. The function first puts the printer into MSR read mode, then asks the user to swipe a credit card The function waits for a response from the user. The user can cancel MSR mode or have the printer read the card.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user
	 * @param portName
	 *     Port name to use for communication. This should be (TCP:<IPAddress> or BT:<Device pair name>)
	 * @param portSettings
	 *     Should be portable as the port settings. It is used for portable printers
	 */
	public static void MCRStart(final Context context, String portName, String portSettings) {
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			portForMoreThanOneFunction = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 portForMoreThanOneFunction = StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			portForMoreThanOneFunction.writePort(new byte[] { 0x1b, 0x4d, 0x45 }, 0, 3);

			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Cancel", new OnClickListener() {
				// If the user cancels MSR mode, the character 0x04 is sent to the printer
				// This function also closes the port
				public void onClick(DialogInterface dialog, int which) {
					((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
					((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);
					try {
						portForMoreThanOneFunction.writePort(new byte[] { 0x04 }, 0, 1);
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}
					} catch (StarIOPortException e) {

					} finally {
						if (portForMoreThanOneFunction != null) {
							try {
								StarIOPort.releasePort(portForMoreThanOneFunction);
							} catch (StarIOPortException e1) {
							}
						}
					}
				}
			});
			AlertDialog alert = dialog.create();
			alert.setTitle("");
			alert.setMessage("Slide credit card");
			alert.setCancelable(false);
			alert.setButton("OK", new OnClickListener() {
				// If the user presses ok then the magnetic stripe is read and displayed to the user
				// This function also closes the port
				public void onClick(DialogInterface dialog, int which) {
					((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
					((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);
					try {
						byte[] mcrData = new byte[256];
						int counts = 0;
						
						for (int i = 0; i < 5; i++) {
							counts += portForMoreThanOneFunction.readPort(mcrData, counts, mcrData.length);
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
						}
						
				        byte[] headerPattern = new byte[]{0x02, 0x45, 0x31, 0x31, 0x1c, 0x1c};
				        byte[] footerPattern = new byte[]{0x1c, 0x03, 0x0d, 0x0a };
				        int headerPatternPos = -1;
				        int fotterPatternPos = -1;
				        
				        byte[] data2 = new byte[headerPattern.length];
				        byte[] data3 = new byte[footerPattern.length];
				       
				        //Check Start header position
				        for(int i = 0; i< (mcrData.length - headerPattern.length +1); i++){
				        	System.arraycopy(mcrData, i, data2, 0, (headerPattern.length));
				               	
				        	if(Arrays.equals(data2, headerPattern))
				        	{
				        		headerPatternPos = i;
				        		break;
				        	}
				        }
				       
				      //Check Start fotter position
				        for(int i = headerPatternPos + headerPattern.length; i< (mcrData.length - footerPattern.length +1); i++){
				       		System.arraycopy(mcrData, i, data3, 0, (footerPattern.length));
				       		        	       	       	
				        	if(Arrays.equals(data3, footerPattern)){       		
				           		fotterPatternPos = i;
				           		break;
				        	}        	
				        }        
				        
				    	if((headerPatternPos < 0) || (fotterPatternPos < 0) ){
							Builder dialog1 = new Builder(context);
							dialog1.setNegativeButton("Ok", null);
							AlertDialog alert = dialog1.create();
							alert.setTitle("No data");
							alert.setMessage("There is nothing available data.");
							alert.show();
				    	} else {
					        byte[] reciveDataList = new byte[fotterPatternPos - headerPatternPos];
					                
					        System.arraycopy(mcrData, headerPatternPos, reciveDataList, 0, fotterPatternPos - headerPatternPos);
	
							Builder dialog1 = new Builder(context);
							dialog1.setNegativeButton("Ok", null);
							AlertDialog alert = dialog1.create();
							alert.setTitle("");
							alert.setMessage(new String(reciveDataList));
							alert.show();
				    	}
						
						portForMoreThanOneFunction.writePort(new byte[] { 0x04 }, 0, 1);
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}

					} catch (StarIOPortException e) {

					} finally {
						if (portForMoreThanOneFunction != null) {
							try {
								StarIOPort.releasePort(portForMoreThanOneFunction);
							} catch (StarIOPortException e1) {
							}
						}
					}
				}
			});
			alert.show();
		} catch (StarIOPortException e) {
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
			if (portForMoreThanOneFunction != null) {
				try {
					StarIOPort.releasePort(portForMoreThanOneFunction);
				} catch (StarIOPortException e1) {
				}
			}
		} finally {

		}
	}

	/**
	 * MSR functionality is supported on Star portable printers only.
	 * 
	 * @param context
	 *     Activity for displaying messages to the user that this function is not supported
	 */
	public static void MCRnoSupport(Context context) {
		Builder dialog = new Builder(context);
		dialog.setNegativeButton("OK", null);
		AlertDialog alert = dialog.create();
		alert.setTitle("Feature Not Available");
		alert.setMessage("MSR functionality is supported only on portable printer models");
		alert.setCancelable(false);
		alert.show();
	}

	private static byte[] createShiftJIS(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("Shift_JIS");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static byte[] createGB2312(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static byte[] createBIG5(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("Big5");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static byte[] createCp1251(String inputText) {
		byte[] byteBuffer = null;
		
		try {
			byteBuffer = inputText.getBytes("Windows-1251");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}
		
		return byteBuffer;
	}

	private static byte[] createCp1252(String inputText) {
		byte[] byteBuffer = null;
		
		try {
			byteBuffer = inputText.getBytes("Windows-1252");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}
		
		return byteBuffer;
	}

	private static byte[] createCpUTF8(String inputText) {
		byte[] byteBuffer = null;
		
		try {
			byteBuffer = inputText.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}
		
		return byteBuffer;
	}
	private static byte[] createRasterCommand(String printText, int textSize, int bold, RasterCommand rasterType) {
		byte[] command;

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);

		Typeface typeface;

		try {
			typeface = Typeface.create(Typeface.SERIF, bold);
		} catch (Exception e) {
			typeface = Typeface.create(Typeface.DEFAULT, bold);
		}

		paint.setTypeface(typeface);
		paint.setTextSize(textSize * 2);
		paint.setLinearText(true);

		TextPaint textpaint = new TextPaint(paint);
		textpaint.setLinearText(true);
		StaticLayout staticLayout = new StaticLayout(printText, textpaint, printableArea, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
		int height = staticLayout.getHeight();

		Bitmap bitmap = Bitmap.createBitmap(staticLayout.getWidth(), height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bitmap);
		c.drawColor(Color.WHITE);
		c.translate(0, 0);
		staticLayout.draw(c);

		StarBitmap starbitmap = new StarBitmap(bitmap, false, printableArea);

		if (rasterType == RasterCommand.Standard) {
			command = starbitmap.getImageRasterDataForPrinting_Standard(true);
		} else {
			command = starbitmap.getImageRasterDataForPrinting_graphic(true);
		}

		return command;
	}

	private static byte[] convertFromListByteArrayTobyteArray(List<byte[]> ByteArray) {
		int dataLength = 0;
		for (int i = 0; i < ByteArray.size(); i++) {
			dataLength += ByteArray.get(i).length;
		}

		int distPosition = 0;
		byte[] byteArray = new byte[dataLength];
		for (int i = 0; i < ByteArray.size(); i++) {
			System.arraycopy(ByteArray.get(i), 0, byteArray, distPosition, ByteArray.get(i).length);
			distPosition += ByteArray.get(i).length;
		}

		return byteArray;
	}

	private static void sendCommand(Context context, String portName, String portSettings, ArrayList<byte[]> byteList) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

			/*
			 * Using Begin / End Checked Block method When sending large amounts of raster data, 
			 * adjust the value in the timeout in the "StarIOPort.getPort" in order to prevent 
			 * "timeout" of the "endCheckedBlock method" while a printing.
			 * 
			 * If receipt print is success but timeout error occurs(Show message which is "There 
			 * was no response of the printer within the timeout period." ), need to change value 
			 * of timeout more longer in "StarIOPort.getPort" method. 
			 * (e.g.) 10000 -> 30000
			 */
			StarPrinterStatus status = port.beginCheckedBlock();

			if (true == status.offline) {
				throw new StarIOPortException("A printer is offline");
			}

			byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
			port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);
			
			port.setEndCheckedBlockTimeoutMillis(30000);// Change the timeout time of endCheckedBlock method.
			
			status = port.endCheckedBlock();
			
			if (status.coverOpen == true) {
				throw new StarIOPortException("Printer cover is open");
			} else if (status.receiptPaperEmpty == true) {
				throw new StarIOPortException("Receipt paper is empty");				
			} else if (status.offline == true) {
				throw new StarIOPortException("Printer is offline");
			}
		} catch (StarIOPortException e) {			
			Builder dialog = new Builder(context);
			dialog.setNegativeButton("OK", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}
}
