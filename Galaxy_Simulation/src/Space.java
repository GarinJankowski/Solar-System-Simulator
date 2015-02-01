import acm.program.GraphicsProgram;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import acm.util.RandomGenerator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
/**
 * 
 */

/**
 * @author Garin
 *
 */
public class Space extends GraphicsProgram{
	
	//size of application
	public static final int APPLICATION_WIDTH = 1200;
	public static final int APPLICATION_HEIGHT = 900;
	//maximum positions in the physics world
	double maxpx = 5.2e20*50;
	double maxpy = 5.2e20*50;
	//maximum positions in the application
    //double maxcx = APPLICATION_WIDTH;
    double maxcx = APPLICATION_HEIGHT;
	double maxcy = APPLICATION_HEIGHT;
	//time
	double time = 0;
	//change in time
	double deltaT = 5e12;
	//gravitational constant
	double G = 6.7e-11;
	//activation variables
	boolean go = false;
	//boolean track = false;
	//boolean info = false;
	//stars array
	final int numStars = 100;
	Star[] stars = new Star[numStars];
	//galaxy x and y
	double g1x = maxpx/4;
	double g1y = maxpy/4;
	
	double g2x = maxpx-maxpx/4;
	double g2y = maxpy-maxpy/4;
	//galaxy x and y distance
	double gdx;
	double gdy;
	//galaxy or solar system indicators
	GOval gm;
	GOval ssm;
	//maximum massRan
	double maxMassRan = (1.2e42-8.6e36)/(numStars/2-1);
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
	JCheckBox initV = new JCheckBox("Randomize Initial Velocity");
	//slider
	JSlider times = new JSlider(JSlider.HORIZONTAL,5,200,30);
	//random number generators
	double rangle;
	double rdistance;
	double massRan;
	
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
		add(new JButton("New Solar System"),SOUTH);
		add(new JButton("Single Planet Orbit"),SOUTH);
		add(new JButton("New Galaxy"),SOUTH);
		add(new JButton("Clear"),SOUTH);
		
		//checkboxes
		add(info,EAST);
		info.setSelected(false);

		add(track,EAST);
		track.setSelected(false);
		
		add(initV,EAST);
		initV.setSelected(true);
		
		//slider
		
		times.setPreferredSize(new Dimension(400,50));
		times.setMajorTickSpacing(20);
		times.setMinorTickSpacing(5);
		times.setPaintTicks(true);
		times.setPaintLabels(true);
		add(times,NORTH);
		
		addActionListeners();
		
		//initialize stars array
		for(int i=0;i<numStars;i++){
			stars[i] = new Star();
		}
		
