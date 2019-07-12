package client;

public class App {
	public static void main(String[] args) {
		Gui myGui = new Gui();

		// Thread sourceThread = new Thread(new MySource(myGui));
		// sourceThread.start();


		myGui.launchVisualizer(args);
	}
}