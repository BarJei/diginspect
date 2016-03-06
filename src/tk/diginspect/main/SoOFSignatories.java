package tk.diginspect.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tk.diginspect.R;
import tk.diginspect.dataconn.AlarmReceiver;
import tk.diginspect.dataconn.DatabaseHelper;
import tk.diginspect.dataconn.Directives;
import tk.diginspect.dataconn.Establishment;
import tk.diginspect.dataconn.FileUpload;
import tk.diginspect.dataconn.Signatories;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SoOFSignatories extends Activity implements OnClickListener {
	EditText et1stFDRO, et2ndFDRO, et1stEstRep, et2ndEstRep, etInspectFrom, etInspectTo;
	ImageView iv1stFDRO, iv2ndFDRO, iv1stEstRep, iv2ndEstRep;
	Intent i;
	Bitmap bpFDRO1, bpFDRO2, bpEstRep1, bpEstRep2;
	FileInputStream fis;
	SharedPreferences sp;
	Button bPrev2, bSave, bPDF;
	boolean boolFDRO1 = false, boolFDRO2 = false, boolEstRep1 = false, boolEstRep2 = false;
	Document doc;
	private ProgressDialog progress;
	private PendingIntent pendingIntent;
	DatabaseHelper db = new DatabaseHelper(this);
	private Menu menuHome;
	
	Date date = new Date(System.currentTimeMillis());
	SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
	SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
	String dateStarted = dateFormat.format(date);
	String strFile = sdf2.format(date);
	String timeS = sdfTime.format(Calendar.getInstance().getTime());
	
	FileUpload fu = new FileUpload();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soofsignatories);
		Initialization();
		etInspectFrom.setText(dateStarted);
		loadSavedPreferences();
		et1stFDRO.setText(sp.getString("loggedInspector", null));
		et1stEstRep.setText(sp.getString("InterviewedName", null));
		etInspectTo.setText(dateStarted);
		Signature("FDRO1", bpFDRO1, iv1stFDRO);
		Signature("FDRO2", bpFDRO2, iv2ndFDRO);
		Signature("EstRep1", bpEstRep1, iv1stEstRep);
		Signature("EstRep2", bpEstRep2, iv2ndEstRep);
		Intent alarmIntent = new Intent(SoOFSignatories.this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(SoOFSignatories.this, 0, alarmIntent, 0);
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		et1stFDRO = (EditText) findViewById(R.id.et1stFDRO);
		iv1stFDRO = (ImageView) findViewById(R.id.iv1stFDRO);
		iv1stFDRO.setOnClickListener(this);
		et2ndFDRO = (EditText) findViewById(R.id.et2ndFDRO);
		iv2ndFDRO = (ImageView) findViewById(R.id.iv2ndFDRO);
		iv2ndFDRO.setOnClickListener(this);
		et1stEstRep = (EditText) findViewById(R.id.et1stEstRep);
		iv1stEstRep = (ImageView) findViewById(R.id.iv1stEstRep);
		iv1stEstRep.setOnClickListener(this);
		et2ndEstRep = (EditText) findViewById(R.id.et2ndEstRep);
		iv2ndEstRep = (ImageView) findViewById(R.id.iv2ndEstRep);
		iv2ndEstRep.setOnClickListener(this);
		etInspectFrom = (EditText) findViewById(R.id.etInspectFrom);
		etInspectTo = (EditText) findViewById(R.id.etInspectTo);
		bPrev2 = (Button) findViewById(R.id.bPrev2);
		bPrev2.setOnClickListener(this);
		bSave = (Button) findViewById(R.id.bSave);
		bSave.setOnClickListener(this);
		bPDF = (Button) findViewById(R.id.bPDF);
		bPDF.setOnClickListener(this);
		progress = new ProgressDialog(this);
		bPDF.getBackground().setColorFilter(new LightingColorFilter(0xFF33B5E5, 0xFF33B5E5));
		bSave.getBackground().setColorFilter(new LightingColorFilter(0xFF33B5E5, 0xFF33B5E5));
	
		etInspectFrom.setEnabled(false);
		et1stFDRO.setEnabled(false);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv1stFDRO:
			SignatureActivity("FDRO1", "boolFDRO1");
			break;
		case R.id.iv2ndFDRO:
			SignatureActivity("FDRO2", "boolFDRO2");
			break;
		case R.id.iv1stEstRep:
			SignatureActivity("EstRep1", "boolEstRep1");
			break;
		case R.id.iv2ndEstRep:
			SignatureActivity("EstRep2", "boolEstRep2");
			break;
		case R.id.bPrev2:
			saveSharedPreferences();
			i = new Intent(SoOFSignatories.this, SoOFOthersDirectives.class);
			startActivity(i);
			break;
		case R.id.bSave:
			boolean error,
			error1;
			error = BlankFields(et1stFDRO,
					"Name of 1st Food-Drug Regulation Officer");
			error1 = BlankBoolean(boolFDRO1,
					"Signature of 1st Food-Drug Regulation Officer");
			error = BlankFields(et2ndFDRO,
					"Name of 2nd Food-Drug Regulation Officer");
			error1 = BlankBoolean(boolFDRO2,
					"Signature of 2nd Food-Drug Regulation Officer");
			error = BlankFields(et1stEstRep,
					"Name of Establishment's 1st Representative");
			error1 = BlankBoolean(boolEstRep1,
					"Signature of Establishment's 1st Representative");
			error = BlankFields(et2ndEstRep,
					"Name of Establishment's 2nd Representative");
			error1 = BlankBoolean(boolEstRep2,
					"Signature of Establishment's 2nd Representative");
			error = BlankFields(etInspectFrom, "Date of Inspection From");
			error = BlankFields(etInspectTo, "Date of Inspection To");
			if (!error && !error1) {
				saveSharedPreferences();    		
				//new SyncWeb().execute();
				insert2db();
				start();
				bPDF.setEnabled(true);
				bSave.setEnabled(false);
				bPrev2.setEnabled(false);
				menuHome.findItem(R.id.menuHome).setEnabled(false);
				menuHome.findItem(R.id.menuClear).setEnabled(false);
				menuHome.findItem(R.id.menuSave).setEnabled(false);
			}
			break;
		case R.id.bPDF:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Generate PDF Report");
			alertDialogBuilder
					.setMessage("You are about to complete the inspection form. By clicking Yes, the system will generate your report in PDF format and clear all collected data. Are you sure about this?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									progress.setMessage("Generating PDF...");
									progress.setIndeterminate(true);
									progress.setCancelable(false);
									progress.show();

									final Thread t = new Thread() {
										@Override
										public void run() {
											createPDF();
											for (int i = 0; i < 101; i++) {
												try {
													sleep(100);
													progress.setProgress(i);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
											fu.fileUpload(sp.getString("fileName", null));
											progress.dismiss();
				                            
											sp = PreferenceManager
													.getDefaultSharedPreferences(getApplicationContext());
											sp.edit().clear().commit();
											deleteFile("FDRO1");
											deleteFile("FDRO2");
											deleteFile("EstRep1");
											deleteFile("EstRep2");
											//Intent i = new Intent(SoOFSignatories.this, MainMenu.class);
											//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											//startActivity(i);
											finish();
										}
									};
									t.start();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			break;
		}
	}
	
	private void SignatureActivity(String stringkey, String booleankey) {
		saveSharedPreferences();
		Bundle bundle = new Bundle();
		bundle.putString("bundlekey", stringkey);
		bundle.putString("bundleboolean", booleankey);
		i = new Intent(SoOFSignatories.this, Signature.class);
		i.putExtras(bundle);
		startActivity(i);
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		/*
		saveSharedPreferences();
		super.onBackPressed();
		i = new Intent(SoOFSignatories.this, SoOFOthersDirectives.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater SoOFMenu = getMenuInflater();
		SoOFMenu.inflate(R.menu.soofmenu, menu);
		menuHome = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menuHome:
			saveSharedPreferences();
			Intent i = new Intent(SoOFSignatories.this, MainMenu.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;
		case R.id.menuClear:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Clear Data");
			alertDialogBuilder
					.setMessage(
							"Are you sure you want to clear all saved data?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									sp = PreferenceManager
											.getDefaultSharedPreferences(getApplicationContext());
									sp.edit().clear().commit();
									deleteFile("FDRO1");
									deleteFile("FDRO2");
									deleteFile("EstRep1");
									deleteFile("EstRep2");
									Toast.makeText(getApplicationContext(),
											"Successfully Cleared All Data",
											Toast.LENGTH_LONG).show();
									Intent i = new Intent(SoOFSignatories.this, MainMenu.class);
									startActivity(i);
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			break;
		case R.id.menuSave:
			saveSharedPreferences();
			break;
		}
		return false;
	}

	private void Signature(String s, Bitmap bp, ImageView iv) {
		try {
			fis = openFileInput(s);
			bp = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			iv.setImageBitmap(bp);
		}
	}

	private void saveSharedPreferences() {
		// TODO Auto-generated method stub
		savePreferences("FDRO1", et1stFDRO.getText().toString());
		savePreferences("FDRO2", et2ndFDRO.getText().toString());
		savePreferences("EstRep1", et1stEstRep.getText().toString());
		savePreferences("EstRep2", et2ndEstRep.getText().toString());
		savePreferences("InspectFrom", etInspectFrom.getText().toString());
		savePreferences("InspectTo", etInspectTo.getText().toString());
		savePreferences("boolFDRO1", boolFDRO1);
		savePreferences("boolFDRO2", boolFDRO2);
		savePreferences("boolEstRep1", boolEstRep1);
		savePreferences("boolEstRep2", boolEstRep2);
		Toast.makeText(getApplicationContext(), "Successfully Saved Data",
				Toast.LENGTH_LONG).show();
	}

	private void savePreferences(String key, String value) {
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = sp.edit();
		if (value.equals("")) {
		} else {
			editor.putString(key, value);
			editor.commit();
		}
	}

	private void loadSavedPreferences() {
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		et1stFDRO.setText(sp.getString("FDRO1", null));
		et2ndFDRO.setText(sp.getString("FDRO2", null));
		et1stEstRep.setText(sp.getString("EstRep1", null));
		et2ndEstRep.setText(sp.getString("EstRep2", null));
		etInspectFrom.setText(sp.getString("InspectFrom", dateStarted));
		etInspectTo.setText(sp.getString("InspectTo", dateStarted));
		boolFDRO1 = sp.getBoolean("boolFDRO1", false);
		boolFDRO2 = sp.getBoolean("boolFDRO2", false);
		boolEstRep1 = sp.getBoolean("boolEstRep1", false);
		boolEstRep2 = sp.getBoolean("boolEstRep2", false);
	}

	private void savePreferences(String key, boolean value) {
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	private boolean BlankFields(EditText editext, String text) {
		boolean error = false;
		String errorMessage = "";
		if (editext.getText().toString().equals("")) {
			errorMessage = errorMessage + "Missing Field:  " + text;
			error = true;
		}
		if (error) {
			Toast.makeText(getApplicationContext(), errorMessage,
					Toast.LENGTH_LONG).show();
		}
		return error;
	}

	private boolean BlankBoolean(boolean Boolean, String text) {
		boolean error = false;
		String errorMessage = "";
		if (Boolean == false) {
			errorMessage = errorMessage + "Missing Field:  " + text;
			error = true;
		}
		if (error) {
			Toast.makeText(getApplicationContext(), errorMessage,
					Toast.LENGTH_LONG).show();
		}
		return error;
	}
	
	public void createPDF() {
		doc = new Document(PageSize.A4);
		try {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/RFO";
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String strFinalname, strFilename2, strFilenames;
			String filename = sp.getString("EstablishmentName", null);
			String strfilename = filename.replaceAll("\\W+", "-");
			String inspector = sp.getString("FDRO1", null);
			String inspby = inspector.replaceAll("\\W+", "-");
			File file = new File(dir, strfilename + "-" + strFile + "-" + inspby +".pdf");

			strFilename2 = strfilename + "-" + strFile + "-" + inspby;
			strFilenames = strFilename2+".pdf";
			strFinalname = path+"/"+strFilenames;
			savePreferences("fileName", strFinalname);
			
			FileOutputStream fOut = new FileOutputStream(file);
			PdfWriter.getInstance(doc, fOut);
			doc.open();

			FDALetterhead();

			// General Info Table
			float[] table1columnWidths = { 1f, 1f, 1f, 1f };
			PdfPTable table1 = new PdfPTable(table1columnWidths);
			table1.setWidthPercentage(110f);
			table1.setSpacingAfter(20f);
			insertCell(table1, "INSPECTION REPORT", Element.ALIGN_CENTER, 4,
					"#8EBAFF", 1, 0);
			insertCell(table1, "Name of Establishment", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("EstablishmentName", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Plant/Office Address", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("PlantOfficeAddress", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Warehouse Address", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("WarehouseAddress", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Owner", Element.ALIGN_LEFT, 1, "#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("Owner", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Telephone Number", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("TelNumber", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Fax No", Element.ALIGN_LEFT, 1, "#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("FaxNumber", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Classification", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);

			String Classification = sp.getString("MainClass", null) + ", "
					+ sp.getString("SecClass", null) + ", "
					+ sp.getString("ThirdClass", null) + ", "
					+ sp.getString("FourthClass", "");

			insertCell(table1, Classification, Element.ALIGN_LEFT, 3,
					"#FFFFFF", 1, 0);
			insertCell(table1, "Product/s:", Element.ALIGN_LEFT, 1, "#8EBAFF",
					1, 0);
			insertCell(table1, sp.getString("Products", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Manner of Notification", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("Notification", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Purpose of Inspection", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("Inspection", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(
					table1,
					"Registered Pharmacist / Authorized Representative / Person",
					Element.ALIGN_LEFT, 4, "#8EBAFF", 1, 0);
			insertCell(table1, "Name", Element.ALIGN_LEFT, 1, "#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("PharmacistName", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Reg. No. (PRC-ID)", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("PrcID", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Date Issued", Element.ALIGN_LEFT, 1, "#8EBAFF",
					1, 0);
			insertCell(table1, sp.getString("PrcDateIssued", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Validity", Element.ALIGN_LEFT, 1, "#8EBAFF", 1,
					0);
			insertCell(table1, sp.getString("PrcValidity", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Position", Element.ALIGN_LEFT, 1, "#8EBAFF", 1,
					0);
			insertCell(table1, sp.getString("PharmacistPosition", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Person/s Interviewed", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("InterviewedName", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Position", Element.ALIGN_LEFT, 1, "#8EBAFF", 1,
					0);
			insertCell(table1, sp.getString("InterviewedPosition", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "License to Operate", Element.ALIGN_LEFT,
					4, "#8EBAFF", 1, 0);
			insertCell(table1, "Number", Element.ALIGN_LEFT, 1, "#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("LTONumber", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Renewal", Element.ALIGN_LEFT, 1, "#8EBAFF", 1,
					0);
			insertCell(table1, sp.getString("LTORenewalDate", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Validity", Element.ALIGN_LEFT, 1, "#8EBAFF", 1,
					0);
			insertCell(table1, sp.getString("LTOValidity", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Payment of Appropriate Fee",
					Element.ALIGN_LEFT, 4, "#8EBAFF", 1, 0);
			insertCell(table1, "OR Number", Element.ALIGN_LEFT, 1, "#8EBAFF",
					1, 0);
			insertCell(table1, sp.getString("ORNum", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Amount", Element.ALIGN_LEFT, 1, "#8EBAFF", 1, 0);
			insertCell(table1, "Php " + sp.getString("ORAmount", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "Date of Payment", Element.ALIGN_LEFT, 0,
					"#8EBAFF", 1, 0);
			insertCell(table1, sp.getString("ORDate", "N/A"),
					Element.ALIGN_LEFT, 3, "#FFFFFF", 1, 0);
			insertCell(table1, "RSN", Element.ALIGN_LEFT, 1, "#8EBAFF", 1, 1);
			insertCell(table1, sp.getString("RSN", "N/A"), Element.ALIGN_LEFT,
					3, "#FFFFFF", 1, 1);
			doc.add(table1);

			footer();

			// 2nd Page
			doc.newPage();
			FDALetterhead();

			PdfPTable table2 = new PdfPTable(1);
			table2.setWidthPercentage(110f);
			table2.setSpacingAfter(20f);
			insertCell(table2, "Observation Findings:", Element.ALIGN_LEFT, 1,
					"#8EBAFF", 1, 0);
			insertCell1(table2, sp.getString("ObservationFindings", ""), 1, 1,
					600f);
			doc.add(table2);
			footer();

			// 3rd Page
			doc.newPage();
			FDALetterhead();

			PdfPTable table3 = new PdfPTable(1);
			table3.setWidthPercentage(110f);
			table3.setSpacingAfter(20f);

			insertCell(table3, "Directives:", Element.ALIGN_LEFT, 1, "#8EBAFF",
					1, 1);

			nested(table3, 0.1f, 2f, R.string.directives1,
					sp.getBoolean("Directives1", false), Element.ALIGN_LEFT);
			nested(table3, 0.1f, 2f, R.string.directives2,
					sp.getBoolean("Directives2", false), Element.ALIGN_LEFT);
			nested(table3, 0.2f, 2f, R.string.directives3,
					sp.getBoolean("Directives3", false), Element.ALIGN_RIGHT);
			nested(table3, 0.2f, 2f, R.string.directives4,
					sp.getBoolean("Directives4", false), Element.ALIGN_RIGHT);
			nested(table3, 0.1f, 2f, R.string.directives5,
					sp.getBoolean("Directives5", false), Element.ALIGN_LEFT);
			nested(table3, 0.1f, 2f, R.string.directives6,
					sp.getBoolean("Directives6", false), Element.ALIGN_LEFT);
			nested(table3, 0.1f, 2f, R.string.directives7,
					sp.getBoolean("Directives7", false), Element.ALIGN_LEFT);

			String Directives8 = getResources().getString(R.string.directives8);
			PdfPCell cellDirectives8 = new PdfPCell(new Paragraph(
					Font.TIMES_ROMAN, Directives8));
			cellDirectives8.setPaddingLeft(50);
			cellDirectives8.setPaddingTop(5);
			cellDirectives8.setPaddingBottom(5);
			cellDirectives8.setPaddingRight(5);
			cellDirectives8.setBorderWidthTop(0);
			cellDirectives8.setBorderWidthBottom(0);
			table3.addCell(cellDirectives8);

			String Directives = sp.getString("Directives8", null);
			boolean Directives9 = false, Directives10 = false;
			if (Directives.equals(R.string.directives9)) {
				Directives9 = true;
				Directives10 = false;
			} else if (Directives.equals(R.string.directives10)) {
				Directives9 = false;
				Directives10 = true;
			} else {
				Directives9 = false;
				Directives10 = false;
			}

			nested(table3, 0.3f, 2f, R.string.directives9, Directives9,
					Element.ALIGN_RIGHT);
			nested(table3, 0.3f, 2f, R.string.directives10, Directives10,
					Element.ALIGN_RIGHT);

			insertCell(table3, "Inspected By:", Element.ALIGN_CENTER, 1,
					"#8EBAFF", 1, 0);

			PdfPTable signature1 = new PdfPTable(2);

			sig(signature1, "FDRO1");
			sig(signature1, "FDRO2");

			insertCell(signature1, sp.getString("FDRO1", null),
					Element.ALIGN_CENTER, 1, "#FFFFFF", 0, 0);
			insertCell(signature1, sp.getString("FDRO2", null),
					Element.ALIGN_CENTER, 1, "#FFFFFF", 0, 0);
			insertCell(signature1, "Food-Drug Regulation Officer",
					Element.ALIGN_CENTER, 1, "#FFFFFF", 1, 0);
			insertCell(signature1, "Food-Drug Regulation Officer",
					Element.ALIGN_CENTER, 1, "#FFFFFF", 1, 0);
			insertCell(signature1, "Date: "+dateStarted, Element.ALIGN_LEFT, 1, "#FFFFFF",
					1, 0);
			insertCell(signature1, "Time: "+timeS, Element.ALIGN_LEFT, 1, "#FFFFFF",
					1, 0);

			sig(signature1, "EstRep1");
			sig(signature1, "EstRep2");
			insertCell(signature1, sp.getString("EstRep1", null),
					Element.ALIGN_CENTER, 1, "#FFFFFF", 0, 0);
			insertCell(signature1, sp.getString("EstRep2", null),
					Element.ALIGN_CENTER, 1, "#FFFFFF", 0, 0);
			insertCell(signature1, "Establishment’s Representative",
					Element.ALIGN_CENTER, 1, "#FFFFFF", 1, 1);
			insertCell(signature1, "Establishment’s Representative",
					Element.ALIGN_CENTER, 1, "#FFFFFF", 1, 1);

			PdfPCell nesthousing1 = new PdfPCell(signature1);
			table3.addCell(nesthousing1);

			doc.add(table3);
			footer();

			// 4th Page
			doc.newPage();
			FDALetterhead();
			PdfPTable table4 = new PdfPTable(1);
			table4.setWidthPercentage(110f);
			table4.setSpacingAfter(20f);

			insertCell(table4, "(FDA USE ONLY)", Element.ALIGN_CENTER, 1,
					"#8EBAFF", 1, 1);
			insertCell(table4, "Compliance Made by the Company",
					Element.ALIGN_LEFT, 1, "#FFFFFF", 0, 0);
			nested(table4, 0.1f, 2f, R.string.CAPA, false, Element.ALIGN_LEFT);
			nested(table4, 0.2f, 2f, R.string.Accepted, false,
					Element.ALIGN_RIGHT);
			nested(table4, 0.2f, 2f, R.string.NotAccepted, false,
					Element.ALIGN_RIGHT);
			insertCell(table4, "Recommendation (to Licensing) :",
					Element.ALIGN_LEFT, 1, "#FFFFFF", 0, 0);
			insertCell1(table4, "", 0, 0, 100f);

			PdfPTable signature2 = new PdfPTable(2);

			insertCell1(signature2, "", 0, 0, 50f);
			insertCell1(signature2, "", 0, 0, 50f);

			insertCell(signature2, "Print Name & Signature of FDRO/s",
					Element.ALIGN_CENTER, 1, "#FFFFFF", 1, 0);
			insertCell(signature2, "Date", Element.ALIGN_CENTER, 1, "#FFFFFF",
					1, 0);

			insertCell(signature2, "Reviewed by:", Element.ALIGN_LEFT, 2,
					"#8EBAFF", 1, 1);

			insertCell1(signature2, "", 0, 0, 50f);
			insertCell1(signature2, "", 0, 0, 50f);

			insertCell(signature2,
					"Print Name & Signature of Team Leader/Supervisor",
					Element.ALIGN_CENTER, 1, "#FFFFFF", 1, 1);
			insertCell(signature2, "Date", Element.ALIGN_CENTER, 1, "#FFFFFF",
					1, 1);

			PdfPCell nesthousing2 = new PdfPCell(signature2);
			table4.addCell(nesthousing2);

			doc.add(table4);
			footer();

		} catch (DocumentException de) {
			Log.e("PDFCreator", "DocumentException:" + de);
		} catch (FileNotFoundException e) {
			Log.e("PDFCreator", "ioException:" + e);
		} finally {
			doc.close();
		}
	}

	private void insertCell(PdfPTable table, String text, int align,
			int colspan, String color, float BorderWidthTop,
			float BorderWidthBottom) {

		PdfPCell cell = new PdfPCell(new Paragraph(Font.TIMES_ROMAN, text));
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setBackgroundColor(WebColors.getRGBColor(color));
		cell.setPadding(5);
		cell.setBorderWidthTop(BorderWidthTop);
		cell.setBorderWidthBottom(BorderWidthBottom);
		table.addCell(cell);
	}

	private void insertCell1(PdfPTable table, String text,
			float borderWidthTop, float borderWidthBottom, float fixedHeight) {
		PdfPCell cell = new PdfPCell(new Paragraph(Font.TIMES_ROMAN, text));
		cell.setBackgroundColor(WebColors.getRGBColor("#FFFFFF"));
		cell.setPadding(5);
		cell.setBorderWidthTop(borderWidthTop);
		cell.setBorderWidthBottom(borderWidthBottom);
		cell.setFixedHeight(fixedHeight);
		table.addCell(cell);
	}

	private void nested(PdfPTable table, float column1, float column2,
			int directives, boolean checkmark, int align) {
		float[] nestedcolumnWidths = { column1, column2 };
		PdfPTable nested = new PdfPTable(nestedcolumnWidths);
		if (checkmark == true) {
			Checked(nested, align, R.drawable.checked);
		} else if (checkmark == false) {
			Checked(nested, align, R.drawable.unchecked);
		}

		String Directives = getResources().getString(directives);
		PdfPCell cell = new PdfPCell(
				new Paragraph(Font.TIMES_ROMAN, Directives));
		cell.setPadding(5);
		cell.setBorderWidth(0);
		nested.addCell(cell);

		PdfPCell nesthousing = new PdfPCell(nested);
		nesthousing.setBorderWidthTop(0);
		nesthousing.setBorderWidthBottom(0);

		table.addCell(nesthousing);
	}

	private void Checked(PdfPTable table, int align, int checkmark) {
		try {
			ByteArrayOutputStream streamChecked = new ByteArrayOutputStream();
			Bitmap Checked = BitmapFactory.decodeResource(getBaseContext()
					.getResources(), checkmark);
			Checked.compress(Bitmap.CompressFormat.JPEG, 100, streamChecked);
			Image ImgChecked = null;
			ImgChecked = Image.getInstance(streamChecked.toByteArray());
			ImgChecked.scalePercent(25f);
			PdfPCell cell = new PdfPCell(ImgChecked, false);
			cell.setHorizontalAlignment(align);
			cell.setPadding(5);
			cell.setBorder(0);
			table.addCell(cell);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sig(PdfPTable table, String image) {
		try {
			FileInputStream fis = openFileInput(image);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			Image Img = null;
			Img = Image.getInstance(stream.toByteArray());
			Img.scalePercent(10f);
			PdfPCell cell = new PdfPCell(Img, false);
			cell.setPadding(10);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthBottom(0);
			table.addCell(cell);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void FDALetterhead() {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
					.getResources(), R.drawable.fda_letterhead);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			Image Img = Image.getInstance(stream.toByteArray());
			Img.setAlignment(Image.MIDDLE);
			Img.scalePercent(40f);
			doc.add(Img);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void footer() {
		Paragraph footer = new Paragraph(Font.TIMES_ROMAN,
				"Summary of Observations/Findings");
		try {
			doc.add(footer);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insert2db(){
		String Classification = sp.getString("MainClass", null) + "/"
				+ sp.getString("SecClass", null) + "/"
				+ sp.getString("ThirdClass", null) + "/"
				+ sp.getString("FourthClass", "");

		db.addEstablishment(new Establishment(
				sp.getString("EstablishmentName", null), 
				sp.getString("PlantOfficeAddress", null), 
				sp.getString("POAddCoordinates", null), 
				sp.getString("WarehouseAddress", null), 
				sp.getString("WAddCoordinates", null), 
				sp.getString("Owner", null), 
				sp.getString("TelNumber", null), 
				sp.getString("FaxNumber", null), 
				Classification, 
				sp.getString("Products", null), 
				sp.getString("Notification", null),
				sp.getString("Inspection", null),
				sp.getString("LTONumber", "N/A"), 
				sp.getString("LTORenewalDate", "N/A"), 
				sp.getString("LTOValidity", "N/A"), 
				sp.getString("PharmacistName", null), 
				sp.getString("PharmacistPosition",null), 
				sp.getString("PrcID", null), 
				sp.getString("PrcDateIssued", null), 
				sp.getString("PrcValidity", null),
				sp.getString("InterviewedName", null),
				sp.getString("InterviewedPosition", null),
				sp.getString("ORNum", null),
				sp.getString("ORAmount", null),
				sp.getString("ORDate", null),
				sp.getString("RSN", null)
				));
		
		db.addSig(new Signatories(
				sp.getString("EstablishmentName", null),
				sp.getString("FDRO1", null),
				sp.getString("FDRO2", null),
				sp.getString("EstRep1", null),
				sp.getString("EstRep2", null),
				sp.getString("InspectFrom", null),
				sp.getString("InspectTo", null)			
				));
		
		String[] arrDirectives = {
				"To always observe Good Manufacturing/ Distribution/ Storage Practice",
				"For Critical Findings",
				"Stop Production",
				"Product Recall",
				"For Warehouse Inspection",
				"For Follow-up Inspection",
				"For Corrective Action/Preventive Action (CAPA) plan"
		};
		
		boolean directives1 = sp.getBoolean("Directives1", false);
		boolean directives2 = sp.getBoolean("Directives2", false);
		boolean directives3 = sp.getBoolean("Directives3", false);
		boolean directives4 = sp.getBoolean("Directives4", false);
		boolean directives5 = sp.getBoolean("Directives5", false);
		boolean directives6 = sp.getBoolean("Directives6", false);
		boolean directives7 = sp.getBoolean("Directives7", false);
		
		
		if(directives2 && directives3 && directives4 && directives5 && directives6 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[2] +"/ "+ arrDirectives[3]
						+"/ "+ arrDirectives[4]+"/ "+ arrDirectives[5]+"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives3 && directives4 && directives5 && directives6){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[2] +"/ "+ arrDirectives[3]
						+"/"+ arrDirectives[4]+"/ "+ arrDirectives[5],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives3 && directives4 && directives5){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[2] +"/ "+ arrDirectives[3]
						+"/ "+ arrDirectives[4],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives3 && directives4){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[2] +"/ "+ arrDirectives[3],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives5 && directives6 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[4]+"/ "+ arrDirectives[5]
							+"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives5 && directives6){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[4]+"/ "+ arrDirectives[5],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives5 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[4]+"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives6 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[5]+"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives3){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[2],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives4){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/"+ arrDirectives[3],
					sp.getString("Directives8", null)
					));
		}
		
		else if(directives2 && directives5){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[4],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives6){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[5],
					sp.getString("Directives8", null)
					));
		}
		else if(directives2 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1] +"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if(directives5 && directives6){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[4] +"/ "+ arrDirectives[5],
					sp.getString("Directives8", null)
					));
		}
		else if(directives5 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[4] +"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if(directives6 && directives7){
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[5] +"/ "+ arrDirectives[6],
					sp.getString("Directives8", null)
					));
		}
		else if (directives1) {
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[0],
					sp.getString("Directives8", null)
					));
		} 
		else if (directives2) {
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[1],
					sp.getString("Directives8", null)
					));
		}
		 else if (directives3) {
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[2],
					sp.getString("Directives8", null)
					));
		} 
		 else if (directives4) {
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[3],
					sp.getString("Directives8", null)
					));
		} 
		 else if (directives5) {
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[4],
					sp.getString("Directives8", null)
					));
		} 
		 else if (directives6) {
			db.addDirectives(new Directives(
					sp.getString("EstablishmentName", null),
					sp.getString("ObservationFindings", null),
					arrDirectives[5],
					sp.getString("Directives8", null)
					));
		} 
		 else if (directives7) {
					db.addDirectives(new Directives(
							sp.getString("EstablishmentName", null),
							sp.getString("ObservationFindings", null),
							arrDirectives[6],
							sp.getString("Directives8", null)
							));
				}
	}
	
	public void start() {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		int interval = 60000*10;
		manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), interval, pendingIntent);
	}	
	
}