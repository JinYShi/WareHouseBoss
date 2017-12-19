package game;
import javax.swing.ImageIcon;

public class Pictures{
	private static String PATH = "src/imgs/";
	private static int PICNUMS = 15;
	private static Pictures singlePic = null;
	private static ImageIcon[] imgs;
	
	/**
	 * Constructor for Pictures
	 * @preconditions the path is available and pictures exists
	 * @postconditions Picture is initiated
	 */
	private Pictures(){
		imgs = new ImageIcon[PICNUMS];
		for(int i = 0;i<PICNUMS;i++){
			imgs[i] = new ImageIcon(PATH + i + ".png",Integer.toString(i));
		}
		singlePic = this;
	}
	
	/**
	 * @preconditions Pictures is initiated
	 * @return return the picture
	 */
	public static Pictures getInstance() {
		if(singlePic == null){
			new Pictures();
		}
		return singlePic;
	}
	
	/**
	 * @preconditions the list of imgs exists
	 * @param index the index of a specific image
	 * @return return the image come with this index
	 */
	public ImageIcon getImgByIndex(int index) {
		return imgs[index];
	}
}
