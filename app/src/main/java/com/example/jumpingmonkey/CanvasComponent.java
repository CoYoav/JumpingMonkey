package com.example.jumpingmonkey;

public interface CanvasComponent {
    double TIME = 0.05;
    void dutyCycle(double time);
    void draw();
}