		//x and y positions
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
	      else if(command.equals("New Solar System")){
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  initializeRandomSS();
	    	  stars[0].circle.sendToFront();
	      }
	      else if(command.equals("Single Planet Orbit")){
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  initialize();
	      }
	      else if(command.equals("New Galaxy")){
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  initializeRandomG();
	    	  stars[0].circle.sendToFront();
	    	  stars[numStars/2].circle.sendToFront();
	      }      
	      else if(command.equals("Clear")){
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  
	      }
	  
	}
	
	private void undisplayAll() {
		//delete everything
		for(int i=0;i<numStars;i++){	
			  stars[i].turnOff();
	  	}
		time = 0;
	}
	//sets up basic one-planet orbit
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
	//creates two random galaxy formations
	public void initializeRandomG(){

		final int numStars = 100;
		
		maxpx = 5.2e20*50;
		maxpy = 5.2e20*50;
		deltaT = 5e11;
		
		//first supermassive black hole
		stars[0].turnOn();
		stars[0].setPosition(g1x,g1y);
		stars[0].setVelocity(0,0);
		stars[0].mass = 9e20;
		stars[0].trueStar(this);
		stars[0].circle.setColor(Color.MAGENTA);
		add(stars[0].circle);
		
		//second black hole
		stars[numStars/2].turnOn();
		stars[numStars/2].setPosition(g2x,g2y);
		stars[numStars/2].setVelocity(0,0);
		stars[numStars/2].mass = 9e20;
		stars[numStars/2].trueStar(this);
		stars[numStars/2].circle.setColor(Color.RED);
		add(stars[numStars/2].circle);
		
		//first set of stars
		for(int i=1;i<numStars/2;i++){
			
			//random distance, nagle, and mass
			rdistance = (double)(Math.random()*5.2e20);
			rangle = (double)(Math.random()*2*(Math.PI));
			massRan = (double)(Math.random()*maxMassRan);
			
			//calculates magnitude and angle
			double vm = Math.sqrt(G*(stars[0].mass)/rdistance);
			double theta = Math.PI/2+rangle;
			
			//calculates initial velocity
			double sxv = vm*Math.cos(theta);
			double syv = vm*Math.sin(theta);
			
			//converts magnitude and angle into x and y
		   	gdx = rdistance*(Math.cos(rangle));
		    gdy = rdistance*(Math.sin(rangle));
		    stars[i].turnOn();
		    stars[i].setPosition(g1x + gdx,g1y + gdy);
		    stars[i].setVelocity(sxv,syv);
	    	stars[i].mass = massRan;
	    	stars[i].trueStar(this);
	   	 	stars[i].circle.setColor(Color.CYAN);
		   	  add(stars[i].circle);
	  	}
		
		//second set of stars
		for(int i=numStars/2+1;i<numStars;i++){
			
			rdistance = (double)(Math.random()*5.2e20);
			rangle = (double)(Math.random()*2*(Math.PI));
			massRan = (double)(Math.random()*maxMassRan);
			
			double vm = Math.sqrt(G*(stars[numStars/2].mass)/rdistance);
			 double theta = Math.PI/2+rangle;
			 
			 double sxv = vm*Math.cos(theta);
			 double syv = vm*Math.sin(theta);
			
			  gdx = rdistance*(Math.cos(rangle));
			  gdy = rdistance*(Math.sin(rangle));
			  stars[i].turnOn();
			  stars[i].setPosition(g2x + gdx,g2y + gdy);
			  stars[i].setVelocity(sxv,syv);
		   	  stars[i].mass = massRan;
		   	  stars[i].trueStar(this);
		      stars[i].circle.setColor(Color.ORANGE);
		   	  add(stars[i].circle);
	  	}
	}
	//creates a random solar system formation
	public void initializeRandomSS(){
		
		//changes number of stars in the array
		final int numStars =  10;
		
		maxpx = 2e10;
		maxpy = 2e10;
		deltaT = 30;
		//maxMassRan = (2.1e30-2e30)/(numStars/2-1);
		
		//center star
		stars[0].turnOn();
		stars[0].setPosition(maxpx/2,maxpy/2);
		stars[0].setVelocity(0,0);
		//stars[0].setVelocity(1e5,0);
		stars[0].mass = 2e30;
		stars[0].trueStar(this);
		stars[0].circle.setColor(Color.YELLOW);
		stars[0].circle.setSize(7, 7);
		add(stars[0].circle);
		
		//surrounding planets
		for(int i=1;i<numStars;i++){
			
			rdistance = (double)(Math.random()*4.5e9);
			rangle = (double)(Math.random()*2*(Math.PI));
			massRan = (double)(Math.random()*1e26);
			
			 gdx = rdistance*(Math.cos(rangle));
			 gdy = rdistance*(Math.sin(rangle));
			
			 
			 double vm = Math.sqrt(G*(stars[0].mass)/rdistance);
			 double theta = Math.PI/2+rangle;
			 
			//setting initial velocity
			 if(initV.isSelected()){
			 	double vmd = vm/5;
			 	double thetad = theta/5;
			 
			 	vm = vm+(Math.random()*vmd);
			 	theta = theta+(Math.random()*thetad);
			 }
			 	
			 double sxv = vm*Math.cos(theta);
			 double syv = vm*Math.sin(theta);
			 
			//planets
			stars[i].turnOn();
			  stars[i].setPosition(maxpx/2 + gdx,maxpy/2 + gdy);
			  stars[i].setVelocity(sxv,syv);
		   	  stars[i].mass = massRan;
		   	  stars[i].trueStar(this);
		      stars[i].circle.setColor(Color.CYAN);
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
						 //sets info
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
