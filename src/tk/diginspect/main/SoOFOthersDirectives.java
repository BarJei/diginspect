package tk.diginspect.main;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SoOFOthersDirectives extends Activity implements
		View.OnClickListener {

	Button bNext, bPrev, bDeficiencies;
	EditText etObservationFindings;
	CheckBox cbDirectives1, cbDirectives2, cbDirectives3, cbDirectives4,
			cbDirectives5, cbDirectives6, cbDirectives7;
	String Deficiencies[];
	SharedPreferences sp;
	Intent i;
	boolean deficiency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soofothersdirectives);
		Initialization();
		bNext.setOnClickListener(this);
		bPrev.setOnClickListener(this);
		bDeficiencies.setOnClickListener(this);
		loadSavedPreferences();
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		bNext = (Button) findViewById(R.id.bNext1);
		bPrev = (Button) findViewById(R.id.bPrev1);
		etObservationFindings = (EditText) findViewById(R.id.etObservationFindings);
		cbDirectives1 = (CheckBox) findViewById(R.id.cbDirectives1);
		cbDirectives2 = (CheckBox) findViewById(R.id.cbDirectives2);
		cbDirectives3 = (CheckBox) findViewById(R.id.cbDirectives3);
		cbDirectives4 = (CheckBox) findViewById(R.id.cbDirectives4);
		cbDirectives5 = (CheckBox) findViewById(R.id.cbDirectives5);
		cbDirectives6 = (CheckBox) findViewById(R.id.cbDirectives6);
		cbDirectives7 = (CheckBox) findViewById(R.id.cbDirectives7);
		bDeficiencies = (Button) findViewById(R.id.bDeficiencies);
		bDeficiencies.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xff888888));
		Deficiencies = getResources().getStringArray(R.array.Deficiencies);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bDeficiencies:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle(R.string.directives8).setItems(
					Deficiencies, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bDeficiencies.setText(Deficiencies[which]);
							deficiency = true;
						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			break;
		case R.id.bNext1:
			boolean error1, error2, error3;
			error1 = BlankFields(etObservationFindings, "Observation Findings");
			error2 = Directives();
			error3 = BlankButton (deficiency, bDeficiencies);
			
			if(!error1 && !error2 && !error3){
			saveSharedPreferences();
			Intent a = new Intent(SoOFOthersDirectives.this, SoOFSignatories.class);
			a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(a);
			finish();
			}
			break;
		case R.id.bPrev1:
				saveSharedPreferences();
				i = new Intent(SoOFOthersDirectives.this,SoOFGenInfo.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
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
			Intent i = new Intent(SoOFOthersDirectives.this, MainMenu.class);
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
									Intent i = new Intent(SoOFOthersDirectives.this, MainMenu.class);
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

	private void saveSharedPreferences(){
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = sp.edit();
		String ObservationFindings = etObservationFindings.getText()
				.toString();
		if (ObservationFindings.equals("")) {
		} else {
			editor.putString("ObservationFindings", ObservationFindings);
		}
		editor.putBoolean("Directives1", cbDirectives1.isChecked());
		editor.putBoolean("Directives2", cbDirectives2.isChecked());
		editor.putBoolean("Directives3", cbDirectives3.isChecked());
		editor.putBoolean("Directives4", cbDirectives4.isChecked());
		editor.putBoolean("Directives5", cbDirectives5.isChecked());
		editor.putBoolean("Directives6", cbDirectives6.isChecked());
		editor.putBoolean("Directives7", cbDirectives7.isChecked());
		String Deficiencies = bDeficiencies.getText().toString();
		editor.putString("Directives8", Deficiencies);
		editor.putBoolean("Deficiency", deficiency);
		editor.commit();
		Toast.makeText(getApplicationContext(), "Successfully Saved Data",
				Toast.LENGTH_LONG).show();
	}
	
	private void loadSavedPreferences() {
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		etObservationFindings
				.setText(sp.getString("ObservationFindings", null));
		boolean directives1 = sp.getBoolean("Directives1", false);
		if (directives1) {
			cbDirectives1.setChecked(true);
		} else {
			cbDirectives1.setChecked(false);
		}
		boolean directives2 = sp.getBoolean("Directives2", false);
		if (directives2) {
			cbDirectives2.setChecked(true);
		} else {
			cbDirectives2.setChecked(false);
		}
		boolean directives3 = sp.getBoolean("Directives3", false);
		if (directives3) {
			cbDirectives3.setChecked(true);
		} else {
			cbDirectives3.setChecked(false);
		}
		boolean directives4 = sp.getBoolean("Directives4", false);
		if (directives4) {
			cbDirectives4.setChecked(true);
		} else {
			cbDirectives4.setChecked(false);
		}
		boolean directives5 = sp.getBoolean("Directives5", false);
		if (directives5) {
			cbDirectives5.setChecked(true);
		} else {
			cbDirectives5.setChecked(false);
		}
		boolean directives6 = sp.getBoolean("Directives6", false);
		if (directives6) {
			cbDirectives6.setChecked(true);
		} else {
			cbDirectives6.setChecked(false);
		}
		boolean directives7 = sp.getBoolean("Directives7", false);
		if (directives7) {
			cbDirectives7.setChecked(true);
		} else {
			cbDirectives7.setChecked(false);
		}
		bDeficiencies.setText(sp.getString("Directives8",
				"Deficiency Compliance"));
		deficiency = sp.getBoolean("Deficiency", false);
	}
	
	private boolean Directives(){
		boolean error = false;	
		boolean cb1 = cbDirectives1.isChecked();
		boolean cb2 = cbDirectives2.isChecked();
		boolean cb3 = cbDirectives3.isChecked();
		boolean cb4 = cbDirectives4.isChecked();
		boolean cb5 = cbDirectives5.isChecked();
		boolean cb6 = cbDirectives6.isChecked();
		boolean cb7 = cbDirectives7.isChecked();
		
		if (!cb1 && !cb2 && !cb3 && !cb4 && !cb5 && !cb6 && !cb7){
			Toast.makeText(getApplicationContext(), "Missing Field: Directives", Toast.LENGTH_LONG).show();
			error = true;
		}		
		return error;
		
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

	private boolean BlankButton(Boolean bool, Button button) {

		boolean error = false;
		String errorMessage = "";
		String text = button.getText().toString();

		if (bool == false && button.getVisibility() == View.VISIBLE) {
			errorMessage = errorMessage + "Missing Field:  " + text;
			error = true;
		}

		if (error) {
			Toast.makeText(getApplicationContext(), errorMessage,
					Toast.LENGTH_LONG).show();
		}
		return error;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		/*
		super.onBackPressed();
		saveSharedPreferences();
		i = new Intent(SoOFOthersDirectives.this,SoOFGenInfo.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
		*/
	}
	
}
