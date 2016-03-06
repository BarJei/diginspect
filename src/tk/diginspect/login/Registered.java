package tk.diginspect.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tk.diginspect.R;

import java.util.HashMap;

import tk.diginspect.library.DatabaseHandler;

public class Registered extends Activity {
    /**
     * Called when the activity is first created.
     */
	
	Button bLog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);
        
        bLog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Login.class);
        	    startActivity(i);
           		finish();
			}
		});
        bLog.getBackground().setColorFilter(new LightingColorFilter(0xFF33B5E5, 0xFF33B5E5));
     
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap user = new HashMap();
        user = db.getUserDetails();
        /**
         * Displays the registration details in Text view
         **/
        final TextView fname = (TextView)findViewById(R.id.fname);
        final TextView lname = (TextView)findViewById(R.id.lname);
        final TextView uname = (TextView)findViewById(R.id.uname);
        final TextView email = (TextView)findViewById(R.id.email);
        final TextView created_at = (TextView)findViewById(R.id.regat);
        fname.setText((CharSequence) user.get("fname"));
        lname.setText((CharSequence) user.get("lname"));
        uname.setText((CharSequence) user.get("uname"));
        email.setText((CharSequence) user.get("email"));
        created_at.setText((CharSequence) user.get("created_at"));
        
        

    }

    public void onBackPressed() {
	    Intent i = new Intent(getApplicationContext(), Login.class);
	    startActivity(i);
   		finish();
	}
    
}
