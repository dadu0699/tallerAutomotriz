package org.didierdominguez.util;

import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileControl {
    private static FileControl instance;
    private String directory;
    private FileChooser fileChooser;
    private File fileControl;

    private FileControl() {
        directory = System.getProperty("user.dir");
    }

    public static FileControl getInstance() {
        if (instance == null) {
            instance = new FileControl();
        }
        return instance;
    }

    public void uploadFile(String description, String extension) {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new java.io.File(directory + "/fileUpload"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(description, extension));

        fileControl = fileChooser.showOpenDialog(null);
        if (fileControl != null) {
            System.out.println(fileControl.getAbsolutePath());
        } else {
            System.out.println("Files is not valid");
        }
    }

    public ArrayList<String> readFile() {
        ArrayList<String> arrayList = new ArrayList<>();
        String command;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileControl));
            while ((command = bufferedReader.readLine()) != null) {
                System.out.println(command);
                arrayList.add(command);
            }
        } catch (IOException ex) {
            System.out.println("\n Archivo no encontrado");
        }
        return arrayList;
    }
}
