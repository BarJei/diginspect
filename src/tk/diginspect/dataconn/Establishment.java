package tk.diginspect.dataconn;

public class Establishment {

	int _id, syncStatus;
	String establishmentName, plantofficeAdd, plantofficeGPS, warehouseAdd,
	warehouseGPS, ownerName, telNum, faxNum, classification, products,
	pharmacistName, position, prcID, dateIssued, validity, ltoNum,
	ltoRenewal, ltoValidity, notif, inspect, personName, personPos, orNum, orAmount, orDate, rsn;

	public Establishment() {

	}

	public Establishment(String EstablishmentName, String PlantOfficeAdd,
			String PlantOfficeGPS, String WarehouseAdd, String WarehouseGPS,
			String OwnerName, String TelNum, String FaxNum,
			String Classification, String Products, String Notif, String Inspect, 
			String LtoNum, String LtoRenewal, String LtoValidity,  
			String PharmacistName, String Position, String PrcID, String DateIssued, String Validity,
			String _personName, String _personPos, String _orNum, String _orAmount, String _orDate, 
			String _rsn) {
		
		this.establishmentName = EstablishmentName;
		this.plantofficeAdd = PlantOfficeAdd;
		this.plantofficeGPS = PlantOfficeGPS;
		this.warehouseAdd = WarehouseAdd;
		this.warehouseGPS = WarehouseGPS;
		this.ownerName = OwnerName;
		this.telNum = TelNum;
		this.faxNum = FaxNum;
		this.classification = Classification;
		this.products = Products;
		this.notif = Notif;
		this.inspect = Inspect;
		
		this.ltoNum = LtoNum;
		this.ltoRenewal = LtoRenewal;
		this.ltoValidity = LtoValidity;

		this.pharmacistName = PharmacistName;
		this.position = Position;
		this.prcID = PrcID;
		this.dateIssued = DateIssued;
		this.validity = Validity;
		this.personName = _personName;
		this.personPos = _personPos;
		this.orNum = _orNum;
		this.orAmount = _orAmount;
		this.orDate = _orDate;
		this.rsn = _rsn;
	}

	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}
	
	public int getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(int SyncStatus) {
		this.syncStatus = SyncStatus;
	}
	
	public String getEstablishmentName() {
		return this.establishmentName;
	}

	public void setEstablishmentName(String EstablishmentName) {
		this.establishmentName = EstablishmentName;
	}

	public String getPlantOfficeAdd() {
		return this.plantofficeAdd;
	}

	public void setPlantOfficeAdd(String PlantOfficeAdd) {
		this.plantofficeAdd = PlantOfficeAdd;
	}

	public String getPlantOfficeGPS() {
		return this.plantofficeGPS;
	}

	public void setPlantOfficeGPS(String PlantOfficeGPS) {
		this.plantofficeGPS = PlantOfficeGPS;
	}

	public String getWarehouseAdd() {
		return this.warehouseAdd;
	}

	public void setWarehouseAdd(String WarehouseAdd) {
		this.warehouseAdd = WarehouseAdd;
	}

	public String getWarehouseGPS() {
		return this.warehouseGPS;
	}

	public void setWarehouseGPS(String WarehouseGPS) {
		this.warehouseGPS = WarehouseGPS;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(String OwnerName) {
		this.ownerName = OwnerName;
	}

	public String getTelNum() {
		return this.telNum;
	}

	public void setTelNum(String TelNum) {
		this.telNum = TelNum;
	}

	public String getFaxNum() {
		return this.faxNum;
	}

	public void setFaxNum(String FaxNum) {
		this.faxNum = FaxNum;
	}

	public String getClassification() {
		return this.classification;
	}

	public void setClassification(String Classification) {
		this.classification = Classification;
	}

	public String getProducts() {
		return this.products;
	}

	public void setProducts(String Products) {
		this.products = Products;
	}
	
	public String getNotif(){
		return this.notif;
	}
	
	public void setNotif(String strNotif){
		this.notif = strNotif;
	}
	
	public String getInspect(){
		return this.inspect;
	}
	
	public void setInspect(String Inspect){
		this.inspect = Inspect;
	}
	
	public String getLtoNum() {
		return this.ltoNum;
	}

	public void setLtoNum(String LtoNum) {
		this.ltoNum = LtoNum;
	}
	
	public String getLtoRenewal() {
		return this.ltoRenewal;
	}

	public void setLtoRenewal(String LtoRenewal) {
		this.ltoRenewal = LtoRenewal;
	}
	
	public String getLtoValidity() {
		return this.ltoValidity;
	}

	public void setLtoValidity(String LtoValidity) {
		this.ltoValidity = LtoValidity;
	}

	public String getPharmacistName() {
		return this.pharmacistName;
	}

	public void setPharmacistName(String PharmacistName) {
		this.pharmacistName = PharmacistName;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String Position) {
		this.position = Position;
	}

	public String getPrcID() {
		return this.prcID;
	}

	public void setPrcID(String PrcID) {
		this.prcID = PrcID;
	}

	public String getDateIssued() {
		return this.dateIssued;
	}

	public void setDateIssued(String DateIssued) {
		this.dateIssued = DateIssued;
	}

	public String getValidity() {
		return this.validity;
	}

	public void setValidity(String Validity) {
		this.validity = Validity;
	}	
	
	public String getPersonName(){
		return this.personName;
	}
	
	public void setPersonName(String _personName){
		this.personName = _personName;
	}
	
	public String getPersonPos(){
		return this.personPos;
	}
	
	public void setPersonPos(String _personPos){
		this.personPos = _personPos;
	}
	
	public String getORnum(){
		return this.orNum;
	}
	
	public void setORnum(String _orNum){
		this.orNum = _orNum;
	}
	
	public String getORamount(){
		return this.orAmount;
	}
	
	public void setORamount(String _orAmount){
		this.orAmount = _orAmount;
	}
	
	public String getORdate(){
		return this.orDate;
	}
	
	public void setORdate(String _orDate){
		this.orDate = _orDate;
	}
	
	public String getRsn(){
		return this.rsn;
	}
	
	public void setRsn(String _rsn){
		this.rsn = _rsn;
	}
	
}
