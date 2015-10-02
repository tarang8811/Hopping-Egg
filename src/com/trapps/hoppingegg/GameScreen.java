package com.trapps.hoppingegg;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.trapps.hoppingegg.World.WorldListener;
import com.trapps.hoppingegg.framework.Game;
import com.trapps.hoppingegg.framework.Input.TouchEvent;
import com.trapps.hoppingegg.framework.gl.Camera2D;
import com.trapps.hoppingegg.framework.gl.FPSCounter;
import com.trapps.hoppingegg.framework.gl.SpriteBatcher;
import com.trapps.hoppingegg.framework.impl.GLGame;
import com.trapps.hoppingegg.framework.impl.GLScreen;
import com.trapps.hoppingegg.framework.math.OverlapTester;
import com.trapps.hoppingegg.framework.math.Rectangle;
import com.trapps.hoppingegg.framework.math.Vector2;

public class GameScreen extends GLScreen {
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static int BasketNO = 0;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;    
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;    
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    Rectangle restartBounds;
    Rectangle rateusBounds;
    Rectangle ExitBounds;
    int lastScore;
    String scoreString;    
    FPSCounter fpsCounter;
    GLGame gl;
    int  c=0;
    
    public GameScreen(Game game) {
        super(game);
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        worldListener = new WorldListener() {
            @Override
            public void jump() {            
                Assets.playSound(Assets.jumpSound);
            }

           public void point(){
        	   Assets.playSound(Assets.pointSound);
           }

            @Override
            public void hit() {
                Assets.playSound(Assets.hitSound);
            }
                    
        };
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        pauseBounds = new Rectangle(320- 64, 480- 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
        quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
        restartBounds = new Rectangle(87, 240 , 157, 33);
        rateusBounds = new Rectangle(87, 240-100 , 157, 33);
        ExitBounds = new Rectangle(87, 240 - 160, 157, 33);
        lastScore = 0;
        scoreString = "0";
        fpsCounter = new FPSCounter();
        gl = (GLGame)game;
    }

	@Override
	public void update(float deltaTime) {
	    if(deltaTime > 0.1f)
	        deltaTime = 0.1f;
	    
	    switch(state) {
	    case GAME_READY:
	        updateReady();
	        break;
	    case GAME_RUNNING:
	        updateRunning(deltaTime);
	        break;
	    case GAME_PAUSED:
	        updatePaused();
	        break;
	    
	    case GAME_OVER:
	        updateGameOver();
	        break;
	    }
	}
	
	private void updateReady() {
	    if(game.getInput().getTouchEvents().size() > 0) {
	        state = GAME_RUNNING;
	    }
	}
	
	private void updateRunning(float deltaTime) {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        if(world.egg.state == Egg.EGG_STATE_STATIC)
	        BasketNO++;
	        
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	     
	        if(world.egg.state != Egg.EGG_STATE_FALL && world.egg.state != Egg.EGG_STATE_AIR && BasketNO != 1 && c!=1)
	        	world.egg.state = Egg.EGG_STATE_JUMP;
	        c =0;
	        if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            state = GAME_PAUSED;
	            return;
	        }
	        else  if(BasketNO != 1 && world.egg.state != Egg.EGG_STATE_AIR && world.egg.state != Egg.EGG_STATE_FALL)
	        	Assets.playSound(Assets.jumpSound);
	        
	        	
	    }
	    
	    world.update(deltaTime, game.getInput().getAccelX());
	    if(world.score != lastScore) {
	        lastScore = world.score;
	        scoreString = "" + lastScore;
	    }
	    
	    if(world.state == World.WORLD_STATE_GAMEOVER) {
	        state = GAME_OVER;
	        if(lastScore >= Settings.highscores[1]) 
	            scoreString = " " + lastScore;
	        else
	            scoreString = " " + lastScore;
	        Settings.addScore(lastScore);
	        Settings.save(game.getFileIO());
	    }
	}
	
	private void updatePaused() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	        
	        if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            c = 1;
	            state = GAME_RUNNING;
	            return;
	        }
	        
	        if(OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            BasketNO = 0;
	            game.setScreen(new MainMenuScreen((Game) game));
	            return;
	        
	        }
	    }
	}
	
	
	
	private void updateGameOver() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {                   
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        BasketNO =0;
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	        if(OverlapTester.pointInRectangle(restartBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            BasketNO = 0;
	            state = GAME_READY;
	            game.setScreen(new GameScreen((Game) game));
	            return;
	        
	        }
	        if(OverlapTester.pointInRectangle(rateusBounds,touchPoint))
            {
            	Assets.playSound(Assets.clickSound);
            	gl.rateus();
            	return;
            }
	        if(OverlapTester.pointInRectangle(ExitBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            BasketNO = 0;
	            
	            game.setScreen(new MainMenuScreen((Game) game));
	            return;
	        
	        }
	     
	    }
	}

	@Override
	public void present(float deltaTime) {
	    GL10 gl = glGraphics.getGL();
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	    
	    renderer.render();
	    
	    guiCam.setViewportAndMatrices();
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    batcher.beginBatch(Assets.images);
	    switch(state) {
	    case GAME_READY:
	    	
	        presentReady();
	        break;
	    case GAME_RUNNING:
	    	
	        presentRunning();
	        break;
	    case GAME_PAUSED:
	    	
	        presentPaused();
	        
	        break;
	    
	    case GAME_OVER:
	    	GameScreen.BasketNO =0;
	    	
	        presentGameOver();
	        
	        break;
	    }
	    batcher.endBatch();
	    gl.glDisable(GL10.GL_BLEND);
	    fpsCounter.logFrame();
	}
	
	private void presentReady() {
	    batcher.drawSprite(160, 240, 212, 32, Assets.ready);
	}
	
	private void presentRunning() {
	    batcher.drawSprite(320 - 32, 480 - 32-32, 64, 64, Assets.pause);
	    Assets.font.drawText(batcher, scoreString, 160, 245);
	}
	
	private void presentPaused() {        
	    batcher.drawSprite(160, 240, 192, 97, Assets.pauseMenu);
	    Assets.font.drawText(batcher, scoreString, 160, 480-20);
	}
	
	
	
	private void presentGameOver() {
	    batcher.drawSprite(160, 240, 236, 345, Assets.gameOver);        
	    float scoreWidth = Assets.font.glyphWidth * scoreString.length();
	    Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2 + 35, 203);
	}

    @Override
    public void pause() {
        if(state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
}
