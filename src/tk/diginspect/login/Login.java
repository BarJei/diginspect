package tk.diginspect.login;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import tk.diginspect.dataconn.AlarmReceiver;
import tk.diginspect.library.DatabaseHandler;
import tk.diginspect.library.UserFunctions;
import tk.diginspect.main.MainMenu;
import tk.diginspect.main.SoOFSignatories;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tk.diginspect.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Login extends Activity {

    Button bLogin;
    TextView tvResetPass;
    EditText etUname, etPass;
    
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

	SharedPreferences sp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        
        etUname = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.pword);
        bLogin = (Button) findViewById(R.id.login);
        tvResetPass = (TextView)findViewById(R.id.passres);
        
        tvResetPass.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
        startActivityForResult(myIntent, 0);
        finish();
        }});
		/**
		 * Login button click event
		 * A Toast is set to alert when the Email and Password field is empty
		 **/
        bLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !etUname.getText().toString().equals("")) && ( !etPass.getText().toString().equals("")) )
                {
                    NetAsync(view);
                }
                else if ( ( !etUname.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !etPass.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Username field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Username and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    bLogin.getBackground().setColorFilter(new LightingColorFilter(0xFF33B5E5, 0xFF33B5E5));
    
    }
    
    public void reg() {
        Intent myIntent = new Intent(Login.this, Register.class);
        startActivity(myIntent);
        finish();
     }

/**
 * Async Task to check whether internet connection is working.
 **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Login.this);
            nDialog.setTitle("Checking Internet Connection");
            nDialog.setMessage("Loading...");
            nDialog.setIndeterminate(true);
            nDialog.setCancelable(false);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
        **/
        @Override
        protected Boolean doInBackground(String... args){

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    
                    e1.printStackTrace();
                } catch (IOException e) {
                    
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(), "No user account found, try again later with Internet Connection!", Toast.LENGTH_SHORT).show();
                /*DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                String strPass = db.accessUser(etUname.getText().toString());	
                if(etPass.getText().toString().equals(strPass)){
					nDialog.setMessage("Loading User Panel...");
                     nDialog.setTitle("Getting Data");
                     Intent i = new Intent(getApplicationContext(), MainMenu.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     nDialog.dismiss();
                     startActivity(i);
                     finish();
                }
                else{

                    Toast.makeText(getApplicationContext(), "No user account found, try again later with Internet Connection!", Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON response.
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String uname,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            etUname = (EditText) findViewById(R.id.etUsername);
            etPass = (EditText) findViewById(R.id.pword);
            uname = etUname.getText().toString();
            password = etPass.getText().toString();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Accessing Database");
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(uname, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
               if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading Home...");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                       /**
                        *If JSON array details are stored in SQlite it launches the User Panel.
                        **/
                        Intent i = new Intent(getApplicationContext(), MainMenu.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(i);
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    }else{
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Username and Password mismatch!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }
    @Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Exit Application")
	        .setMessage("Are you sure you want to exit the application?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	                Login.super.onBackPressed();
	                android.os.Process.killProcess(android.os.Process.myPid());
	                finish();
	            }
	        }).create().show();
	}
    
    /*
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater SoOFMenu = getMenuInflater();
		SoOFMenu.inflate(R.menu.register, menu);
		
		return true;
		
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menuReg:
			reg();
			break;
		}
		
		return false;
	}
	*/
    
}