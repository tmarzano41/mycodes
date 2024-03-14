package application;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class BoardRendering extends Pane {
    private static final int CELL_SIZE = 31;

    public BoardRendering(int width, int height) {
        setPrefSize(width * CELL_SIZE, height * CELL_SIZE);
        drawBackground(width, height);
    }

    private void drawBackground(int width, int height) {
        // Draw background
        Rectangle background = new Rectangle(0, 0, width, height);
        background.setFill(Color.BLACK);
        getChildren().add(background);
    }

    public void renderSnake(ArrayList<Point2D> snake) {
        // Clear previous snake segments
        getChildren().removeIf(node -> node.getStyleClass().contains("snake-segment"));

        // Render snake segments
        for (Point2D segment : snake) {
            Ellipse segmentBall = new Ellipse(segment.getX(), segment.getY(), CELL_SIZE/2, CELL_SIZE/2);
            segmentBall.setFill(Color.GREEN);//you can mess with the colors
            segmentBall.getStyleClass().add("snake-segment");
            getChildren().add(segmentBall);
        }
    }

    public void renderApple(Point2D foodPosition) {
        // Render food item
    	getChildren().removeIf(node -> node.getStyleClass().contains("apple"));
        Rectangle foodRect = new Rectangle(foodPosition.getX(), foodPosition.getY(), CELL_SIZE, CELL_SIZE);
        foodRect.setFill(Color.RED);
        foodRect.getStyleClass().add("apple");
        getChildren().add(foodRect);
    }
}