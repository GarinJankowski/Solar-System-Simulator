import acm.program.GraphicsProgram;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import acm.util.RandomGenerator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
/**
 * 
 */

/**
 * @author Garin
 *
 */
public class Space extends GraphicsProgram{
	
	//size of application
	public static final int APPLICATION_WIDTH = 800;
	public static final int APPLICATION_HEIGHT = 800;
	//maximum positions in the physics world
	double maxpx = 7.5e16;
	double maxpy = 7.5e16;
	//maximum positions in the application
    double maxcx = APPLICATION_WIDTH;
	double maxcy = APPLICATION_HEIGHT;
	//time
	double time = 0;
	//change in time
	double deltaT = 50000;
	//gravitational constant
	double G = 6.7e-11;
	//activation variables
	boolean go = false;
	//boolean track = false;
	//boolean info = false;
	//stars array
	final int numStars = 10;
	Star[] stars = new Star[numStars];
	//galaxy x and y
	double g1x = .5e16;
	double g1y = .5e16;
	
	double g2x = 7e16;
	double g2y = 7e16;
	//galaxy x and y distance
	double gdx;
	double gdy;
	
	//debug string object
	GLabel debugLabel1 = new GLabel("debug");
	GLabel debugLabel2 = new GLabel("debug");
	GLabel debugLabel3 = new GLabel("debug");
	GLabel debugLabel4 = new GLabel("debug");
	//speed of light
	public double c = 3e8;
	//toggle buttons
	JCheckBox info = new JCheckBox("Info");
	JCheckBox track = new JCheckBox("Tracker");
	//random number generators
	
	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		new Space().start(args);
		// TODO Auto-generated method stub
		
	}
	//initialize
	public void init(){
		//background color
		setBackground(Color.BLACK);
		//buttons
		add(new JButton("Start"),SOUTH);
		add(new JButton("Stop"),SOUTH);
		add(new JButton("Reset"),SOUTH);
		add(new JButton("New"),SOUTH);
		
		add(info,(EAST));
		info.setSelected(false);
		
		
		add(track,EAST);
		track.setSelected(false);
		
		addActionListeners();
		
		//initialize stars array
		for(int i=0;i<numStars;i++){
			stars[i] = new Star();
		}
		
		// debug
		debugLabel1.setLabel("");
		debugLabel1.setColor(Color.white);
		
		debugLabel2.setLabel("");
		debugLabel2.setColor(Color.white);
		
		debugLabel3.setLabel("");
		debugLabel3.setColor(Color.white);
		
		debugLabel4.setLabel("");
		debugLabel4.setColor(Color.white);
		
		add(debugLabel1,10,10);		
		add(debugLabel2,10,25);			
		add(debugLabel3,10,40);
		add(debugLabel4,10,55);
		
		//initialize();

	}
	//code for buttons
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
	    	  removeAll();
	    	  undisplayAll();
	    	  initialize();
	      }
	      else if(command.equals("New")){
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();	
	    	  initializeRandom();
	      }
	}
	
	private void undisplayAll() {
		// TODO Auto-generated method stub
		for(int i=0;i<numStars;i++){	
			  stars[i].turnOff();
	  	}
		time = 0;
	}
	//sets up stars
	public void initialize(){
		deltaT = 50000;
		maxpx = 40e10;
		maxpy = 40e10;
		
		//time
		time = 0;
		//sun
		stars[0].turnOn();
		stars[0].setPosition(20e10, 20e10);
		stars[0].setVelocity(0,0);
		stars[0].mass = 2e30;
		stars[0].trueStar(this);
		add(stars[0].circle);
		
		//earth
		stars[1].turnOn();
		stars[1].setPosition(2.8e11,3.3e11);
		//stars[1].setVelocity(0, 0);
		stars[1].setVelocity(-2.88e4,1.55e4);
		//stars[1].setVelocity(-1.5e4,0.8e4);
		//stars[1].setVelocity(-1.e4,0.6e4);
		//stars[1].setVelocity(-0.7e4,0.4e4);
		//stars[1].setVelocity(-0.4e4,0.3e4);
		stars[1].mass = 6e24;
		stars[1].trueStar(this);
		add(stars[1].circle);
		

	}
	
	public void initializeRandom(){
		double rangle;
		double rdistance;
		double massRan;
		
		rdistance = (double)(Math.random()*4.5e15);
		rangle = (double)(Math.random()*2*(Math.PI));
		massRan = 1e37*(double)(Math.random()*1e30);
		
		
		deltaT = 1e11;
		maxpx = 1.125e22;
		maxpy = 1.125e22;
		g1x = 7.5e20;
		g1y = 7.5e20;
		g2x = 1.05e22;
		g2y = 1.05e22;
		
		stars[0].turnOn();
		stars[0].setPosition(g1x,g1y);
		stars[0].setVelocity(0,0);
		stars[0].mass = 9e15;
		stars[0].trueStar(this);
		add(stars[0].circle);
		
		stars[numStars/2].turnOn();
		stars[numStars/2].setPosition(g2x,g2y);
		stars[numStars/2].setVelocity(0,0);
		stars[numStars/2].mass = 9e15;
		stars[numStars/2].trueStar(this);
		add(stars[numStars/2].circle);
		
		for(int i=1;i<numStars/2;i++){	

		   	  gdx = rdistance*(Math.cos(rangle));
			  gdy = rdistance*(Math.sin(rangle));
			  stars[i].turnOn();
			  stars[i].setPosition(g1x + gdx,g1y + gdy);
			  stars[i].setVelocity(0,0);
		   	  stars[i].mass = massRan;
		   	  stars[i].trueStar(this);
		   	  add(stars[i].circle);
	  	}
		
		for(int i=numStars/2+1;i<numStars;i++){				
			
			  gdx = rdistance*(Math.cos(rangle));
			  gdy = rdistance*(Math.sin(rangle));
			  stars[i].turnOn();
			  stars[i].setPosition(g2x + gdx,g2y + gdy);
			  stars[i].setVelocity(0,0);
		   	  stars[i].mass = massRan;
		   	  stars[i].trueStar(this);
		   	  add(stars[i].circle);
	  	}
	}
	
	//main loop
	public void run(){
		double debugLabelY;
		 while(true){
			 if(go){
				 debugLabelY = 10;
				 for(int i=0;i<numStars;i++){
					 if(stars[i].display == true){
						 add(stars[i].circle);
						 stars[i].trueStar(this);
						 stars[i].updateP(this);
						 time += deltaT;
						 if(info.isSelected()){
							debugLabel1.setLabel(String.format("Time = %e seconds = %e years",time,time/(86400*365.25)));
							add(debugLabel1);
						 	debugLabel2.setLabel(String.format("pot/c = %e, vcsq = %e",stars[1].maxpotc,stars[1].vcsq));
						 	debugLabel3.setLabel(stars[1].debug);
						 	
						 	stars[i].label.setLabel(stars[i].debug);
						 	stars[i].label.setLocation(maxcx, debugLabelY);
						 	add(stars[i].label);
						 	debugLabelY += 15;
						 }
						 else{
							 debugLabel1.setLabel("");
							 debugLabel2.setLabel("");
							 debugLabel3.setLabel("");
							 
							 stars[i].label.setLabel("");
						 }
					 }
				 }
			 }
			 pause(1);
   	  	}
	}
}
