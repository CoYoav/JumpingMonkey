package com.example.jumpingmonkey;

import static com.example.jumpingmonkey.Constants.SPRING_K;

public class RubberBandForceCalculator {

    // Method to calculate the force applied by the rubber band
    public static Vector2D calculateForce(Vector2D screwPoint,Vector2D bodyPoint, double restLength) {
        // Calculate the vector from the body point to the screw point
        Vector2D vectorFromBodyToScrew = new Vector2D(screwPoint.getX() - bodyPoint.getX(), screwPoint.getY() - bodyPoint.getY());

        // Calculate the distance between the two points
        double distance = vectorFromBodyToScrew.getDistance();

        // Calculate the stretch of the rubber band (how much it is extended)
        double stretch = distance - restLength;

        // If the rubber band is stretched, calculate the force
        if (stretch > 0) {
            // The force is in the direction of the vector from the body to the screw
            double forceMagnitude = SPRING_K * stretch ;
            return vectorFromBodyToScrew.withDistance(forceMagnitude); // Apply the force in the correct direction
        } else {
            // If the rubber band is not stretched (or compressed), no force is applied
            return new Vector2D(0, 0); // No force when compressed or at rest length
        }
    }
}
