package tk.diginspect.main;

import tk.diginspect.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

	 private final Activity context;
	 private final String[] itemName, subText;
	 private final Integer[] imgID;
	
	public CustomListAdapter(Activity context, String[] itemName, Integer[] imgID, String[] subText) {
		super(context, R.layout.customlist, itemName);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.itemName = itemName;
		this.imgID = imgID;
		this.subText = subText;
	}
	
	public View getView(int position, View view, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.customlist, null, true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
		ImageView imgView = (ImageView) rowView.findViewById(R.id.icon);
		TextView subTxt = (TextView) rowView.findViewById(R.id.tvSub);
		
		txtTitle.setText(itemName[position]);
		imgView.setImageResource(imgID[position]);
		subTxt.setText(subText[position]);
		return rowView;
	}

}
