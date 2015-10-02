package com.trapps.hoppingegg;

import javax.microedition.khronos.opengles.GL10;

import com.trapps.hoppingegg.framework.gl.Animation;
import com.trapps.hoppingegg.framework.gl.Camera2D;
import com.trapps.hoppingegg.framework.gl.SpriteBatcher;
import com.trapps.hoppingegg.framework.gl.TextureRegion;
import com.trapps.hoppingegg.framework.impl.GLGraphics;

public class WorldRenderer {
	
	static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;    
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;
    long startTime;
 
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;        
    }
    
    public void render() {
        if(world.egg.position.y > cam.position.y )
            cam.position.y = world.egg.position.y;
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
        
    	}
    public void renderBackground() {
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(cam.position.x, cam.position.y,
                           FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                           Assets.backgroundRegion);
        batcher.endBatch();
    }
    
    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.images);
        
        renderBasket();
        renderEgg();
        renderBird();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }
    
   
    private void renderEgg(){
    	
    	if(world.egg.state == Egg.EGG_STATE_AIR || world.egg.state == Egg.EGG_STATE_FALL)
    		batcher.drawSprite(world.egg.position.x, world.egg.position.y, 0.8f, 1.2f, Assets.egg);
    }
    
    private void renderBasket(){
    	int len = world.baskets.size();
    	TextureRegion KeyFrame;
    	if(GameScreen.BasketNO == 1 )
    	{
    	if(world.egg.state == Egg.EGG_STATE_STATIC )
    	{
			KeyFrame = Assets.basketWithEgg;
			
		}
		else
			KeyFrame = Assets.basket;
    	batcher.drawSprite(world.baskets.get(0).position.x, world.baskets.get(0).position.y, 2, 2, KeyFrame);
    	}
    	for(int i=0; i<len;i++){
    		Basket basket = world.baskets.get(i);
    		
    		
    	
    		
    		if(basket.state == Basket.BASKET_STATE_WITHFIRE )
    		{
    			 KeyFrame = Assets.fireAnim.getKeyFrame(basket.stateTime, Animation.ANIMATION_LOOPING);
    			 if(Animation.frameNumber == 1 && basket.state != Basket.BASKET_STATE_WITHEGG){
    				 batcher.drawSprite(basket.position.x, basket.position.y+0.6f, 2, 4, KeyFrame);
    				 if(world.egg.state == Egg.EGG_STATE_STATIC && i == GameScreen.BasketNO -1  ){
    					 world.state = World.WORLD_STATE_GAMEOVER;
    					 Assets.playSound(Assets.hitSound);
    				 }continue;
    			 }
    			 else
    			 {
    				 if(world.egg.state == Egg.EGG_STATE_STATIC && i == GameScreen.BasketNO -1  )
    				 {
    					 KeyFrame = Assets.basketWithEgg;
    					 basket.state = Basket.BASKET_STATE_WITHEGG;
    					 world.score += 1;
    					 Assets.playSound(Assets.pointSound);}
    				 		
    			 }
    		}
    		
    		else
    			if(world.egg.state == Egg.EGG_STATE_STATIC && i == GameScreen.BasketNO -1  )
    	    		
        			KeyFrame = Assets.basketWithEgg;
        		else
    				KeyFrame = Assets.basket;
    		
    		 if(basket.state == Basket.BASKET_STATE_COUNTDOWN  && World.collisioncount == i)
     		{
    			 
     			KeyFrame = Assets.countdownAnim.getKeyFrame(basket.stateTime, Animation.ANIMATION_NONLOOPING);
     				
     				 if(Animation.frameNumber == 3 && world.egg.state != Egg.EGG_STATE_AIR)
     				{
     				world.egg.state = Egg.EGG_STATE_FALL;
     				renderEgg();
     				world.egg.velocity.set(0,-5);
     				}
     				 else if(world.egg.state != Egg.EGG_STATE_STATIC){
     					KeyFrame = Assets.basket;
     				}
     		}
    		
    			
			batcher.drawSprite(basket.position.x, basket.position.y, 2, 2, KeyFrame);
    	
    		
    			
    		
    		
    		
    	}
    }
    
 private void renderBird(){
    	
        
        int len = world.birds.size();
        for(int i = 0; i < len; i++) {
        	
            Bird bird = world.birds.get(i);
            TextureRegion keyFrame = Assets.birdAnim.getKeyFrame(bird.stateTime, Animation.ANIMATION_LOOPING);
            float side = bird.velocity.x < 0?-1:1;
            
            batcher.drawSprite(bird.position.x, bird.position.y, side * 2, 2, keyFrame);
        }  
        
    }
    

}
