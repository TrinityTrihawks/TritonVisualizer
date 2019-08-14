package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class VizBox {

    Text messageBox;
    LineChart<Number, Number> chart;
    GridPane checkBoxes;
    NumberAxis xAxis;
    NumberAxis yAxis;

    Map<String, Boolean> persistentVisibleTracker;


    public VizBox() {
        // Message box
		messageBox = new Text("I'm a text box");
        // Axis
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		// Chart
		chart = new LineChart<Number,Number>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        // Checkbox grid pane
        checkBoxes = new GridPane();
        checkBoxes.addRow(0, new CheckBox("I'm a checkbox"));
        checkBoxes.addRow(1, new CheckBox("I'm another one"));

        
        persistentVisibleTracker = new HashMap<>();
    }

    public LineChart<Number,Number> getChart() {
        return chart;
    }
    public Text getMessageBox() {
        return messageBox;
    }
    public GridPane getCheckBoxes() {
        return checkBoxes;
    }

    public void updateFromFile(File csvFile) {
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csvFile));

            String title = br.readLine();
            chart.setTitle(title);

            // //save csv file in history log directory
            // try {
            //     String logFileName = title+"_"+LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replace(':', '-');
            //     Files.copy(new File(csvFile).toPath(), new File("csv_log/"+logFileName+".csv").toPath());
            // } catch(IOException e) {
            //     e.printStackTrace();
            // }
            
            // get x-axis name and graph data
            String[] firstLine = br.readLine().split(",");
            String xAxisName = firstLine[0];
            double[] xAxisData = Arrays.stream(Arrays.copyOfRange(firstLine, 1, firstLine.length)).mapToDouble(Double::parseDouble).toArray();

            //clear out graph to be ready for the incoming data
            chart.getData().clear();
            checkBoxes.getChildren().clear();
            messageBox.setText("");
            xAxis.setLabel(xAxisName);

            // State var used to update chart when checkbox pressed
		    Map<String, Node> graphNameMap = new HashMap<String, Node>();

            while ((line = br.readLine()) != null) {
                //looping through each y-axis entry

                //get this entry's name and graph data
                String[] thisEntryData = line.split(",");
                String thisName = thisEntryData[0];
                double[] thisData = Arrays.stream(Arrays.copyOfRange(thisEntryData,1,thisEntryData.length)).mapToDouble(Double::parseDouble).toArray();

                //chart this entry's name and graph data
                Series<Number, Number>  series = new Series<>();
                series.setName(thisName);
                graphNameMap.put(thisName, series.getNode());
                for(int i=0; i < thisData.length; i++) {
                    series.getData().add(new XYChart.Data<Number,Number>( xAxisData[i], thisData[i]));
                }
                chart.getData().add(series);

                //Update visibility tracker and only show this entry if visible
                if( !persistentVisibleTracker.containsKey(thisName) ) {
                    persistentVisibleTracker.put(thisName, true);
                }
                series.getNode().setVisible(persistentVisibleTracker.get(thisName));
                
                //create checkbox for this entry
                CheckBox cb = new CheckBox(thisName);
                cb.setId(thisName);
                cb.setSelected(persistentVisibleTracker.get(thisName));
                cb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    // When checkbox clicked
                    public void handle(MouseEvent event) {
                        series.getNode().setVisible(cb.isSelected());
                        persistentVisibleTracker.replace(thisName, cb.isSelected());
                    }
                });
                checkBoxes.addRow(checkBoxes.getRowCount(), cb);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    	

}