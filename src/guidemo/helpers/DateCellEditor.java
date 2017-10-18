/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.helpers;

/**
 *
 * 
 */
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.table.TableCellEditor;

public class DateCellEditor extends AbstractCellEditor implements
        TableCellEditor {

    Date currentDate;
    JSpinner spinner;

    protected static final String EDIT = "edit";

    public DateCellEditor() {
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 200);
        Date latestDate = calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate,
                earliestDate,
                latestDate,
                Calendar.YEAR);//ignored for user input
        spinner = new JSpinner(dateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
    }

    // Implement the one CellEditor method that AbstractCellEditor doesn't.
    @Override
    public Object getCellEditorValue() {
        currentDate = ((SpinnerDateModel) spinner.getModel()).getDate();
        return ((SpinnerDateModel) spinner.getModel()).getDate();
    }

    // Implement the one method defined by TableCellEditor.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        currentDate = (Date) value;
        spinner.setValue(value);
        return spinner;
    }

}
