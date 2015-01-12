import java.awt.Color;
import acm.graphics.GOval;

/**
 * 
 */

/**
 * @author Garin
 *
 */
public class Star {
	double mass;
	Vector position;
	Vector acceleration;
	Vector velocity;
	Vector force;
	boolean display;
	GOval circle;
	int screenx;
	int screeny;
	public int screendx;
	public int screendy;
	
	public Star(int x,int y){
		screenx = x;
		screeny = y;
		display = false;
		circle = new GOval(x,y,3,3);
		circle.setFilled(true);
		circle.setColor(Color.WHITE);
	}
	public Star(){
		this(400, 400);
	}
	
	public void turnOn(){
		display = true;
	}
	public void trueStar(int x,int y){
		
		circle.setLocation(x,y);
	}
}
