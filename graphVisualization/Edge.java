/*
this code was written by Roland (https://stackoverflow.com/users/1844265/roland)
and can be found at https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx
 */
package graphVisualization;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Edge extends Group {

    protected Cell source;
    protected Cell target;

    Line line;
    Label label;

    public Edge(Cell source, Cell target, String description) {

        if (source == null || target == null) {
            return;
        }

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));


        label = new Label(description);
        label.layoutXProperty().bind((line.startXProperty().add(line.endXProperty())).divide(2));
        label.layoutYProperty().bind((line.startYProperty().add(line.endYProperty())).divide(2));

        getChildren().addAll(line, label);

    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

}