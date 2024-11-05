package com.example.jumpingmonkey;

public class BranchPoint implements CanvasComponent{
    private final int x, y, regenerateTime = 2, collapseTime = 6;
    private boolean onHold, collapsed;
    private double timeToRegeneration = 0, timeToCollapse = this.collapseTime;
    public static double springK = 10;
    public BranchPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.onHold = false;
        this.collapsed = false;
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
    public void regenerate(){
        this.collapsed = false;
    }
    public void  release(){
        this.onHold = false;
        this.timeToCollapse = collapseTime;
    }
    @Override
    public void dutyCycle(double time){
        if(this.onHold) {
            timeToCollapse -= time;
            if(timeToCollapse <= 0) collapse();
        } else if (this.collapsed){
            timeToRegeneration -= time;
            if(timeToRegeneration <= 0) regenerate();
        }

    }

    @Override
    public void draw() {
        //TODO: implement method
    }
    public Vector2D getDisplacement(double x, double y){
        return new Vector2D(x - this.x, y - this.y);
    }
}
