package com.example.jumpingmonkey;

/**
 */
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

    public float getX() {
        return (float) m_x;
    }

    public float getY() {
        return (float) m_y;
    }

    public double getDistance() {
        return Math.sqrt(m_x * m_x + m_y * m_y);
    }

    public double getDirection() {
        return Math.atan2(m_y , m_x);
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
    public Vector2D withDistance(double newDistance) {
        double direction = getDirection();
        return new Vector2D(newDistance * Math.cos(direction), newDistance * Math.sin(direction));
    }

    /**
     * A static function to linearly interpolate between two Vector2D objects.
     *
     * @param start The starting vector.
     * @param end The ending vector.
     * @param t The interpolation parameter, where 0 <= t <= 1.
     * @return A new Vector2D that is the result of the interpolation.
     */
    public static Vector2D lerp(Vector2D start, Vector2D end, double t) {
        double x  = start.getX() + (end.getX() - start.getX()) * t;
        double y = start.getY() + (end.getY() - start.getY()) * t;
        return new Vector2D(x, y);
    }
}
