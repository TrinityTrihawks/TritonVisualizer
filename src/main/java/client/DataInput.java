package client;

import java.io.File;

public class DataInput {

    public boolean newDataAvailable() {
        return false;
    }

    public File getData() {
        return new File("data.csv");
    }
}