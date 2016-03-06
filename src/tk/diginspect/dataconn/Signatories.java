package tk.diginspect.dataconn;

public class Signatories {
	
	int _id, syncStatus;
	String establishmentName, officer1, officer2, repName1, repName2, dateStarted, dateFinished;

	public Signatories()
	{
		
	}
	
	public Signatories(String strEstName, String strOfficer1, String strOfficer2, String strRep1, String strRep2, String strDates, String strDatef)
	{
		this.establishmentName = strEstName;
		this.officer1 = strOfficer1;
		this.officer2 = strOfficer2;
		this.repName1 = strRep1;
		this.repName2 = strRep2;
		this.dateStarted = strDates;
		this.dateFinished = strDatef;
		
	}
	
	public int getID()
	{
		return this._id;
	}
	
	public void setID(int id)
	{
		this._id = id;
	}
	
	public int getSync()
	{
		return this.syncStatus;
	}
	
	public void setSync(int syncStat)
	{
		this.syncStatus = syncStat;
	}
	
	public String getEstName()
	{
		return this.establishmentName;
	}
	
	public void setEstName(String estName)
	{
		this.establishmentName = estName;
	}
	
	public String getOff1()
	{
		return this.officer1;
	}
	
	public void setOff1(String off1)
	{
		this.officer1 = off1;
	}
	
	public String getOff2()
	{
		return this.officer2;
	}
	
	public void setOff2(String off2)
	{
		this.officer2 = off2;
	}
	
	public String getRep1()
	{
		return  this.repName1;
	}
	
	public void setRep1(String rep1)
	{
		this.repName1 = rep1;
	}
	
	public String getRep2()
	{
		return  this.repName2;
	}
	
	public void setRep2(String rep2)
	{
		this.repName2 = rep2;
	}
	
	public String getDateS()
	{
		return this.dateStarted;
	}
	
	public void setDateS(String dateS)
	{
		this.dateStarted = dateS;
	}
	
	public String getDateF()
	{
		return this.dateFinished;
	}
	
	public void setDateF(String dateF)
	{
		this.dateFinished = dateF;
	}

}
