package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.input.KeyCode;
import javafx.geometry.Point2D;

public class GameLogic {
	
    private int width;
    private int height;
    private List<Point2D> snake; // List of (x, y) coordinates representing snake segments
    private Point2D apple;
    private Random random;
    private Direction direction = null;
    private Direction previousDirection = null;
    private Direction[] directions = new Direction[2];
    private int score=0;
    private static final int CELL_SIZE=31;
    private static final double SPEED=3.1;
    
    public GameLogic(int width, int height) {
        this.width = width;
        this.height = height;
        this.snake = new ArrayList<>();
        this.random = new Random();
        snake.add(new Point2D(335.5,335.5));
        apple = generateApple();
        directions[0] = null;
    	directions[1] = null;
        // Initialize snake and food position
    }
    public List<Point2D> getSnake() {return new ArrayList<Point2D>(snake);}
    public Point2D getApple() {return apple;}
    public int getScore() {return score;}
    public void handleKeyPress(KeyCode keyCode) {
    	
    	if(previousDirection != direction)//1 is previous, 0 is now
    		directions[1] = directions[0];
    	
    	switch (keyCode) {
    	case UP:
    		direction = Direction.UP;
    		break;
    	case DOWN:
    		direction = Direction.DOWN;
    		break;
    	case LEFT:
    		direction = Direction.LEFT;
    		break;
    	case RIGHT:
    		direction = Direction.RIGHT;
    		break;
    	default:
    		break;
    	}
    	if (direction != null)
    		moveSnake(direction);
    		
    }
    public void moveSnake(Direction direction) {
        // Move the snake in the specified direction
    	Point2D head = snake.get(0);
    	double newX = head.getX();
    	double newY = head.getY();
        int framesLeft;
    	switch(direction) {
    		case UP:
    			newY-=SPEED;
    			break;
    		case DOWN:
    			newY+=SPEED;
    			break;
    		case LEFT:
    			newX-=SPEED;
    			break;
    		case RIGHT:
    			newX+=SPEED;
    			break;
    		default:
    			break;
    	}
    	
    	Point2D newHead = new Point2D(newX,newY);
    	snake.add(0,newHead);
    	if (eatApple())
            apple = generateApple();
        else
            snake.remove(snake.size() - 1);
    }
    /*if(directions[1] == Direction.LEFT) {
	framesLeft = (int) (((newX-25.5)%CELL_SIZE)/3.1);
	while(framesLeft!=0)
		newX-=SPEED;
	System.out.println(framesLeft);
	newY-=SPEED;
}
else if(directions[1] == Direction.LEFT)
	newX-=SPEED;
else if(directions[1] == Direction.RIGHT)
*/
    public boolean collision() {
        // Check for collisions with walls, snake itself, or food
        return false;
    }
    public boolean eatApple() {
    	if(-46.5<apple.getX()-snake.get(0).getX() 
		&& apple.getX()-snake.get(0).getX()<0 
		&& -46.5<apple.getY()-snake.get(0).getY() 
		&& apple.getY()-snake.get(0).getY()<0) {
    		score++;
    		return true;
    	}
    	else
    		return false;
    }
    public Point2D generateApple() {
    	Point2D newApple;
    	double x = random.nextInt(20)*CELL_SIZE+10;//might have to subtract cellsize so it doesn't go beyond borders
    	double y = random.nextInt(20)*CELL_SIZE+10;
    	newApple = new Point2D(x,y);
    	return newApple;
    }
    public void update() {
    	if(direction!=null)
    		moveSnake(direction);
    	collision();
    	eatApple();
        // Update the game state
        // Move snake, check collisions, update scores, etc.
    }

    // Define methods for getting game state, scores, etc.
}