package com.trapps.hoppingegg;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.trapps.hoppingegg.framework.Game;
import com.trapps.hoppingegg.framework.Input.TouchEvent;
import com.trapps.hoppingegg.framework.gl.Camera2D;
import com.trapps.hoppingegg.framework.gl.SpriteBatcher;
import com.trapps.hoppingegg.framework.impl.GLScreen;
import com.trapps.hoppingegg.framework.math.OverlapTester;
import com.trapps.hoppingegg.framework.math.Rectangle;
import com.trapps.hoppingegg.framework.math.Vector2;

public class HighScoreScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle backBounds;
    Vector2 touchPoint;
    String[] highScores;  
    float xOffset = 0;
    
    public HighScoreScreen(Game game) {
        super(game);
        
        guiCam = new Camera2D(glGraphics, 320, 480);
        backBounds = new Rectangle(83,480-378,156,29);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 100);
        highScores = new String[3];        
        for(int i = 0; i < 3; i++) {
            highScores[i] = "  " +Settings.highscores[i];
            xOffset = Math.max(highScores[i].length() * Assets.font.glyphWidth, xOffset);
        }        
        xOffset = 160 - xOffset / 2 + Assets.font.glyphWidth / 2;
    }    

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(event.type == TouchEvent.TOUCH_UP) {
                if(OverlapTester.pointInRectangle(backBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new MainMenuScreen((Game) game));
                    return;
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
        batcher.drawSprite(160, 240, 320, 480, Assets.highScoreRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.images);
        
        float y =153f;
        for(int i = 2; i >= 0; i--) {
            Assets.font.drawText(batcher, highScores[i], xOffset-25-5, y);
            y += Assets.font.glyphHeight + 8;
        }
        
        
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }
    
    @Override
    public void resume() {        
    }
    
    @Override
    public void pause() {        
    }

    @Override
    public void dispose() {
    }
}
