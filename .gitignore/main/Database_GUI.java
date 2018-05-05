//Ida Hönigmann 3AHIF
package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;

import static javafx.geometry.Pos.CENTER;


public class Database_GUI extends Application{

    private static void start_window(Stage stage) {
        //root is providing the basic structure of the window
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 400, 300);

        //add stylesheet to this window
        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        //these labels are used to display text
        Label name = new Label("Bla Blub Bla");
        Label version = new Label("Version 1.0");
        Label create = new Label("Create Database");
        Label open = new Label("Open Database");

        //setting size of the labels
        name.getStyleClass().add("display");
        version.getStyleClass().add("body_small");
        create.getStyleClass().add("body_large");
        open.getStyleClass().add("body_large");

        //loading the icons
        ImageView create_db = create_image("createDB.png", 40, 40);

        ImageView open_db = create_image("openDB.png", 40, 40);


        //click on create_db
        EventHandler<MouseEvent> create_db_event = event -> DBWindows.create_db_window(new Stage());

        create_db.setOnMouseClicked(create_db_event);
        create.setOnMouseClicked(create_db_event);

        //click on open_db
        EventHandler<MouseEvent> open_db_event = event -> DBWindows.open_db_window(new Stage());

        open.setOnMouseClicked(open_db_event);
        open_db.setOnMouseClicked(open_db_event);

        //set shortcuts ctrl_n (new file) and ctrl_o (open file)
        final KeyCombination ctrl_n = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        final KeyCombination ctrl_o = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (ctrl_n.match(event)) {
                DBWindows.create_db_window(new Stage());
            } else if (ctrl_o.match(event)) {
                DBWindows.open_db_window(new Stage());
            }
        });

        //adding the labels to root
        root.add(name, 0, 0, 2, 1);
        root.add(version, 0, 1, 2, 1);
        root.add(create,1, 2, 1, 1);
        root.add(open, 1, 3, 1, 1);

        //adding icons to root
        root.add(create_db, 0, 2, 1, 1);
        root.add(open_db, 0, 3, 1, 1);

        //adjusting the alignment of root
        //root.setGridLinesVisible(true);
        root.setAlignment(CENTER);
        GridPane.setMargin(name, new Insets(10));
        GridPane.setMargin(version, new Insets(5, 0, 30, 0));
        GridPane.setMargin(create, new Insets(12, 0, 12, 10));
        GridPane.setMargin(open, new Insets(12, 0, 12, 10));

        //centering the label name and version
        GridPane.setHalignment(name, HPos.CENTER);
        GridPane.setHalignment(version, HPos.CENTER);

        Database_GUI.close_window_on_esc(scene, stage);

        //setting the title and display the window
        stage.setTitle("Bla Blub Bla");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //easier creation of images
    static ImageView create_image(String image_name, int width, int height) {
        Image image = new Image("file:../../Images/" + image_name, width, height, true, true);
        ImageView image_view = new ImageView();
        image_view.setImage(image);
        return image_view;
    }

    //some buttons need to be disabled if no information has been entered yet
    static void disable_button_if_text_empty(LinkedList<TextField> textFields, Button button) {
        button.setDisable(true);
        for (TextField textField : textFields) {    //go through all textfields
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.trim().equals("") || newValue.isEmpty()) {
                    button.setDisable(true);
                } else {    //information was entered
                    for (TextField textField1 : textFields) {
                        if (textField1.getText().isEmpty()) {
                            button.setDisable(true);    //all textfields are filled out
                            return;
                        }
                    }
                    button.setDisable(false);   //at least one textfield is still empty
                }
            });
        }
    }

    static void close_window_on_esc(Scene scene, Stage stage) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode()== KeyCode.ESCAPE) {
                stage.close();
            }
        });

    }

    static boolean no_database_exists() {
        if (Database_GUI.active_database == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No database exists");
            alert.setHeaderText("This feature needs a (open) database.");
            alert.setContentText("Open or create a database first.");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    @Override
    public void start(Stage stage) {
        Database_GUI.stage = stage;

        Logging.create_log_file();

        start_window(stage);

    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    static LinkedList<Database> databases = new LinkedList<>();     //stores all opened databases
    public static Database active_database = null;      //allows access to the active database from all classes
    static Stage stage = null;
}

