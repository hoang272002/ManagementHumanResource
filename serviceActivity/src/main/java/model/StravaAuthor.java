package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "StravaAuthor")
public class StravaAuthor {
	@Id
	private String  manv;
	
	private String clientID;
	private String clientSecret;
	private String refesh;
	private String code;
	
	public StravaAuthor() {super();}
	
    public StravaAuthor(String manv, String clientID,String clientSecret,String refesh) {
    	super();
        this.manv = manv;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.refesh = refesh;

    }
    
    public String getManv() {
        return manv;
    }
    
    public String getClientID() {
        return clientID;
    }
    public String getClientSecret() {
        return clientSecret;
    }
    public String getRefesh() {
        return refesh;
    }
    public String getCode() {
        return code;
    }
    
    public void setManv(String manv) {
        this.manv = manv;
    }
    public void setClientid(String id) {
        this.clientID = id;
    }
    public void setRefesh(String id) {
        this.refesh = id;
    }
    public void setsecret(String id) {
        this.clientSecret = id;
    }
    public void setcode(String id) {
        this.code = id;
    }

}