package com.example.jumpingmonkey;


import static com.example.jumpingmonkey.Constants.DRAG_COEFFICIENT;
import static com.example.jumpingmonkey.Constants.WIDTH_METERS;

public class Monkey {
    private double x, y;
    private Vector2D velocity;
    private boolean onJump;
    private double mass = 1.2;
    private BranchPoint currentPoint;
    private double radius = 0.3;
    private boolean onDrag;

    public Monkey(BranchPoint startPoint) {
        this.x = 2.5;
        this.y = 4;
        this.currentPoint = startPoint;
        this.velocity = new Vector2D(0, 4);
        this.onJump = true;
        this.onDrag = false;
    }

    public boolean onDrag() {
        return this.onDrag;
    }

    private Vector2D getForce() {
        Vector2D sigmaForce = new Vector2D(0, 0);

        // Gravity force
        Vector2D gravity = new Vector2D(0, -10 * mass);
        sigmaForce = sigmaForce.plus(gravity);

        // Spring force from the current branch point
        if (this.currentPoint != null) {
            Vector2D dis_spring = this.currentPoint.getSpringForce(this.x, this.y);
            sigmaForce = sigmaForce.plus(dis_spring);
        }

        // Air resistance (drag force)
        double dragCoeff = DRAG_COEFFICIENT /(( this.currentPoint != null && !this.onJump)? 1: 50);
            Vector2D drag = velocity.mul(-dragCoeff * velocity.getDistance());
            sigmaForce = sigmaForce.plus(drag);

        return sigmaForce;
    }

    public void dutyCycle(double time) {
        if (this.currentPoint != null && this.currentPoint.isCollapsed()) {
            this.currentPoint = null;
            this.onJump = true;
            this.onDrag = false;
        }
        if (!this.onDrag) {
            Vector2D sigmaForce = getForce();
            Vector2D acceleration = sigmaForce.mul(1 / this.mass);
            this.velocity = this.velocity.plus(acceleration.mul(time));
            if (this.x - this.radius < 0)
                this.velocity = new Vector2D(Math.abs(this.velocity.getX()), this.velocity.getY());
            if (this.x + this.radius > WIDTH_METERS)
                this.velocity = new Vector2D(-Math.abs(this.velocity.getX()), this.velocity.getY());

            this.x += this.velocity.getX() * time;
            this.y += this.velocity.getY() * time;
            if (this.onJump && this.currentPoint != null && Math.sqrt(Math.pow(this.x - currentPoint.getX(), 2) + Math.pow(this.y - currentPoint.getY(), 2)) < this.radius) {
                this.currentPoint.release();
                this.currentPoint = null;
            }
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public BranchPoint getCurrentPoint() {
        return currentPoint;
    }

    public void catchPoint(BranchPoint point) {
        if (this.currentPoint != null) return;
        if(!point.isAvailable()) return;
        if (Math.sqrt(Math.pow(point.getX() - this.x, 2) + Math.pow(point.getY() - this.y, 2)) < this.radius) {
            this.currentPoint = point;
            this.onJump = false;
            point.hold();
        }
    }

    public double getRadius() {
        return radius;
    }

    public double[] findTangents() {
        if (this.currentPoint == null) return new double[]{};
        // Calculate the vector from the circle's center to the external point
        Vector2D centerToPoint = new Vector2D(
                this.x - this.currentPoint.getX(),
                this.y - this.currentPoint.getY());
        double dist = centerToPoint.getDistance();

        // If the point is inside or on the circle, return an empty array
        if (dist <= this.radius) {
            return new double[]{};
        }
        double sideLength = Math.sqrt(Math.pow(this.radius, 2) + Math.pow(dist, 2));
        double[] tangents = CircleMath.findIntersectionPoints(this.x, this.y, this.radius * 0.85, this.currentPoint.getX(), this.currentPoint.getY(), sideLength);
        return tangents;
    }

    public double getAngleRad() {
        double angleRadians = this.velocity.getDirection() + Math.PI / 2;
        if (currentPoint != null) {
            angleRadians = new Vector2D(
                    x - currentPoint.getX(),
                    y - currentPoint.getY()
            ).getDirection() + Math.PI / 2;
        }
        return angleRadians;
    }

    public void drag(double x, double y) {
        if (this.currentPoint == null) return;
        if(Math.sqrt(Math.pow(x-this.x,2)+Math.pow(y-this.y,2))>this.radius) return;
        this.velocity = new Vector2D(0, 0);
        this.onDrag = true;
        setPosition(x, y);

    }

    public void setPosition(double x, double y) {
        if(this.currentPoint == null) return;
        Vector2D brunchToPoint = new Vector2D(x-this.currentPoint.getX(), y - this.currentPoint.getY());
        if(brunchToPoint.getDistance()>1.8) brunchToPoint = brunchToPoint.withDistance(1.8);
        this.x = this.currentPoint.getX()+brunchToPoint.getX();
        this.y = this.currentPoint.getY() + brunchToPoint.getY();
    }

    public void jump() {
        this.onDrag = false;
        this.onJump = true;
    }
}
