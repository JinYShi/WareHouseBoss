package game;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Block extends Rectangle {
	private ImageIcon icon;
	private int type;
	
	// from "BlockType"
	public static final Integer SPACE = 0;
	public static final Integer BORDER = 1;
	public static final Integer ROAD = 2;
	public static final Integer BOX = 3;
	public static final Integer TERMINAL = 4;
	public static final Integer MEN_DOWN = 5;
	public static final Integer MEN_LEFT = 6;
	public static final Integer MEN_RIGHT = 7;
	public static final Integer MEN_UP = 8;
	public static final Integer BOX_DONE = 9;
	public static final Integer MEN_DOWN_2 = 11;
	public static final Integer MEN_LEFT_2 = 12;
	public static final Integer MEN_RIGHT_2 = 13;
	public static final Integer MEN_UP_2 = 14;
	
	/**
	 * Constructor for Block to figure out different picture and its using
	 * @preconditions the picture is initiated 
	 * @postconditions the block is initiated
	 * @param point the location come with  axis coordinates
	 * @param dimension the dimension of the image
	 * @param icon the image for the map creating
	 */
	public Block(Point point, Dimension dimension ,ImageIcon icon) {
		super(point,dimension);
		this.icon = icon;
		type = Integer.parseInt(icon.getDescription());
		if(isMen(type) || BOX == type) {
			type = ROAD;
		}else if(BOX_DONE == type) {
			type = TERMINAL;
		}
	}
	/**
	 * getter for block type
	 * @preconditions the block exists
	 * @return return the integer of the block
	 */
	public int getBlockType() {
		return type;
	}
	
	/**
	 * getter for image icon
	 * @preconditions the image exists
	 * @return return the icon for the image
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * setter for the image icon
	 * @preconditions the new icon exists
	 * @postconditions a new icon is set
	 * @param icon the new one need to set
	 */
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	/**
	 * getter for icon type
	 * @preconditions the icon exists
	 * @return return the type of icon as integer
	 */
	public int getIconType() {
		return Integer.parseInt(icon.getDescription());
	}

	/**
	 * isMen is to check if the type of block is for Men 
	 * @preconditions the block exists
	 * @param type integer as the type of block
	 * @return return a boolean 
	 */
	public static boolean isMen(int type) {
		if(type == MEN_DOWN || type == MEN_LEFT || type == MEN_RIGHT || type == MEN_UP || type == MEN_DOWN_2 || type == MEN_LEFT_2 || type == MEN_RIGHT_2 || type == MEN_UP_2)
			return true;
		return false;
	}
	
	/**
	 * whichMen is for two player situation
	 * @preconditions the block exists
	 * @postconditions to figure out the men block is for which people
	 * @param type integer as the type of block
	 * @return return an integer
	 */
	public static int whichMen(int type) {
		if(type == MEN_DOWN || type == MEN_LEFT || type == MEN_RIGHT || type == MEN_UP)
			return 1;
		if(type == MEN_DOWN_2 || type == MEN_LEFT_2 || type == MEN_RIGHT_2 || type == MEN_UP_2)
			return 2;
		return 0;
	}
	
	/**
	 * isBox is to check if the block for the box
	 * @preconditions the block exists
	 * @param type integer as type of block
	 * @return return a boolean
	 */
	public static boolean isBox(int type) {
		if(type == BOX || type == BOX_DONE)
			return true;
		return false;
	}
	
	/**
	 * isRoad is to check if the block for road
	 * @preconditions the block exists
	 * @param type integer as type of block
	 * @return return a boolean
	 */
	public static boolean isRoad(int type) {
		if(type == ROAD || type == TERMINAL)
			return true;
		return false;
	}
	
}

