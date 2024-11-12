package com.example.jumpingmonkey;

import static com.example.jumpingmonkey.Constants.WIDTH_METERS;

import java.util.ArrayList;
import java.util.Random;

public class BranchPoint {
    private final double regenerateTime = 2, collapseTime = 6;
    protected Vector2D mainPoint;
    private boolean onHold, collapsed;
    private double timeToRegeneration = 0, timeToCollapse = this.collapseTime;
    public final Vector2D rootPoint;
    public final Vector2D lowerRootPoint;
    protected int r, g, b;
    private double timeSinceRelease = 2;

    public BranchPoint(double x, double y) {
        Random random = new Random();
        this.mainPoint = new Vector2D(x, y);
        this.onHold = false;
        this.collapsed = false;
        double angle = random.nextInt(13);
        if (x < WIDTH_METERS / 2) {
            angle = 180 - angle;
        }
        double thickness = (random.nextInt(40) + 30) / 100.0;

        double fullBranchX = angle > 90 ? -this.mainPoint.getX() : WIDTH_METERS - this.mainPoint.getX();
        double fullBranchY = Math.tan(Math.PI * angle / 180) * fullBranchX;
        this.rootPoint = new Vector2D(this.mainPoint.getX() + fullBranchX, fullBranchY + this.mainPoint.getY());
        this.lowerRootPoint = new Vector2D(this.mainPoint.getX() + fullBranchX, this.mainPoint.getY() + fullBranchY - thickness);

        r = 117 + random.nextInt(40) - 20;
        g = 57 + random.nextInt(40) - 20;
        b = 33 + random.nextInt(40) - 20;
    }

    public void hold() {
        this.onHold = true;
        timeToCollapse = collapseTime;
    }

    public void collapse() {
        this.collapsed = true;
        this.onHold = false;
        timeToRegeneration = regenerateTime;
    }

    public void regenerate() {
        this.collapsed = false;
    }

    public void release() {
        this.onHold = false;
        this.timeToCollapse = collapseTime;
        this.timeSinceRelease = 0;
    }

    public void dutyCycle(double time) {
        this.timeSinceRelease += time;
        if (this.onHold) {
            timeToCollapse -= time;
            if (timeToCollapse <= 0) collapse();
        } else if (this.collapsed) {
            timeToRegeneration -= time;
            if (timeToRegeneration <= 0) regenerate();
        }

    }

    public Vector2D getSpringForce(double x, double y) {
        return RubberBandForceCalculator.calculateForce(new Vector2D(this.mainPoint.getX(), this.mainPoint.getY()), new Vector2D(x, y), 0);
    }

    public double getX() {
        return this.mainPoint.getX();
    }

    public double getY() {
        return this.mainPoint.getY();
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public Vector2D getDisplacement(double x, double y) {
        return new Vector2D(x - this.mainPoint.getX(), y - this.mainPoint.getY());
    }

    public ArrayList<Vector2D> getDrawnPoints() {
        ArrayList<Vector2D> drawnPoints = new ArrayList<>();
        drawnPoints.add(this.rootPoint);
        Vector2D upperFlowerPoint = getUpperFlowerPoint();
        Vector2D lowerFlowerPoint = getLowerFlowerPoint();

        if (this.collapsed) {
            double t = 0.4 + ((2 - this.timeToRegeneration) / 2) * 0.6;
            drawnPoints.add(Vector2D.lerp(this.rootPoint, upperFlowerPoint, t));
            drawnPoints.add(Vector2D.lerp(this.lowerRootPoint, lowerFlowerPoint, t));
        } else if (this.onHold) {
            drawnPoints.add(Vector2D.lerp(this.rootPoint, upperFlowerPoint, 0.4));
            double t = timeToCollapse / collapseTime;
            Vector2D upperCenter, lowerCenter, breakPoint;
            upperCenter = Vector2D.lerp(rootPoint, upperFlowerPoint, 0.4);
            lowerCenter = Vector2D.lerp(lowerRootPoint, lowerFlowerPoint, 0.4);
            breakPoint = Vector2D.lerp(lowerCenter, upperCenter, t);
            drawnPoints.add(breakPoint);
            upperCenter = Vector2D.lerp(rootPoint, upperFlowerPoint, 0.45);
            lowerCenter = Vector2D.lerp(lowerRootPoint, lowerFlowerPoint, 0.45);
            breakPoint = Vector2D.lerp(lowerCenter, upperCenter, t);
            drawnPoints.add(breakPoint);
            drawnPoints.add(Vector2D.lerp(rootPoint, upperFlowerPoint, 0.45));
            drawnPoints.add(upperFlowerPoint);
            drawnPoints.add(lowerFlowerPoint);
        } else {
            drawnPoints.add(upperFlowerPoint);
            drawnPoints.add(lowerFlowerPoint);

        }
        drawnPoints.add(this.lowerRootPoint);
        return drawnPoints;
    }

    private Vector2D getUpperFlowerPoint() {
        double[] upperIntersects = CircleMath.findIntersectionPoints(rootPoint.getX(), rootPoint.getY(), Math.sqrt(Math.pow(this.mainPoint.getX() - rootPoint.getX(), 2) + Math.pow(this.mainPoint.getY() - rootPoint.getY(), 2)), this.mainPoint.getX(), this.mainPoint.getY(), 0.1);
        return upperIntersects[1] > upperIntersects[3] ?
                new Vector2D(upperIntersects[0], upperIntersects[1]) :
                new Vector2D(upperIntersects[2], upperIntersects[3]);
    }

    private Vector2D getLowerFlowerPoint() {
        double[] loweIntersects = CircleMath.findIntersectionPoints(lowerRootPoint.getX(), lowerRootPoint.getY(), Math.sqrt(Math.pow(this.mainPoint.getX() - lowerRootPoint.getX(), 2) + Math.pow(this.mainPoint.getY() - lowerRootPoint.getY(), 2)), this.mainPoint.getX(), this.mainPoint.getY(), 0.1);
        return loweIntersects[1] < loweIntersects[3] ?
                new Vector2D(loweIntersects[0], loweIntersects[1]) :
                new Vector2D(loweIntersects[2], loweIntersects[3]);

    }

    public boolean isAvailable() {
        return !this.collapsed && timeSinceRelease > 1;
    }

    protected void setMainPoint(Vector2D mainPoint) {
        this.mainPoint = mainPoint;
    }
}
