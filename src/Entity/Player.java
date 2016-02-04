package Entity;

import GameState.GameOnState;
import TileMap.*;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;



import javax.swing.SwingUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	//private ArrayList<FireBall> fireBalls;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 8, 1, 2, 4, 2, 5
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	
	public int DeathCount = 0; // count time of death
	public String LastTrapHit = "null";
	
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 81;
		height = 80;
		cwidth = 30;
		cheight = 50;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.5;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;	
		fire = maxFire = 2500;	
			
		fireCost = 200;
		fireBallDamage = 5;
		//fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 8;
		scratchRange = 40;
		
		// load sprites
		try {			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/reimu22.gif"//playersprites.gif
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != 6) {	
						bi[j] = spritesheet.getSubimage(
								j * width*3/3,
								i * height,
								width*3/3,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width,
								height
						);
					}
					
				}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void setFiring() { 
		firing = true;
	}
	public void setScratching() {
		scratching = true;
	}
	public void setGliding(boolean b) { 
		gliding = b;
	}
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if(
		(currentAction == SCRATCHING || currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			dy = jumpStart;

			falling = true;
		
			//edited
			int rc = tileMap.map[currRow-2][currCol];
			int r = rc /21;
			int c = rc % 21;
			
			System.out.println("rc :" + rc + " , row :" + r + ", c :" + c );
			
			if(tileMap.tiles[r][c].isTrap(	))
			{
				String type = tileMap.tiles[r][c].getTrapType();
				System.out.println("hit Trap!  curRow = " + currRow + "  ,  currCol = " + currCol + ". tile type : " +  tileMap.tiles[r][c].getTrapType());		
				TrapEffects(type);
			}
			
		}
		
		// falling
		if(falling) {
			
//			System.out.println("falling~");//debug
			
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public String curPlaying = "null";// indicate which song is playing atm.
//	public String lastTrapHit = "null"; 
	private String nextSong = "null";
	
	public void TrapEffects(String type){ //  invoke trap effects 
		//ddd edit
		
		//mark as hit by trap name
		LastTrapHit =  type;
		System.out.println("last hit by " + LastTrapHit);
		
		if(type.equals(Tile.TypeList[0])){// Communist, disrupt the keypressed, and start music that lasts for 10s 
			
			nextSong = "comm.wav";

			if(!curPlaying.equals(nextSong))
			{
				Sound.playMusic("./Resources/SoundEffects/"+ curPlaying, "stop");
				Sound.playMusic("./Resources/SoundEffects/"+ nextSong, "play");// play till music ends, 15s in total
				curPlaying = nextSong;
			}
			
			TrapEffects goodEnd = new TrapEffects();
			Executor executor = Executors.newSingleThreadExecutor();
			executor.execute(goodEnd.CommunistEffect);
			
			
		}// end of comm	
		
		if(type.equals(Tile.TypeList[1])){ // warp gate 
			Sound.playMusic("./Resources/SoundEffects/warp.wav", "play");// play till music ends 
			
//			System.out.println("rc :" + rc + " , row :" + r + ", c :" + c );
			System.out.println("curCol =  " + currCol);
			TrapEffects WarpGate = new TrapEffects(currCol);
			 SwingUtilities.invokeLater(WarpGate.Warp);//  
//			goodEnd.Warp.interrupt();
		}
		
		
		if(type.equals(Tile.TypeList[2])){ // Death 
			
			nextSong = "DeathBGM.wav";

			if(!curPlaying.equals(nextSong))
			{
				Sound.playMusic("./Resources/SoundEffects/"+ curPlaying, "stop");
				Sound.playMusic("./Resources/SoundEffects/"+ nextSong, "play");// play till music ends, 15s in total
				curPlaying = nextSong;
			}

			//			Sound.playMusic("./Resources/SoundEffects/death.wav", "play");// play till music ends 
			System.out.println("curCol =  " + currCol);
			TrapEffects DeadEnd = new TrapEffects();
			 SwingUtilities.invokeLater(DeadEnd.Death);//  
		}
		
		
		
		if(type.equals(Tile.TypeList[3])){ // blind cat 
			
			nextSong = "blind.wav";

			if(!curPlaying.equals(nextSong))
			{
				Sound.playMusic("./Resources/SoundEffects/"+ curPlaying, "stop");
				Sound.playMusic("./Resources/SoundEffects/"+ nextSong, "play");// play till music ends, 15s in total
				curPlaying = nextSong;
			}
			
//			Sound.playMusic("./Resources/SoundEffects/"+ curPlaying , "play");// play till music ends 
				
			TrapEffects blindEffects = new TrapEffects();
			Executor executor = Executors.newSingleThreadExecutor();
			executor.execute(blindEffects.Blind);
		}
		
		if(type.equals(Tile.TypeList[4])){ // Soonasu 
//			Sound.playMusic("./Resources/SoundEffects/Bounce.wav", "play");// play till music ends 
						
			Sound.playMusic("./Resources/SoundEffects/hit.wav", "play");// play till music ends 

			TrapEffects bounceEffects = new TrapEffects();
			Executor executor = Executors.newSingleThreadExecutor();
			
			if(dx > 0 && dy < 0)
				bounceEffects.setSoonasuDirection("RightUp");
			
			else if(dx < 0 && dy < 0)
				bounceEffects.setSoonasuDirection("LeftUp");
			
			else if(dx < 0 && dy > 0)
				bounceEffects.setSoonasuDirection("LeftDown");

			else if(dx > 0 && dy > 0)
				bounceEffects.setSoonasuDirection("RightDown");

			else if(dx > 0 && dy == 0)
				bounceEffects.setSoonasuDirection("Right");
			
			else if(dx ==  0 && dy > 0)
				{
					System.out.println("bounce up");
					bounceEffects.setSoonasuDirection("Up");
				}
			
			executor.execute(bounceEffects.Soonasu);
		}
		
		if(type.equals(Tile.TypeList[5])){ // blind cat 
			
			nextSong = "Victory1.wav";

			if(!curPlaying.equals(nextSong))
			{
				Sound.playMusic("./Resources/SoundEffects/"+ curPlaying, "stop");
				Sound.playMusic("./Resources/SoundEffects/"+ nextSong, "play");// play till music ends, 15s in total
				curPlaying = nextSong;
				
				TrapEffects destinationEffects = new TrapEffects();
				Executor executor = Executors.newSingleThreadExecutor();
				executor.execute(destinationEffects.Destination);
			}

//			Sound.playMusic("./Resources/SoundEffects/Victory1.wav", "play");// play till music ends 
			
		
		}
		
		
	}
	
	
	
	
		 	
	public void update() {
			
		// update position
		getNextPosition();
		checkTileMapCollision();			
		setPosition(xtemp, ytemp);
		
		// set animation
		if(scratching) {
			if(currentAction != SCRATCHING) {
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// draw player
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;	
			}
		}
			
		if(facingRight) {
			g.drawImage( 
				animation.getImage(),
				(int)(x + xmap - width / 2-20),
				(int)(y + ymap - height / 2-10),
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width + 10),
				(int)(y + ymap - height / 2-10),
				-width,
				height,
				null
			);
			
		}
		
	}
	
}














