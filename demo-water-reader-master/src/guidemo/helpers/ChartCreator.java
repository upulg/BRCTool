/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidemo.helpers;

import guidemo.models.PredictingWaterDetail;
import guidemo.models.ReticEntry;
import guidemo.models.WaterDetail;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

/**
 *
 */
public class ChartCreator {
    static public JPanel generateNitrificationPotentialChart(WaterDetail[] data) {
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        TimeSeries no2Series = new TimeSeries("NO2", Day.class);
        TimeSeries tclSeries = new TimeSeries("TCl-BRC", Day.class);
        TimeSeries tabletSeries = new TimeSeries("Tablet", Day.class);
        
        // 0.2, 0.4, 2.00
        TimeSeries line02 = new TimeSeries("0.2", Day.class);
        TimeSeries line04 = new TimeSeries("0.4", Day.class);
        TimeSeries line2 = new TimeSeries("2.00", Day.class);
        List<String> removeList = Arrays.asList("0.2", "0.4", "2.00");
                        
        for (WaterDetail dt : data) {
            no2Series.addOrUpdate(new Day(dt.date), dt.no2);
            tclSeries.addOrUpdate(new Day(dt.date), dt.tclBRC);
            line02.addOrUpdate(new Day(dt.date), 0.2);
            line04.addOrUpdate(new Day(dt.date), 0.4);
            line2.addOrUpdate(new Day(dt.date), 2);
            
            double tablet = 0;
            
            if (dt.dosed == true) {
                tablet = 0.5;
            }else {
                tablet = 0;
            }
            tabletSeries.addOrUpdate(new Day(dt.date), tablet);
        }
        
        dataset.addSeries(tclSeries);
        dataset.addSeries(tabletSeries);
        dataset.addSeries(line02);
        dataset.addSeries(line04);
        dataset.addSeries(line2);
        
        // Second dataset
        TimeSeriesCollection secondDataset = new TimeSeriesCollection();
        secondDataset.addSeries(no2Series);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Nitrification Potential within the reservoir", "Date", "Nitrification Potential Indicator", dataset);        
        

        XYPlot plot = (XYPlot) chart.getPlot();
        final NumberAxis axis2 = new NumberAxis("Nitrite (mg-N/L)");
        axis2.setRange(0.000,0.020);
        axis2.setAutoTickUnitSelection(true);
        axis2.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, secondDataset);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        renderer2.setPlotLines(true);
        plot.setRenderer(1, renderer2);
        
        LegendItemCollection legendItemsOld = plot.getLegendItems();
        final LegendItemCollection legendItemsNew = new LegendItemCollection();
        
        for(int i = 0; i< legendItemsOld.getItemCount(); i++){
            if (!removeList.contains(legendItemsOld.get(i).getLabel())) {
                legendItemsNew.add(legendItemsOld.get(i));
            }          
        }
        
        LegendItemSource source = new LegendItemSource() {
            LegendItemCollection lic = new LegendItemCollection();
            
            {lic.addAll(legendItemsNew);}
            
            @Override
            public LegendItemCollection getLegendItems() {
                return lic;
            }
        };
        
        chart.removeLegend();
        
        LegendTitle lt = new LegendTitle(source);
        lt.setVerticalAlignment(org.jfree.ui.VerticalAlignment.BOTTOM);
        lt.setHorizontalAlignment(HorizontalAlignment.CENTER);
        lt.setFrame(new BlockBorder(Color.black));
        lt.setPosition(RectangleEdge.BOTTOM);
        chart.addLegend(lt);
        
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setAutoTickUnitSelection(true);
        axis.setVerticalTickLabels(true);
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yy"));
        
