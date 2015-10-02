package com.trapps.hoppingegg;

import com.trapps.hoppingegg.framework.DynamicGameObject;

public class Bird extends DynamicGameObject {
    public static final float BIRD_WIDTH = 1.8f;
    public static final float BIRD_HEIGHT = 1.6f;
    public static final float BIRD_VELOCITY = 3f;
    
    float stateTime = 0;
    
    public Bird(float x, float y) {
        super(x, y, BIRD_WIDTH, BIRD_HEIGHT);
        velocity.set(BIRD_VELOCITY, 0);
    }
    
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(BIRD_WIDTH / 2, BIRD_HEIGHT / 2);
        
        if(position.x < BIRD_WIDTH / 2 ) {
            position.x = BIRD_WIDTH / 2;
            velocity.x = BIRD_VELOCITY;
        }
        if(position.x > World.WORLD_WIDTH - BIRD_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - BIRD_WIDTH / 2;
            velocity.x = -BIRD_VELOCITY;
        }
        stateTime += deltaTime;
    }
}

