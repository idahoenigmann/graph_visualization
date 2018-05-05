package main;

import graphVisualization.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

class DatabaseVisualization {
    //wrapper method for addGraphComponents
    static BorderPane display_database(Database database) {
        //initializes everything needed to display the rectangles and lines
        BorderPane borderPane = new BorderPane();

        Graph graph = new Graph();

        borderPane.setCenter(graph.getScrollPane());

        addGraphComponents(graph, database);

        Layout layout = new RandomLayout(graph);
        layout.execute();

        return borderPane;
    }

    //draws the entities(rectangles) and the relationships(lines)
    private static void addGraphComponents(Graph graph, Database database) {

        Model model = graph.getModel();

        graph.beginUpdate();

        for (String table : database.get_table_names()) {
            for (int id : database.get_valid_ids(table)) {
                EntityIdentifier entityIdentifier = new EntityIdentifier(table, id);
                RectangleCell cell = new RectangleCell(entityIdentifier.toString(), entityIdentifier);
                cell.setOnMouseClicked(event -> {          //handler for double clicks
                    if(event.getButton().equals(MouseButton.PRIMARY)){      //mouse was clicked
                        if (event.getClickCount() == 2) {       //double click occurred
                            Object obj = event.getSource();
                            if (obj instanceof Cell) {
                                graph.getModel().deselectAllCells();        //remove border from all cells
                                Cell c = (Cell)obj;
                                c.select();         //add border to clicked cell
                                EntityIdentifier identifier = (EntityIdentifier) c.getUserData();

                                //show information of the entity
                                MainWindow.display_entity_attributes(identifier.getTable(), identifier.getId());
                            }
                        }
                    }
                });
                model.addCell(cell);        //add all cells to the new model
            }
        }

        for (int id : database.get_valid_ids("relationship")) {
            //draw all lines / relationships / edges
            model.addEdge(database.get_attributes("relationship", id).get(0),
                    database.get_attributes("relationship", id).get(1),
                    database.get_attributes("relationship", id).get(2));
        }

        graph.endUpdate();

    }
}
