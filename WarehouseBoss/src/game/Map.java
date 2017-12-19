 package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map{
	private static String path = "src/maps/";
	private List<String> mapData;
	
	/**
	 * Constructor for Map which is help to read the input map data
	 * @precondition the WarehouseBoss is initiated and file exists
	 * @postconditions the mapData is initiated
	 * @param mapName name of the maps
	 */
	public Map(String mapName) {
		BufferedReader stream;
		try {
		    stream = new BufferedReader(new FileReader(new File(path + mapName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		String s = null;
		mapData = new ArrayList<>();
		try {
			while((s = stream.readLine()) != null){
				mapData.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * @preconditions the map file exists and data is initiated
	 * @return a list of mapData is returned
	 */
	public List<String> getMapData() {
		return mapData;
	}
	
	/**
	 * creatMapData is to return a new map from blocks and handle the key pressed
	 * @preconditions the list of block is initiated
	 * @postconditions a list of data is created
	 * @param blocks a list of block
	 * @param row number of rows in the data map
	 * @param column number of column in the data map
	 * @return a list of data is returned
	 */
	public static List<String> creatMapData(Block[] blocks,int row,int column){
		List<String> res = new ArrayList<>();
		for(int i = 0;i<row;i++){
			String data="";
			for(int j = 0;j<column;j++){
				if(blocks[i*column + j].getIconType()>=11){
					data += blocks[i*column + j].getIconType()-6;
				}else{
					data += blocks[i*column + j].getIconType();
				}
			}
			res.add(data);
		}
		return res; 
	}
	
	/**
	 * @preconditions the path is available and files exists
	 * @return all of the map tack are returned
	 */
	public static List<String> getAllMapName() {
		 File[] files = new File(path).listFiles();  
		 List<String> res = new ArrayList<>();
		 for (File file : files) {
			res.add(file.getName());
		 }
		 return res;
	}
}
