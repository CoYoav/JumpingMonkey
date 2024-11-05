package com.example.jumpingmonkey;

public class Vector2D {
    private final double m_x, m_y;

    /**
     * A constructor that takes two doubles representing the x and y components:
     *
     * @param x The x component of the Vector
     * @param y The y component of the Vector
     */
    public Vector2D(double x, double y) {
        this.m_x = x;
        this.m_y = y;
    }

    /**
     * A constructor that takes a double representing the distance,
     * and a Rotation2D representing the angle:
     *
     * @param magnitude The distance from the origin
     * @param direction The angle of the Vector
     */
    public static Vector2D fromPollar(double magnitude, double direction) {
        return new Vector2D(magnitude * Math.cos(direction), magnitude * Math.sin(direction));
    }

    public double getX() {
        return m_x;
    }

    public double getY() {
        return m_y;
    }

    public double getDistance() {
        return Math.sqrt(m_x * m_x + m_y * m_y);
    }

    public double getDirection() {
        return Math.atan(m_y / m_x);
    }

    public Vector2D plus(Vector2D other) {
        return new Vector2D(m_x + other.m_x, m_y + other.m_y);
    }

    /**
     * A function that multiplies the Vector by a scalar
     *
     * @return a new Vector2D that represents the product of the Vectors
     */
    public Vector2D mul(double scalar) {
        return new Vector2D(m_x * scalar, m_y * scalar);
    }

    public Vector2D rotate(double deltaDirection) {
        return new Vector2D(getDistance(), getDirection() + deltaDirection);
    }
}
