package tk.diginspect.dataconn;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class BackgroundService extends IntentService {
	DatabaseHelper db = new DatabaseHelper(this);
	JSONParser jsonParser = new JSONParser();
	private static String url = "http://diginspect.tk/diginspect-mobile/company_details.php";
	private static String url_dir = "http://diginspect.tk/diginspect-mobile/company_directives.php";
	private static String url_sig = "http://diginspect.tk/diginspect-mobile/company_signatories.php";
	private static final String TAG_SUCCESS = "success";

	public BackgroundService() {
		super("BackgroundService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("Test", "Intent Service started");
		new SyncWeb().execute();
	}

	private class SyncWeb extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			if(db.syncCount() != 0 && db.dirSync() != 0 && db.sigSync() != 0){
				List<Establishment> establishments = db.getAllEstablishment();
				for (Establishment est : establishments) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("establishmentName", est.getEstablishmentName()));
					params.add(new BasicNameValuePair("plantofficeAdd", est.getPlantOfficeAdd()));
					params.add(new BasicNameValuePair("plantofficeGPS", est.getPlantOfficeGPS()));
					params.add(new BasicNameValuePair("warehouseAdd", est.getWarehouseAdd()));
					params.add(new BasicNameValuePair("warehouseGPS", est.getWarehouseGPS()));
					params.add(new BasicNameValuePair("ownerName", est.getOwnerName()));
					params.add(new BasicNameValuePair("telNum", est.getTelNum()));
					params.add(new BasicNameValuePair("faxNum", est.getFaxNum()));
					params.add(new BasicNameValuePair("classification", est.getClassification()));
					params.add(new BasicNameValuePair("products", est.getProducts()));
					params.add(new BasicNameValuePair("notif", est.getNotif()));
					params.add(new BasicNameValuePair("inspect", est.getInspect()));
					params.add(new BasicNameValuePair("ltoNum", est.getLtoNum()));
					params.add(new BasicNameValuePair("ltoRenewal", est.getLtoRenewal()));
					params.add(new BasicNameValuePair("ltoValidity", est.getLtoValidity()));
					params.add(new BasicNameValuePair("pharmacistName", est.getPharmacistName()));
					params.add(new BasicNameValuePair("position", est.getPosition()));
					params.add(new BasicNameValuePair("prcID", est.getPrcID()));
					params.add(new BasicNameValuePair("dateIssued", est.getDateIssued()));
					params.add(new BasicNameValuePair("validity", est.getValidity()));
					params.add(new BasicNameValuePair("personName", est.getPersonName()));
					params.add(new BasicNameValuePair("personPos", est.getPersonPos()));
					params.add(new BasicNameValuePair("orNum", est.getORnum()));
					params.add(new BasicNameValuePair("orAmount", est.getORamount()));
					params.add(new BasicNameValuePair("orDate", est.getORdate()));
					params.add(new BasicNameValuePair("rsn", est.getRsn()));

					JSONObject json = jsonParser.makeHttpRequest(url, "POST",
							params);
					
					try {
						int success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							db.updateEstablishment(est.getID());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				List<Directives> directives = db.getDirectives();
				for(Directives dir : directives){
					List<NameValuePair> param2 = new ArrayList<NameValuePair>();
					param2.add(new BasicNameValuePair("establishmentName", dir.getEstName()));
					param2.add(new BasicNameValuePair("observation", dir.getObservation()));
					param2.add(new BasicNameValuePair("directive", dir.getDirective()));
					param2.add(new BasicNameValuePair("deficiencyCompliance", dir.getCompliance()));
					
					JSONObject json2 = jsonParser.makeHttpRequest(url_dir, "POST", param2);
					
					try{
						int success = json2.getInt(TAG_SUCCESS);
						if (success == 1) {
							db.updateDirectives(dir.getID());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				List<Signatories> signatories = db.getSig();
				for(Signatories sig : signatories){
					List<NameValuePair> param3 = new ArrayList<NameValuePair>();
					param3.add(new BasicNameValuePair("establishmentName", sig.getEstName()));
					param3.add(new BasicNameValuePair("officer1", sig.getOff1()));
					param3.add(new BasicNameValuePair("officer2", sig.getOff2()));
					param3.add(new BasicNameValuePair("repName1", sig.getRep1()));
					param3.add(new BasicNameValuePair("repName2", sig.getRep2()));
					param3.add(new BasicNameValuePair("dateStarted", sig.getDateS()));
					param3.add(new BasicNameValuePair("dateFinished", sig.getDateF()));
					
					JSONObject json3 = jsonParser.makeHttpRequest(url_sig, "POST", param3);
					
					try{
						int success = json3.getInt(TAG_SUCCESS);
						if(success == 1){
							db.updateSig(sig.getID());
						}
					}
					catch(JSONException e){
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {

		}
	}
}
