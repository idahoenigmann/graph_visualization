/*
this code was written by Roland (https://stackoverflow.com/users/1844265/roland)
and can be found at https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx
 */
package graphVisualization;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Database_GUI;
import main.EntityIdentifier;


public class RectangleCell extends Cell {

    public RectangleCell( String id, EntityIdentifier database_table_id) {
        super( id, database_table_id);

        view = new Rectangle( 50,50);

        int color_index = Database_GUI.active_database.get_table_names().indexOf(database_table_id.getTable());
        if (color_index > colors.size() || color_index < 0) {
            color_index = 0;
        }
        color = colors.get(color_index);

        view.setStrokeWidth(3.0);
        view.setStroke(color);
        view.setFill(color);

        setView( view);

    }

    public void select() {
        view.setStroke(color.darker());
    }

    public void deselect() {
        view.setStroke(color);
    }

    Rectangle view;
    Color color;

}