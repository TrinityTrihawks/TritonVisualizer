package client;

import java.io.File;

public class MySource extends Thread {

    Gui gui;

    public MySource(Gui gui) {
        this.gui = gui;
    }

    public void run() { 
        try { 
            // Displaying the thread that is running 
            System.out.println ("Thread " + 
                                Thread.currentThread().getId() + 
                                " is running");

            Thread.sleep(5000); // wait for 5 seconds
            System.out.println("Still running");
            System.out.println("Other thread gui initialzed: "+ gui.hasBeenInitialized);
            gui.push("C:/Users/dqnykamp/Documents/Robotics/SideProjects/TritonVisualizer/data.csv");

        } catch (Exception e) { 
            // Throwing an exception 
            e.printStackTrace();
        } 
    }

}