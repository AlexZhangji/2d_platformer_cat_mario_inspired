package TileMap;

import java.util.Random;

import Entity.Player;
import GameState.GameOnState;
import Main.GamePanel;



public class TrapEffects {

	private static int curCol =  -1;
	
	public TrapEffects(){
		running = true;
		
	}
	
	
public TrapEffects(int col){
	curCol = col;
	running = true;
	}
	
public volatile static boolean running = true;
	
public static void stopRunning()
{
	GameOnState.player.curPlaying = "null";// ends song
    running = false;
    
}


	public static Thread CommunistEffect = new Thread() {		
		public void run() {
			
			while(running){
			long t= System.currentTimeMillis();
			long end = t+10300;
			GameOnState.CommStatus = true;

			while(System.currentTimeMillis() < end) {
				// mess the keyboard input in GameState 
		}
			GameOnState.setAllFalse();
			GameOnState.CommStatus = false;
			

			stopRunning();
			}
		}
		
//		public void stopRunning()
//		{
//		    running = false;
//		}
	
	};
	
	
public static Thread Warp = new Thread() {		
//		public volatile boolean running = true;
		public void run() {	
			while(running){
				// get a random col number between [1, curCol+5]
				Random rand =  new Random();
				int randNum = rand.nextInt(curCol+2)+1;
				
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}// wait 1 s
					
				System.out.println("randomNum = " + randNum);
				
				GameOnState.tileMap.setPosition(randNum, 0);
				GameOnState.player.setPosition(30*(randNum+2), 0);// set player to random loc
				System.out.println("//reach here");
				GamePanel.update();
				
				stopRunning();
			}
		}
		
//		public void stopRunning()
//		{
//		    running = false;
//		}
		
	};
	
	

	
public static Thread Death = new Thread() {		
//		public volatile boolean running = true;
		public void run() {	
			while(running){
				// get a random col number between [1, curCol+5]		
				try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}// wait 1 s
					
				
				GameOnState.tileMap.setPosition(0, 0);
				GameOnState.player.setPosition(50, 0);// set player to random loc
				GamePanel.update();
				
				GameOnState.player.DeathCount++;
				System.out.println("Valar Morghulis, death Count : " + GameOnState.player.DeathCount);
				
				
				stopRunning();
			}
		}
		
//		public void stopRunning()
//		{
//		    running = false;
//		}
		
	};
	
	public static Thread Blind = new Thread() {		
		public void run() {
			
			while(running){
			long t= System.currentTimeMillis();
			long end = t+10300;
			GameOnState.BlindStatus = true;
			GamePanel.update();
			
			System.out.println("Blind effect on");
			while(System.currentTimeMillis() < end) {
		}
			System.out.println("Blind effect off");

			GameOnState.BlindStatus = false;
			stopRunning();
			
			}
		}
		
//		public void stopRunning()
//		{
//		    running = false;
//		}
	
	};
	
	static String direction = "null";

	public static Thread Destination = new Thread() {		
		public void run() {
			
			while(running){
			long t= System.currentTimeMillis();
			long end = t+70000;
			GameOnState.DestinationStatus = true;
			GamePanel.update();
			
			System.out.println("Blind effect on");
			while(System.currentTimeMillis() < end) {
		}
			System.out.println("Blind effect off");

			GameOnState.DestinationStatus = false;
			stopRunning();
			
			}
		}
		
//		public void stopRunning()
//		{
//		    running = false;
//		}
	
	};
	
	
	public void setSoonasuDirection(String direction)
	{
		this.direction = direction;
	}
	
	//soonasu
	public static Thread Soonasu = new Thread() {		
//		public volatile boolean running = true;
	
	
	
		public void run() {	
			while(running){
				
				// get a random col number between [1, curCol+5]		
				try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}// wait 1 s
					
				
				long t2= System.currentTimeMillis();
				long end = t2+1000;
				
				if(direction.equals("LeftUp")){
					GameOnState.player.setDown(true);	
					GameOnState.player.setRight(true);	
				}
				
				if(direction.equals("RightUp")){
					GameOnState.player.setDown(true);	
					GameOnState.player.setLeft(true);	
				}

				if(direction.equals("LeftDown")){
					GameOnState.player.setJumping(true);	
					GameOnState.player.setLeft(true);	
				}

				if(direction.equals("RightDown")){
					GameOnState.player.setJumping(true);	
					GameOnState.player.setLeft(true);	
				}

				if(direction.equals("Right")){
					GameOnState.player.setLeft(true);	
				}
				
				if(direction.equals("Up")){
					GameOnState.player.setJumping(true);	
				}
				
				while(System.currentTimeMillis() < end) {
				}

				GameOnState.setAllFalse();
				//				GameOnState.tileMap.setPosition(0, 0);
//				GameOnState.player.setPosition(50, 0);// set player to random loc
//				GamePanel.update();
				
				stopRunning();
			}
		}
		
//		public void stopRunning()
//		{
//		    running = false;
//		}
		
	};
	
}
