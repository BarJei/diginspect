package tk.diginspect.dataconn;

public class Directives {
	
	int _id, syncStatus;
	String establishmentName, observation, directive, compliance;
	
	public Directives(){
		
	}
	
	public Directives(String strEstName, String strObservation, String strDirective, String strCompliance){
		
		this.establishmentName = strEstName;
		this.observation = strObservation;
		this.directive = strDirective;
		this.compliance = strCompliance;
		
	}
	
	public int getID(){
		return this._id;
	}
	
	public void setID(int id){
		this._id = id;
	}
	
	public int getSyncStatus(){
		return this.syncStatus;
	}
	
	public void setSyncStatus(int SyncStatus){
		this.syncStatus = SyncStatus;
	}
	
	public String getEstName(){
		return this.establishmentName;
	}
	
	public void setEstName(String EstName){
		this.establishmentName = EstName;
	}
	
	public String getObservation(){
		return this.observation;
	}
	
	public void setObservation(String Observation){
		this.observation = Observation;
	}
	
	public String getDirective(){
		return this.directive;
	}
	
	public void setDirective(String Directive){
		this.directive = Directive;
	}
	
	public String getCompliance(){
		return this.compliance;
	}
	
	public void setCompliance(String Compliance){
		this.compliance = Compliance;
	}

}
