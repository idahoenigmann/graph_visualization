package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;

class RelationshipWindows {
    static void create_relationship_window(Stage stage) {
        RelationshipWindows.stage = stage;
        if (Database_GUI.no_database_exists()) {
            return;
        }

        if (!entity_exists()) { //error dialogue in case no entity exists
            return;
        }

        VBox root = new VBox();     //provides basic structure

        root.getChildren().add(choose_entity1());   //enables input of the first entity

        Label description_label = new Label("Description: ");
        TextField description = new TextField();    //input of description
        root.getChildren().addAll(description_label, description);

        root.getChildren().add(choose_entity2());   //enables input of the second entity

        //cancel button, close window
        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());

        //create button, new data record in table relationship
        Button create = new Button("Create");
        create.setOnAction(event -> {
            //specify first entity by the values selected
            EntityIdentifier entity1 = new EntityIdentifier(entity_type1.getValue(),
                    Database_GUI.active_database.get_valid_ids(entity_type1.getValue()).get(EntityWindows.cnt_in_table));
            //specify second entity by the values selected
            EntityIdentifier entity2 = new EntityIdentifier(entity_type2.getValue(),
                    Database_GUI.active_database.get_valid_ids(entity_type2.getValue()).get(EntityWindows.cnt_in_table2));
            if (!same_entities(entity1, entity2)) {     //check if the two entities are equal and display error dialogue if so
                Database_GUI.active_database.create_relationship(entity1, entity2, description.getText());    //modify table relationship
                //MainWindow.root.setCenter(MainWindow.display_tab());       //redraw main window
                MainWindow.display_tab(Database_GUI.active_database);       //redraw main window
            }
            stage.close();

            String text = String.format("%-30s", "new relationship created") +
                    "database: " +
                    Database_GUI.active_database.get_name() +
                    ", entity1: " +
                    entity1.toString() +
                    ", entity2: " +
                    entity2.toString() +
                    ", description: " +
                    description.getText();

            Logging.write_logging_information(text);
        });

        LinkedList<TextField> textFields = new LinkedList<>();
        textFields.add(description);
        Database_GUI.disable_button_if_text_empty(textFields, create);      //disable create button if nothing is entered in description

