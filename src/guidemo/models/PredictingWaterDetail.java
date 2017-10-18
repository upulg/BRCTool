/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class PredictingWaterDetail {
    public Date date;
    public double tcl;
    public double umkd;
    public double brc;
    public double tclBRC;
    
    public PredictingWaterDetail(Date date,
            float tcl,
            float umkd,
            float brc,
            float tclBRC) {
        this.date = date;
        this.tcl = tcl;
        this.umkd = umkd;
        this.brc = brc;
        this.tclBRC = tclBRC;
    }
    
    public static PredictingWaterDetail[] calculateWaterDetail(WaterDetail detail, int numberOfDay) {
        ArrayList<PredictingWaterDetail> rs = new ArrayList<>();
        
        for (int i = 0; i < numberOfDay; i++) {
            int index = i + 1;
            float tcl = (float) (detail.tciIn / (1.0000 + ((detail.tciIn - detail.tciOut) / detail.tciOut) * Math.exp(-6924.0000 * (1.0000/(273.0000+(detail.temperature + 1.0000 / 10.0000 * index)) - 1.00/(273.0000 + detail.temperature)))));
            float umkd = (float) (-0.0008 * Math.pow(detail.temperature + index / 10.0000, 3.0000) + 0.0419 * Math.pow(detail.temperature + index / 10.0000, 2) - 0.6253 * (detail.temperature + index / 10.0000) + 3.9132 + 0.2300);
            float brc = (float) (umkd * ( detail.nh3 - tcl / 5.0000) / ((detail.nh3 - tcl / 5.0000) + 0.1800));
            float tclBRC = tcl - brc;
            
            if(Float.isNaN(tcl)) {
                tcl = 0;
            }
            
            if(Float.isNaN(umkd)) {
                umkd = 0;
            }
            
            if(Float.isNaN(brc)) {
                brc = 0;
            }
            
            if(Float.isNaN(tclBRC)) {
                tclBRC = 0;
            }
            
            Date newDate = PredictingWaterDetail.addDays(detail.date, index);
            
            PredictingWaterDetail data = new PredictingWaterDetail(newDate, tcl, umkd, brc, tclBRC);
            rs.add(data);
        }
        
        return rs.toArray(new PredictingWaterDetail[rs.size()]);
    }
    
    static public Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
