package com.trapps.hoppingegg.framework.impl;

import com.trapps.hoppingegg.framework.Game;
import com.trapps.hoppingegg.framework.Screen;

public abstract class GLScreen extends Screen {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    
    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = ((GLGame)game).getGLGraphics();
    }

}
