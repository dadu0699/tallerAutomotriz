package org.didierdominguez.view.administrator;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewSparePart extends Stage {
    private static ViewSparePart instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;

    private ViewSparePart() {
    }

    public static ViewSparePart getInstance() {
        if (instance == null) {
            instance = new ViewSparePart();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }
}
