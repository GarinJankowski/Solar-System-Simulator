/**
 * 
 */

/**
 * @author Garin
 *
 */
public class Vector {
	double xp;
	double yp;

	public void addVector(double magnitude,double angle){
		xp = xp + (magnitude*Math.cos(angle));
		yp = yp +(magnitude*Math.sin(angle));
	}
	
	public double magnitude() {
		return Math.sqrt((xp*xp) + (yp*yp));
	}
}
