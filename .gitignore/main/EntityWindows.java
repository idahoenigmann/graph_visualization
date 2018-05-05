package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;

class EntityWindows {
    static void create_entity_window(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }

        if (!EntityWindows.entity_type_exists("create")) {
            return;
        }

        GridPane root = new GridPane();
        int height = 130;

        Label entity_type_label = new Label("Entity Type: ");
        entity_type_label.getStyleClass().add("body");

        ObservableList<String> entity_type_names = FXCollections.observableArrayList();
        entity_type_names.addAll(Database_GUI.active_database.get_table_names());

        Spinner<String> entity_type = new Spinner<>(entity_type_names);
        entity_type.getStyleClass().add("body");


        Label property_name_label = new Label("Property Name:");
        property_name_label.getStyleClass().add("body");
        Label value_label = new Label("Value:");
        value_label.getStyleClass().add("body");

        VBox properties = new VBox();
        for (String property : Database_GUI.active_database.get_attribute_name(entity_type.getValue())) {
            Label label = new Label(property);
            properties.getChildren().add(label);
            label.getStyleClass().add("body");
            label.getStyleClass().add("entity_table");
            height += 29;

        }

        LinkedList<TextField> textFields = new LinkedList<>();

        VBox values = new VBox();
        for (int i=0; i < properties.getChildren().size(); i++) {
            TextField textField = new TextField();
            textField.getStyleClass().add("body");
            values.getChildren().add(textField);
            textFields.add(textField);
        }

        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());

        Button create = new Button("Create");
        create.setOnAction(event -> {
            LinkedList<String> attributes = new LinkedList<>();
            VBox vBox = new VBox();
            for (Node node : root.getChildren()) {
                if (GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 2) {
                    vBox = (VBox)node;
                }
            }

            for (Node node : vBox.getChildren()) {
                TextField textField = (TextField)node;
                attributes.add(textField.getText());
            }

            Database_GUI.active_database.create_entity(entity_type.getValue(), attributes);
            //MainWindow.root.setCenter(MainWindow.display_tab());
            MainWindow.display_tab(Database_GUI.active_database);
            stage.close();

            StringBuilder text = new StringBuilder();

            text.append(String.format("%-30s", "new entity created"));
            text.append("database: ");
            text.append(Database_GUI.active_database.get_name());

            text.append(", entity type: ");
            text.append(entity_type.getValue());

            text.append(", values: (");
            for (String attribute : attributes) {
                text.append(attribute);
                text.append(", ");
            }
            text.delete(text.length()-2, text.length());
            text.append(")");

            Logging.write_logging_information(text.toString());
        });

        Database_GUI.disable_button_if_text_empty(textFields, create);

        root.add(entity_type_label, 0, 0);
        root.add(entity_type, 1, 0, 2, 1);
        root.add(property_name_label, 0, 1);
        root.add(value_label, 1, 1, 2, 1);
        root.add(properties, 0, 2);
        root.add(values, 1,2, 2, 1);
        root.add(cancel, 1, 3);
        root.add(create, 2, 3);

        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));

        root.setAlignment(Pos.CENTER);

        entity_type.valueProperty().addListener((observable, oldValue, newValue) -> {
            stage.setHeight(155);

            LinkedList<TextField> textFields1 = new LinkedList<>();

            VBox new_properties = new VBox();
            for (String property : Database_GUI.active_database.get_attribute_name(entity_type.getValue())) {
                Label label = new Label(property);
                label.getStyleClass().add("body");
                label.getStyleClass().add("entity-table");
                new_properties.getChildren().add(label);
                stage.setHeight(stage.getHeight() + 29);
            }
            new_properties.setSpacing(10);

            VBox new_values = new VBox();
            for (int i=0; i < new_properties.getChildren().size(); i++) {
                TextField textField = new TextField();
                textField.getStyleClass().add("body");
                new_values.getChildren().add(textField);
                textFields1.add(textField);
            }

            Database_GUI.disable_button_if_text_empty(textFields1, create);

            root.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 2);
            root.add(new_properties, 0, 2);
            root.add(new_values, 1, 2, 2, 1);
        });

        Scene scene = new Scene(root, 300, height);

        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { create.fire(); }});

        stage.setTitle("Create Entity");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    static void delete_entity_window(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }

        if (!EntityWindows.entity_type_exists("delete")) {
            return;
        }

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 200);

        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Label entity_type_label = new Label("Entity Type: ");

        ObservableList<String> entity_type_names = FXCollections.observableArrayList();
        for (String table : Database_GUI.active_database.get_table_names()) {
            if (Database_GUI.active_database.get_row_count(table) > 0) {
                entity_type_names.add(table);
            }
        }

        Spinner<String> entity_type = new Spinner<>(entity_type_names);

        Label property_name_label = new Label("Property Name:");
        Label value_label = new Label("Value:");

        cnt_in_table = 0;
        EntityWindows.display_entity_table(root, Database_GUI.active_database, entity_type.getValue());

        entity_type.valueProperty().addListener((observable, oldValue, newValue) -> {
            cnt_in_table = 0;
            EntityWindows.display_entity_table(root, Database_GUI.active_database, newValue);
        });

        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());

        Button delete = new Button("Delete");
        delete.setOnAction(event -> {
            int id = Database_GUI.active_database.get_valid_ids(entity_type.getValue()).get(cnt_in_table);
            Database_GUI.active_database.delete_entity(entity_type.getValue(), id);
            //MainWindow.root.setCenter(MainWindow.display_tab());
            MainWindow.display_tab(Database_GUI.active_database);
            stage.close();

            String text = String.format("%-30s", "entity deleted") +
                    "database: " +
                    Database_GUI.active_database.get_name() +
                    ", entity type: " +
                    entity_type.getValue() +
                    ", id: " +
                    id;

            Logging.write_logging_information(text);
        });

        root.add(entity_type_label, 0, 0);
        root.add(entity_type, 1, 0, 2, 1);
        root.add(property_name_label, 0, 1);
        root.add(value_label, 1, 1, 2, 1);
        root.add(cancel, 1, 4);
        root.add(delete, 2, 4);

        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));

        root.setAlignment(Pos.CENTER);

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { delete.fire(); }});

        stage.setTitle("Delete Entity");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    static void modify_entity_window1(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }

        if (!EntityWindows.entity_type_exists("modify")) {
            return;
        }

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 200);

        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Label entity_type_label = new Label("Entity Type: ");

        ObservableList<String> entity_type_names = FXCollections.observableArrayList();
        for (String table : Database_GUI.active_database.get_table_names()) {
            if (Database_GUI.active_database.get_row_count(table) > 0) {
                entity_type_names.add(table);
            }
        }

        Spinner<String> entity_type = new Spinner<>(entity_type_names);

        Label property_name_label = new Label("Property Name:");
        Label value_label = new Label("Value:");

        cnt_in_table = 0;
        EntityWindows.display_entity_table(root, Database_GUI.active_database, entity_type.getValue());

        entity_type.valueProperty().addListener((observable, oldValue, newValue) -> {
            cnt_in_table = 0;
            EntityWindows.display_entity_table(root, Database_GUI.active_database, newValue);
        });

        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());

        Button modify = new Button("Modify");
        modify.setOnAction(event -> {
            LinkedList<String> old_values = new LinkedList<>();
            for (Node node : root.getChildren()) {
                if (GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 2) {
                    VBox vBox = (VBox) node;
                    for (Node node_text : vBox.getChildren()) {
                        Label label = (Label) node_text;
                        old_values.add(label.getText());
                    }
                }
            }

            modify_entity_window2(stage, entity_type.getValue(), old_values);
        });

        root.add(entity_type_label, 0, 0);
        root.add(entity_type, 1, 0, 2, 1);
        root.add(property_name_label, 0, 1);
        root.add(value_label, 1, 1, 2, 1);
        root.add(cancel, 1, 4);
        root.add(modify, 2, 4);

        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));

        root.setAlignment(Pos.CENTER);

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { modify.fire(); }});

        stage.setTitle("Modify Entity");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    static private void modify_entity_window2(Stage stage, String entity_type_name, LinkedList<String> old_values) {
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 200);

        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Label entity_type_label = new Label("Entity Type: ");

        Label entity_type = new Label(entity_type_name);

        Label property_name_label = new Label("Property Name:");
        Label value_label = new Label("Value:");

        VBox properties = new VBox();
        for (String property : Database_GUI.active_database.get_attribute_name(entity_type_name)) {
            properties.getChildren().add(new Label(property));
        }

        VBox values = new VBox();
        for (int i=0; i < properties.getChildren().size(); i++) {
            TextField textField = new TextField(old_values.get(i));
            values.getChildren().add(textField);
        }


        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());

        Button modify = new Button("Modify");
        modify.setOnAction(event -> {
            LinkedList<String> attributes = new LinkedList<>();
            VBox vBox = new VBox();
            for (Node node : root.getChildren()) {
                if (GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 2) {
                    vBox = (VBox)node;
                }
            }

            for (Node node : vBox.getChildren()) {
                TextField textField = (TextField)node;
                attributes.add(textField.getText());
            }
            int id = Database_GUI.active_database.get_valid_ids(entity_type_name).get(cnt_in_table);
            Database_GUI.active_database.update_entity(entity_type_name, id, attributes);

            //Database_GUI.active_database.delete_entity(entity_type_name, Database_GUI.active_database.get_valid_ids(entity_type_name).get(cnt_in_table));
            //Database_GUI.active_database.create_entity(entity_type_name, attributes);
            //MainWindow.root.setCenter(MainWindow.display_tab());
            MainWindow.display_tab(Database_GUI.active_database);
            stage.close();

            String text = String.format("%-30s", "entity modified") +
                    "database: " +
                    Database_GUI.active_database.get_name() +
                    ", entity type: " +
                    entity_type_name +
                    ", id: " +
                    id;

            Logging.write_logging_information(text);
        });

        root.add(entity_type_label, 0, 0);
        root.add(entity_type, 1, 0, 2, 1);
        root.add(property_name_label, 0, 1);
        root.add(value_label, 1, 1, 2, 1);
        root.add(properties, 0, 2);
        root.add(values, 1,2, 2, 1);
        root.add(cancel, 1, 3);
        root.add(modify, 2, 3);

        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));

        root.setAlignment(Pos.CENTER);

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { modify.fire(); }});

        stage.setResizable(false);
        stage.setTitle("Modify Entity");
        stage.setScene(scene);
        stage.show();
    }

    static void display_entity_table(GridPane gridPane, Database database, String table) {
        LinkedList<Integer> valid_ids = (LinkedList<Integer>) database.get_valid_ids(table);

        VBox properties = new VBox();
        for (String property : database.get_attribute_name(table)) {
            properties.getChildren().add(new Label(property));
        }

        VBox values = new VBox();
        for (String text : database.get_attributes(table, valid_ids.get(cnt_in_table))) {
            values.getChildren().add(new Label(text));
            RelationshipWindows.height += 16;
        }

        Button next = new Button(">");
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cnt_in_table++;
            cnt_in_table = cnt_in_table % valid_ids.size();

            display_entity_table(gridPane, database, table);
        });


        Button prev = new Button("<");
        prev.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cnt_in_table--;
            if (cnt_in_table < 0) {
                cnt_in_table = valid_ids.size()-1;
            }
            display_entity_table(gridPane, database, table);
        });

        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 2);
        gridPane.add(properties, 0, 2);
        gridPane.add(values, 1, 2, 2, 1);

        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 3);
        gridPane.add(prev, 1, 3);
        gridPane.add(next, 2, 3);
    }

    static void display_entity_table2(GridPane gridPane, Database database, String table) {
        LinkedList<Integer> valid_ids = (LinkedList<Integer>) database.get_valid_ids(table);

        VBox properties = new VBox();
        for (String property : database.get_attribute_name(table)) {
            properties.getChildren().add(new Label(property));
        }

        VBox values = new VBox();
        for (String text : database.get_attributes(table, valid_ids.get(cnt_in_table2))) {
            values.getChildren().add(new Label(text));
            RelationshipWindows.height += 16;
        }

        Button next = new Button(">");
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cnt_in_table2++;
            cnt_in_table2 = cnt_in_table2 % valid_ids.size();

            display_entity_table2(gridPane, database, table);
        });


        Button prev = new Button("<");
        prev.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cnt_in_table2--;
            if (cnt_in_table2 < 0) {
                cnt_in_table2 = valid_ids.size()-1;
            }
            display_entity_table2(gridPane, database, table);
        });

        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 2);
        gridPane.add(properties, 0, 2);
        gridPane.add(values, 1, 2, 2, 1);

        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 3);
        gridPane.add(prev, 1, 3);
        gridPane.add(next, 2, 3);
    }

    static private boolean entity_type_exists(String verb) {
        if (Database_GUI.active_database.get_table_names().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can't " + verb + " entity");
            alert.setContentText("You can not " + verb + " entities if there exists\nno entity type.\n\nPlease create an entity type first.");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    //static int id = 1;
    static int cnt_in_table = 0;
    static int cnt_in_table2 = 0;
}
