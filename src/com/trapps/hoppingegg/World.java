package com.trapps.hoppingegg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.trapps.hoppingegg.framework.math.OverlapTester;
import com.trapps.hoppingegg.framework.math.Vector2;

public class World {

	 public interface WorldListener {
	        public void jump();

	        public void hit();

			public void point();
	    }
	 
	 public static final float WORLD_WIDTH = 10;
	 public static final float WORLD_HEIGHT = 15 * 100;
	 public static final int WORLD_STATE_RUNNING = 0;
	 public static final int WORLD_STATE_GAMEOVER = 1;
	 public static final Vector2 gravity = new Vector2(0,-11);
	 
	  
	 public final Egg egg;
	 public final List<Basket> baskets;
	 public final List<Bird> birds;
	 public final WorldListener listener;
	 public final Random random;
	 public static int collisioncount = 0;
	 public float heightSoFar;
	 public int state;
	 public int score;
	 public Basket prevbasket;
	 
	 public World(WorldListener listener){
		 
		 this.baskets = new ArrayList<Basket>();
		 this.birds = new ArrayList<Bird>();
		 this.listener = listener;
		 random = new Random();
		 collisioncount = 0;
		 generateLevel();
		 this.egg = new Egg(baskets.get(0).position.x,baskets.get(0).position.y);
		 this.heightSoFar = 0;
		 this.state = WORLD_STATE_RUNNING;
		 this.score = 0;
	 }
	 
	 private void generateLevel()
	 
	 {
		 float y = Basket.BASKET_HEIGHT/2;
		 float maxJumpHeight = Egg.EGG_JUMP_VELOCITY * Egg.EGG_JUMP_VELOCITY/(2*-gravity.y);
		 while(y < WORLD_HEIGHT - WORLD_WIDTH / 2){
			 int type;
			 if(random.nextFloat() <= 0.4f)
			    type = Basket.BASKET_TYPE_STATIC;
			 else if(random.nextFloat() > 0.4f && random.nextFloat() <= 0.75f)
				 	type = Basket.BASKET_TYPE_MOVING;
			 
			  else 
				 	type = Basket.BASKET_TYPE_SLANTING;
			 
			  int basketstate;
			  if(random.nextFloat() < 0.4f && baskets.size() > 10 && baskets.size() != 0)
				  basketstate = Basket.BASKET_STATE_WITHFIRE;
		
			  else if( random.nextFloat() > 0.4f && random.nextFloat() < 0.6f && baskets.size() > 5  && baskets.size() != 0)
				  	basketstate = Basket.BASKET_STATE_COUNTDOWN;
			  else
				  basketstate = Basket.BASKET_STATE_NORMAL;
				  
			  
			 
			 float x = random.nextFloat() * (WORLD_WIDTH - Basket.BASKET_WIDTH) + Basket.BASKET_WIDTH/2;
			 
			 Basket basket = new Basket(type , x , y,basketstate);
			 
			 baskets.add(basket);
			 
			 if (random.nextFloat() > 0.7f && basket.type != Basket.BASKET_TYPE_SLANTING ) {
		            Bird bird = new Bird(basket.position.x
		                    + random.nextFloat(), basket.position.y
		                    + Bird.BIRD_HEIGHT + random.nextFloat() * 2);
		            
		            birds.add(bird);
		        }
			 
			 
			 y += maxJumpHeight - 0.65f;
			 y -= random.nextFloat() * (maxJumpHeight / 3);
		 }
	 }
	 
	 public void update(float deltaTime , float accelX)
	 {
		 
		 updateEgg(deltaTime , accelX);
		 updateBasket(deltaTime , accelX);
		 updateBird(deltaTime);
		 if(egg.state != Egg.EGG_STATE_STATIC)
			 checkforcollision(deltaTime,accelX);
		 checkforGameOver();
	 }
	 
	 private void updateEgg(float deltaTime , float accelX){
		 
		 if( egg.state != Egg.EGG_STATE_STATIC)
			 egg.velocity.x = -accelX/10 * Egg.EGG_SIDE_VELOCITY;
		 
		 if(egg.state == Egg.EGG_STATE_JUMP){
			    egg.velocity.y = Egg.EGG_JUMP_VELOCITY;
			    egg.velocity.x = -accelX/10 * Egg.EGG_SIDE_VELOCITY;
			    egg.state = Egg.EGG_STATE_AIR;}
		
		 
		 
		
			 
		 egg.update(deltaTime);
		 heightSoFar = Math.max(egg.position.y, heightSoFar);
		 
			 
	 }
	 
