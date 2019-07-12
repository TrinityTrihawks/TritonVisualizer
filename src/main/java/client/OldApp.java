// package client;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Set;

// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableInstance;

// import javafx.application.Application;
// import javafx.beans.value.ChangeListener;
// import javafx.beans.value.ObservableValue;

// import javafx.event.ActionEvent;
// import javafx.event.EventHandler;
// import javafx.scene.Node;
// import javafx.scene.Scene;
// import javafx.scene.chart.LineChart;
// import javafx.scene.chart.NumberAxis;
// import javafx.scene.chart.XYChart;
// import javafx.scene.chart.XYChart.Series;
// import javafx.scene.control.Button;
// import javafx.scene.control.CheckBox;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.StackPane;
// import javafx.scene.text.Text;
// import javafx.stage.Stage;

// public class OldApp extends Application {

// 	//// PARAMETERS: must match robot program ////
// 	static String tableName = "Visualizer";
// 	static String xAxisSubtableName = "xaxis";
// 	static int team = 4215;


// 	static NetworkTableInstance inst;
// 	static Map<String, Boolean> visible;

// 	public static void main(String[] args) {

// 		inst = NetworkTableInstance.getDefault();
// 		visible = new HashMap<String, Boolean>();

// 	    inst.startClientTeam(team);
//     	inst.startDSClient();

//     	launch(args);
// 	}


// 	/**
// 	 * Get data from NetworkTables
// 	 */
// 	public Map<String, double[]> getData(NetworkTable table) {
// 		Set<String> keys = table.getKeys();

// 		Map<String,double[]> map = new HashMap<>();
// 		for(String key : keys) {
// 			map.put(key, table.getEntry(key).getDoubleArray(new double[0]));
// 		}

// 		return map;
// 	}

// 	public String getXAxisName(NetworkTable baseTable) {
// 		Set<String> keySet = baseTable.getSubTable(xAxisSubtableName).getKeys();
// 		return keySet.toArray(new String[keySet.size()])[0]; // there should never be more than one key in NTables
// 	}

// 	public double[] getXAxisData(NetworkTable baseTable) {
// 		String key = getXAxisName(baseTable);
// 		return baseTable.getSubTable(xAxisSubtableName).getEntry(key).getDoubleArray(new double[0]);
// 	}

// 	@Override     
// 	public void start(Stage stage) throws Exception {

// 		// JAVAFX COMPONENTS+

// 		stage.setTitle("Visualizer");
// 		// Message box
// 		Text messageBox = new Text("Press the button to get the values");
// 		// Button
// 		Button button = new Button("Check network tables");
// 		// Axis
// 		final NumberAxis xAxis = new NumberAxis();
// 		final NumberAxis yAxis = new NumberAxis();
// 		// Chart
// 		LineChart<Number, Number> chart = new LineChart<Number,Number>(xAxis, yAxis);
// 		chart.setCreateSymbols(false);
// 		// Checkbox grid pane
// 		GridPane checkBoxes = new GridPane();
// 		// Border pane
// 		BorderPane border = new BorderPane();
// 		border.setLeft(button);
// 		// border.setBottom(button);
// 		border.setRight(checkBoxes);
// 		// border.setRight(checkBoxes);
// 		// Root pane
// 		StackPane root = new StackPane();
// 		root.getChildren().addAll(chart, border, messageBox);

// 		stage.setScene(new Scene(root, 1200, 700));

		
// 		// State var used to update chart when checkbox pressed
// 		Map<String, Node> seriesNodes = new HashMap<String, Node>();


// 		button.setOnAction(new EventHandler<ActionEvent>() {
// 		@Override
// 		public void handle(ActionEvent event) {

// 			if(inst.isConnected() == false) {
// 				messageBox.setText("Network Tables is not connected");
// 				return;
// 			}
			
// 			NetworkTable table = inst.getTable(tableName);
// 			if(table.getKeys().isEmpty()) {
// 				messageBox.setText("NT table '"+ tableName+"' is empty");
// 				return;
// 			}


// 			Map<String,double[]> data = getData(table);

// 			String xName = getXAxisName(table);
// 			double[] xData = getXAxisData(table);

// 			if(xName == null) {
// 				messageBox.setText("Network tables must include an x-axis");
// 				return;
// 			}

// 			// Include new entry keys in visible (default to true)
// 			for(String key : data.keySet()) {
// 				if( !visible.containsKey(key) ) {
// 					visible.put(key, true);
// 				}
// 			}

// 			System.out.println("hi");

// 			// UPDATE CHART

// 			chart.getData().clear();
// 			messageBox.setText("");
// 			xAxis.setLabel(xName);
// 			seriesNodes.clear();

// 			for(Map.Entry<String, double[]> entry : data.entrySet()) {
// 				String key = entry.getKey();
// 				double[] val = entry.getValue();
				
// 				Series<Number, Number>  series = new Series<>();
// 				series.setName(key);

// 				for(int i=0; i < val.length; i++) {
// 					series.getData().add(new XYChart.Data<Number,Number>( xData[i], val[i] ));
// 				}

// 				chart.getData().add(series);
// 				series.getNode().setVisible( visible.get(key) );
// 				seriesNodes.put(entry.getKey(), series.getNode());
// 			}

// 			// CREATE TOGGLE BUTTONS
// 			checkBoxes.getChildren().clear();

// 			for(String key : data.keySet()) {
// 				CheckBox cb = new CheckBox(key);
// 				// cb.setTranslateY(20*i);
// 				cb.setId(key);
// 				cb.setSelected(visible.get(key));

// 				// When checkbox clicked
// 				cb.selectedProperty().addListener(new ChangeListener<Boolean>() {

// 					public void changed(ObservableValue<? extends Boolean> ov,
// 						Boolean old_val, Boolean new_val) {
// 							visible.put(cb.getId(), cb.isSelected());
// 							seriesNodes.get(cb.getId()).setVisible(cb.isSelected());
// 				}
// 				});
// 				checkBoxes.addRow(checkBoxes.getRowCount(), cb);
// 			}

//         }});

// 		// SHOW STAGE
// 		stage.show();
// 	}


// }

