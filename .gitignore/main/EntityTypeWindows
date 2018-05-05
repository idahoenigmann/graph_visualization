package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;

class EntityTypeWindows {
    static void create_entity_type_window(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 260, 200);

        //add stylesheet
        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        //input field for name of entity type
        Label name_label = new Label("Name:");
        TextField name = new TextField();
        name.setPromptText("Entity Type Name");

        //input of properties
        Label property_label = new Label("Properties:");
        VBox properties = new VBox();
        TextField textField = new TextField();
        textField.setPromptText("Attribute Name");
        properties.getChildren().add(textField);

        //set font and weight of text
        name_label.getStyleClass().add("body");
        name.getStyleClass().add("body");
        property_label.getStyleClass().add("body");
        properties.getStyleClass().add("body");

        properties.setSpacing(5);

        //add plus button
        Button add_line = new Button("+");

        //cancel button; can always be pressed
        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());

        //create button; can only be pressed when all textfields are filled in
        Button create = new Button("Create");
        create.setDisable(true);
        create.setOnAction(event -> {
            if (!Database_GUI.active_database.get_table_names().contains(name.getText())) {     //entity type does not exist
                LinkedList<String> attributes = new LinkedList<>();
                for (Node node : properties.getChildren()) {
                    TextField textField1 = (TextField) node;
                    attributes.add(textField1.getText());
                }
                Database_GUI.active_database.create_table(name.getText(), attributes);  //create the new table
                stage.close();

                String text = String.format("%-30s", "new entity type created") +
                        "database: " +
                        Database_GUI.active_database.get_name() +
                        ", entity type name: " +
                        name.getText();

                Logging.write_logging_information(text);

            } else {        //entity type already exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Entity Type already exists");
                alert.setContentText("You can not create two entity types with\nthe same name.\n\n" +
                        "Please enter a different name.");
                alert.showAndWait();
            }
        });

        LinkedList<TextField> textFields = new LinkedList<>();
        textFields.add(name);
        textFields.add(textField);

        add_line.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            TextField textField12 = new TextField();
            textField12.setPromptText("Attribute Name");
            textFields.add(textField12);
            Database_GUI.disable_button_if_text_empty(textFields, create);
            properties.getChildren().add(textField12);
            stage.setHeight(stage.getHeight() + 35);
        });

        Database_GUI.disable_button_if_text_empty(textFields, create);

        gridPane.add(name_label, 0, 0);
        gridPane.add(name, 1, 0, 2, 1);
        gridPane.add(property_label, 0, 1, 3, 1);
        gridPane.add(properties, 0, 2, 3, 1);

        gridPane.add(add_line, 2, 3);
        gridPane.add(cancel, 1, 4);
        gridPane.add(create, 2, 4);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.setAlignment(Pos.CENTER);

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { create.fire(); }});

        stage.setTitle("Create Entity Type");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    static void delete_entity_type_window(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }

        if (!entity_type_exists()) {
            return;
        }
        GridPane root = new GridPane();

        int height = 90;

        Label label = new Label("Choose Entity Type:");
        label.getStyleClass().add("body");

        VBox vBox = new VBox();
        ToggleGroup group = new ToggleGroup();

        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());

        Button delete = new Button("Delete");
        delete.setOnAction(event -> {
            RadioButton radioButton = (RadioButton) group.getSelectedToggle();
            if (radioButton == null) {
                return;
            }

            Database_GUI.active_database.delete_table(radioButton.getText());
            //MainWindow.root.setCenter(MainWindow.display_tab());
            MainWindow.display_tab(Database_GUI.active_database);
            stage.close();

            String text = String.format("%-30s", "entity type deleted") +
                    "database: " +
                    Database_GUI.active_database.get_name() +
                    ", entity type name: " +
                    radioButton.getText();

            Logging.write_logging_information(text);
        });

        for (String table_name : Database_GUI.active_database.get_table_names()) {
            RadioButton radioButton = new RadioButton(table_name);
            radioButton.setToggleGroup(group);
            vBox.getChildren().add(radioButton);
            height += 23;
            radioButton.getStyleClass().add("body");
        }

        group.getToggles().get(0).setSelected(true);

        vBox.setSpacing(5.0);

        root.add(label, 0, 0, 3, 1);

        root.add(vBox, 0, 1, 3, 1);
        root.add(cancel, 1, 2);
        root.add(delete, 2, 2);

        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 300, height);

        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { delete.fire(); }});

        stage.setTitle("Delete Entity Type");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    static void show_all_entity_type_window(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }

        BorderPane borderPane = new BorderPane();

        VBox root = new VBox();
        int height = 100;

        Label label = new Label("Entity Types:\n\n");
        label.getStyleClass().add("body_bold");
        root.getChildren().add(label);

        for (String table : Database_GUI.active_database.get_table_names()) {
            Text text = new Text(table);
            text.getStyleClass().add("body");
            root.getChildren().add(text);
            height += 18;
        }

        root.setPadding(new Insets(10));

        Button ok = new Button("OK");
        ok.setOnAction(event -> {
            stage.close();
        });

        borderPane.setAlignment(ok, Pos.BOTTOM_RIGHT);

        borderPane.setCenter(root);
        borderPane.setBottom(ok);
        borderPane.setPadding(new Insets(0, 10, 10, 10));

        borderPane.setPrefWidth(300);

        Scene scene = new Scene(borderPane, 300, height);

        //add stylesheet
        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { ok.fire(); }});

        stage.setTitle("Show all");
        stage.setScene(scene);
        stage.setResizable(false);      //prevents user from changing window size
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);      //keep window focused
        stage.show();
    }

    //error dialogue in case no entity type exists (when deleting an entity type)
    static private boolean entity_type_exists() {
        if (Database_GUI.active_database.get_table_names().size() == 0) {   //check if entity type was created already
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can't delete entity type");
            alert.setContentText("You can not " + "delete" + " entity types if none exist.");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

}
