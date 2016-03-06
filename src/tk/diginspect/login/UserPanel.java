package tk.diginspect.login;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import tk.diginspect.R;

import java.util.HashMap;

import tk.diginspect.library.DatabaseHandler;
import tk.diginspect.library.UserFunctions;
import tk.diginspect.main.MainMenu;

public class UserPanel extends Activity {
    /**
     * Called when the activity is first created.
     */
	
	private PendingIntent pendingIntent;
	SharedPreferences sp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpanel);
        
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		Intent alarmIntent = new Intent(UserPanel.this, tk.diginspect.dataconn.AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(UserPanel.this, 0, alarmIntent, 0);
        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();
		/**
		 * Sets user first name and last name in text view.
		 **/

        final TextView uname = (TextView)findViewById(R.id.tvUname);
        final TextView email = (TextView)findViewById(R.id.tvEmail);
        final TextView regDate = (TextView)findViewById(R.id.tvRegDate);
        final TextView login = (TextView) findViewById(R.id.tvName);
        
        login.setText("Inspector: "+user.get("lname")+", "+user.get("fname"));
        uname.setText(user.get("uname"));
        email.setText(user.get("email"));
        regDate.setText(user.get("created_at"));
        
        savePreferences("loggedInspector", user.get("lname")+ ", "+user.get("fname"));
        
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
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater miLogOut = getMenuInflater();
		miLogOut.inflate(R.menu.user, menu);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	switch(item.getItemId()){
    	case R.id.menuChecklist:
    		 Intent menu = new Intent(getApplicationContext(), MainMenu.class);
             startActivity(menu);
    		break;
    	case R.id.menuLogOut:
    		
    		 new AlertDialog.Builder(this)
 	        .setTitle("Log Out User")
 	        .setMessage("Are you sure you want to log out?")
 	        .setNegativeButton(android.R.string.no, null)
 	        .setPositiveButton(android.R.string.yes, new OnClickListener() {
 	            public void onClick(DialogInterface arg0, int arg1) {
 	                UserPanel.super.onBackPressed();
 	              UserFunctions logout = new UserFunctions();
 	              logout.logoutUser(getApplicationContext());
 	              Intent login = new Intent(getApplicationContext(), Login.class);
 	              login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 	              cancel();
 	              startActivity(login);
 	              finish();
 	            }
 	        }).create().show();
    		 
             break;
    	case R.id.menuChangePass:
    		 Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);
             startActivity(chgpass);
     		break;
             
    	}
    	return false;
    }
    
    @Override
	public void onBackPressed() {
    	 Intent menu = new Intent(getApplicationContext(), MainMenu.class);
         startActivity(menu);
	}
    
    public void cancel() {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pendingIntent);
	}
}