import java.awt.Color;

import acm.graphics.GLabel;
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
	double maxpotc;
	double vcsq;
	String debug;
	GLabel label;
	
	
	public Star(int x,int y){
		display = false;
		circle = new GOval(x,y,3,3);
		circle.setFilled(true);
		circle.setColor(Color.WHITE);
		position = new Vector();
		acceleration = new Vector();
		velocity = new Vector();
		force = new Vector();
		label = new GLabel("");
		label.setColor(Color.white);
	}
	public Star(){
		this(400, 400);
	}
	
	public void turnOn(){
		display = true;
	}
	public void setPosition(double xp,double yp){
		position.xp = xp;
		position.yp = yp;
	}
	//converts real physics into computer and moves the image to the object's location
	public void trueStar(Space space){
		
		double xc = (space.maxcx/space.maxpx)*position.xp;
		double yc = (-space.maxcy/space.maxpy)*position.yp + space.maxcy;
		
		if(space.track.isSelected()){
			GOval trackcircle = new GOval(xc,yc,2,2);
			trackcircle.setColor(Color.red);
			space.add(trackcircle);
		}
		
		circle.setLocation(xc,yc);
	}
	//psuedo-code
	public void updateP(Space space) {
		//force
		this.update_f(space);
		//acceleration
		this.uptade_a(space);
		//velocity
		this.update_v(space);
		//position
		this.update_p(space);
		}
	private void update_f(Space space) {
		//calculates force
		//F = Gm1m2/r^2
		Star currentStar;
		double x1;
		double y1;
		double x2;
		double y2;
		double distance;
		double deltax;
		double deltay;
		double magnitude;
		double angle;
		double origangle;
		double potc;
		double vc;
		//set force to 0
		force.xp = 0;
		force.yp = 0;
		maxpotc = 0;
		vcsq = 0;
		for(int i=0;i<space.numStars;i++){
			//current star in the array of stars
			currentStar = space.stars[i];
			//position of current star
			x2 = currentStar.position.xp;
			y2 = currentStar.position.yp;
			//position of this star
			x1 = position.xp;
			y1 = position.yp;
			
			deltay = y2 - y1;
			deltax = x2 - x1;
			
			distance = Math.sqrt(deltax*deltax + deltay*deltay);
			//only calculate force if we are not our own star and the other start should be displayed
			if(distance > 0 && currentStar.display){
				magnitude = space.G*mass*currentStar.mass/(distance*distance);
				angle = Math.atan(Math.abs(deltay/deltax));
				origangle = angle;
						
				if (deltax < 0 && deltay > 0)
				{
					angle = Math.PI - angle;
				}
				else if (deltax < 0 && deltay < 0)
				{
					angle = Math.PI + angle;
				}
				else if (deltax > 0 && deltay < 0)
				{
					angle = (2 * Math.PI) - angle;
				}
				
				//add the force of this mass to the total force
				force.addVector(magnitude, angle);
				
				//debug = String.format("Fg orig angle %f magnitude %e angle %f x %e y %e", origangle,magnitude,angle,force.xp,force.yp);
				debug = String.format("x %e y %e", position.xp,position.yp);
				label.setLabel(debug);
				
				potc = space.G * currentStar.mass / (distance * space.c * space.c);
				if (potc > maxpotc) {
					maxpotc = potc;
				}
				
				vc = velocity.magnitude()/space.c;
				vcsq = vc * vc;
			}
		}
	}
	private void uptade_a(Space space) {
		//calculates acceleration
		//a = F/m
		acceleration.xp = force.xp/mass;
		acceleration.yp = force.yp/mass;
	}
	private void update_v(Space space) {
		//calculates velocity
		//V final = V initial + a deltaT
		velocity.xp = velocity.xp + acceleration.xp*space.deltaT;
		velocity.yp = velocity.yp + acceleration.yp*space.deltaT;
	}
	private void update_p(Space space) {
		//calculates position
		//P final = P initial + v deltaT
		position.xp = position.xp + velocity.xp*space.deltaT;
		position.yp = position.yp + velocity.yp*space.deltaT;
	}
	public void setVelocity(double xp,double yp) {
		velocity.xp = xp;
		velocity.yp = yp;
		
	}
	public void turnOff() {
		// TODO Auto-generated method stub
		display = false;
	}
	
	
	
}
