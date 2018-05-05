/*
this code was written by Roland (https://stackoverflow.com/users/1844265/roland)
and can be found at https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx
 */
package graphVisualization;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.EntityIdentifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cell extends Pane {

    static LinkedList<Color> colors = new LinkedList<>();
    static {

        colors.add(new Color(33.0/255,150.0/255,243.0/255, 1));
        colors.add(new Color(244.0/255,67.0/255,54.0/255, 1));
        colors.add(new Color(255.0/255,193.0/255,7.0/255, 1));
        colors.add(new Color(0.0/255,150.0/255,136.0/255, 1));
        colors.add(new Color(63.0/255,81.0/255,181.0/255, 1));
        colors.add(new Color(233.0/255,30.0/255,99.0/255, 1));
        colors.add(new Color(255.0/255,235.0/255,59.0/255, 1));
        colors.add(new Color(76.0/255,175.0/255,80.0/255, 1));
        colors.add(new Color(3.0/255,169.0/255,244.0/255, 1));
        colors.add(new Color(156.0/255,39.0/255,176.0/255, 1));
        colors.add(new Color(255.0/255,152.0/255,0.0/255, 1));
        colors.add(new Color(96.0/255,125.0/255,139.0/255, 1));
        colors.add(new Color(139.0/255,195.0/255,74.0/255, 1));
        colors.add(new Color(0.0/255,188.0/255,212.0/255, 1));
        colors.add(new Color(255.0/255,87.0/255,34.0/255, 1));
        colors.add(new Color(205.0/255,220.0/255,57.0/255, 1));
        colors.add(new Color(121.0/255,85.0/255,72.0/255, 1));
        colors.add(new Color(103.0/255,58.0/255,183.0/255, 1));
        colors.add(new Color(158.0/255,158.0/255,158.0/255, 1));

    }

    String cellId;

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;

    public Cell(String cellId, EntityIdentifier database_table_id) {
        this.cellId = cellId;
        this.setUserData(database_table_id);
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }

    public void select() {
        if (this instanceof RectangleCell) {
            RectangleCell rectangleCell = (RectangleCell)this;
            rectangleCell.select();
        }
    }
}
