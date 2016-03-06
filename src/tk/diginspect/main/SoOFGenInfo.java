package tk.diginspect.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tk.diginspect.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SoOFGenInfo extends Activity implements View.OnClickListener {

	private Button bMainClass, bSecClass, bThirdClass, bFourthClass, bNotif,
			bInspect, bNext;
	String SecArray[], ThirdArray[], FourthArray[], SecTitle, ThirdTitle,
			FourthTitle, FirstClass, SecClass, MainClass[], CosFoodHuhsClass[],
			DrugClass[], HUHSClass[], ManuClass[], FoodManuClass[], RDOClass[],
			DistriClass[], Notification[], Inspection[];
	EditText etEstablishmentName, etPOAddress, etPOAddCoordinates, etWAddress,
			etWAddCoordinates, etOwner, etTelNum, etFaxNum, etProducts, etLTONum,
			etLTORenewalDate, etLTOValidity, etPharmacistName,
			etPharmacistPosition, etPrcID, etPrcDateIssued, etPrcValidity,
			etInterviewedName, etInterviewedPosition, etORNum, etORAmount,
			etORDate, etRSN;
	boolean Class1, Class2, Class3, Class4, Notif, InspectPurpose = false;
	LinearLayout layoutLTO;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soofgeninfo);
		Array();
		Initialization();
		// Button listener for Main Classification
		bMainClass.setOnClickListener(this);
		// Button listener for Secondary Classification
		bSecClass.setOnClickListener(this);
		// Button listener for Third Classification
		bThirdClass.setOnClickListener(this);
		// Button listener for Fourth Classification
		bFourthClass.setOnClickListener(this);
		// Button listener for Manner of Notification
		bNotif.setOnClickListener(this);
		// Button listener for Purpose of Inspection
		bInspect.setOnClickListener(this);
		// Button listener for Next
		bNext.setOnClickListener(this);		
		
		loadSavedPreferences();
	}

	private void Array() {
		// TODO Auto-generated method stub
		MainClass = getResources().getStringArray(R.array.MainClass);
		CosFoodHuhsClass = getResources().getStringArray(
				R.array.CosFoodHuhsClass);
		DrugClass = getResources().getStringArray(R.array.DrugClass);
		HUHSClass = getResources().getStringArray(R.array.HUHSClass);
		ManuClass = getResources().getStringArray(R.array.ManuClass);
		FoodManuClass = getResources().getStringArray(R.array.FoodManuClass);
		RDOClass = getResources().getStringArray(R.array.RDOClass);
		DistriClass = getResources().getStringArray(R.array.DistriClass);
		Notification = getResources().getStringArray(R.array.Notification);
		Inspection = getResources().getStringArray(R.array.InspectionPurpose);
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		etEstablishmentName = (EditText) findViewById(R.id.etEstablishmentName);
		etPOAddress = (EditText) findViewById(R.id.etPOAddress);
		etPOAddCoordinates = (EditText) findViewById(R.id.etPOAddCoordinates);
		etWAddress = (EditText) findViewById(R.id.etWAddress);
		etWAddCoordinates = (EditText) findViewById(R.id.etWAddCoordinates);
		etOwner = (EditText) findViewById(R.id.etOwner);
		etTelNum = (EditText) findViewById(R.id.etTelNum);
		etFaxNum = (EditText) findViewById(R.id.etFaxNum);
		etProducts = (EditText) findViewById(R.id.etProducts);
		etLTONum = (EditText) findViewById(R.id.etLTONum);
		etLTORenewalDate = (EditText) findViewById(R.id.etLTORenewalDate);
		etLTOValidity = (EditText) findViewById(R.id.etLTOValidity);
		etPharmacistName = (EditText) findViewById(R.id.etPharmacistName);
		etPharmacistPosition = (EditText) findViewById(R.id.etPharmacistPosition);
		etPrcID = (EditText) findViewById(R.id.etPrcID);
		etPrcDateIssued = (EditText) findViewById(R.id.etPrcDateIssued);
		etPrcValidity = (EditText) findViewById(R.id.etPrcValidity);
		etInterviewedName = (EditText) findViewById(R.id.etInterviewedName);
		etInterviewedPosition = (EditText) findViewById(R.id.etInterviewedPosition);
		etORNum = (EditText) findViewById(R.id.etORNum);
		etORAmount = (EditText) findViewById(R.id.etORAmount);
		etORDate = (EditText) findViewById(R.id.etORDate);
		etRSN = (EditText) findViewById(R.id.etRSN);
		bMainClass = (Button) findViewById(R.id.bMainClass);
		bMainClass.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xff888888));
		bSecClass = (Button) findViewById(R.id.bSecClass);
		bSecClass.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xff888888));
		bThirdClass = (Button) findViewById(R.id.bThirdClass);
		bThirdClass.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xff888888));
		bFourthClass = (Button) findViewById(R.id.bFourthClass);
		bFourthClass.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xff888888));
		bNotif = (Button) findViewById(R.id.bNotif);
		bNotif.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xff888888));
		bInspect = (Button) findViewById(R.id.bInspect);
		bInspect.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xff888888));
		layoutLTO = (LinearLayout) findViewById(R.id.layoutLTO);
		bNext = (Button) findViewById(R.id.bNext);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bMainClass:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Classification").setItems(MainClass,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bMainClass.setText(MainClass[which]);
							bSecClass.setVisibility(View.VISIBLE);
							bThirdClass.setVisibility(View.GONE);
							bFourthClass.setVisibility(View.GONE);
							Class1 = true;
							Class2 = false;
							Class3 = false;
							Class4 = false;
							switch (which) {
							case 0:
								SecTitle = "Cosmetic Classification";
								bSecClass.setText("Cosmetics Classification");
								SecArray = CosFoodHuhsClass;
								break;
							case 1:
								SecTitle = "Drug Classification";
								bSecClass.setText("Drug Classification");
								SecArray = DrugClass;
								break;
							case 2:
								SecTitle = "Food Classification";
								bSecClass.setText("Food Classification");
								SecArray = CosFoodHuhsClass;
								break;
							case 3:
								SecTitle = "HUHS Classification";
								bSecClass.setText("HUHS Classification");
								SecArray = HUHSClass;
								break;
							}
						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			break;

		case R.id.bSecClass:
			AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
					this);
			alertDialogBuilder1.setTitle(SecTitle).setItems(SecArray,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							bSecClass.setText(SecArray[which]);
							bThirdClass.setVisibility(View.VISIBLE);
							bFourthClass.setVisibility(View.GONE);
							FirstClass = bMainClass.getText().toString();
							SecClass = bSecClass.getText().toString();
							Class2 = true;
							Class3 = false;
							Class4 = false;
							switch (FirstClass) {
							case "Cosmetics":
								if (SecClass.equals("Manufacturer")) {
									ThirdTitle = "Cosmetics Manufacturer Classification";
									bThirdClass
											.setText("Cosmetics Manufacturer Classification");
									ThirdArray = ManuClass;
								} else if (SecClass.equals("Distributor")) {
									ThirdTitle = "Cosmetics Distributor Classification";
									bThirdClass
											.setText("Cosmetics Distributor Classification");
									ThirdArray = DistriClass;
								}
								break;
							case "Drug":
								if (SecClass.equals("Manufacturer")) {
									ThirdTitle = "Drug Manufacturer Classification";
									bThirdClass
											.setText("Drug Manufacturer Classification");
									ThirdArray = ManuClass;
								} else if (SecClass.equals("Distributor")) {
									ThirdTitle = "Drug Distributor Classification";
									bThirdClass
											.setText("Drug Distributor Classification");
									ThirdArray = DistriClass;
								} else if (SecClass
										.equals("Retail Drug Outlet")) {
									ThirdTitle = "Drug Retail Drug Outlet Classification";
									bThirdClass
											.setText("Drug Retail Drug Outlet Classification");
									ThirdArray = RDOClass;
								}
								break;
							case "Food":
								if (SecClass.equals("Manufacturer")) {
									ThirdTitle = "Food Manufacturer Classification";
									bThirdClass
											.setText("Food Manufacturer Classification");
									ThirdArray = FoodManuClass;
								} else if (SecClass.equals("Distributor")) {
									ThirdTitle = "Food Distributor Classification";
									bThirdClass
											.setText("Food Distributor Classification");
									ThirdArray = DistriClass;
								}
								break;
							case "Household Urban Hazardous Substance":
								if (SecClass
										.equals("Household Urban Hazardous Substance")) {
									ThirdTitle = "Household Urban Hazardous Substance Sub-Classification";
									bThirdClass
											.setText("Household Urban Hazardous Substance Sub-Classification");
									ThirdArray = CosFoodHuhsClass;
								} else if (SecClass.equals("Toys")) {
									ThirdTitle = "Toys Classification";
									bThirdClass.setText("Toys Classification");
									ThirdArray = CosFoodHuhsClass;
								} else if (SecClass.equals("Pesticides")) {
									ThirdTitle = "Pesticides";
									bThirdClass
											.setText("Pesticides Classification");
									ThirdArray = CosFoodHuhsClass;
								}
								break;
							}
						}
					});
			AlertDialog alertDialog1 = alertDialogBuilder1.create();
			alertDialog1.show();
			break;

		case R.id.bThirdClass:
			AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
					this);
			alertDialogBuilder2.setTitle(ThirdTitle).setItems(ThirdArray,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							bThirdClass.setText(ThirdArray[which]);
							String ThirdClass = bThirdClass.getText()
									.toString();
							Class3 = true;
							Class4 = true;
							switch (FirstClass) {
							case "Household Urban Hazardous Substance":
								if (ThirdClass.equals("Manufacturer")) {
									FourthTitle = "Manufacturer";
									bFourthClass
											.setText("Manufacturer Classification");
									FourthArray = ManuClass;
									bFourthClass.setVisibility(View.VISIBLE);
									Class4 = false;
								} else if (ThirdClass.equals("Distributor")) {
									FourthTitle = "Distributor";
									bFourthClass
											.setText("Distributor Classification");
									FourthArray = DistriClass;
									bFourthClass.setVisibility(View.VISIBLE);
									Class4 = false;
								}
								break;
							}
						}
					});
			AlertDialog alertDialog2 = alertDialogBuilder2.create();
			alertDialog2.show();
			break;

		case R.id.bFourthClass:
			AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(
					this);
			alertDialogBuilder3.setTitle(FourthTitle).setItems(FourthArray,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bFourthClass.setText(FourthArray[which]);
							Class4 = true;
						}
					});
			AlertDialog alertDialog3 = alertDialogBuilder3.create();
			alertDialog3.show();
			break;

		case R.id.bNotif:
			AlertDialog.Builder alertDialogBuilder4 = new AlertDialog.Builder(
					this);
			alertDialogBuilder4.setTitle("Manner of Notification").setItems(
					Notification, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bNotif.setText(Notification[which]);
							Notif = true;
						}
					});
			AlertDialog alertDialog4 = alertDialogBuilder4.create();
			alertDialog4.show();
			break;

		case R.id.bInspect:
			AlertDialog.Builder alertDialogBuilder5 = new AlertDialog.Builder(
					this);
			alertDialogBuilder5.setTitle("Purpose of Inspection").setItems(
					Inspection, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bInspect.setText(Inspection[which]);
							String Inspect = bInspect.getText().toString();
							InspectPurpose = true;
							if (Inspect.equals("Initial")) {
								layoutLTO.setVisibility(View.GONE);
							} else {
								layoutLTO.setVisibility(View.VISIBLE);
							}
						}
					});
			AlertDialog alertDialog5 = alertDialogBuilder5.create();
			alertDialog5.show();
			break;

		case R.id.bNext:
			boolean error, error1;
			boolean error2 = true;
			error = BlankFields(etEstablishmentName, "Name of Establishment");
			error = BlankFields(etPOAddress, "Plant/Office Address");
			error = BlankFields(etPOAddCoordinates, "Plant/Office Address GPS Coordinates");
			error = BlankFields(etWAddress, "Warehouse Address");
			error = BlankFields(etWAddCoordinates, "Warehouse Address GPS Coordinates");
			error = BlankFields(etOwner, "Owner");
			error = BlankFields(etTelNum, "Telephone Number");
			error = BlankFields(etFaxNum, "Fax Number");
			
			error1 = BlankButton(Class1, bMainClass);
			error1 = BlankButton(Class2, bSecClass);
			error1 = BlankButton(Class3, bThirdClass);
			error1 = BlankButton(Class4, bFourthClass);
			error = BlankFields(etProducts, "Product/s");
			error1 = BlankButton(Notif, bNotif);
			error1 = BlankButton(InspectPurpose, bInspect);
			
			if (layoutLTO.getVisibility() == View.VISIBLE) {
				error2 = true;
				if (etLTONum.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"Missing Field: Number", Toast.LENGTH_LONG).show();
				} else {
					error2 = false;
				}
				if (etLTORenewalDate.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"Missing Field: Renewal(Year - YYYY)",
							Toast.LENGTH_LONG).show();
				} else {
					error2 = false;
				}
				if (etLTOValidity.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"Missing Field: Validity", Toast.LENGTH_LONG)
							.show();
				} else {
					error2 = false;
				}
			} else {
				error2 = false;
			}
			error = BlankFields(etPharmacistName, "Name");
			error = BlankFields(etPharmacistPosition, "Position");
			error = BlankFields(etPrcID, "Reg. No. (PRC-ID)");
			error = BlankFields(etPrcDateIssued, "Date Issued");
			error = BlankFields(etPrcValidity, "Validity");
			error = BlankFields(etInterviewedName, "Name");
			error = BlankFields(etInterviewedPosition, "Position");
			error = BlankFields(etORNum, "OR Number");
			error = BlankFields(etORAmount, "Amount");
			error = BlankFields(etORDate, "Date of Payment");
			error = BlankFields(etRSN, "RSN");
			
			if (!error && !error1 && !error2) {
				saveSharedPreferences();
				Intent i = new Intent(SoOFGenInfo.this, SoOFOthersDirectives.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater SoOFMenu = getMenuInflater();
		SoOFMenu.inflate(R.menu.soofmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.menuHome:
			saveSharedPreferences();
			Intent i = new Intent(SoOFGenInfo.this, MainMenu.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;
		case R.id.menuClear:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("Clear Data");
			alertDialogBuilder
					.setMessage(
							"Are you sure you want to clear all saved data?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									SharedPreferences  sp = PreferenceManager
											.getDefaultSharedPreferences(getApplicationContext());
									sp.edit().clear().commit();
									deleteFile("FDRO1");
									deleteFile("FDRO2");
									deleteFile("EstRep1");
									deleteFile("EstRep2");
									Toast.makeText(getApplicationContext(),
											"Successfully Cleared All Data",
											Toast.LENGTH_LONG).show();
									Intent i = new Intent(SoOFGenInfo.this, MainMenu.class);
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

	private void saveSharedPreferences() {
		savePreferences("EstablishmentName", etEstablishmentName.getText()
				.toString());
		savePreferences("PlantOfficeAddress", etPOAddress.getText().toString());
		savePreferences("POAddCoordinates", etPOAddCoordinates.getText().toString());
		savePreferences("WarehouseAddress", etWAddress.getText().toString());
		savePreferences("WAddCoordinates", etWAddCoordinates.getText()
				.toString());
		savePreferences("Owner", etOwner.getText().toString());
		savePreferences("TelNumber", etTelNum.getText().toString());
		savePreferences("FaxNumber", etFaxNum.getText().toString());
		savePreferences("MainClass", bMainClass.getText().toString());
		savePreferences("Class1", Class1);
		savePreferences("SecClass", bSecClass.getText().toString());
		savePreferences("Class2", Class2);
		savePreferences("ThirdClass", bThirdClass.getText().toString());
		savePreferences("Class3", Class3);
		savePreferences("FourthClass", bFourthClass.getText().toString());
		savePreferences("Class4", Class4);
		savePreferences("Products", etProducts.getText().toString());
		savePreferences("Notification", bNotif.getText().toString());
		savePreferences("Notif", Notif);
		savePreferences("Inspection", bInspect.getText().toString());
		savePreferences("Inspect", InspectPurpose);
		savePreferences("LTONumber", etLTONum.getText().toString());
		savePreferences("LTORenewalDate", etLTORenewalDate.getText().toString());
		savePreferences("LTOValidity", etLTOValidity.getText().toString());
		savePreferences("PharmacistName", etPharmacistName.getText().toString());
		savePreferences("PharmacistPosition", etPharmacistPosition.getText()
				.toString());
		savePreferences("PrcID", etPrcID.getText().toString());
		savePreferences("PrcDateIssued", etPrcDateIssued.getText().toString());
		savePreferences("PrcValidity", etPrcValidity.getText().toString());
		savePreferences("InterviewedName", etInterviewedName.getText()
				.toString());
		savePreferences("InterviewedPosition", etInterviewedPosition.getText()
				.toString());
		savePreferences("ORNum", etORNum.getText().toString());
		savePreferences("ORAmount", etORAmount.getText().toString());
		savePreferences("ORDate", etORDate.getText().toString());
		savePreferences("RSN", etRSN.getText().toString());
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

	private void savePreferences(String key, boolean value) {
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	private void loadSavedPreferences() {
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		etEstablishmentName.setText(sp.getString("EstablishmentName", null));
		etPOAddress.setText(sp.getString("PlantOfficeAddress", null));
		etPOAddCoordinates.setText(sp.getString("POAddCoordinates", null));
		etWAddress.setText(sp.getString("WarehouseAddress", null));
		etWAddCoordinates.setText(sp.getString("WAddCoordinates", null));
		etOwner.setText(sp.getString("Owner", null));
		etTelNum.setText(sp.getString("TelNumber", null));
		etFaxNum.setText(sp.getString("FaxNumber", null));
		bMainClass.setText(sp.getString("MainClass", "Classification"));
		Class1 = sp.getBoolean("Class1", false);
		if (sp.contains("SecClass")) {
			bSecClass.setText(sp.getString("SecClass", null));
			bSecClass.setVisibility(View.VISIBLE);
		}
		Class2 = sp.getBoolean("Class2", false);
		if (sp.contains("ThirdClass")) {
			bThirdClass.setText(sp.getString("ThirdClass", null));
			bThirdClass.setVisibility(View.VISIBLE);
		}
		Class3 = sp.getBoolean("Class3", false);
		if (sp.contains("FourthClass")) {
			bFourthClass.setText(sp.getString("FourthClass", null));
			bFourthClass.setVisibility(View.VISIBLE);
		}
		Class4 = sp.getBoolean("Class4", false);
		etProducts.setText(sp.getString("Products", null));
		bNotif.setText(sp.getString("Notification", "Manner of Notification"));
		Notif = sp.getBoolean("Notif", false);
		bInspect.setText(sp.getString("Inspection", "Purpose of Inspection"));
		String Inspection = bInspect.getText().toString();
		InspectPurpose = sp.getBoolean("Inspect", false);
		if (Inspection.equals("Initial")) {
			layoutLTO.setVisibility(View.GONE);
		} else if (Inspection.equals("Renewal") || Inspection.equals("Routine")) {
			layoutLTO.setVisibility(View.VISIBLE);
			etLTONum.setText(sp.getString("LTONumber", null));
			etLTORenewalDate.setText(sp.getString("LTORenewalDate", null));
			etLTOValidity.setText(sp.getString("LTOValidity", null));
		}
		etPharmacistName.setText(sp.getString("PharmacistName", null));
		etPharmacistPosition.setText(sp.getString("PharmacistPosition", null));
		etPrcID.setText(sp.getString("PrcID", null));
		etPrcDateIssued.setText(sp.getString("PrcDateIssued", null));
		etPrcValidity.setText(sp.getString("PrcValidity", null));
		etInterviewedName.setText(sp.getString("InterviewedName", null));
		etInterviewedPosition
				.setText(sp.getString("InterviewedPosition", null));
		etORNum.setText(sp.getString("ORNum", null));
		etORAmount.setText(sp.getString("ORAmount", null));
		etORDate.setText(sp.getString("ORDate", null));
		etRSN.setText(sp.getString("RSN", null));
	}

	private boolean BlankFields(EditText editext, String text) {

		boolean error = false;
		if (editext.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "Missing Field:  " + text, Toast.LENGTH_LONG).show();
			error = true;
		}
		return error;
	}

	private boolean BlankButton(Boolean bool, Button button) {

		boolean error = false;
		String text = button.getText().toString();
		if (bool == false && button.getVisibility() == View.VISIBLE) {
			Toast.makeText(getApplicationContext(), "Missing Field:  " + text, Toast.LENGTH_LONG).show();
			error = true;
		}
		return error;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		saveSharedPreferences();
		Intent i = new Intent(SoOFGenInfo.this, MainMenu.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

}
