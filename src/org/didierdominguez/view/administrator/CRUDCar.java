package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.didierdominguez.Main;
import org.didierdominguez.bean.Car;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.controller.ControllerCar;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CRUDCar {
}

class CreateCar {
    private static CreateCar instance;
    private String directory;
    private ImageView imageView;

    private CreateCar() {
        directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
    }

    static CreateCar getInstance() {
        if (instance == null) {
            instance = new CreateCar();
        }
        return instance;
    }

    public GridPane getGridPane(Customer customer) {
        GridPane gridPane = new GridPane();
        FileControl.getInstance().setFileControlImage(null);

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(0, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("AGREGAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0, 2, 1);
        gridPane.setMargin(textTitle, new Insets(0, 0, -15, 0));

        try {
            Image image = new Image(new FileInputStream(directory + "upload-pic.jpg"));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(3* Main.getStage().getWidth()/16);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(event -> {
            FileControl.getInstance().uploadImage("carImage", "*.jpg");
            if (FileControl.getInstance().getFileControlImage() != null) {
                try {
                    Image image = new Image(new FileInputStream(directory
                            + FileControl.getInstance().getFileControlImage().getName()));
                    imageView.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(FileControl.getInstance().getFileControlImage().getName());
            }
        });
        imageView.getStyleClass().addAll("uploadImageView");
        gridPane.add(imageView, 0, 1, 1, 4);
        gridPane.setMargin(imageView, new Insets(10, 0, 0, 0));

        JFXTextField fieldID = new JFXTextField();
        fieldID.setPromptText("PLACA");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 1, 2);

        JFXTextField fieldBrand = new JFXTextField();
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        gridPane.add(fieldBrand, 1, 3);

        JFXTextField fieldModel = new JFXTextField();
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        gridPane.add(fieldModel, 1, 4);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 4);
        buttonAdd.setOnAction(event -> {
            if (fieldID.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0
                    || FileControl.getInstance().getFileControlImage() == null) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                Car car = ControllerCar.getInstance().searchCar(fieldID.getText().trim());
                if (car != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL AUTOMOVIL YA ESTÁ REGISTRADO");
                } else {
                    ControllerCar.getInstance().createCar(fieldID.getText().trim(), fieldBrand.getText().trim(),
                            fieldModel.getText().trim(), FileControl.getInstance().getFileControlImage().getName(),
                            customer);
                    ViewCustomer.getInstance().updateTableViewItemsCar(customer);
                    Alert.getInstance().showNotification("CLIENTES", "AUTOMOVIL AGREGADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonAdd, 0, 5, 2, 1);
        gridPane.setMargin(buttonAdd, new Insets(-5, 0, -10, 0));

        return gridPane;
    }
}

class UpdateCar{
    private static UpdateCar instance;
    private String directory;
    private ImageView imageView;

    private UpdateCar() {}

    static UpdateCar getInstance() {
        if (instance == null) {
            instance = new UpdateCar();
        }
        return instance;
    }

    GridPane getGridPane(Car car) {
        GridPane gridPane = new GridPane();
        this.directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
        FileControl.getInstance().setFileControlImage(null);

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(0, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MODIFICAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0, 2, 1);
        gridPane.setMargin(textTitle, new Insets(0, 0, -15, 0));

        File directory = new File(this.directory + car.getPicture());
        if (directory.exists()) {
            this.directory += car.getPicture();
        } else {
            this.directory += "image-not-found.jpg";
        }
        try {
            Image image = new Image(new FileInputStream(this.directory));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(3* Main.getStage().getWidth()/16);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(event -> {
            FileControl.getInstance().uploadImage("carImage", "*.jpg");
            if (FileControl.getInstance().getFileControlImage() != null) {
                try {
                    this.directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
                    Image image = new Image(new FileInputStream(this.directory
                            + FileControl.getInstance().getFileControlImage().getName()));
                    imageView.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(FileControl.getInstance().getFileControlImage().getName());
            }
        });
        imageView.getStyleClass().addAll("uploadImageView");
        gridPane.add(imageView, 0, 1, 1, 4);
        gridPane.setMargin(imageView, new Insets(10, 0, 0, 0));

        JFXTextField fieldID = new JFXTextField(String.valueOf(car.getId()));
        fieldID.setPromptText("PLACA");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 1, 2);

        JFXTextField fieldBrand = new JFXTextField(car.getBrand());
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        gridPane.add(fieldBrand, 1, 3);

        JFXTextField fieldModel = new JFXTextField(car.getModel());
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        gridPane.add(fieldModel, 1, 4);

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "primaryButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y / 4);
        buttonUpdate.setOnAction(event -> {
            String imageRoute;
            if (fieldID.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                if (FileControl.getInstance().getFileControlImage() != null
                        && !car.getPicture().equals(FileControl.getInstance().getFileControlImage().getName())) {
                    imageRoute = FileControl.getInstance().getFileControlImage().getName();
                } else {
                    imageRoute = car.getPicture();
                }
                ControllerCar.getInstance().updateCar(fieldID.getText().trim(), fieldBrand.getText().trim(),
                        fieldModel.getText().trim(), imageRoute);
                if (!ControllerCar.getInstance().updateCar()){
                    Alert.getInstance().showAlert(gridPane, "ERROR", "ERROR AL MODIFICAR AUTOMOVIL");
                } else {
                    ViewCustomer.getInstance().updateTableViewItemsCar(car.getCustomer());
                    Alert.getInstance().showNotification("CLIENTES", "AUTOMOVIL ACTUALIZADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonUpdate, 0, 5, 2, 1);
        gridPane.setMargin(buttonUpdate, new Insets(-5, 0, -10, 0));

        return gridPane;
    }
}

class ShowCar {
    private static ShowCar instance;
    private String directory;
    private ImageView imageView;

    private ShowCar() {
    }

    static ShowCar getInstance() {
        if (instance == null) {
            instance = new ShowCar();
        }
        return instance;
    }

    GridPane getGridPane(Car car) {
        GridPane gridPane = new GridPane();
        this.directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
        FileControl.getInstance().setFileControlImage(null);

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(0, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MOSTRAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0, 2, 1);
        gridPane.setMargin(textTitle, new Insets(0, 0, -15, 0));

        File directory = new File(this.directory + car.getPicture());
        if (directory.exists()) {
            this.directory += car.getPicture();
        } else {
            this.directory += "image-not-found.jpg";
        }
        try {
            Image image = new Image(new FileInputStream(this.directory));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(3* Main.getStage().getWidth()/16);
        imageView.setPreserveRatio(true);
        gridPane.add(imageView, 0, 1, 1, 4);
        gridPane.setMargin(imageView, new Insets(10, 0, 0, 0));

        JFXTextField fieldID = new JFXTextField(String.valueOf(car.getId()));
        fieldID.setPromptText("PLACA");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        fieldID.setEditable(false);
        gridPane.add(fieldID, 1, 2);

        JFXTextField fieldBrand = new JFXTextField(car.getBrand());
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        fieldBrand.setEditable(false);
        gridPane.add(fieldBrand, 1, 3);

        JFXTextField fieldModel = new JFXTextField(car.getModel());
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        fieldModel.setEditable(false);
        gridPane.add(fieldModel, 1, 4);

        JFXButton buttonCopy = new JFXButton("COPIAR");
        buttonCopy.getStyleClass().addAll("customButton", "primaryButton");
        buttonCopy.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCopy.setPrefSize(x, y / 4);
        buttonCopy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString("PLACA:         " + car.getId()
                    + "\nMARCA:         " + car.getBrand()
                    + "\nMODELO:        " + car.getModel());
            clipboard.setContent(content);
        });
        gridPane.add(buttonCopy, 0, 5, 2, 1);
        gridPane.setMargin(buttonCopy, new Insets(-5, 0, -10, 0));

        return gridPane;
    }
}

