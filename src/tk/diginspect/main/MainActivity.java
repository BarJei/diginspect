package tk.diginspect.main;

import tk.diginspect.login.Login;

import tk.diginspect.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread timer = new Thread(){
        	public void run(){
        		try{
        			sleep(3000);
        		}catch (InterruptedException e){
        			e.printStackTrace();
        		}finally{
        			Intent i = new Intent(MainActivity.this, Login.class);
        			startActivity(i);
        		}	
        	}
        };
        timer.start();
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
