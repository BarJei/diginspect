package tk.diginspect.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import tk.diginspect.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Signature extends Activity implements OnClickListener {
	GestureOverlayView gestureView;
	String path, stringkey, booleankey;
	File file;
	Bitmap bitmap;
	public boolean gestureTouch = false;
	Button bCancel, bClear, bSave;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signature);
		Initialization();

		gestureView = (GestureOverlayView) findViewById(R.id.signaturePad);
		gestureView.setDrawingCacheEnabled(true);
		gestureView.setAlwaysDrawnWithCacheEnabled(true);
		gestureView.setHapticFeedbackEnabled(false);
		gestureView.cancelLongPress();
		gestureView.cancelClearAnimation();
		gestureView.addOnGestureListener(new OnGestureListener() {
			@Override
			public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGestureCancelled(GestureOverlayView arg0,
					MotionEvent arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGestureEnded(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGestureStarted(GestureOverlayView arg0,
					MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
					gestureTouch = false;
				} else {
					gestureTouch = true;
					bSave.setEnabled(true);
				}
			}
		});
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		bCancel = (Button) findViewById(R.id.bCancel);
		bClear = (Button) findViewById(R.id.bClear);
		bSave = (Button) findViewById(R.id.bSave);
		bCancel.setOnClickListener(this);
		bClear.setOnClickListener(this);
		bSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bCancel:
			i = new Intent(Signature.this, SoOFSignatories.class);
			startActivity(i);
			finish();
			break;
		case R.id.bClear:
			gestureView.invalidate();
			gestureView.clear(true);
			gestureView.clearAnimation();
			gestureView.cancelClearAnimation();
			bSave.setEnabled(false);
			break;
		case R.id.bSave:
			try {
				Bundle bundle = getIntent().getExtras();
				stringkey = bundle.getString("bundlekey");
				booleankey = bundle.getString("bundleboolean");
				bitmap = Bitmap.createBitmap(gestureView.getDrawingCache());
				FileOutputStream fos = openFileOutput(stringkey, 0);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {			
				savePreferences(booleankey, true);
				i = new Intent(Signature.this, SoOFSignatories.class);
				startActivity(i);
				finish();
			}
			break;
		}
	}

	private void savePreferences(String key, boolean value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		i = new Intent(Signature.this, SoOFSignatories.class);
		startActivity(i);
		finish();
	}

}
