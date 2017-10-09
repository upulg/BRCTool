/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.helpers;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * 
 */
public class DateCellRender extends DefaultTableCellRenderer{
 
 public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int col){
     super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
 
 
         if(value instanceof Date){
 
             //String strDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format((Date)value);
             String strDate = new SimpleDateFormat("dd-MM-yyyy").format((Date)value);
             this.setText(strDate);
         }
 
     return this;
 }
 }