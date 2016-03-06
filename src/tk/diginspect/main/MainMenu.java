package tk.diginspect.main;

import java.util.HashMap;

import tk.diginspect.R;
import tk.diginspect.library.DatabaseHandler;
import tk.diginspect.library.UserFunctions;
import tk.diginspect.login.Login;
import tk.diginspect.login.UserPanel;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainMenu extends Activity {
	
	Class<?> menuClick;
	ListView list;
	String[] itemName = {
			"Summary of Observation/Findings"
			//"View PDF Reports"
	};
	
	Integer[] imgID = {
		R.drawable.ic_inspect
		//R.drawable.ic_pdf
	};
	
	String[] subText = {
		""
		//"Start inspection by providing General Information of Establishment, Directive, and Signatories."
		//"View generated PDF Inspection Reports"
	};
	SharedPreferences sp;
	
	PendingIntent pendingIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.menu);
	
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap<String,String> user = new HashMap<String, String>();
        user = db.getUserDetails();
        
        savePreferences("loggedInspector", user.get("lname")+ ", "+user.get("fname"));
	    
	    CustomListAdapter adapter = new CustomListAdapter(this, itemName, imgID, subText);
	    list = (ListView) findViewById(R.id.list);
	    list.setAdapter(adapter);
	    
	    list.setOnItemClickListener(new OnItemClickListener() {
	    	
	    	@Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
	    		
		    	String selectedItem = itemName[position];
		    	String strEq = null;
	    		switch(position){
	    		case 0 :
	    			selectedItem = itemName[0];
		    		strEq = "SoOFGenInfo";
	    			break;
	    		case 1 :
	    			selectedItem = itemName[1];
	    			strEq = "ViewFile";
	    		}
	    		
	    		
		    	try {
					menuClick = Class.forName("tk.diginspect.main."+ strEq);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	Intent i = new Intent(MainMenu.this, menuClick);
		    	startActivity(i);
		    	//Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
		    }
	    });
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
        .setTitle("Log Out User")
        .setMessage("Are you sure you want to log out?")
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                MainMenu.super.onBackPressed();
              UserFunctions logout = new UserFunctions();
              logout.logoutUser(getApplicationContext());
              Intent login = new Intent(getApplicationContext(), Login.class);
              login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              cancel();
              startActivity(login);
              finish();
            }
        }).create().show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater SoOFMenu = getMenuInflater();
		SoOFMenu.inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menuUser:
			Intent i = new Intent(MainMenu.this, UserPanel.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;
		}
		
		return false;
	}
	
	public void cancel() {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);		
		manager.cancel(pendingIntent);
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
	
}