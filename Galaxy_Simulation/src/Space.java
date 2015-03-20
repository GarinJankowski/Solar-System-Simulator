import acm.program.GraphicsProgram;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import acm.util.RandomGenerator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	//titles
	GLabel lss = new GLabel("Solar System");
	GLabel lxy = new GLabel("Planets XY");
	GLabel lspo = new GLabel("Single Planet Orbit");
	GLabel lg = new GLabel("Galaxy");
	//debug string object
	GLabel debugLabel1 = new GLabel("debug");
	GLabel debugLabel2 = new GLabel("debug");
	GLabel debugLabel3 = new GLabel("debug");
	GLabel debugLabel4 = new GLabel("debug");
	GLabel debugLabel5 = new GLabel("debug");
	//initial velocities and angles of planets xy
	GLabel vn30= new GLabel("-30%");
	GLabel vn15= new GLabel("-15%");
	GLabel v0= new GLabel("+0% (circular orbit)");
	GLabel v15= new GLabel("+15%");
	GLabel v30= new GLabel("+30%");
	GLabel tiv= new GLabel("Initial Velocity Added");
	
	GLabel an15= new GLabel("-15%");
	GLabel an75= new GLabel("-7.5%");
	GLabel a0= new GLabel("+0% (circular orbit)");
	GLabel a75= new GLabel("+7.5%");
	GLabel a15= new GLabel("+15%");
	GLabel tia= new GLabel("Initial Angle Added");
	//speed of light
	public double c = 3e8;
	//cycle controller
	boolean cycle = false;
	//toggle buttons
	JCheckBox initV2 = new JCheckBox("Initial Velocity");
	JCheckBox initA = new JCheckBox("Initial Angle");
	JCheckBox info = new JCheckBox("Info");
	JCheckBox track = new JCheckBox("Tracker");
	JCheckBox initV = new JCheckBox("Randomize Initial Velocity");
	JCheckBox limit = new JCheckBox("Limit Planet Position");
	//sliders
	JSlider times = new JSlider(JSlider.VERTICAL,1,4,2);
	JSlider planetiv = new JSlider(JSlider.HORIZONTAL,-30,30,0);
	JSlider stariv = new JSlider(JSlider.HORIZONTAL,0,7,0);
	JSlider planetia = new JSlider(JSlider.HORIZONTAL,-30,30,0);
	
	
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
		setTitle("Solar System Simulator");
		//background color
		setBackground(Color.WHITE);
		//buttons
		add(new JButton("Start"),SOUTH);
		add(new JButton("Stop"),SOUTH);
		add(new JButton("Run"),SOUTH);
		add(new JButton("New Solar System"),SOUTH);
		add(new JButton("Planets XY"),SOUTH);
		add(new JButton("Single Planet Orbit"),SOUTH);
		add(new JButton("New Galaxy"),SOUTH);
		add(new JButton("Clear"),SOUTH);
		
		//checkboxes
		add(info,EAST);
		info.setSelected(false);
		
		add(initV2,EAST);
		initV2.setSelected(false);
		
		add(initA,EAST);
		initA.setSelected(false);

		add(track,EAST);
		track.setSelected(false);
		
		add(initV,EAST);
		initV.setSelected(false);
		
		add(limit,EAST);
		limit.setSelected(false);
		
		//slider
		
		planetiv.setPreferredSize(new Dimension(200,50));
		planetiv.setMajorTickSpacing(15);
		planetiv.setPaintTicks(true);
		planetiv.setPaintLabels(true);
		
		JPanel planetivpanel = new JPanel();
		planetivpanel.setLayout(new BoxLayout(planetivpanel, BoxLayout.Y_AXIS));
		planetivpanel.add(new Label("Planet Initial Velocity"));
		planetivpanel.add(planetiv);		
		add(planetivpanel,NORTH);
		
		planetia.setPreferredSize(new Dimension(200,50));
		planetia.setMajorTickSpacing(15);
		planetia.setPaintTicks(true);
		planetia.setPaintLabels(true);
		
		JPanel planetiapanel = new JPanel();
		planetiapanel.setLayout(new BoxLayout(planetiapanel, BoxLayout.Y_AXIS));		
		planetiapanel.add(new Label("Planet Initial Angle"));
		planetiapanel.add(planetia);		
		add(planetiapanel,NORTH);
		
		stariv.setPreferredSize(new Dimension(200,50));
		stariv.setMajorTickSpacing(1);
		stariv.setPaintTicks(true);
		stariv.setPaintLabels(true);
		
		JPanel starivpanel = new JPanel();
		starivpanel.setLayout(new BoxLayout(starivpanel, BoxLayout.Y_AXIS));		
		starivpanel.add(new Label("Star Initial Velocity"));
		starivpanel.add(stariv);	
		add(starivpanel,NORTH);
		
		times.setPreferredSize(new Dimension(50,400));
		times.setMajorTickSpacing(1);
		times.setPaintTicks(true);
		times.setPaintLabels(true);
		
		JPanel timespanel = new JPanel();
		timespanel.setLayout(new BoxLayout(timespanel, BoxLayout.Y_AXIS));		
		timespanel.add(new Label("Time Scale"));
		timespanel.add(times);	
		add(timespanel,WEST);
		
		Hashtable ttable = new Hashtable();
		ttable.put( new Integer(4), new JLabel("10000") );
		ttable.put( new Integer(3), new JLabel("1000") );
		ttable.put( new Integer(2), new JLabel("100") );
		ttable.put( new Integer(1), new JLabel("10") );
		times.setLabelTable(ttable);
		
		
		
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
	    	  cycle = true;
	      }
	      else if(command.equals("Stop")){  	 
	    	  go = false;
	    	  cycle = false;
	      }
	      else if(command.equals("Run")){
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  cycle = true;
	    	  initializeRandomSS();
	    	  go = true;
	    	  deltaT = 10;
	    	  add(debugLabel5);
	    	  debugLabel5.setLabel("Solar System Simulation    Running automatically     Please do not touch");
	    	  debugLabel5.setLocation(200, 200);
	    	  debugLabel5.setFont("Helvetica-24");
	    	  debugLabel5.setColor(Color.YELLOW);
	      }
	      else if(command.equals("New Solar System")){ 	  
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  cycle = false;
	    	  initializeRandomSS();
	    	  lss.setColor(Color.white);
	    	  lss.setFont("Helvetica-24");
	    	  add(lss,400,50);		
	    	  stars[0].circle.sendToFront();
	      }
	      else if(command.equals("Planets XY")){    	  
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  cycle = false;
	    	  initializeXYSS();
	    	  lxy.setColor(Color.BLACK);
	    	  lxy.setFont("Helvetica-24");
	    	  tiv.setFont("Helvetica-16");
	    	  v30.setFont("Helvetica-16");
	    	  v15.setFont("Helvetica-16");
	    	  v0.setFont("Helvetica-16");
	    	  vn15.setFont("Helvetica-16");
	    	  vn30.setFont("Helvetica-16");
	    	  tia.setFont("Helvetica-16");
	    	  a15.setFont("Helvetica-16");
	    	  a75.setFont("Helvetica-16");
	    	  a0.setFont("Helvetica-16");
	    	  an15.setFont("Helvetica-16");
	    	  an75.setFont("Helvetica-16");
	    	  add(lxy,300,50);
	    	  stars[0].circle.sendToFront();
	    	  //initial velocity and angle labels for planets xy
	    	  if(initV2.isSelected()){
	    		  tiv.setColor(Color.black);
	    		  add(tiv,1000,50);
	    		  v30.setColor(Color.gray);
	    		  add(v30,1000,100);
	    		  v15.setColor(Color.magenta);
	    		  add(v15,1000,125);
	    		  v0.setColor(Color.blue);
	    		  add(v0,1000,150);
	    		  vn15.setColor(Color.black);
	    		  add(vn15,1000,175);
	    		  vn30.setColor(Color.red);
	    		  add(vn30,1000,200);
	    		  
	    		  lxy.setLabel("Modifying Magnitude of Planet's Initial Velocity (same angle)");
	    	  }
	    	  if(initA.isSelected()){
	    		  tia.setColor(Color.black);
	    		  add(tia,1200,50);
	    		  an15.setColor(Color.gray);
	    		  add(an15,1200,100);
	    		  an75.setColor(Color.magenta);
	    		  add(an75,1200,125);
	    		  a0.setColor(Color.blue);
	    		  add(a0,1200,150);
	    		  a75.setColor(Color.black);
	    		  add(a75,1200,175);
	    		  a15.setColor(Color.red);
	    		  add(a15,1200,200);
	    		  
	    		  lxy.setLabel("Modifying Angle of Planet's Initial Velocity (same magnitude)");
	    	  }
	    	  if(initV2.isSelected()&&initA.isSelected()){
	    		  tiv.setColor(Color.black);
	    		  add(tiv,1000,50);
	    		  v30.setColor(Color.gray);
	    		  add(v30,1000,100);
	    		  v15.setColor(Color.magenta);
	    		  add(v15,1000,125);
	    		  v0.setColor(Color.blue);
	    		  add(v0,1000,150);
	    		  vn15.setColor(Color.black);
	    		  add(vn15,1000,175);
	    		  vn30.setColor(Color.red);
	    		  add(vn30,1000,200);
	    		  
	    		  tia.setColor(Color.black);
	    		  add(tia,1200,50);
	    		  an15.setColor(Color.gray);
	    		  add(an15,1200,100);
	    		  an75.setColor(Color.magenta);
	    		  add(an75,1200,125);
	    		  a0.setColor(Color.blue);
	    		  add(a0,1200,150);
	    		  a75.setColor(Color.black);
	    		  add(a75,1200,175);
	    		  a15.setColor(Color.red);
	    		  add(a15,1200,200);
	    		  
	    		  lxy.setLocation(200,50);
	    		  lxy.setLabel("Modifying Planet Initial Velocity and Angle");
	    	  }
	      }
	    	 
	      else if(command.equals("Single Planet Orbit")){   	  
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  cycle = false;
	    	  initialize();
	    	  lspo.setColor(Color.white);
	    	  lspo.setFont("Helvetica-24");
	    	  add(lspo,300,50);
	      }
	      else if(command.equals("New Galaxy")){    	 
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  cycle = false;
	    	  initializeRandomG();
	    	  lg.setColor(Color.white);
	    	  lg.setFont("Helvetica-24");
	    	  add(lg,400,50);
	    	  stars[0].circle.sendToFront();
	    	  stars[numStars/2].circle.sendToFront();
	      }      
	      else if(command.equals("Clear")){
	    	  go = false;
	    	  removeAll();
	    	  undisplayAll();
	    	  cycle = false;
	    	  
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
		deltaT = Math.pow(10,times.getValue());
		maxpx = 20e10;
		maxpy = 20e10;
		
		//get values from sliders
		int getVal = stariv.getValue();
		int getVal2 = planetiv.getValue();
		int getVal3 = planetia.getValue();
		
		rdistance = 1.496e10;
		rangle = Math.PI/2;
		
		//calculate xa nd y from distance and angle
		gdx = rdistance*(Math.cos(rangle));
		gdy = rdistance*(Math.sin(rangle));
	
		//time
		time = 0;
		//sun
		stars[0].turnOn();
		stars[0].setPosition(maxpx/2, maxpy/2);
		stars[0].setVelocity(1*Math.pow(10,getVal),0);
		stars[0].mass = 2e30;
		stars[0].trueStar(this,0);
		stars[0].circle.setColor(Color.YELLOW);
		add(stars[0].circle);
		
		//magnitude and angle
		double vm = (Math.sqrt(G*(stars[0].mass)/rdistance))*(1+((double)getVal2/100));
		double theta = (Math.PI/2+rangle)*(1+((double)getVal3/100));
		 
		//setting initial velocity
		 
		 double sxv = vm*Math.cos(theta);
		 double syv = vm*Math.sin(theta);
		
		//earth
		stars[1].turnOn();
		stars[1].setPosition(maxpx/2 + gdx,maxpy/2 + gdy);
		//stars[1].setVelocity(0, 0);
		stars[1].setVelocity(sxv,syv);
		//stars[1].setVelocity(-2.88e4,1.55e4);
		//stars[1].setVelocity(-1.5e4,0.8e4);
		//stars[1].setVelocity(-1.e4,0.6e4);
		//stars[1].setVelocity(-0.7e4,0.4e4);
		//stars[1].setVelocity(-0.4e4,0.3e4);
		stars[1].mass = 6e24;
		stars[1].trueStar(this,1);
		stars[1].circle.setColor(Color.CYAN);
		add(stars[1].circle);
		

	}
	//creates two random galaxy formations
	public void initializeRandomG(){

		//numbers of stars in array
		final int numStars = 100;
		
		//sets screen size in physics world
		maxpx = 5.2e20*50;
		maxpy = 5.2e20*50;
		//sets time scale
		deltaT =  Math.pow(10,times.getValue());
		
		//first supermassive black hole
		stars[0].turnOn();
		stars[0].setPosition(g1x,g1y);
		stars[0].setVelocity(0,0);
		stars[0].mass = 9e20;
		stars[0].trueStar(this,0);
		stars[0].circle.setColor(Color.MAGENTA);
		add(stars[0].circle);
		
		//second black hole
		stars[numStars/2].turnOn();
		stars[numStars/2].setPosition(g2x,g2y);
		stars[numStars/2].setVelocity(0,0);
		stars[numStars/2].mass = 9e20;
		stars[numStars/2].trueStar(this,numStars/2);
		stars[numStars/2].circle.setColor(Color.RED);
		add(stars[numStars/2].circle);
		
		//first set of stars
		for(int i=1;i<numStars/2;i++){
			
			//random distance, nagle, and mass
			rdistance = (double)(Math.random()*4.2e20);
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
	    	stars[i].trueStar(this,i);
	   	 	stars[i].circle.setColor(Color.CYAN);
		   	  add(stars[i].circle);
	  	}
		
		//second set of stars
		for(int i=numStars/2+1;i<numStars;i++){
			
			//sets distance, andgle, and mass
			rdistance = (double)(Math.random()*5.2e20);
			rangle = (double)(Math.random()*2*(Math.PI));
			massRan = (double)(Math.random()*maxMassRan);
			
			//magnitude and angle
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
		   	  stars[i].trueStar(this,i);
		      stars[i].circle.setColor(Color.ORANGE);
		   	  add(stars[i].circle);
	  	}
	}
	//creates a random solar system formation
	public void initializeRandomSS(){
		//changes number of stars in the array
		final int numStars =  10;
		int getVal = stariv.getValue();
		maxpx = 2e10;
		maxpy = 2e10;
		deltaT =  Math.pow(10,times.getValue());
		//maxMassRan = (2.1e30-2e30)/(numStars/2-1);
		
		//center star
		stars[0].turnOn();
		stars[0].setPosition(maxpx/2,maxpy/2);
		stars[0].setVelocity(1*Math.pow(10,getVal),0);
		stars[0].mass = 2e30;
		stars[0].trueStar(this,0);
		stars[0].circle.setColor(Color.YELLOW);
		stars[0].circle.setSize(7, 7);
		add(stars[0].circle);
		
		//surrounding planets
		for(int i=1;i<numStars;i++){
			
			rdistance = (double)(Math.random()*4.5e9);
			if(limit.isSelected()){
				rdistance = (double)(Math.random()*3.4e9)+1e9;
			}
			rangle = (double)(Math.random()*2*(Math.PI));
			massRan = (double)(Math.random()*2e27)+3.285e23;
			
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
		   	  stars[i].trueStar(this,i);
		      stars[i].circle.setColor(Color.CYAN);
		   	  add(stars[i].circle);
		}
	}
	public void initializeXYSS(){
		//changes number of stars in the array
		final int numStars =  6;
		int getVal = stariv.getValue();
		maxpx = 2e10;
		maxpy = 2e10;
		deltaT =  Math.pow(10,times.getValue());
		//maxMassRan = (2.1e30-2e30)/(numStars/2-1);
		
		//center star
		stars[0].turnOn();
		stars[0].setPosition(maxpx/2,maxpy/2);
		stars[0].setVelocity(1*Math.pow(10,getVal),0);
		stars[0].mass = 2e30;
		stars[0].trueStar(this,0);
		stars[0].circle.setColor(Color.BLUE);
		stars[0].circle.setSize(7, 7);
		add(stars[0].circle);
		
		//surrounding planets
		for(int i=1;i<numStars;i++){
			rdistance = 1.8e9;
			if(limit.isSelected()){
				rdistance = 3.4e9+1e9;
			}
			rangle = 2*(Math.PI);
			massRan = 2e27+3.285e23;
			
			 gdx = rdistance*(Math.cos(rangle));
			 gdy = rdistance*(Math.sin(rangle));
			
			 
			 double vm = Math.sqrt(G*(stars[0].mass)/rdistance);
			 double theta = Math.PI/2+rangle;
			
			 if(initA.isSelected()){
			 	if(i==1){
			 		theta = theta*(1+(-15.0/100));
			 	}
			 	else if(i==2){
			 		theta = theta*(1+(-7.5/100));
			 	}
			 	else if(i==4){
			 		theta = theta*(1+(7.5/100));
			 	}
			 	else if(i==5){
			 		theta = theta*(1+(15.0/100));
			 	}
			 }
			 
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
		   	  stars[i].trueStar(this,i);
		      stars[i].circle.setColor(Color.CYAN);
		   	  add(stars[i].circle);
		   	  
		   	 if(initV2.isSelected()){
		   	  	stars[1].setVelocity(sxv-(sxv/10)*3,syv-(syv/10)*3);
		   	  	stars[2].setVelocity(sxv-(sxv/10)*1.5,syv-(syv/10)*1.5);
		   	  	stars[4].setVelocity(sxv+(sxv/10)*1.5,syv+(syv/10)*1.5);
		   	  	stars[5].setVelocity(sxv+(sxv/10)*3,syv+(syv/10)*3);
		   	 }
		}
	}
	//main loop
	public void run(){
		double debugLabelY;
		long starttime = System.currentTimeMillis();
		long dif;
		 while(true){
			 if(go){
				 debugLabelY = 10;
				 for(int i=0;i<numStars;i++){
					 if(stars[i].display == true){
						 add(stars[i].circle);
						 stars[i].trueStar(this,i);
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
				 if(cycle == true){
				 long time = System.currentTimeMillis();
				 dif = time-starttime;
				 if(time-starttime > 60000){
					 go = false;
			    	  removeAll();
			    	  undisplayAll();
			    	  starttime = System.currentTimeMillis();
			    	  initializeRandomSS();
			    	  go = true;
			    	  deltaT = 10;
			    	  add(debugLabel5);
			    	  debugLabel5.setLabel("Solar System Simulation    Running automatically     Please do not touch");
			    	  debugLabel5.setLocation(200, 200);
			    	  debugLabel5.setFont("Helvetica-24");
			    	  debugLabel5.setColor(Color.YELLOW);
			    	  
				 }
				 }

			 }
			 pause(1);
			
   	  	}
	}
}
