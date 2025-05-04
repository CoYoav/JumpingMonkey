package com.example.jumpingmonkey.animation;

import com.example.jumpingmonkey.physics.Vector2D;

import java.util.Random;

public class Stone {
    public  final Vector2D position;
    Type type;
    public Stone(Vector2D position){
        this.position = position;
        this.type = new Random().nextInt(5)!=0? Type.LAVA_STONE : Type.REGULAR_STONE;
    }

    public enum Type {
        LAVA_STONE,
        REGULAR_STONE
    }
}
