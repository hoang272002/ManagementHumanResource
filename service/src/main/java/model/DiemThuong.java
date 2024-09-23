package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "DiemThuong")
public class DiemThuong {
	@Id
	private String  manv;
	
	private int diemThuong;
	
	public DiemThuong() {super();}
	
    public DiemThuong(String manv, int diemThuong) {
    	super();
        this.manv = manv;
        this.diemThuong = diemThuong;
    }
    
    public String getManv() {
        return manv;
    }
    
    public int getDiemThuong() {
        return diemThuong;
    }

    public void setDiemThuong(int diemThuong) {
        this.diemThuong = diemThuong;
    }
    
    public void setManv(String manv) {
        this.manv = manv;
    }
	
}