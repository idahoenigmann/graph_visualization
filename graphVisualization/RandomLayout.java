/*
this code was written by Roland (https://stackoverflow.com/users/1844265/roland)
and can be found at https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx
 */
package graphVisualization;

import main.MainWindow;

import java.util.List;
import java.util.Random;

public class RandomLayout extends Layout {

    Graph graph;

    Random rnd = new Random();

    public RandomLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            //set area where the cells can be created
            double window_width = MainWindow.getSceneWidth() - 200;
            double window_height = MainWindow.getSceneHeight() - 200;
            double x = rnd.nextDouble() * window_width;
            double y = rnd.nextDouble() * window_height;

            cell.relocate(x, y);    //move cell to given location

        }

    }

}