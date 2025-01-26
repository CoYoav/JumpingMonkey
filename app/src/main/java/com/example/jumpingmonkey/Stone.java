package com.example.jumpingmonkey;

import java.util.Random;

public class Stone {
    public  final Vector2D position;
    Type type;
    public Stone(Vector2D position){
        this.position = position;
        this.type = new Random().nextBoolean()? Type.LAVA_STONE : Type.REGULAR_STONE;
    }

    public enum Type {
        LAVA_STONE,
        REGULAR_STONE
    }
}
