package application;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid extends Pane {
    private int rows;
    private int columns;
    private double cellWidth;
    private double cellHeight;
    private double width;
    private double height;

    public Grid(int rows, int columns, double width, double height) {
        this.rows = rows;
        this.columns = columns;
        this.width = width;
        this.height = height;
        this.cellWidth = width / columns;
        this.cellHeight = height / rows;
        drawGrid();
    }

    private void drawGrid() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                Rectangle cell = new Rectangle(c * cellHeight+10, r * cellHeight+10, cellWidth, cellHeight);
                cell.setFill(Color.TRANSPARENT);
                cell.setStroke(Color.WHITE);
                getChildren().add(cell);
            }
        }
    }
}