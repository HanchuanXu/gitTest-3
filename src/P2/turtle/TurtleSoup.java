/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TurtleSoup {
	 
	private static final double circleAngle = 360.0;
	 private static final double straightAngle = 180.0;
	 private static final int sidesOfSquare = 4;

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	for (int i = 0; i < sidesOfSquare; ++i) {
            turtle.turn(straightAngle * (sidesOfSquare - 2) / sidesOfSquare);
            turtle.forward(sideLength);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
    	//鍐呰鍜�=(sides-2)*180
    	 return straightAngle * (sides - 2) / sides;    
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
    	//宸茬煡姝ｅ杈瑰舰鍐呰搴︽暟鍒欏叾杈规暟涓猴細360掳梅锛�180掳锛嶅唴瑙掑害鏁帮級
    	//浠绘剰姝ｅ杈瑰舰鐨勫瑙掑拰=360掳
        return Math.toIntExact(Math.round(circleAngle / (straightAngle - angle)));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
    	for (int i = 0; i < sides; ++i) {
            turtle.turn(straightAngle - calculateRegularPolygonAngle(sides));
            turtle.forward(sideLength);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
    	 int x = targetX - currentX;
         int y = targetY - currentY;

         // Compute the angle between Y axis and (x,y)
         //1掳=2*Math.PI/360
         //acos杩斿洖鐨勬槸鐭㈤噺鍚孻杞寸殑澶硅锛�0-180 degree銆�
         double targetAngle = Math.acos(y / Math.sqrt(x * x + y * y)) * (straightAngle / Math.PI);
         if (x < 0) { //澶勭悊涓嬬2鍜岀3璞￠檺鐨勯棶棰�
             targetAngle = circleAngle - targetAngle;
         }

         double angleHeadingToPoint = targetAngle - currentBearing;
         // Keep the angle between [0,360)
         if (angleHeadingToPoint < 0) {
             angleHeadingToPoint = circleAngle + angleHeadingToPoint;
         }

         return angleHeadingToPoint;    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
    	 List<Double> result = new ArrayList<>();
         double currentHeading = 0;

         for (int i = 0; i < xCoords.size() - 1; i++) {
             double angle = calculateBearingToPoint(currentHeading, xCoords.get(i), yCoords.get(i), xCoords.get(i + 1),
                     yCoords.get(i + 1));
             currentHeading = angle;
             result.add(angle);
         }

         return result;
    }
    
    /**
     * Given a set of points, Compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
    	Set<Point> convexHullPoints = new HashSet<>(); //save the convex hull points
    	Set<Point> remainPoints=new HashSet<>(); //save the points which have not been put into convex hull
    	
    	for (Point point: points)  //let remainPoints equal to points
    		remainPoints.add(point); 
    	
    
    	if (points==null || points.size()==0) //deal with the case that points contains 0 or 1 or 2 points. 
    		return convexHullPoints;
    	else if (points.size()==1 || points.size()==2)
    		return points;
    	   		 
    	Point leftMostPoint=null;    	
    	//find the leftmost point
    	for (Point point: points) {
    		if (leftMostPoint==null)
    			leftMostPoint=point;
    		else if (point.x()<leftMostPoint.x())
    			leftMostPoint=point;    		
    	}
    	//remainPoints.remove(leftMostPoint);
    	//convexHullPoints.add(leftMostPoint);
    	
    	Point curPoint=leftMostPoint;

    	//Point endPoint=leftMostPoint;
    	
    	boolean endFlag=false;
    	while (!remainPoints.isEmpty()) {
    		
    		//璁＄畻鎵�鏈夊墿浣欑偣鍚屽綋鍓嶇偣鐨勮搴︼紝浠ュ強鏈�灏忚搴�
    		Map<Point,Double> nextPoints=new HashMap<>(); //all the points in remainPoints and their angles with current point
    		double minAngle=361;
    		for(Point point:remainPoints) { //璁＄畻鎵�鏈夊墿浣欑偣鍚屽綋鍓嶇偣鐨勮搴︼紝浠ュ強鏈�灏忚搴�
    			// Compute the angle between Y axis
    			double x = point.x() - curPoint.x();
    	        double y = point.y() - curPoint.y();
    	        double targetAngle = Math.acos(y / Math.sqrt(x * x + y * y)) * (straightAngle / Math.PI);
    	        if (x < 0)   
    	            targetAngle = 360 - targetAngle;
    	        if (targetAngle < 0) 
    	        	targetAngle = 360 + targetAngle;
    	        
    	        if (targetAngle<minAngle)  //record the minimum angle
    	        	minAngle=targetAngle;
    	        
    	        nextPoints.put(point, targetAngle);   			
    		}
    		
			
			/*
			 * System.out.println("current point  "+curPoint.x()+" " + curPoint.y());
			 * for(Point point:nextPoints.keySet()) {
			 * System.out.println("瑕佹娴嬬偣  "+point.x()+" " + point.y() + "  "+
			 * nextPoints.get(point)); } System.out.println("min angle "+minAngle);
			 */
			 
    		
    		//鑾峰彇鎵�鏈夊叿鏈夋渶灏忚搴︾殑鐐圭殑闆嗗悎锛岃绠楄繖浜涚偣鍚屽綋鍓嶇偣鐨勮窛绂伙紝浠ュ強鏈�澶ц窛绂�
    		Map<Point,Double> pointsWithMinAngle=new HashMap<>(); //all the points with the minimal angle and their distance to current point
    		double maxDistance=0;
    		for(Point point:nextPoints.keySet()) {  
    			if (nextPoints.get(point)!=minAngle)
    				continue;
    			double x = point.x() - curPoint.x();
    	        double y = point.y() - curPoint.y();
    	        
    			double distance=Math.sqrt(x * x + y * y);
    			if (distance>maxDistance)
    				maxDistance=distance;
    			
    			pointsWithMinAngle.put(point, distance);    			
    		}
    		
    		//灏嗗叿鏈夋渶澶ц窛绂荤殑鐐瑰姞鍏ュ埌convex hull涓紝浠庡墿浣欓泦鍚堜腑鍒犻櫎鎵�鏈夎繖浜涚偣
    		for(Point point:pointsWithMinAngle.keySet()) {
    			if (pointsWithMinAngle.get(point)==maxDistance) {   				
    				convexHullPoints.add(point); 
    				curPoint=point;
    				if (curPoint.equals(leftMostPoint))
    					endFlag=true;    				
    				break;    				
    			}
    		}
    		
    		if (endFlag==true)
    			break;
    		remainPoints.removeAll(pointsWithMinAngle.keySet());
    		remainPoints.add(leftMostPoint);
    	}
    	    	  	
    	return convexHullPoints;
        
    }
    
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
    	PenColor[] colorSet = { PenColor.RED, PenColor.GREEN, PenColor.YELLOW, PenColor.GREEN, PenColor.BLACK,
                PenColor.ORANGE };
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 180; j++) {
                turtle.color(colorSet[j % (colorSet.length)]);
                turtle.forward(j);
                turtle.turn(59);
            }
            for (int k = 180; k > 0; k--) {
                turtle.color(colorSet[k % (colorSet.length)]);
                turtle.forward(k);
                turtle.turn(59);
            }
        }
    }

 
    
	public static boolean isSetEqual(Set<Point> set1, Set<Point> set2) {

		if (set1 == null && set2 == null) {
			return true; // Both are null
		} else if (set1.isEmpty() && set2.isEmpty()) {
			return true; // Both are empty
		} else if (set1 == null || set2 == null || set1.size() != set2.size())
			return false;

		boolean isFullEqual = true;

		for (Point point : set1) {
			if (!set2.contains(point)) {
				isFullEqual = false;
				break;
			}
		}

		return isFullEqual;
	}
}