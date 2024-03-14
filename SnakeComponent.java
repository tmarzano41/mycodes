import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
//import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
/*
* Name: Tyler Marzano
* Date: May 8, 2019
* Program Name: SnakeComponent.java
* Description:
*/
public class SnakeComponent extends JComponent implements ActionListener {
	
	private Dimension d = new Dimension(801, 641);
	private final static int BALL_RADIUS = 20;
	private Image apple;
	private Image sf;
	Random ran = new Random();
	private int dx = 0;
	private int dy = 0;
	private int appleX = ran.nextInt(16)*40;
	private int appleY = ran.nextInt(16)*40;
	private int score;
	//private Timer timer;
	private final static int APPLE_HEIGHT = 40;
	private final static int APPLE_WIDTH = 40;
	private boolean gameover = false;
	ArrayList<Ellipse2D> snake = new ArrayList<Ellipse2D>();
	private ArrayList<Color> colors;
	public boolean 
	leftPressed = false, 
	rightPressed = false, 
	upPressed = false, 
	downPressed = false, 
	paused = false, 
	prevPaused = false,
	thing = false;
	private int ax = d.width/2; //snake head location
	private int ay = d.height/2;
	int red = (int)(Math.random()*256);
	int blue = (int)(Math.random()*256);
	int green = (int)(Math.random()*256);
	int pd;
	int pausedDX;
	int pausedDY;
	double nextX1, nextX2, nextX3, nextY1, nextY2, nextY3, currentX, currentY, headX, headY;
	public void init()
	{
		snake.add(new Ellipse2D.Double(ax, ay, BALL_RADIUS*2, BALL_RADIUS*2));
		snake.add(new Ellipse2D.Double(ax, ay, BALL_RADIUS*2, BALL_RADIUS*2));
		colors = new ArrayList<>();
		colors.add(Color.GREEN);
		colors.add(Color.GREEN);
	}
	public SnakeComponent()
	{
		super();
		this.setPreferredSize(d);	
		init();
		ImageIcon icon=new ImageIcon(this.getClass().getResource("CS Apple.png"));
		ImageIcon snakeFace=new ImageIcon(this.getClass().getResource("CS Snake.png"));
		apple = icon.getImage();
		sf = snakeFace.getImage();
		//timer = new Timer(1000/60, this);
		//timer.start();
	}
	
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GREEN);
		g2.draw(snake.get(0)); //draws the front ball
		g2.fill(snake.get(0));//fills the front ball
		g2.setFont(new Font("Dialog", Font.PLAIN, 100));
		g2.drawString(score + "", d.width/2-29, d.height/8);
		g2.setColor(Color.WHITE);
		for(int x = 0; x<=d.width; x+=40) //creates the grid
			g2.drawLine(x, 0, x, d.height);
		for(int y = 0; y<=d.height; y+=40)
			g2.drawLine(0, y, d.width, y);
		g2.drawImage(apple, appleX, appleY, APPLE_WIDTH, APPLE_HEIGHT, this); //draws apple
		g2.drawImage(sf, ax, ay, BALL_RADIUS*2, BALL_RADIUS*2, this); //draws snake head have it track snake location
		g2.setColor(Color.WHITE);
		if(gameover)
				g2.drawString("Game Over", 100, 400); 
		if(paused)
			g2.drawString("Paused", 100, 400);
		//prints game over when the snake collides with the wall or itself
		for(int i = snake.size()-1; i>=0; i--) {
			g2.setColor(colors.get(i));
			g2.draw(snake.get(i));
			g2.fill(snake.get(i));
		}
		if(thing)//debugging tool
			g2.drawString("active", 100, 400);
	}
	
	public void start(){
	while(true)
	{
		//call methods here
		moveSnake();
		controls();
		collision();
		gameover();
		try{
			Thread.sleep((long) (1000/60.0));
		}
		catch(InterruptedException e) {}
		repaint();
	}
	
	}
	public void relocateApple() //randomizes where the apple puts itself
	{
		int x = ran.nextInt(16)*40;
		int y = ran.nextInt(16)*40;
		
		for(int i=snake.size()-1; i>=0; i--) { //prevents apple from spawning where the snake is
			while(x == snake.get(i).getX() && y == snake.get(i).getY()) {
				x = ran.nextInt(16)*40;
				y = ran.nextInt(16)*40;
			}
			appleX = x;
			appleY = y;
		}
	}
	public void collision()
	{ //If the Apple collides w/ the snake, create 8 more balls extending the snake.
		if(ax == appleX && ay == appleY || ax >= appleX-20 && ax < appleX+20 && ay >= appleY-20 && ay < appleY+20) 
		{
			relocateApple();
			score++;
			int colorCounter = 0;
			
			for(int i=0; i<8; i++)
			{
				Ellipse2D snakeBooty = snake.get(snake.size()-1);
				//Ellipse2D snakeBooty2 = snake.get(snake.size()-2);
				double newX = Math.round(snakeBooty.getX() / 40.0) * 40.0;
				double newY = Math.round(snakeBooty.getY() / 40.0) * 40.0;
				if(snake.size()>=2)
					snake.add(new Ellipse2D.Double(
						newX, newY,
						BALL_RADIUS*2, BALL_RADIUS*2));
				else
					snake.add(new Ellipse2D.Double(
						newX, newY, BALL_RADIUS*2, BALL_RADIUS*2));
				/*if(snake.size()<3)
					if(upPressed)
						snake.add(new Ellipse2D.Double(newX, newY+4, BALL_RADIUS*2, BALL_RADIUS*2));
					else if(downPressed)
						snake.add(new Ellipse2D.Double(newX, newY-4, BALL_RADIUS*2, BALL_RADIUS*2));
					else if(rightPressed)
						snake.add(new Ellipse2D.Double(newX-4, newY, BALL_RADIUS*2, BALL_RADIUS*2));
					else if(leftPressed)
						snake.add(new Ellipse2D.Double(newX+4, newY, BALL_RADIUS*2, BALL_RADIUS*2));
				else
					if(snakeBooty2.getX()>snakeBooty.getX())
						snake.add(new Ellipse2D.Double(newX-4, newY, BALL_RADIUS*2, BALL_RADIUS*2));
					else if(snakeBooty2.getX()<snakeBooty.getX())
						snake.add(new Ellipse2D.Double(newX+4, newY, BALL_RADIUS*2, BALL_RADIUS*2));
					else if(snakeBooty2.getY()>snakeBooty.getY())
						snake.add(new Ellipse2D.Double(newX, newY+4, BALL_RADIUS*2, BALL_RADIUS*2));
					else if(snakeBooty2.getY()<snakeBooty.getY())
						snake.add(new Ellipse2D.Double(newX, newY-4, BALL_RADIUS*2, BALL_RADIUS*2));
				*/
				//above adds new snake segments in the right spot
				//below sets the color of the entire snake, reevaluating on collision
				
				int redUp = ((colorCounter+2) % 256);
				int blueUp = ((colorCounter+8) % 256);
				int greenUp = ((colorCounter+1) % 256);
				if(red+redUp<=255 && red+redUp>0) 
					red+=redUp;
				else {
					redUp = -redUp;
					red += redUp;
				}
				if(blue+blueUp<=255 && blue+blueUp>0) 
					blue+=blueUp;
				else {
					blueUp = -blueUp;
					blue += blueUp;
				}
				if(green+greenUp<=255 && green+greenUp>0) 
					green+=greenUp;
				else {
					greenUp = -greenUp;
					green += greenUp;
				}
				colorCounter+=5;
				colors.add(new Color(red, blue, green));
/* failed idea below
				if(green+greenUp<=255 && green+greenUp>0 && //if all 3 colors are within bounds,
					blue+blueUp<=255 && blue+blueUp>0 && 
					red+redUp<=255 && red+redUp>0) {
						colors.add(new Color(red, blue, green)); // add a new color then adjust the next one
							red += redUp;
							blue += blueUp;
							green += greenUp;
				}
				
				if(green>=256 || green<0 || blue>=256 || blue<0 || red>=256 || red<0) {//if any color value is out of bounds
					if(red+redUp<=255 && red+redUp>=0)//these if/else statements find which bound is surpassed
						red += redUp;//and responds accordingly and makes a color at the end
					else {
						redUp = -redUp;
						red += redUp;
					}
					if(blue+blueUp<=255 && blue+blueUp>=0)
						blue += blueUp;
					else {
						blueUp = -blueUp;
						blue += blueUp;
					}
					if(green+greenUp<=255 && green+greenUp>=0)
						green += greenUp;
					else {
						greenUp = -greenUp;
						green += greenUp;
					}
					colors.add(new Color(red, blue, green));*/
				}
			}//closes the very important for loop
				
						
				}

	public void gameover()
	{

		for(int i = 11; i<=snake.size()-1; i++) 
		{
			if(ax == snake.get(i).getX() && ay == snake.get(i).getY() && !paused)
				gameover = true;
		} 
		if(ax<0 || ax>d.width-40 || ay<0 || ay>d.height-40 && !paused)
			gameover = true;
		
		if(gameover)
			dx = dy = 0;
	}
	public void moveSnake()
	{
		ax += dx;
		ay += dy;
		if(upPressed || downPressed) {
			if((ax%40)/40.0 > .25)
				headX = Math.ceil(ax / 40.0) * 40;
			else if((ax%40)/40.0 <= .25)
				headX = Math.floor(ax / 40.0) * 40;
		}
		if(rightPressed || leftPressed) {
			if((ay%40)/40.0 > .25)
				headY = Math.ceil(ay / 40.0) * 40;
			else if((ax%40)/40.0 <= .25)
				headY = Math.floor(ay / 40.0) * 40;
		}
		if(dx!=0 && !paused && !gameover){//if moving horizontally
			snake.get(0).setFrame(ax, headY, BALL_RADIUS*2, BALL_RADIUS*2);
			//snake.get(1).setFrame(ax, headY, BALL_RADIUS*2, BALL_RADIUS*2);
		}
		else if(dy!=0 && !paused && !gameover){//if moving vertically
			snake.get(0).setFrame(headX, ay, BALL_RADIUS*2, BALL_RADIUS*2);
			//snake.get(1).setFrame(headX, ay, BALL_RADIUS*2, BALL_RADIUS*2);

		}
		//above moves the head, below moves the body
		if(!gameover && !paused) { 
			for(int i = snake.size()-1; i>0; i--) {
				if(snake.get(i).getX() != snake.get(i-1).getX() && 
					snake.get(i).getY() != snake.get(i-1).getY()) {//this is for corners
					if(i>3) { 
						
						if(snake.get(i-2).getX() == snake.get(i-3).getX())
							snake.get(i).setFrame(
								snake.get(i-2).getX(), 
								Math.round(snake.get(i-1).getY()/40.0)*40, 
								BALL_RADIUS*2, BALL_RADIUS*2);
						else if(snake.get(i-2).getY() == snake.get(i-3).getY())
							snake.get(i).setFrame(
								Math.round(snake.get(i-1).getX()/40.0)*40, 
								snake.get(i-2).getY(), 
								BALL_RADIUS*2, BALL_RADIUS*2);
					}
					else
						snake.get(i).setFrame(
								snake.get(i-1).getFrame().getX(), 
								snake.get(i-1).getFrame().getY(), 
								BALL_RADIUS*2, BALL_RADIUS*2);
				}
				else
					snake.get(i).setFrame(
						snake.get(i-1).getFrame().getX(), 
						snake.get(i-1).getFrame().getY(), 
						BALL_RADIUS*2, BALL_RADIUS*2);
					
				}
			}
	}
				/*if(snake.size()>15 && i>2) {
				if(currentX != nextX2 || currentY != nextY2) {//finds corners
						//if going vertical/horizontal && next segment is misplaced which it does for some reason
						if(nextX2 == nextX3 && (nextX1 != nextX2 || nextY1 != currentY))//vertical turns
							snake.get(i).setFrame(nextX2, currentY, BALL_RADIUS*2, BALL_RADIUS*2);
						else if(nextY2 == nextY3 && (nextX1 != currentX || nextY1 != nextY2))//horizontal turns
							snake.get(i).setFrame(currentX, nextY2, BALL_RADIUS*2, BALL_RADIUS*2);
				}
				else
						snake.get(i).setFrame(
							snake.get(i-1).getFrame().getX(), 
							snake.get(i-1).getFrame().getY(), 
							BALL_RADIUS*2, BALL_RADIUS*2);
				}
				else
					snake.get(i).setFrame(
							snake.get(i-1).getFrame().getX(), 
							snake.get(i-1).getFrame().getY(), 
							BALL_RADIUS*2, BALL_RADIUS*2);
					
				//catch(Exception e){}
			}
		}
		else if(snake.size()<=2)
			for(int i = snake.size()-1; i>0; i--)
				snake.get(i).setFrame(
					snake.get(i-1).getFrame().getX(), 
					snake.get(i-1).getFrame().getY(), 
					BALL_RADIUS*2, BALL_RADIUS*2);
}
					if(i>2) {
						thing = true;
						if(snake.get(i-2).getX() == snake.get(i-3).getX())//if turning vertically
							snake.get(i).setFrame(
								snake.get(i-1).getX(), 
								Math.round(snake.get(i-1).getY()/40.0)*40, 
								BALL_RADIUS*2, BALL_RADIUS*2);
						else if(snake.get(i-2).getY() == snake.get(i-3).getY())//if turning horizontally
							snake.get(i).setFrame(
								Math.round(snake.get(i-1).getX()/40.0)*40, 
								snake.get(i-2).getY(), 
								BALL_RADIUS*2, BALL_RADIUS*2);
					}
					else
						snake.get(i).setFrame(
								snake.get(i-1).getFrame().getX(), 
								snake.get(i-1).getFrame().getY(), 
								BALL_RADIUS*2, BALL_RADIUS*2);
				}*/
				
	public void controls()
	{
		
		if (upPressed && !downPressed) {
			if (ax % 40 == 0) {
				dx = 0;
				dy = -4;
				leftPressed = rightPressed = false;
			}
		} 
		else if (downPressed && !upPressed) {
			if (ax % 40 == 0) {
				dx = 0;
				dy = 4;
				leftPressed = rightPressed = false;
			}
		} 
		else if (leftPressed && !rightPressed) {
			if (ax % 40 == 0) {
				dx = -4;
				dy = 0;
				upPressed = downPressed = false;
			}
		} 
		else if (rightPressed && !leftPressed) {
			if (ax % 40 == 0) {
				dx = 4;
				dy = 0;
				upPressed = downPressed = false;
			}
		} 
		else {
			dx = dy = 0;
		}
		if(!paused && !prevPaused) {//saves the previous direction into a variable
			pausedDX = dx;
			pausedDY = dy;
			if (upPressed)
				pd = 1;
			else if(downPressed)
				pd = 2;
			else if(rightPressed)
				pd = 3;
			else if(leftPressed)
				pd = 4;
		}
		if(paused) { //stops movement when paused
			dx = dy = 0;
			upPressed = downPressed = rightPressed = leftPressed = false;
			prevPaused = true;
		}
		if(!paused && prevPaused)//restarts the movement with the same data as before pausing
			if(pd == 1) {
				upPressed = true;
				dy=pausedDY;
			}
			if(pd == 2) {
				downPressed = true;
				dy=pausedDY;
			}
			if(pd == 3) {
				rightPressed = true;
				dx=pausedDX;
			}
			if(pd == 4) {
				leftPressed = true;
				dx=pausedDX;;
			}
			prevPaused = false;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

	}
	public void setUpPressed(boolean upPressed)
	{
		this.upPressed = upPressed;
	}
	public void setDownPressed(boolean downPressed)
	{
		this.downPressed = downPressed;
	}
	public void setLeftPressed(boolean leftPressed)
	{
		this.leftPressed = leftPressed;
	}
	public void setRightPressed(boolean rightPressed)
	{
		this.rightPressed = rightPressed;
	}
	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}
}
/*
 * To do list:
 * 
 * snake face (in progress)
 * uniform movement (fixed (almost))
 * maybe rainbow snake? (colors and fade done, rainbow in progress )
 * make turning around not an SD (fixed besides turning left when going right)
 * "play again?"
 * no quick turnarounds
 * easy medium hard?
 * pausing (in progress)
 * 
 */