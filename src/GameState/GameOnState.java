package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.net.www.content.text.plain;

public class GameOnState extends GameState {
	
	public static TileMap tileMap;
	private Background bg;
	
	public static Player player;
	
	public GameOnState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}	
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ww.gif");//grass_com.gif  , grasstileset.gif £¬ comm.gif
		tileMap.loadMap("/Maps/ww.map");//level1-1  , comm.map, newCom.map
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/lim.png", 0.1);//backLimbo.jpg   , grassbg1.gif
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
	}
	
	
	public void update() {
		
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
			
	}	
	

	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);	
					
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		if(InGameStatus == true)//progress bar
		{
			java.awt.Image img1 = Toolkit.getDefaultToolkit().getImage("./Resources/TrapImage/progress.gif");
		    g.drawImage(img1, 0, 10, null);	    
		    
			java.awt.Image p1 = Toolkit.getDefaultToolkit().getImage("./Resources/TrapImage/p1.png");
			int p1Loc = GameOnState.player.currCol*145/tileMap.numCols;
		    g.drawImage(p1,p1Loc, 40, null);	    
		    
			
			java.awt.Image p2 = Toolkit.getDefaultToolkit().getImage("./Resources/TrapImage/p2.png");
			
			int p2CurCol = 0; //networking part, p2's currCol
			int p2loc = p2CurCol*145/tileMap.numCols;
		    g.drawImage(p2, 0, 05, null);	    
	}	

		
		if(CommStatus == true){// communist trap effect
		    java.awt.Image img1 = Toolkit.getDefaultToolkit().getImage("./Resources/TrapImage/comm.jpg");
		    g.drawImage(img1, 320-img1.getWidth(null), 0, null);	    
		}
	
		if(BlindStatus == true){// blind trap effect

			java.awt.Image img2 = Toolkit.getDefaultToolkit().getImage("./Resources/TrapImage/cat2.gif");
		    g.drawImage(img2, 0, 0, null);	    
			//200*480 Left
		    //140*480 Right
		    //640*100 Top
		}

		if(DestinationStatus == true){// communist trap effect
		    java.awt.Image img1 = Toolkit.getDefaultToolkit().getImage("./Resources/TrapImage/issac.png");
		    g.drawImage(img1, 80,70, null);	    
		}
	
		
	}
	
	public static boolean CommStatus = false; // a boolean to check if Communist trap is activated
	public static boolean BlindStatus = false;
	public static boolean DestinationStatus = false; 
	public static boolean InGameStatus = true;// mark false when game is over

	
	public void keyPressed(int k) {
		
		if(CommStatus == false)
		{
			if(k == KeyEvent.VK_LEFT) player.setLeft(true);		
			if(k == KeyEvent.VK_RIGHT) player.setRight(true);
			if(k == KeyEvent.VK_UP) player.setJumping(true);
			if(k == KeyEvent.VK_DOWN) player.setDown(true);
		}
		else{
			if(k == KeyEvent.VK_LEFT) 	player.setRight(true);
			if(k == KeyEvent.VK_RIGHT) player.setLeft(true);	
			if(k == KeyEvent.VK_UP) player.setDown(true);	
			if(k == KeyEvent.VK_DOWN) player.setJumping(true);
		}
		
//		if(k == KeyEvent.VK_W) player.setJumping(true);
//		if(k == KeyEvent.VK_E) player.setGliding(true);
//		if(k == KeyEvent.VK_R) player.setScratching();
//		if(k == KeyEvent.VK_F) player.setFiring();
	}
		
		public void keyReleased(int k) {
			if(CommStatus == false)
			{
				if(k == KeyEvent.VK_LEFT) player.setLeft(false);		
				if(k == KeyEvent.VK_RIGHT) player.setRight(false);
				if(k == KeyEvent.VK_UP) player.setJumping(false);
				if(k == KeyEvent.VK_DOWN) player.setDown(false);
			}
			else{
				if(k == KeyEvent.VK_LEFT) 	player.setRight(false);
				if(k == KeyEvent.VK_RIGHT) player.setLeft(false);	
				if(k == KeyEvent.VK_UP) player.setDown(false);	
				if(k == KeyEvent.VK_DOWN) player.setJumping(false);
			}		
//		if(k == KeyEvent.VK_W) player.setJumping(false);
//		if(k == KeyEvent.VK_E) player.setGliding(false);
	}
		
		
		public static  void setAllFalse(){ // set all the action false;
			player.setRight(false);
			player.setLeft(false);	
			player.setDown(false);
			player.setJumping(false);
		}
	
}












