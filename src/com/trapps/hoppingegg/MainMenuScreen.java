package com.trapps.hoppingegg;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.trapps.hoppingegg.framework.Game;
import com.trapps.hoppingegg.framework.Input.TouchEvent;
import com.trapps.hoppingegg.framework.gl.Camera2D;
import com.trapps.hoppingegg.framework.gl.SpriteBatcher;
import com.trapps.hoppingegg.framework.impl.GLGame;
import com.trapps.hoppingegg.framework.impl.GLScreen;
import com.trapps.hoppingegg.framework.math.OverlapTester;
import com.trapps.hoppingegg.framework.math.Rectangle;
import com.trapps.hoppingegg.framework.math.Vector2;

public class MainMenuScreen extends GLScreen {
	
	 Camera2D guiCam;
	 SpriteBatcher batcher;
	 Rectangle soundBounds;
	 Rectangle playBounds;
	 Rectangle highscoresBounds;
	 Rectangle controlsBounds;
	 Rectangle exitBounds;
	 Rectangle rateusBounds;
	 Vector2 touchPoint;
	 GLGame gl;
	 static int f=1;
	 public MainMenuScreen(Game game){
		 super(game);
		 guiCam = new Camera2D(glGraphics, 320, 480);
	     batcher = new SpriteBatcher(glGraphics, 100);
	     soundBounds = new Rectangle(0,0,64,64);
	     playBounds = new Rectangle(83, 480-227, 156, 29);
	     highscoresBounds = new Rectangle(83, 480-267, 156, 29);
	     exitBounds = new Rectangle(83, 480-380, 156, 29);
	     rateusBounds = new Rectangle(83, 480-347, 156, 29);
	     controlsBounds = new Rectangle(83,480-306,156,29);
	     touchPoint = new Vector2();  
	     gl = (GLGame)game;
	     Settings.load(game.getFileIO());
	 }
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);                        
            if(event.type == TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x, event.y);
                guiCam.touchToWorld(touchPoint);
                
                
                if(OverlapTester.pointInRectangle(playBounds,touchPoint))
                {
                	Assets.playSound(Assets.clickSound);
                	game.setScreen(new GameScreen((Game) game));
                	return;
                }
                
                if(OverlapTester.pointInRectangle(soundBounds,touchPoint))
                {
                	Assets.playSound(Assets.clickSound);
                	Settings.soundEnabled = !Settings.soundEnabled;
                	
                	return;
                }
                
                if(OverlapTester.pointInRectangle(highscoresBounds,touchPoint))
                {
                	Assets.playSound(Assets.clickSound);
                	game.setScreen(new HighScoreScreen((Game) game));
                	return;
                }
                if(OverlapTester.pointInRectangle(controlsBounds,touchPoint))
                {
                	Assets.playSound(Assets.clickSound);
                	game.setScreen(new ControlsScreen((Game) game));
                	return;
                }
                if(OverlapTester.pointInRectangle(rateusBounds,touchPoint))
                {
                	Assets.playSound(Assets.clickSound);
                	gl.rateus();
                	return;
                }
                if(OverlapTester.pointInRectangle(exitBounds,touchPoint))
                {
                	System.exit(0);
                }
            }
        }

	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.allScreen);
        batcher.drawSprite(160, 240, 320, 480, Assets.mainmenuRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);               
        
        batcher.beginBatch(Assets.images);                 
        batcher.drawSprite(32, 32, 64, 64, Settings.soundEnabled?Assets.soundon:Assets.soundoff);
                
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);

	}

	@Override
	public void pause() {
		Settings.save(game.getFileIO());

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
