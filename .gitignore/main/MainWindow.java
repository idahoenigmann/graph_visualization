package main;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;

public class MainWindow {
    static void main_window() {
        stage = new Stage();
        scene = new Scene(root, 800, 700);

        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());
        //root.setTop(create_toolbar(stage));
        root.setTop(create_menubar());

        display_entity_attributes( null,1);

        TabPane tabPane = create_tab();

        Database_GUI.close_window_on_esc(scene, stage);

        root.setCenter(tabPane);
        scene.setRoot(root);

        //setting the title and display the window
        stage.setTitle("Bla Blub Bla");
        stage.setScene(scene);
        stage.show();

    }

    private static MenuBar create_menubar() {
        MenuBar menuBar = new MenuBar();

        Menu database = new Menu("_Database");
        MenuItem create_database = new MenuItem("_Create", Database_GUI.create_image("createDB.png", 20, 20));
        create_database.setOnAction(event -> {
            DBWindows.create_db_window(new Stage());
            //MainWindow.stage = stage;
        });
        create_database.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        MenuItem open_database = new MenuItem("_Open", Database_GUI.create_image("openDB.png", 20, 20));
        open_database.setOnAction(event -> DBWindows.open_db_window(new Stage()));
        open_database.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        MenuItem delete_database = new MenuItem("_Delete", Database_GUI.create_image("deleteDB.png", 20, 20));
        delete_database.setOnAction(event -> DBWindows.delete_db_window(new Stage()));
        delete_database.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        MenuItem import_database = new MenuItem("_Import CSV");
        import_database.setOnAction(event -> DBWindows.import_db_window(new Stage()));
        MenuItem export_database = new MenuItem("_Export CSV");
        export_database.setOnAction(event -> DBWindows.export_db_window(new Stage()));
        database.getItems().addAll(create_database, open_database, delete_database, import_database, export_database);


        Menu entity_type = new Menu("Entity _Type");
        MenuItem create_entity_type = new MenuItem("_Create", Database_GUI.create_image("createEntityType.png", 20, 20));
        create_entity_type.setOnAction(event -> EntityTypeWindows.create_entity_type_window(new Stage()));
        create_entity_type.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        MenuItem delete_entity_type = new MenuItem("_Delete", Database_GUI.create_image("deleteEntityType.png", 20, 20));
        delete_entity_type.setOnAction(event -> EntityTypeWindows.delete_entity_type_window(new Stage()));
        MenuItem show_entity_type = new MenuItem("_Show all");
        show_entity_type.setOnAction(event -> EntityTypeWindows.show_all_entity_type_window(new Stage()));
        entity_type.getItems().addAll(create_entity_type, delete_entity_type, show_entity_type);

        Menu entity = new Menu("_Entity");
        MenuItem create_entity = new MenuItem("_Create", Database_GUI.create_image("createEntity.png", 20, 20));
        create_entity.setOnAction(event -> EntityWindows.create_entity_window(new Stage()));
        create_entity.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        MenuItem modify_entity = new MenuItem("_Modify", Database_GUI.create_image("modifyEntity.png", 20, 20));
        modify_entity.setOnAction(event -> EntityWindows.modify_entity_window1(new Stage()));
        MenuItem delete_entity = new MenuItem("_Delete", Database_GUI.create_image("deleteEntity.png", 20, 20));
        delete_entity.setOnAction(event -> EntityWindows.delete_entity_window(new Stage()));
        entity.getItems().addAll(create_entity, modify_entity, delete_entity);

        Menu relationship = new Menu("_Relationship");
        MenuItem create_relationship = new MenuItem("_Create", Database_GUI.create_image("createRelationship.png", 20, 20));
        create_relationship.setOnAction(event -> RelationshipWindows.create_relationship_window(new Stage()));
        create_relationship.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        /*
        MenuItem modify_relationship = new MenuItem("_Modify", Database_GUI.create_image("modifyRelationship.png", 20, 20));
        modify_relationship.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        */
        MenuItem delete_relationship = new MenuItem("_Delete", Database_GUI.create_image("deleteRelationship.png", 20, 20));
        delete_relationship.setOnAction(event -> RelationshipWindows.delete_relationship_window(new Stage()));
        //relationship.getItems().addAll(create_relationship, modify_relationship, delete_relationship);
        relationship.getItems().addAll(create_relationship, delete_relationship);

        Menu help = new Menu("_Help");
        MenuItem about = new MenuItem("_About");
        about.setOnAction(event -> HelpWindow.about_window(new Stage()));
        MenuItem getting_started = new MenuItem("_Getting Started");
        getting_started.setOnAction(event -> HelpWindow.getting_started_window(new Stage()));
        help.getItems().addAll(about, getting_started);

        menuBar.getMenus().addAll(database, entity_type, entity, relationship, help);

        return menuBar;
    }

    static void display_entity_attributes(String entity, int entity_id) {
        if (entity == null) {
            return;
        }

        LinkedList<String> name_of_attributes = (LinkedList<String>)Database_GUI.active_database.get_attribute_name(entity);

        if (!Database_GUI.active_database.get_valid_ids(entity).contains(entity_id)) {
            throw new RuntimeException("entity_id does not exist.");
        }

        LinkedList<String> attributes = (LinkedList<String>)Database_GUI.active_database.get_attributes(entity, entity_id);

        TextFlow textFlow = new TextFlow();

        Text text = new Text(entity + "\n\n");
        text.getStyleClass().add("title");
        textFlow.getChildren().add(text);

        for (int i=0; i < name_of_attributes.size(); i++) {
            Text name = new Text(name_of_attributes.get(i) + ":");
            Text value = new Text(attributes.get(i));
            Text new_line = new Text("\n");
            Text new_attribute = new Text("\n\n");

            name.getStyleClass().add("body_bold");
            value.getStyleClass().add("body");
            new_line.getStyleClass().add("body");
            new_attribute.getStyleClass().add("body");
            textFlow.getChildren().addAll(name, new_line, value, new_attribute);
        }
        textFlow.setLineSpacing(3.0);
        textFlow.setPrefWidth(100.0);
        textFlow.setPadding(new Insets(0, 0, 0, 5));

        root.setLeft(textFlow);
    }

    static void display_tab(Database database) {
        int idx = 0;
        for (Tab t : tabPane.getTabs()) {
            if (t.getUserData().equals(database)) {
                break;
            }
            idx++;
        }

        Tab tab = tabPane.getTabs().get(idx);

        tab.setContent(DatabaseVisualization.display_database(Database_GUI.active_database));

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(tab);
    }

    static TabPane create_tab() {
        Tab tab = new Tab();

        tab.setText(Database_GUI.active_database.get_name());
        tab.setUserData(Database_GUI.active_database);
        tab.setContent(DatabaseVisualization.display_database(Database_GUI.active_database));

        tabPane.getTabs().add(tab);

        tab.setOnCloseRequest(event -> Database_GUI.databases.remove(Database_GUI.active_database));

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(tab);

        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            if (tabPane.getTabs().size() > 0) {
                Database_GUI.active_database = (Database) newValue.getUserData();
            } else {
                Database_GUI.active_database = null;
            }
            root.setLeft(null);
        });

        return tabPane;
    }

    static void close_tab(Database database) {
        Tab close_tab = new Tab();
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(database.get_name())) {
                close_tab = tab;
            }
        }
        tabPane.getTabs().remove(close_tab);
        Database_GUI.databases.remove(database);
    }

    public static double getSceneWidth() {
        return scene.getWidth();
    }

    public static double getSceneHeight() {
        return scene.getHeight();
    }

    static BorderPane root = new BorderPane();
    private static TabPane tabPane = new TabPane();
    static Stage stage = null;
    public static Scene scene = null;
}
