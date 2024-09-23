package model;

import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "DoiThuong")
public class DoiThuong {

	private String  manv;
	private String idGift;
	private String nameGift;
	private int point;
	
	public DoiThuong() {super();}
	
    public DoiThuong(String manv,String idGift,String nameGift,int point) {
    	super();
        this.manv = manv;
        this.idGift = idGift;
        this.nameGift = nameGift;
        this.point = point;
    }
    
    public String getManv() {
        return manv;
    }
    
    public String getIdGift() {
        return idGift;
    }
    public String getNameGift() {
        return nameGift;
    }
    public int getPoint() {
        return point;
    }
    
    public void setManv(String manv) {
    	this.manv = manv;
    }
    public void setPoint(int point) {
    	this.point = point;
    }
    public void setIdGift(String idGift) {
    	this.idGift = idGift;
    }
    public void setNameGift(String nameGift) {
    	this.nameGift = nameGift;
    }
}