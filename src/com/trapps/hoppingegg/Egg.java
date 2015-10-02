package com.trapps.hoppingegg;






import com.trapps.hoppingegg.framework.DynamicGameObject;



public class Egg extends DynamicGameObject{
	
	public static final int EGG_STATE_STATIC = 0;
	public static final int EGG_STATE_JUMP = 1;
	public static final int EGG_STATE_FALL = 2;
	public static final int EGG_STATE_AIR =3;
	
	
	public static final float EGG_JUMP_VELOCITY = 13;
	public static final float EGG_SIDE_VELOCITY = 20;
	
	
	public static final float EGG_WIDTH = 0.8f;
	public static final float EGG_HEIGHT = 0.8f;
	
	int state;
	float stateTime;
	
	public Egg(float x, float y)
	{
		super(x, y, EGG_WIDTH, EGG_HEIGHT);
		
		state = EGG_STATE_STATIC;
		stateTime = 0;
	}
	
	public void update(float deltaTime)
	{
		
		
			if(state == Egg.EGG_STATE_AIR){
				velocity.add(World.gravity.x*deltaTime, World.gravity.y*deltaTime);
				if(velocity.y < 0)
					velocity.add(World.gravity.x*deltaTime, World.gravity.y/3 - World.gravity.y*deltaTime*10);
				}
		
		position.add(velocity.x *deltaTime ,velocity.y *deltaTime);
		
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
        if(velocity.y > 0 && state != EGG_STATE_STATIC) {
            if(state != EGG_STATE_AIR) {
                state = EGG_STATE_AIR;
                stateTime = 0;
            }
        }
        
        if(velocity.y < 0 && state != EGG_STATE_STATIC) {
            if(state != EGG_STATE_FALL) {
                state = EGG_STATE_FALL;
                stateTime = 0;
            }
        }
        
        if(state == EGG_STATE_AIR || state == EGG_STATE_FALL)
        {
        	if(position.x > World.WORLD_WIDTH - EGG_WIDTH/2)
        		position.x = World.WORLD_WIDTH - EGG_WIDTH/2;
        	if(position.x < EGG_WIDTH/2)
        		position.x = EGG_WIDTH/2;
        }
       
        
       
        
        stateTime+=deltaTime;
	}
	
	public void hitBasket()
	{
		
		state = EGG_STATE_STATIC;
		stateTime = 0;
	}
	
	

}
