package com.trapps.hoppingegg;

import com.trapps.hoppingegg.framework.Music;
import com.trapps.hoppingegg.framework.Sound;
import com.trapps.hoppingegg.framework.gl.Animation;
import com.trapps.hoppingegg.framework.gl.Font;
import com.trapps.hoppingegg.framework.gl.Texture;
import com.trapps.hoppingegg.framework.gl.TextureRegion;
import com.trapps.hoppingegg.framework.impl.GLGame;


public class Assets{
	public static Texture background;
	public static TextureRegion backgroundRegion;
	
	
	public static TextureRegion mainmenuRegion;
	

	public static TextureRegion highScoreRegion;
	
	
	public static TextureRegion controlsRegion;
	public static TextureRegion burningRegion;
	public static TextureRegion countdownRegion;
	public static TextureRegion birdRegion;
	
	public static Texture allScreen;
	
	
	
	public static Texture rateus;
	public static TextureRegion rateusRegion;
	
	public static Texture images;
	public static TextureRegion basket;
	public static TextureRegion egg;
	public static TextureRegion basketWithEgg;
	public static TextureRegion blank;
	public static TextureRegion basketWithFire;
	public static TextureRegion pause;
	public static TextureRegion soundon;
	public static TextureRegion soundoff;
	public static TextureRegion ready;
	public static TextureRegion gameOver;
	public static TextureRegion pauseMenu;
	
	
	
	public static Animation countdownAnim;
	public static Animation fireAnim;
	public static Animation birdAnim;
	public static Animation countdownAnim3;
	public static Font font;
	public static Music music;
	
    public static Sound jumpSound;
    public static Sound hitSound;
    public static Sound clickSound;
    public static Sound pointSound;

	public static void load(GLGame game)
	{
	background = new Texture(game,"backgorund.png");
	backgroundRegion = new TextureRegion(background,0,0,640,960);
	
	
	
	allScreen = new Texture(game,"imageee.png");
	
	mainmenuRegion = new TextureRegion(allScreen,1280,960,640,960);
	highScoreRegion = new TextureRegion(allScreen,640,960,640,960);
	controlsRegion = new TextureRegion(allScreen,1280,0,640,960);
	countdownRegion = new TextureRegion(allScreen,0,960,640,960);
	burningRegion = new TextureRegion(allScreen,640,0,640,960);
	birdRegion = new TextureRegion(allScreen,0,0,640,960);
	
	images = new Texture(game,"jjj.png");
	basket = new TextureRegion(images,0,303,128,128);
	basketWithEgg = new TextureRegion(images,468,345,128,128);
	egg = new TextureRegion(images,64,431,52,68);
	basketWithFire = new TextureRegion(images,212,174,128,256);
	pause = new TextureRegion(images,0,431,64,64);
	soundon = new TextureRegion(images,128,303,64,64);
	soundoff = new TextureRegion(images,128,367,64,64);
	ready = new TextureRegion(images,0,174,212,32);
	gameOver = new TextureRegion(images,384,0,236,345);
	pauseMenu = new TextureRegion(images,0,206,192,97);
	
	countdownAnim = new Animation(1f,
								  /*new TextureRegion(images,0,248,64,64),
								  new TextureRegion(images,320,160,64,64),*/
								  new TextureRegion(images,620,128,128,128),
								  new TextureRegion(images,340,345,128,128),
								  new TextureRegion(images,620,0,128,128),
								  new TextureRegion(images,748,0,128,128));
	
	countdownAnim3 = new Animation(1f,
									new TextureRegion(images,384,192,64,64),
									new TextureRegion(images,384,128,64,64),
									new TextureRegion(images,64,248,64,64),
									new TextureRegion(images,256,160,64,64));

	fireAnim = new Animation(2f,new TextureRegion(images,0,303,128,128),
							 new TextureRegion(images,212,174,128,256)
							 );
	
	birdAnim = new Animation(0.75f,new TextureRegion(images,764,128,128,128),
								new TextureRegion(images,876,0,128,128));
	
	
	font = new Font(images,0,0,16,24,29);
	
	
    jumpSound = game.getAudio().newSound("jump.mp3");
    hitSound = game.getAudio().newSound("hit.mp3");
   clickSound = game.getAudio().newSound("click.ogg");
   pointSound = game.getAudio().newSound("point.mp3");
	}
	public static void reload() {
        background.reload();
        
        images.reload();
        allScreen.reload();
        
        
    }
	public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}