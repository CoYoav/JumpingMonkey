package com.example.jumpingmonkey.animation;

import com.example.jumpingmonkey.physics.Vector2D;

public final class Segment {
    private BranchPoint[] brunchPoints;
    private Stone[] stones;
    private double bottom, top;
    private final int id;

    private Segment(int id, double bottom, double margin, Stone[] stones, BranchPoint... brunchPoints) {
        this.id = id;
        this.top = Double.NEGATIVE_INFINITY;
        for (BranchPoint point : brunchPoints) top = Math.max(top, margin + point.getTop());
        this.bottom = bottom;

        this.brunchPoints = brunchPoints;
        this.stones = stones;
    }

    public BranchPoint[] getBrunchPoints() {
        return brunchPoints;
    }

    public Stone[] getStonePoints() {
        return stones;
    }

    public double getBottom() {
        return bottom;
    }

    public double getTop() {
        return top;
    }

    public int getId() {
        return id;
    }

    public void dutyCycle(double time) {
        for (BranchPoint branchPoint : this.brunchPoints) branchPoint.dutyCycle(time);
    }

    public static Segment generateSegment(double bottom, double margin, int segmentID) {
        switch (segmentID) {
            case 0:
                return new Segment(
                        segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(1.5, bottom + 3))
                        },
                        new BranchPoint(1, bottom),
                        new BranchPoint(4, bottom + 3),
                        new BranchPoint(1, bottom + 6)
                );
            case 1:
                return new Segment(
                        segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(3.5, bottom + 3))
                        },
                        new BranchPoint(4, bottom),
                        new BranchPoint(1, bottom + 3),
                        new BranchPoint(4, bottom + 6)
                );
            case 2:
                return new Segment(
                        segmentID, bottom, margin,
                        new Stone[]{},
                        new VinePoint(0.6, bottom, 4.4, bottom + 8, 0),
                        new VinePoint(4.4, bottom + 9, 0.6, bottom + 16, 1)
                );
            case 3:
                return new Segment(
                        segmentID, bottom, margin,
                        new Stone[]{},
                        new VinePoint(4.4, bottom, 0.6, bottom + 8, 1),
                        new VinePoint(0.6, bottom + 9, 4.4, bottom + 16, 0)
                );
            case 4:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new VinePoint(0.39, bottom, 4.6, bottom, 0),
                        new VinePoint(4.61, bottom + 1, 0.4, bottom + 1, 0.2),
                        new VinePoint(0.39, bottom + 2, 4.6, bottom + 2, 0.4),
                        new VinePoint(4.61, bottom + 3, 0.4, bottom + 3, 0.6),
                        new VinePoint(0.39, bottom + 4, 4.6, bottom + 4, 0.8),
                        new VinePoint(4.61, bottom + 5, 0.4, bottom + 5, 1));
            case 5:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new VinePoint(4.61, bottom, 1, bottom, 0),
                        new VinePoint(0.39, bottom + 1, 4.6, bottom + 1, 0.2),
                        new VinePoint(4.61, bottom + 2, 0.4, bottom + 2, 0.4),
                        new VinePoint(0.39, bottom + 3, 4.6, bottom + 3, 0.6),
                        new VinePoint(4.61, bottom + 4, 0.4, bottom + 4, 0.8),
                        new VinePoint(0.39, bottom + 5, 4.6, bottom + 5, 1));
            case 6:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new BranchPoint(1.1, bottom),
                        new BranchPoint(3.7, bottom + 1));
            case 7:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new BranchPoint(3.9, bottom),
                        new BranchPoint(1.3, bottom + 1));
            case 8:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(2.5, bottom + 3.5))
                        },
                        new VinePoint(0.4, bottom, 4.61, bottom + 2.5, 0.5),
                        new VinePoint(4.6, bottom + 4.5, 0.39, bottom + 7, 0.5)
                );
            case 9:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(2.5, bottom + 3.5))
                        },
                        new VinePoint(4.61, bottom, 0.4, bottom + 2.5, 0.5),
                        new VinePoint(0.39, bottom + 4.5, 4.6, bottom + 7, 0.5)
                );
            case 10:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(2.2, bottom + 4.5))
                        },
                        new BranchPoint(2.8, bottom),
                        new BranchPoint(2.2, bottom + 2),
                        new VinePoint(1, bottom + 7, 4, bottom + 7, 0.5)
                );
            case 11:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(2.8, bottom + 4.5))
                        },
                        new BranchPoint(2.2, bottom),
                        new BranchPoint(2.8, bottom + 2),
                        new VinePoint(4, bottom + 7, 1, bottom + 7, 0.5)
                );
            case 12:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(0.5, bottom + 3.5)),
                                new Stone(new Vector2D(4.5, bottom + 3.5))
                        },
                        new BranchPoint(1, bottom),
                        new BranchPoint(3, bottom),
                        new BranchPoint(2, bottom + 7),
                        new BranchPoint(4, bottom + 7)
                );
            case 13:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(0.5, bottom + 3.5)),
                                new Stone(new Vector2D(4.5, bottom + 3.5))
                        },
                        new BranchPoint(2, bottom),
                        new BranchPoint(4, bottom),
                        new BranchPoint(1, bottom + 7),
                        new BranchPoint(3, bottom + 7)
                );
            case 14:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(2.5, bottom + 3))
                        },
                        new BranchPoint(4, bottom),
                        new BranchPoint(1.5, bottom + 6)
                );
            case 15:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{
                                new Stone(new Vector2D(2.5, bottom + 3))
                        },
                        new BranchPoint(3.5, bottom),
                        new BranchPoint(1, bottom + 6)
                );
            case 16:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new BranchPoint(4, bottom),
                        new BranchPoint(1, bottom + 0.5),
                        new VinePoint(1, bottom + 2.5, 4, bottom + 3.5, 0),
                        new BranchPoint(3.5, 5)

                );
            case 17:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new BranchPoint(1, bottom),
                        new BranchPoint(4, bottom + 0.5),
                        new VinePoint(4, bottom + 2.5, 1, bottom + 3.5, 0),
                        new BranchPoint(1.5, 5)

                );
            case 18:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new VinePoint(1, bottom, 4, bottom, 0),
                        new VinePoint(1, bottom + 1, 4, bottom + 1, 1),
                        new VinePoint(1, bottom + 2.5, 1, bottom + 7, 1),
                        new VinePoint(4, bottom + 2.5, 4, bottom + 7, 0)

                );
            case 19:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new BranchPoint(1, bottom),
                        new BranchPoint(4, bottom + 0.5),
                        new VinePoint(4, bottom + 2.5, 1, bottom + 3.5, 0),
                        new BranchPoint(1.5, 5)

                );

            default:
                return new Segment(segmentID, bottom, margin,
                        new Stone[]{},
                        new BranchPoint(bottom, 10 / 9.0),
                        new BranchPoint(70 / 9.0, bottom),
                        new BranchPoint(2.5, bottom + 0.3));
        }
    }

    public static boolean areNumbersValid(int num1, int num2) {
        // If the numbers are equal, return true
        if (num1 == num2) {
            return true;
        }

        // If the difference between the numbers is 1 and the smaller number is even, return true
        if (Math.abs(num1 - num2) == 1 && Math.min(num1, num2) % 2 == 0) {
            return true;
        }

        // Otherwise, return false
        return false;
    }
}
