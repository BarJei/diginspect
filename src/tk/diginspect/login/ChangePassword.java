package tk.diginspect.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tk.diginspect.R;
import tk.diginspect.library.DatabaseHandler;
import tk.diginspect.library.UserFunctions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ChangePassword extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    EditText newpass;
    Button bChangePass, bCancel;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.changepassword);
        newpass = (EditText) findViewById(R.id.newpass);

            
    }

    public void changePass() {
    	
    	if(newpass.getText().toString().equals("") || newpass.getText().toString().length()<8){
    		Toast.makeText(getApplicationContext(), "Please enter a valid password!", Toast.LENGTH_SHORT).show();
    	}
    	else{
            NetAsync();
    	}
    }
    
    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   		super.onCreateOptionsMenu(menu);
   		MenuInflater miLogOut = getMenuInflater();
   		miLogOut.inflate(R.menu.savecancel, menu);
   		return true;
   	}
       
       @Override
       public boolean onOptionsItemSelected(MenuItem item){
       	
       	switch(item.getItemId()){
       	case R.id.menuSave:
       		changePass();
       		break;
       	case R.id.menuCancel:
       		  Intent login = new Intent(getApplicationContext(), UserPanel.class);
                 startActivity(login);
                 finish();
        		break;
                
       	}
       	return false;
       }
       
    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(ChangePassword.this);
            nDialog.setMessage("Loading...");
            nDialog.setTitle("Checking Internet Connection");
            nDialog.setIndeterminate(true);
            nDialog.setCancelable(false);
            nDialog.show();
        }

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
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                "Error in Network Connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String newpas,email;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();
            user = db.getUserDetails();

            newpas = newpass.getText().toString();
            email = user.get("email");

            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Accessing Database");
            pDialog.setMessage("Getting Data...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.chgPass(newpas, email);
            Log.d("Button", "Register");
            return json;

        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Password successfully changed.", Toast.LENGTH_SHORT).show();
                        Intent mainPnl = new Intent(getApplicationContext(), UserPanel.class);
                        startActivity(mainPnl);
                        finish();

                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Invalid old Password.", Toast.LENGTH_SHORT).show();
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Error changing Password!", Toast.LENGTH_SHORT).show();
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }}
    public void NetAsync(){
        new NetCheck().execute();
    }
    public void onBackPressed(){
    	Intent login = new Intent(getApplicationContext(), UserPanel.class);
        startActivity(login);
        finish();
    }
    
   
    
}
