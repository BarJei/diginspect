package tk.diginspect.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tk.diginspect.R;

import org.json.JSONException;
import org.json.JSONObject;

import tk.diginspect.library.UserFunctions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PasswordReset extends Activity {

private static String KEY_SUCCESS = "success";
private static String KEY_ERROR = "error";

  EditText email;
  TextView alert;
  Button resetpass, b2login;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  
        setContentView(R.layout.passwordreset);

        email = (EditText) findViewById(R.id.forpas);
        alert = (TextView) findViewById(R.id.alert);
        b2login = (Button) findViewById(R.id.bBackHome); 

        b2login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View vw) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Login.class);
        	    startActivity(i);
           		finish();
			}
		});
        b2login.getBackground().setColorFilter(new LightingColorFilter(0xFF33B5E5, 0xFF33B5E5));
        }
    
    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   		super.onCreateOptionsMenu(menu);
   		MenuInflater miLogOut = getMenuInflater();
   		miLogOut.inflate(R.menu.okcancel, menu);
   		return true;
   	}
    
    public void resetPass() {
    	
    	if(email.getText().toString().equals("")){
    		Toast.makeText(getApplicationContext(), "Please enter an e-mail address!", Toast.LENGTH_SHORT).show();
    	}
    	else{
            NetAsync();
    	}
    }	
       
       @Override
       public boolean onOptionsItemSelected(MenuItem item){
       	
       	switch(item.getItemId()){
       	case R.id.menuOk:
       		resetPass();
       		break;
       	case R.id.menuCancel:
       		  Intent login = new Intent(getApplicationContext(), Login.class);
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
                        nDialog = new ProgressDialog(PasswordReset.this);
                        nDialog.setMessage("Loading...");
                        nDialog.setTitle("Checking Network");
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
                            alert.setText("Error in Network Connection!");
                        }
                    }
                }

                private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

                    private ProgressDialog pDialog;

                    String forgotpassword;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        forgotpassword = email.getText().toString();

                        pDialog = new ProgressDialog(PasswordReset.this);
                        pDialog.setTitle("Accessing Database");
                        pDialog.setMessage("Getting Data...");
                        pDialog.setIndeterminate(true);
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }

                    @Override
                    protected JSONObject doInBackground(String... args) {

                        UserFunctions userFunction = new UserFunctions();
                        JSONObject json = userFunction.forPass(forgotpassword);
                        return json;

                    }

                    @Override
                    protected void onPostExecute(JSONObject json) {
                  /**
                   * Checks if the Password Change Process is success
                   **/
                        try {
                            if (json.getString(KEY_SUCCESS) != null) {
                                alert.setText("");
                                String res = json.getString(KEY_SUCCESS);
                                String red = json.getString(KEY_ERROR);

                                if(Integer.parseInt(res) == 1){
                                   pDialog.dismiss();
                                    alert.setText("New password is sent to your e-mail address");
                                    b2login.setVisibility(1);

                                }
                                else if (Integer.parseInt(red) == 2)
                                {    pDialog.dismiss();
                                    alert.setText("E-mail does not exist in database!");
                                }
                                else {
                                    pDialog.dismiss();
                                    alert.setText("Error occured in changing password!");
                                }

                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}
            public void NetAsync(){
                new NetCheck().execute();
            }
            public void onBackPressed() {
        	    Intent i = new Intent(getApplicationContext(), Login.class);
        	    startActivity(i);
           		finish();
        	}
}


