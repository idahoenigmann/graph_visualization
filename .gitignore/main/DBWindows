package main;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Optional;

import static javafx.geometry.Pos.CENTER;

class DBWindows {
    static void create_db_window(Stage stage) {
        //root_sw is providing the basic structure of the window
        GridPane root_sw = new GridPane();

        //set window size
        Scene scene = new Scene(root_sw, 350, 130);

        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());
        //these labels are used to display text
        Label l_db_name = new Label("Database Name:");

        //setting size of the labels
        l_db_name.getStyleClass().add("body");

        //textfield for input
        TextField db_name = new TextField("");
        db_name.setPromptText("File name");

        db_name.getStyleClass().add("body");

        //buttons
        Button cancel = new Button("Cancel");
        Button create = new Button("Create");

        LinkedList<TextField> textFields = new LinkedList<>();
        textFields.add(db_name);

        Database_GUI.disable_button_if_text_empty(textFields, create);

        //click on button cancel
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (EventHandler<Event>) event -> stage.close());


        create.setOnAction(event -> {
            //creating database
            Database database;

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Create Database");
            fileChooser.setInitialDirectory(new File("../../databases/"));
            fileChooser.setInitialFileName(db_name.getText() + ".db");
            File database_file = fileChooser.showSaveDialog(stage);     //open filechooser
            if (database_file != null) {
                database_file.delete();     //delete old file if it exists
                database = new Database(database_file.getPath());
                if (!Database_GUI.databases.contains(database)) {
                    Database_GUI.databases.add(database);       //add database to opened databases
                }
            } else {
                stage.close();
                return;
            }
            if (MainWindow.stage != null) {     //if the start window is still open
                stage.close();
                Database_GUI.active_database = database;    //set active database
                MainWindow.root.setCenter(MainWindow.create_tab());
            } else {
                Database_GUI.stage.close();
                stage.close();
                Database_GUI.active_database = database;    //set active database
                MainWindow.main_window();       //open main window
            }

            String text = String.format("%-30s", "new database created") +
                    "path: " +
                    database_file.getAbsoluteFile();

            Logging.write_logging_information(text);
        });

        //adding the labels to root
        root_sw.add(l_db_name, 0, 0, 1, 1);

        //adding textfield to root
        root_sw.add(db_name, 1, 0, 2, 1);

        //adding buttons to root
        root_sw.add(cancel, 1, 3, 1, 1);
        root_sw.add(create, 2, 3, 1, 1);


        //adjusting the alignment of root
        root_sw.setAlignment(CENTER);
        GridPane.setMargin(l_db_name, new Insets(10));
        GridPane.setMargin(db_name, new Insets(10));
        GridPane.setMargin(cancel, new Insets(10));
        GridPane.setMargin(create, new Insets(10));

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { create.fire(); }});

        //setting the title and display the window
        stage.setTitle("Create Database");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    static void open_db_window(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database");
        fileChooser.setInitialDirectory(new File("../../databases/"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Database Files", "*.db"));
        File database_file = fileChooser.showOpenDialog(stage);

        if (database_file != null) {
            Database database = new Database(database_file.getPath());
            if (!Database_GUI.databases.contains(database)) {
                Database_GUI.databases.add(database);
            }
            if (MainWindow.stage != null) {
                stage.close();
                Database_GUI.active_database = database;
                MainWindow.root.setCenter(MainWindow.create_tab());
            } else {
                Database_GUI.stage.close();
                Database_GUI.active_database = database;
                MainWindow.main_window();
                stage.close();
            }
            String text = String.format("%-30s", "database opened") +
                    "path: " +
                    database_file.getAbsoluteFile();

            Logging.write_logging_information(text);
        }
    }

    static void delete_db_window(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Delete Database");
        fileChooser.setInitialDirectory(new File("../../databases/"));

        File database_file = fileChooser.showOpenDialog(stage);

        if (database_file != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Database");
            alert.setHeaderText("Warning!");
            alert.setContentText("Are you sure you want to delete this database?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Database database = new Database(database_file.getPath());
                Database_GUI.databases.remove(database);

                database_file.delete();
                Database_GUI.active_database = Database_GUI.databases.getFirst();
                MainWindow.close_tab(database);
            }
            stage.close();

            String text = String.format("%-30s", "database deleted") +
                    "path: " +
                    database_file.getAbsoluteFile();

            Logging.write_logging_information(text);
        }
    }

    static void import_db_window(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Database");
        fileChooser.setInitialDirectory(new File("../../databases/"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File database_file = fileChooser.showOpenDialog(stage);

        if (database_file != null) {
            File db_file = new File(database_file.getAbsolutePath().replace(".csv", ".db"));
            db_file.delete();

            Database database = Csv.import_db(database_file);

            if (!Database_GUI.databases.contains(database)) {
                Database_GUI.databases.add(database);
            }
            stage.close();
            Database_GUI.active_database = database;
            MainWindow.root.setCenter(MainWindow.create_tab());

            String text = String.format("%-30s", "database imported") +
                    "path: " +
                    database_file.getAbsoluteFile();

            Logging.write_logging_information(text);

        }
    }

    static void export_db_window(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }

        LinkedList<String> datasets = Csv.export_db(Database_GUI.active_database);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Database");
        fileChooser.setInitialDirectory(new File("../../databases/"));
        fileChooser.setInitialFileName(Database_GUI.active_database.get_name().replace("db", "csv"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                //file.createNewFile();
                Files.write(file.toPath(), datasets, Charset.forName("UTF-8"));

                String text = String.format("%-30s", "database exported") +
                        "path: " +
                        file.getAbsoluteFile();

                Logging.write_logging_information(text);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