        return new ChartPanel(chart);
    }
    
    static public JPanel generateChloramineChart(WaterDetail[] data){
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries krtSeries = new TimeSeries("KRT 20");
        
        TimeSeries line004 = new TimeSeries("0.004", Day.class);
        TimeSeries line02 = new TimeSeries("0.02", Day.class);
        List<String> removeList = Arrays.asList("0.004", "0.02");
        

        for (WaterDetail dt : data) {
            krtSeries.addOrUpdate(new Minute(dt.date), dt.krt20);
            line004.addOrUpdate(new Day(dt.date), 0.004);
            line02.addOrUpdate(new Day(dt.date), 0.02);
        }
        
        dataset.addSeries(krtSeries);
        dataset.addSeries(line004);
        dataset.addSeries(line02);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Chloramine decay behaviour", "Date", "Chloramine Stability", dataset,true,true,false);
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setAutoTickUnitSelection(true);
        axis.setVerticalTickLabels(true);
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yy"));
        
        LegendItemCollection legendItemsOld = plot.getLegendItems();
        LegendItemCollection legendItemsNew = new LegendItemCollection();
        
        for(int i = 0; i< legendItemsOld.getItemCount(); i++){
            if (!removeList.contains(legendItemsOld.get(i).getLabel())) {
                legendItemsNew.add(legendItemsOld.get(i));
            }          
        }
        
        LegendItemSource source = new LegendItemSource() {
            LegendItemCollection lic = new LegendItemCollection();
            
            {lic.addAll(legendItemsNew);}
            
            @Override
            public LegendItemCollection getLegendItems() {
                return lic;
            }
        };
        
        chart.removeLegend();
        
        LegendTitle lt = new LegendTitle(source);
        lt.setVerticalAlignment(org.jfree.ui.VerticalAlignment.BOTTOM);
        lt.setHorizontalAlignment(HorizontalAlignment.CENTER);
        lt.setFrame(new BlockBorder(Color.black));
        lt.setPosition(RectangleEdge.BOTTOM);
        chart.addLegend(lt);
        
        return new ChartPanel(chart);
    }
    static public JPanel generateForecastingChart(WaterDetail[] data){
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries tclSeries = new TimeSeries("TCL");
        TimeSeries tclBRCSeries = new TimeSeries("TCL BRC");
        
        // 0.1 , 0.3 , 1.6
        TimeSeries line01 = new TimeSeries("01", Day.class);
        TimeSeries line03 = new TimeSeries("03", Day.class);
        TimeSeries line106 = new TimeSeries("106", Day.class);
        
        int index = data.length - 1;
        
        if (index < 0) {
            return new JPanel();
        }
        
        PredictingWaterDetail[] predictingData = PredictingWaterDetail.calculateWaterDetail(data[index], 7);
        
        // Series
        for (PredictingWaterDetail dt : predictingData) {
            tclSeries.addOrUpdate(new Day(dt.date), dt.tcl);
            tclBRCSeries.addOrUpdate(new Day(dt.date), dt.tclBRC);
            line01.addOrUpdate(new Day(dt.date), 0.1);
            line03.addOrUpdate(new Day(dt.date), 0.3);
            line106.addOrUpdate(new Day(dt.date), 1.6);
        }
        
        dataset.addSeries(tclBRCSeries);
        dataset.addSeries(tclSeries);
        dataset.addSeries(line01);
        dataset.addSeries(line03);
        dataset.addSeries(line106);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Forecasting residual without interference", "Date", "Predicting Data", dataset);
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setAutoTickUnitSelection(true);
        axis.setVerticalTickLabels(true);
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yy"));
        
        List<String> removeList = Arrays.asList("01", "03", "106");
        LegendItemCollection legendItemsOld = plot.getLegendItems();
        LegendItemCollection legendItemsNew = new LegendItemCollection();
        
        for(int i = 0; i< legendItemsOld.getItemCount(); i++){
            if (!removeList.contains(legendItemsOld.get(i).getLabel())) {
                legendItemsNew.add(legendItemsOld.get(i));
            }          
        }
        
        LegendItemSource source = new LegendItemSource() {
            LegendItemCollection lic = new LegendItemCollection();
            
            {lic.addAll(legendItemsNew);}
            
            @Override
            public LegendItemCollection getLegendItems() {
                return lic;
            }
        };
        
        chart.removeLegend();
        
        LegendTitle lt = new LegendTitle(source);
        lt.setVerticalAlignment(org.jfree.ui.VerticalAlignment.BOTTOM);
        lt.setHorizontalAlignment(HorizontalAlignment.CENTER);
        lt.setFrame(new BlockBorder(Color.black));
        lt.setPosition(RectangleEdge.BOTTOM);
        chart.addLegend(lt);
        
        return new ChartPanel(chart);
    }
    static public JPanel generateReticChart(ReticEntry[] reticData){
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries NO2Series = new TimeSeries("NO2");
        TimeSeries nitrificationBRCSeries = new TimeSeries("Nitrification Potential Indicator");
        
        // 0.2, 0.5, 2.0
        TimeSeries line02 = new TimeSeries("02", Day.class);
        TimeSeries line05 = new TimeSeries("05", Day.class);
        TimeSeries line20 = new TimeSeries("20", Day.class);
        
        for (ReticEntry dt : reticData) {
            NO2Series.addOrUpdate(new Day(dt.date), dt.no2);
            nitrificationBRCSeries.addOrUpdate(new Day(dt.date), dt.nitrificationPotentialIndicator);
            line02.addOrUpdate(new Day(dt.date), 0.2);
            line05.addOrUpdate(new Day(dt.date), 0.5);
            line20.addOrUpdate(new Day(dt.date), 2.0);
        }
                    
        dataset.addSeries(nitrificationBRCSeries);
        dataset.addSeries(line02);
        dataset.addSeries(line05);
        dataset.addSeries(line20);
        
        // Second dataset
        TimeSeriesCollection secondDataset = new TimeSeriesCollection();
        secondDataset.addSeries(NO2Series);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Retic system behaviour", "Date", "Nitrification Potential indicator", dataset);
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setAutoTickUnitSelection(true);
        axis.setVerticalTickLabels(true);
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yy"));
        
        final NumberAxis axis2 = new NumberAxis("Nitrite (mg-N/L)");
        axis2.setAutoRangeIncludesZero(false);
        axis2.setRange(0.000,0.020);

        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, secondDataset);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        renderer2.setPlotLines(true);
        plot.setRenderer(1, renderer2);
        
        List<String> removeList = Arrays.asList("02", "05", "20");
        LegendItemCollection legendItemsOld = plot.getLegendItems();
        LegendItemCollection legendItemsNew = new LegendItemCollection();
        
        for(int i = 0; i< legendItemsOld.getItemCount(); i++){
            if (!removeList.contains(legendItemsOld.get(i).getLabel())) {
                legendItemsNew.add(legendItemsOld.get(i));
            }          
        }
        
        LegendItemSource source = new LegendItemSource() {
            LegendItemCollection lic = new LegendItemCollection();
            
            {lic.addAll(legendItemsNew);}
            
            @Override
            public LegendItemCollection getLegendItems() {
                return lic;
            }
        };
        
        chart.removeLegend();
        
        LegendTitle lt = new LegendTitle(source);
        lt.setVerticalAlignment(org.jfree.ui.VerticalAlignment.BOTTOM);
        lt.setHorizontalAlignment(HorizontalAlignment.CENTER);
        lt.setFrame(new BlockBorder(Color.black));
        lt.setPosition(RectangleEdge.BOTTOM);
        chart.addLegend(lt);
        
        return new ChartPanel(chart);
    }
    
    static public JPanel generateChloramineChartOldVer(WaterDetail[] data) {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800)
                .height(600).title("Chloramine decay behaviour")
                .yAxisTitle("Chloramine Stability").xAxisTitle("Date").build();

        // Customize Chart
        chart.getStyler().setDefaultSeriesRenderStyle(org.knowm.xchart.XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

        // Series
        List<Date> xData = new ArrayList<>();
        List<Double> yKrt20 = new ArrayList<>();

        for (WaterDetail dt : data) {
            xData.add(dt.date);
            yKrt20.add(dt.krt20);
        }

        chart.addSeries("KRT 20", xData, yKrt20);

        // Show it
        return new XChartPanel<>(chart);
    }
}