        HBox hBox = new HBox();
        hBox.getChildren().addAll(cancel, create);
        hBox.setSpacing(10);
        root.getChildren().add(hBox);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);

        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 300, 345 + height_entity1 + height_entity2);

        //add stylesheet
        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { create.fire(); }});

        stage.setTitle("Create Relationship");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);      //always prioritize this window
        stage.show();
    }

    static private GridPane choose_entity1() {
        GridPane gridPane = new GridPane();

        Label entity_type_label = new Label("Entity Type: ");

        ObservableList<String> entity_type_names = FXCollections.observableArrayList();
        for (String table : Database_GUI.active_database.get_table_names()) {
            if (Database_GUI.active_database.get_row_count(table) > 0) {
                entity_type_names.add(table);
            }
        }

        entity_type1 = new Spinner<>(entity_type_names);        //spinner for choosing the entity type

        Label property_name_label = new Label("Property Name:");
        Label value_label = new Label("Value:");

        height = 0;
        EntityWindows.cnt_in_table = 0;
        EntityWindows.display_entity_table(gridPane, Database_GUI.active_database, entity_type1.getValue());
        height_entity1 = height;

        entity_type1.valueProperty().addListener((observable, oldValue, newValue) -> {
            height = 0;
            EntityWindows.cnt_in_table = 0;
            EntityWindows.display_entity_table(gridPane, Database_GUI.active_database, newValue);       //update displayed values
            height_entity1 = height;
            stage.setHeight(370 + height_entity1 + height_entity2);

        });

        gridPane.add(entity_type_label, 0, 0);
        gridPane.add(entity_type1, 1, 0, 2, 1);
        gridPane.add(property_name_label, 0, 1);
        gridPane.add(value_label, 1, 1, 2, 1);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        return gridPane;
    }

    static private GridPane choose_entity2() {
        GridPane gridPane = new GridPane();     //provide basic structure

        Label entity_type_label = new Label("Entity Type: ");

        ObservableList<String> entity_type_names = FXCollections.observableArrayList();
        for (String table : Database_GUI.active_database.get_table_names()) {
            if (Database_GUI.active_database.get_row_count(table) > 0) {
                entity_type_names.add(table);
            }
        }

        entity_type2 = new Spinner<>(entity_type_names);       //spinner for choosing the entity type

        Label property_name_label = new Label("Property Name:");
        Label value_label = new Label("Value:");

        EntityWindows.cnt_in_table2 = 0;

        height = 0;
        EntityWindows.display_entity_table2(gridPane, Database_GUI.active_database, entity_type2.getValue());
        height_entity2 = height;

        entity_type2.valueProperty().addListener((observable, oldValue, newValue) -> {
            height = 0;
            EntityWindows.cnt_in_table2 = 0;
            EntityWindows.display_entity_table2(gridPane, Database_GUI.active_database, newValue);       //update displayed values
            height_entity2 = height;
            stage.setHeight(370 + height_entity1 + height_entity2);
        });


        gridPane.add(entity_type_label, 0, 0);
        gridPane.add(entity_type2, 1, 0, 2, 1);
        gridPane.add(property_name_label, 0, 1);
        gridPane.add(value_label, 1, 1, 2, 1);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        return gridPane;
    }

    static void delete_relationship_window(Stage stage) {
        if (Database_GUI.no_database_exists()) {
            return;
        }

        if (!relationship_exists()) {       //check if a relationship exists and display error
            return;
        }

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 200);

        //add stylesheet
        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        //get list of all relationships
        LinkedList<RelationshipIdentifier> relationships = new LinkedList<>();
        for (int i : Database_GUI.active_database.get_valid_ids("relationship")) {
            LinkedList<String> attributes = (LinkedList<String>) Database_GUI.active_database.get_attributes("relationship", i);
            EntityIdentifier entity1 = new EntityIdentifier(attributes.get(0));
            EntityIdentifier entity2 = new EntityIdentifier(attributes.get(1));
            String description = attributes.get(2);
            RelationshipIdentifier relationshipIdentifier = new RelationshipIdentifier(entity1, entity2, description, i);
            relationships.add(relationshipIdentifier);
        }


        //create a radiobutton for each relationship
        VBox vBox = new VBox();
        ToggleGroup group = new ToggleGroup();
        for (RelationshipIdentifier relationship : relationships) {
            RadioButton radioButton = new RadioButton(relationship.toString() + "\n");
            radioButton.getStyleClass().add("body");

            radioButton.setToggleGroup(group);
            radioButton.setUserData(relationship.getId());
            vBox.getChildren().add(radioButton);
        }
        group.getToggles().get(0).setSelected(true);


        //close window
        Button cancel = new Button("Cancel");
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> stage.close());


        //delete relationship from the database
        Button delete = new Button("Delete");
        delete.setOnAction(event -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            Database_GUI.active_database.delete_entity("relationship", (int)group.getSelectedToggle().getUserData());
            //MainWindow.root.setCenter(MainWindow.display_tab());
            MainWindow.display_tab(Database_GUI.active_database);
            stage.close();

            String text = String.format("%-30s", "relationship deleted") +
                    "database: " +
                    Database_GUI.active_database.get_name() +
                    ", id: " +
                    selected.getUserData();

            Logging.write_logging_information(text);
        });

        root.add(vBox, 0, 0, 3, 1);
        root.add(cancel, 1, 1);
        root.add(delete, 2, 1);

        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));

        root.setAlignment(Pos.CENTER);      //center text

        Database_GUI.close_window_on_esc(scene, stage);

        scene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.ENTER) { delete.fire(); }});

        stage.setTitle("Delete Relationship");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);      //prioritize window over main window
        stage.show();
    }

    static private boolean entity_exists() {
        int entity_num = 0;
        for (String table : Database_GUI.active_database.get_table_names()) {
            entity_num += Database_GUI.active_database.get_row_count(table);
        }
        if (entity_num < 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can't create relationship");
            alert.setContentText("You can not create relationships if\nless than two entities exist.");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    static private boolean relationship_exists() {
        if (Database_GUI.active_database.get_row_count("relationship") == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can't delete relationship!");
            alert.setContentText("Can not delete relationships when\nno relationship exists.");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    static private boolean same_entities(EntityIdentifier e1, EntityIdentifier e2) {
        if (e1.equals(e2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can't create relationship!");
            alert.setContentText("Can not create relationships\nbetween two equal entities.");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private static Spinner<String> entity_type1 = null;
    private static Spinner<String> entity_type2 = null;
    static int height_entity1;
    static int height_entity2;
    static int height;
    private static Stage stage;

}
