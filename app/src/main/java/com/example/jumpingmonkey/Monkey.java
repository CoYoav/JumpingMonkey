package com.example.jumpingmonkey;

public class Monkey implements CanvasComponent {
    private int x;
    private int y;
    private Vector2D velocity;
    private boolean onJump;
    private double mass = 5;
    private BranchPoint currentPoint;
    private double radius = 0.3;

    public Monkey(BranchPoint startPoint) {
        this.x = 100;
        this.y = 100;
        this.currentPoint = startPoint;
        this.velocity = new Vector2D(0, 0);
    }

    private boolean onDrag() {
        return this.currentPoint == null;
    }

    public Vector2D getForce() {
        Vector2D sigmaForce = new Vector2D(0, 0);
        Vector2D gravity = new Vector2D(0, -9.8 * mass);
        sigmaForce = sigmaForce.plus(gravity);
        if (this.currentPoint != null)
            sigmaForce = sigmaForce.plus(this.currentPoint
                    .getDisplacement(this.x, this.y).mul(BranchPoint.springK));
        return sigmaForce;
    }

    @Override
    public void dutyCycle(double time) {
        if (this.onDrag()) {
            //TODO: implement
        } else {
            Vector2D sigmaForce = getForce();
            Vector2D acceleration = sigmaForce.mul(1 / this.mass);
            this.velocity = this.velocity.plus(acceleration.mul(time));
            this.x += this.velocity.getX() * time;
            this.y += this.velocity.getY() * time;
            if (currentPoint != null && currentPoint.getDisplacement(this.x, this.y).getDistance() < this.radius) {
                currentPoint.release();
                currentPoint = null;
            }
        }
    }

    @Override
    public void draw() {

    }
}
