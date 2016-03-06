package tk.diginspect.dataconn;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "db_rfoinspection";
	
	private static final String TBL_ESTABLISHMENT = "tbl_details";
	private static final String ESTABLISHMENT_COLUMN1 = "_id";
	private static final String SYNC_STATUS = "syncStatus";
	private static final String ESTABLISHMENT_COLUMN2 = "establishmentName";
	private static final String ESTABLISHMENT_COLUMN3 = "plantofficeAdd";
	private static final String ESTABLISHMENT_COLUMN4 = "plantofficeGPS";
	private static final String ESTABLISHMENT_COLUMN5 = "warehouseAdd";
	private static final String ESTABLISHMENT_COLUMN6 = "warehouseGPS";
	private static final String ESTABLISHMENT_COLUMN7 = "ownerName";
	private static final String ESTABLISHMENT_COLUMN8 = "telNum";
	private static final String ESTABLISHMENT_COLUMN9 = "faxNum";
	private static final String ESTABLISHMENT_COLUMN10 = "classification";
	private static final String ESTABLISHMENT_COLUMN11 = "products";
	
	private static final String NOTIF_MANNER = "notif";
	private static final String PURPOSE_INSPECTION = "purpose";
	
	private static final String LTO_COLUMN1 = "ltoNum";
	private static final String LTO_COLUMN2 = "ltoRenewal";
	private static final String LTO_COLUMN3 = "ltoValidity";
	
	private static final String PHARMACIST_COLUMN1 = "pharmacistName";
	private static final String PHARMACIST_COLUMN2 = "position";
	private static final String PHARMACIST_COLUMN3 = "prcID";
	private static final String PHARMACIST_COLUMN4 = "dateIssued";
	private static final String PHARMACIST_COLUMN5 = "validity";
	private static final String INTERVIEW_COLUMN1 = "personName";
	private static final String INTERVIEW_COLUMN2 = "personPos";
	private static final String PAYMENT_COLUMN1 = "orNum";
	private static final String PAYMENT_COLUMN2 = "orAmount";
	private static final String PAYMENT_COLUMN3 = "orDate";
	private static final String RSN = "rsn";
	
	
	private static final String TBL_DIRECTIVES = "tbl_directives";
	private static final String DIRECTIVES_ID = "_id";
	private static final String DIRECTIVES_STATUS = "syncStatus";
	private static final String DIRECTIVES_ESTNAME = "establishmentName";
	private static final String DIRECTIVES_OBSERVATION = "observation";
	private static final String DIRECTIVES = "directive";
	private static final String DIRECTIVES_COMPLIANCE = "deficiencyCompliance";
	
	private static final String TBL_SIGNATORIES = "tbl_signatories";
	private static final String SIG_ID = "_id";
	private static final String SIG_STATUS = "syncStatus";
	private static final String SIG_ESTNAME = "establishmentName";
	private static final String SIG_OFFICER1 = "officer1";
	private static final String SIG_OFFICER2 = "officer2";
	private static final String SIG_REPNAME1 = "repName1";
	private static final String SIG_REPNAME2 = "repName2";
	private static final String SIG_DATESTART = "dateStarted";
	private static final String SIG_DATEFINISHED = "dateFinished";
	
	

	private static final String CREATE_ESTABLISHMENT = "CREATE TABLE "
			+ TBL_ESTABLISHMENT + "(" 
			+ ESTABLISHMENT_COLUMN1 + " INTEGER PRIMARY KEY," 
			+ SYNC_STATUS + " INTEGER," 
			+ ESTABLISHMENT_COLUMN2 + " TEXT,"
			+ ESTABLISHMENT_COLUMN3 + " TEXT," 
			+ ESTABLISHMENT_COLUMN4 + " TEXT," 
			+ ESTABLISHMENT_COLUMN5 + " TEXT,"
			+ ESTABLISHMENT_COLUMN6 + " TEXT," 
			+ ESTABLISHMENT_COLUMN7 + " TEXT," 
			+ ESTABLISHMENT_COLUMN8 + " TEXT,"
			+ ESTABLISHMENT_COLUMN9 + " TEXT," 
			+ ESTABLISHMENT_COLUMN10 + " TEXT," 
			+ ESTABLISHMENT_COLUMN11 + " TEXT,"
			+ NOTIF_MANNER + " TEXT," 
			+ PURPOSE_INSPECTION + " TEXT,"
			+ LTO_COLUMN1 + " TEXT," 
			+ LTO_COLUMN2 + " TEXT,"
			+ LTO_COLUMN3 + " TEXT,"
			+ PHARMACIST_COLUMN1 + " TEXT," 
			+ PHARMACIST_COLUMN2 + " TEXT," 
			+ PHARMACIST_COLUMN3 + " TEXT," 
			+ PHARMACIST_COLUMN4 + " TEXT," 
			+ PHARMACIST_COLUMN5 + " TEXT," 
			+ INTERVIEW_COLUMN1 + " TEXT,"
			+ INTERVIEW_COLUMN2 + " TEXT,"
			+ PAYMENT_COLUMN1 + " TEXT,"
			+ PAYMENT_COLUMN2 + " TEXT,"
			+ PAYMENT_COLUMN3 + " TEXT,"
			+ RSN + " TEXT)";
	
	private static final String CREATE_DIRECTIVES = "CREATE TABLE "
			+ TBL_DIRECTIVES + "("
			+ DIRECTIVES_ID + " INTEGER PRIMARY KEY,"
			+ DIRECTIVES_STATUS + " INTEGER,"
			+ DIRECTIVES_ESTNAME + " TEXT,"
			+ DIRECTIVES_OBSERVATION + " TEXT,"
			+ DIRECTIVES + " TEXT,"
			+ DIRECTIVES_COMPLIANCE + " TEXT)";
	
	private static final String CREATE_SIG = "CREATE TABLE "
			+ TBL_SIGNATORIES + "("
			+ SIG_ID + " INTEGER PRIMARY KEY,"
			+ SIG_STATUS + " INTEGER,"
			+ SIG_ESTNAME + " TEXT,"
			+ SIG_OFFICER1 + " TEXT,"
			+ SIG_OFFICER2 + " TEXT,"
			+ SIG_REPNAME1 + " TEXT,"
			+ SIG_REPNAME2 + " TEXT,"
			+ SIG_DATESTART + " TEXT,"
			+ SIG_DATEFINISHED + " TEXT)";

	SharedPreferences sp;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_ESTABLISHMENT);
		db.execSQL(CREATE_DIRECTIVES);
		db.execSQL(CREATE_SIG);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TBL_ESTABLISHMENT);
		onCreate(db);
	}

	//Establishment
	
	public void addEstablishment(Establishment establishment) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SYNC_STATUS, 0);
		values.put(ESTABLISHMENT_COLUMN2, establishment.getEstablishmentName());
		values.put(ESTABLISHMENT_COLUMN3, establishment.getPlantOfficeAdd());
		values.put(ESTABLISHMENT_COLUMN4, establishment.getPlantOfficeGPS());
		values.put(ESTABLISHMENT_COLUMN5, establishment.getWarehouseAdd());
		values.put(ESTABLISHMENT_COLUMN6, establishment.getWarehouseGPS());
		values.put(ESTABLISHMENT_COLUMN7, establishment.getOwnerName());
		values.put(ESTABLISHMENT_COLUMN8, establishment.getTelNum());
		values.put(ESTABLISHMENT_COLUMN9, establishment.getFaxNum());
		values.put(ESTABLISHMENT_COLUMN10, establishment.getClassification());
		values.put(ESTABLISHMENT_COLUMN11, establishment.getProducts());
		values.put(NOTIF_MANNER, establishment.getNotif());
		values.put(PURPOSE_INSPECTION, establishment.getInspect());
		values.put(LTO_COLUMN1, establishment.getLtoNum());
		values.put(LTO_COLUMN2, establishment.getLtoRenewal());
		values.put(LTO_COLUMN3, establishment.getLtoValidity());
		values.put(PHARMACIST_COLUMN1, establishment.getPharmacistName());
		values.put(PHARMACIST_COLUMN2, establishment.getPosition());
		values.put(PHARMACIST_COLUMN3, establishment.getPrcID());
		values.put(PHARMACIST_COLUMN4, establishment.getDateIssued());
		values.put(PHARMACIST_COLUMN5, establishment.getValidity());
		values.put(INTERVIEW_COLUMN1, establishment.getPersonName());
		values.put(INTERVIEW_COLUMN2, establishment.getPersonPos());
		values.put(PAYMENT_COLUMN1, establishment.getORnum());
		values.put(PAYMENT_COLUMN2, establishment.getORamount());
		values.put(PAYMENT_COLUMN3, establishment.getORdate());
		values.put(RSN, establishment.getRsn());
		
		db.insert(TBL_ESTABLISHMENT, null, values);
	}
	
	public List<Establishment> getAllEstablishment() {
		List<Establishment> establishmentList = new ArrayList<Establishment>();
		String selectQuery = "SELECT  * FROM " + TBL_ESTABLISHMENT + " WHERE "
				+ SYNC_STATUS + " = 0";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Establishment establishment = new Establishment();
				establishment.setID(cursor.getInt(0));
				establishment.setSyncStatus(cursor.getInt(1));
				establishment.setEstablishmentName(cursor.getString(2));
				establishment.setPlantOfficeAdd(cursor.getString(3));
				establishment.setPlantOfficeGPS(cursor.getString(4));
				establishment.setWarehouseAdd(cursor.getString(5));
				establishment.setWarehouseGPS(cursor.getString(6));
				establishment.setOwnerName(cursor.getString(7));
				establishment.setTelNum(cursor.getString(8));
				establishment.setFaxNum(cursor.getString(9));
				establishment.setClassification(cursor.getString(10));
				establishment.setProducts(cursor.getString(11));
				establishment.setNotif(cursor.getString(12));
				establishment.setInspect(cursor.getString(13));
				establishment.setLtoNum(cursor.getString(14));
				establishment.setLtoRenewal(cursor.getString(15));
				establishment.setLtoValidity(cursor.getString(16));
				establishment.setPharmacistName(cursor.getString(17));
				establishment.setPosition(cursor.getString(18));
				establishment.setPrcID(cursor.getString(19));
				establishment.setDateIssued(cursor.getString(20));
				establishment.setValidity(cursor.getString(21));
				establishment.setPersonName(cursor.getString(22));
				establishment.setPersonPos(cursor.getString(23));
				establishment.setORnum(cursor.getString(24));
				establishment.setORamount(cursor.getString(25));
				establishment.setORdate(cursor.getString(26));
				establishment.setRsn(cursor.getString(27));

				establishmentList.add(establishment);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return establishmentList;
	}

	public void updateEstablishment(int id) {
		SQLiteDatabase database = this.getWritableDatabase();
		String updateQuery = "Update " + TBL_ESTABLISHMENT + " set "
				+ SYNC_STATUS + " = 1 where "
				+ ESTABLISHMENT_COLUMN1 + " = " + "'" + id + "'";
		database.execSQL(updateQuery);
		database.close();
	}

	public int syncCount() {
		int count = 0;
		String selectQuery = "SELECT  * FROM " + TBL_ESTABLISHMENT + " WHERE "
				+ SYNC_STATUS + " = 0";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		count = cursor.getCount();
		database.close();
		return count;
	}
	
	//Directives
	
	public void addDirectives(Directives directives){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(DIRECTIVES_STATUS, 0);
		values.put(DIRECTIVES_ESTNAME, directives.getEstName());
		values.put(DIRECTIVES_OBSERVATION, directives.getObservation());
		values.put(DIRECTIVES, directives.getDirective());
		values.put(DIRECTIVES_COMPLIANCE, directives.getCompliance());
		
		db.insert(TBL_DIRECTIVES, null, values);
	}
	
	public List<Directives> getDirectives() {
		List<Directives> dirList = new ArrayList<Directives>();
		String selectQuery = "SELECT  * FROM " + TBL_DIRECTIVES + " WHERE "
				+ DIRECTIVES_STATUS + " = 0";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Directives dir = new Directives();
				dir.setID(cursor.getInt(0));
				dir.setSyncStatus(cursor.getInt(1));
				dir.setEstName(cursor.getString(2));
				dir.setObservation(cursor.getString(3));
				dir.setDirective(cursor.getString(4));
				dir.setCompliance(cursor.getString(5));

				dirList.add(dir);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return dirList;
	}
	
	public void updateDirectives(int id){
		
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "update "+ TBL_DIRECTIVES + " set " 
		+ DIRECTIVES_STATUS + " = 1 where " 
		+ DIRECTIVES_ID + " = "+"'"+ id + "'";
		db.execSQL(updateQuery);
		db.close();
		
	}
	
	public int dirSync(){
		
		int count = 0;
		String selectQuery = "SELECT  * FROM " + TBL_DIRECTIVES + " WHERE "
				+ DIRECTIVES_STATUS + " = 0";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		count = cursor.getCount();
		database.close();
		return count;
		
	}

	//Signatories
	
		public void addSig(Signatories signatories){
			
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			
			values.put(SIG_STATUS, 0);
			values.put(SIG_ESTNAME, signatories.getEstName());
			values.put(SIG_OFFICER1, signatories.getOff1());
			values.put(SIG_OFFICER2, signatories.getOff2());
			values.put(SIG_REPNAME1, signatories.getRep1());
			values.put(SIG_REPNAME2, signatories.getRep2());
			values.put(SIG_DATESTART, signatories.getDateS());
			values.put(SIG_DATEFINISHED, signatories.getDateF());
			
			db.insert(TBL_SIGNATORIES, null, values);
		}
		
		public List<Signatories> getSig() {
			List<Signatories> sigList = new ArrayList<Signatories>();
			String selectQuery = "SELECT  * FROM " + TBL_SIGNATORIES + " WHERE "
					+ SIG_STATUS + " = 0";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					Signatories signa = new Signatories();
					signa.setID(cursor.getInt(0));
					signa.setSync(cursor.getInt(1));
					signa.setEstName(cursor.getString(2));
					signa.setOff1(cursor.getString(3));
					signa.setOff2(cursor.getString(4));
					signa.setRep1(cursor.getString(5));
					signa.setRep2(cursor.getString(6));
					signa.setDateS(cursor.getString(7));
					signa.setDateF(cursor.getString(8));

					sigList.add(signa);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			return sigList;
		}
		
		public void updateSig(int id){
			
			SQLiteDatabase db = this.getWritableDatabase();
			String updateQuery = "update "+ TBL_SIGNATORIES + " set " 
			+ SIG_STATUS + " = 1 where " 
			+ SIG_ID + " = "+"'"+ id + "'";
			db.execSQL(updateQuery);
			db.close();
			
		}
		
		public int sigSync(){
			
			int count = 0;
			String selectQuery = "SELECT  * FROM " + TBL_SIGNATORIES + " WHERE "
					+ SIG_STATUS + " = 0";
			SQLiteDatabase database = this.getWritableDatabase();
			Cursor cursor = database.rawQuery(selectQuery, null);
			count = cursor.getCount();
			database.close();
			return count;
			
		}
		
}
