/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.models;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class ReticEntry {
    public Date date; // I - 8
    public float totalChlorine; // J - 9
    public float temperature; // K - 10
    public float nh3; // L - 11
    public float no2; // M - 12
    public double nitrificationPotentialIndicator;
    
    public ReticEntry(Date date,
            float totalChlorine,
            float temperature,
            float nh3,
            float no2) {
        this.date = date;
        this.totalChlorine = totalChlorine;
        this.temperature = temperature;
        this.nh3 = nh3;
        this.no2 = no2;
    }
    
    public void calculateValue() {
        this.nitrificationPotentialIndicator = 
                this.totalChlorine - this.temperature / 10 * ((this.nh3) - this.totalChlorine / 5) / (0.18 + this.nh3 - this.totalChlorine / 5);
    }

    @Override
    public String toString() {
        return String.format("WaterDetail (%s, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f)", date,
                totalChlorine, temperature, nh3, no2);
    }
    
    public String getDateString() {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(this.date);
        return dateString;
    }
}
