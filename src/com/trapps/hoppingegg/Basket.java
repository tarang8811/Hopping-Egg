package com.trapps.hoppingegg;

import java.util.Random;

import com.trapps.hoppingegg.framework.DynamicGameObject;

public class Basket extends DynamicGameObject{
	
	public static final float BASKET_WIDTH = 1.9f;
	public static final float BASKET_HEIGHT = 1;
	public static final int BASKET_TYPE_STATIC = 0;
	public static final int BASKET_TYPE_MOVING = 1;
	public static final int BASKET_TYPE_SLANTING = 2;
	public static final int BASKET_STATE_NORMAL = 0;
	public static final int BASKET_STATE_WITHEGG = 1;
	public static final int BASKET_STATE_COUNTDOWN =2;
	public static final int BASKET_STATE_WITHFIRE = 3;
	public static final float BASKET_COUNTDOWN_TIME =5f;
	public  final Random random;
	public static final float BASKET_VELOCITY = 3;
	public static final float BASKET_VELOCITY_Y = 0.5f;
	
	int type;
	int state;
	float stateTime;
	
	
	public Basket(int type, float x, float y, int state){
		super(x, y, BASKET_WIDTH, BASKET_HEIGHT);
		this.type = type;
		this.state = state;
		this.stateTime = 0;
		
		 random = new Random();
		if(type == BASKET_TYPE_MOVING)
			velocity.x = BASKET_VELOCITY * random.nextFloat() + 2;
		if(type == BASKET_TYPE_SLANTING)
		{
			velocity.x = BASKET_VELOCITY * random.nextFloat() + 2 ;
			velocity.y = BASKET_VELOCITY_Y * random.nextFloat() + BASKET_VELOCITY_Y;
		}
		
	}
	
	public void update(float deltaTime){
		if(type == BASKET_TYPE_MOVING){
			
			
			if(position.x < BASKET_WIDTH/2){
				velocity.x = -velocity.x;
				position.x = BASKET_WIDTH/2;
			}
			
			if(position.x > World.WORLD_WIDTH - BASKET_WIDTH / 2) {
                velocity.x = -velocity.x;
                position.x = World.WORLD_WIDTH - BASKET_WIDTH / 2;
            } 
			position.add(velocity.x*deltaTime,0);
			bounds.lowerLeft.set(position).sub(BASKET_WIDTH / 2, BASKET_HEIGHT / 2);
		}
		if(type == BASKET_TYPE_SLANTING)
		{
			
			
			if(position.x < BASKET_WIDTH/2){
				velocity.x = -velocity.x;
				velocity.y = -velocity.y;
				position.x = BASKET_WIDTH/2;
			}
			
			if(position.x > World.WORLD_WIDTH - BASKET_WIDTH / 2) {
                velocity.x = -velocity.x;
                velocity.y = -velocity.y;
                position.x = World.WORLD_WIDTH - BASKET_WIDTH / 2;
            } 
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
			bounds.lowerLeft.set(position).sub(BASKET_WIDTH / 2, BASKET_HEIGHT / 2);
			
		}
		
		if(state != BASKET_STATE_COUNTDOWN)
			stateTime+=deltaTime;
		
		
	}
	
	
	
	

}
