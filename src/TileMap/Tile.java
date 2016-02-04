package TileMap;

import java.awt.image.BufferedImage;

public class Tile {
	
	private BufferedImage image;
	private int type;
	
	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	//added
	public static final int TRAP = 3;

	public boolean ifTrap = false; // a tile is by default not a trap;
	
	public boolean isTrap(){
		return ifTrap;
	}
	
	public String TrapType = "no type";
	public static String []TypeList = {"Communism","warpGate","Death","BlindCat","Soonansu","Destination"};   
	
	public void setTrap(int loc){
		
		if(loc <= TypeList.length + 4 )
		TrapType = TypeList[loc -5];
	}
	
	public String getTrapType(){
		return TrapType;
	}
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage() { return image; }
	public int getType() { return type; }
	
}
