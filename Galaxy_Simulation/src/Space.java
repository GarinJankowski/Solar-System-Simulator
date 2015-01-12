import acm.program.GraphicsProgram;
import acm.graphics.GOval;
import acm.graphics.GRect;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import acm.util.RandomGenerator;
import javax.swing.JButton;
/**
 * 
 */

/**
 * @author Garin
 *
 */
public class Space extends GraphicsProgram{
	public static final int APPLICATION_WIDTH = 800;
	public static final int APPLICATION_HEIGHT = 800;
	boolean go = false;
	final int numStars = 10;
	Star[] stars = new Star[numStars];
	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		new Space().start(args);
		// TODO Auto-generated method stub

	}
	public void init(){
		setBackground(Color.BLACK);
		add(new JButton("Start"),SOUTH);
		add(new JButton("Stop"),SOUTH);
		add(new JButton("Reset"),SOUTH);
		addActionListeners();
		
		for(int i=0;i<numStars;i++){
			stars[i] = new Star();
		}
			 
		initialize();

	}
	public void actionPerformed(ActionEvent e) {
	      String command = e.getActionCommand();
	      if(command.equals("Start")){	    	  
	    	  go = true;
	      }
	      else if(command.equals("Stop")){
	    	  go = false;
	    	  
	      }
	      else if(command.equals("Reset")){
	    	  go = false;
	    	  initialize();
	      }
	}
	
	public void initialize(){
		stars[0].turnOn();
		stars[0].trueStar(400, 400);
		stars[0].screendx = 1;
		stars[0].screendy = 1;
		
		stars[1].turnOn();
		stars[1].trueStar(300, 300);
		stars[1].screendx = 1;
		stars[1].screendy = -1;
		
		stars[2].turnOn();
		stars[2].trueStar(500, 500);
		stars[2].screendx = -1;
		stars[2].screendy = -1;
	}
	
	public void run(){
		 while(true){
			 if(go){
				 for(int i=0;i<numStars;i++){
					 if(stars[i].display == true){
						 add(stars[i].circle);
						 stars[i].circle.move(stars[i].screendx, stars[i].screendy);
					 }
				 }
			 }
			 pause(10);
   	  	}
	}
}
