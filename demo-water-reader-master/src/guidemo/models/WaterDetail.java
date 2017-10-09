/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WaterDetail {
    public Date date; // A
    public float tciIn; // B
    public float tciOut; // C
    public float temperature; // D
    public float nh3; // E
    public float no2; // F
    public boolean dosed; // G - 0.5 or empty
    
    public double umkd; // -0.0008*Temp^3+0.0419*Temp^2-0.6253*Temp+3.9132+0.23
    public double kn; // +(NH3)- TCL_out/5
    public double brc; // +Um/kd*Kn/(Kn+0.18)
    public double retTime; // 55
    public double krt; // +(Tcl_in/Tcl_out-1)*1/Rectime
    public double krt20; // EXP(-6900*(1/293-1/(273+Temp)))*Krt
    public double tclBRC; // +Tcl_out- BRC
    
    public WaterDetail(Date date,
            float tciIn,
            float tciOut,
            float temperature,
            float nh3,
            float no2,
            String dosed) {
        this.date = date;
        this.tciIn = tciIn;
        this.tciOut = tciOut;
        this.temperature = temperature;
        this.nh3 = nh3;
        this.no2 = no2;
        
        String lowerStr = dosed.toLowerCase();
        
        this.dosed = "yes".equals(lowerStr);
    }
    
    public void calculateValue() {
        this.umkd = -0.0008 * Math.pow(this.temperature, 3) + 0.0419 * Math.pow(this.temperature, 2) - 0.6253 * this.temperature + 3.9132 + 0.23;
        this.kn = this.nh3 - this.tciOut / 5;
        this.brc = this.umkd * this.kn / (this.kn + 0.18);
        this.retTime = 55;
        this.krt = (this.tciIn / this.tciOut - 1) * 1 / this.retTime;
        this.krt20 = Math.exp(-6900 * (1 / 293 - 1 / (273 + this.temperature))) * this.krt;
        this.tclBRC = this.tciOut - this.brc;
    }

    @Override
    public String toString() {
        return String.format("WaterDetail (%s, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f)", date,
                tciIn, tciOut, temperature, nh3, no2, dosed);
    }
    
    public String getDateString() {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(this.date);
        return dateString;
    }
    
    public String getDosed() {
        if(this.dosed){
            return "Yes";
        }
        
        return "No";
    }
}