	 private void updateBasket(float deltaTime, float accelX){
		 int len = baskets.size();
		    for (int i = 0; i < len; i++) {
		        Basket basket = baskets.get(i);
		         
		        if(i == 0) 
		        	basket.type = Basket.BASKET_TYPE_STATIC;
		        if(egg.position.y < basket.position.y && egg.state == Egg.EGG_STATE_FALL)
		        	egg.velocity.set(0,gravity.y/2 );
		        
		        if(basket.type == Basket.BASKET_TYPE_SLANTING && egg.state == Egg.EGG_STATE_STATIC && i == GameScreen.BasketNO -1){
		        	
		        	if(egg.position.x < Basket.BASKET_WIDTH/2 ){
						 egg.velocity.set(-egg.velocity.x,-egg.velocity.y);
							egg.position.x = Basket.BASKET_WIDTH/2;
							
						}
						
						if(egg.position.x > World.WORLD_WIDTH - Basket.BASKET_WIDTH / 2 ) {
							 egg.velocity.set(-egg.velocity.x,-egg.velocity.y);
				            egg.position.x = World.WORLD_WIDTH - Basket.BASKET_WIDTH / 2;
				           
				        } 
						
		        }
		        else if(egg.state == Egg.EGG_STATE_STATIC && basket.type == Basket.BASKET_TYPE_MOVING && i == GameScreen.BasketNO -1){
		        	
					 if(egg.position.x < Basket.BASKET_WIDTH/2){
						 egg.velocity.x = - egg.velocity.x;
							egg.position.x = Basket.BASKET_WIDTH/2;
							}
						
						if(egg.position.x > World.WORLD_WIDTH - Basket.BASKET_WIDTH / 2) {
							egg.velocity.x = - egg.velocity.x;
				            egg.position.x = World.WORLD_WIDTH - Basket.BASKET_WIDTH / 2;
				            
				            } 
				}
		          if(basket.state == Basket.BASKET_STATE_COUNTDOWN && World.collisioncount == i)
		        	  basket.stateTime+=deltaTime;
		       
		        basket.update(deltaTime);
		         
		    }
	 
	 }
	 
	 private void updateBird(float deltaTime) {
		    int len = birds.size();
		    for (int i = 0; i < len; i++) {
		        Bird squirrel = birds.get(i);
		        squirrel.update(deltaTime);
		    }
		}
	 
	 private void checkforcollision(float deltaTime,float accelX){
		 checkBasketcollision(deltaTime, accelX);
		 checkBirdcollisions();
	 }
	 
	 private void checkBasketcollision(float deltaTime,float accelX){
		 
		 if(egg.velocity.y > 0)
			 return;
		 
		 int len = baskets.size();
		 for(int i = 0; i < len; i++){
			 if(i!=0)
			 prevbasket = baskets.get(i-1);
			 
				
			 Basket basket = baskets.get(i);
			 
			 if(egg.position.y > basket.position.y + Basket.BASKET_HEIGHT/2 && i == GameScreen.BasketNO -1){
				 if (OverlapTester
		                    .overlapRectangles(egg.bounds, basket.bounds)){
					 egg.hitBasket();
					 if(basket.state != Basket.BASKET_STATE_WITHFIRE){
					 if(prevbasket.state == Basket.BASKET_STATE_NORMAL){
						 score += 1;
						 listener.point();}
					 else if( prevbasket.state == Basket.BASKET_STATE_COUNTDOWN)
						 	{score +=2;
						 	 listener.point();
						 	 listener.point();}
					 else if(prevbasket.state == Basket.BASKET_STATE_WITHEGG)
						 		{score += 3;
						 		 listener.point();
						 		 listener.point();
						 		 listener.point();}}
						 
					 egg.position.set(basket.position.x,basket.position.y);
					 egg.velocity.set(basket.velocity.x,basket.velocity.y);
					
					 collisioncount++;
					
					
					 
					 break;
				 
			 }
		 }
	 }}
	 
	 private void checkBirdcollisions() {
		    int len = birds.size();
		    for (int i = 0; i < len; i++) {
		        Bird squirrel = birds.get(i);
		        if (OverlapTester.overlapRectangles(squirrel.bounds, egg.bounds)) {
		            state = WORLD_STATE_GAMEOVER;
		            listener.hit();
		        }
		    }
		}
	 private void checkforGameOver(){
		 if(heightSoFar - 8.2f > egg.position.y)
			 {state = WORLD_STATE_GAMEOVER;
			 listener.hit();
			 }
	 }
	 
}
