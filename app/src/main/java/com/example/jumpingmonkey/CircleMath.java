package com.example.jumpingmonkey;

public class CircleMath {
    public static double[] findIntersectionPoints(
            double x1, double y1, double r1,
            double x2, double y2, double r2) {

        // Distance between the centers
        double d = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        // Check if there are no intersections
        if (d > r1 + r2 || d < Math.abs(r1 - r2) || d == 0) {
            return new double[0]; // No intersection
        }

        // Find the distance from (x1, y1) to the line joining the points of intersection
        double a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);

        // Find the midpoint between the two intersection points
        double midpointX = x1 + a * (x2 - x1) / d;
        double midpointY = y1 + a * (y2 - y1) / d;

        // Calculate the distance from the midpoint to each intersection point
        double h = Math.sqrt(r1 * r1 - a * a);

        // Offset vector from midpoint to each intersection point
        double offsetX = h * (y2 - y1) / d;
        double offsetY = h * (x2 - x1) / d;

        // The two intersection points
        double x3 = midpointX + offsetX;
        double y3 = midpointY - offsetY;
        double x4 = midpointX - offsetX;
        double y4 = midpointY + offsetY;

        // Return the intersection points as an array
        return new double[] {x3, y3, x4, y4};
    }
}
