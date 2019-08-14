package client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App extends Application {

	final int QUERY_RATE_IN_MILLISECONDS = 2000;

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		VizBox viz = new VizBox();
		// Border pane
		BorderPane border = new BorderPane();
		border.setRight(viz.getCheckBoxes());
		// Root pane
		StackPane root = new StackPane();
		root.getChildren().addAll(viz.getChart(), border, viz.getMessageBox());

		primaryStage.setTitle("Visualizer");
		primaryStage.setScene(new Scene(root, 1200, 700));
		primaryStage.show();

		final Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				DataInput di = new DataInput();

				byte[] latestFileByteArray = new byte[0];

				// int counter = 0;
				while(! isCancelled()) {

					try{
						Thread.sleep(QUERY_RATE_IN_MILLISECONDS);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}

					File file = di.getData();
					byte[] fileAsBytes;
					try {
						fileAsBytes = Files.readAllBytes(file.toPath());
					} catch(IOException e) {
						e.printStackTrace();
						fileAsBytes = new byte[0];
					}

					boolean fileAlreadyShowing = true;
					if(Arrays.equals(fileAsBytes, latestFileByteArray)) {
						fileAlreadyShowing = true;
					} else {
						fileAlreadyShowing = false;
					}

					// System.out.println(counter+". File already showing: "+fileAlreadyShowing);
					// counter++;

					if( !fileAlreadyShowing ) {
						latestFileByteArray = fileAsBytes;
						Platform.runLater(new Runnable() {
							@Override public void run() {
								viz.updateFromFile(file);
							}
						});
					}
				}
				return null;
			}
		};

		new Thread(task).start();
	}
}