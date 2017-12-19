package game;


import java.util.*;
import java.io.*;
import java.net.*;


public class MapGenerator {
	
	private int flagA = 0;
	private int flagB = 0;
	private int flagC = 0;
	
	/**
	 * checkflag is for generate the random map, to change the num to 0 back
	 * @preconditions the x exists
	 * @postconditions change all number back to flag which is 0
	 * @param x an integer to recording the map generate
	 * @return return the flag as integer
	 */
	public int checkflag(int x){
		if(x == 0)	return this.flagA;
		if(x == 1)	return this.flagB;
		return this.flagC;
	}
	
	/**
	 * create if for generating an random map,each time from a 
	 * different map. By using of checkflag to change the number 
	 * of map back to 0, and restart again
	 * @preconditions the warehouse is initiated
	 * @postconditions a random map is generated
	 */
	public void create(){
		
		int[][] map = new int[12][20];
		
		int randmap = (int)(Math.random()*100 % 3);
		
		while(true){
			randmap = (int)(Math.random()*100 % 3);
			if(this.checkflag(randmap) == 0)	break;
		}
		
		if(randmap == 0){

			for(int i= 2; i<5; i++){
				int j = 5;
				map[i][j] = 1;
			}
			
			for(int j=2; j<6; j++){
				int i = 5;
				map[i][j] = 1;
			}
			
			for(int j=6; j<15; j++){
				int i = 2;
				map[i][j] = 1;
			}

			for(int i=2; i<5; i++){
				int j = 14;
				map[i][j] = 1;
			}
			
			for(int j=14; j<18; j++){
				int i = 5;
				map[i][j] = 1;
			}
			
			for(int i= 6; i<10; i++){
				int j = 2;
				map[i][j] = 1;
			}
			
			for(int i= 6; i<10; i++){
				int j = 17;
				map[i][j] = 1;
			}
			
			for(int j=2; j<18; j++){
				int i = 9;
				map[i][j] = 1;
			}
			
			for(int j=6; j<=13; j++){
				for(int i=3; i<=5; i++){
					map[i][j] = 2;
				}
			}
			
			for(int j=3; j<=16; j++){
				for(int i=6; i<=8; i++){
					map[i][j] = 2;
				}
			}
			
			int rand = (int)(Math.random()*100) % 3;
			for(int i=7; i<10; i++){
				for(int j=3; j<17; j++){
					rand = (int)(Math.random()*100) % 2;
					if(rand == 0)
						map[i][j] = 1;
				}
			}
			
			rand = (int)(Math.random()*100) % 3;
			map[6][3+rand] = 4;
			
			map[7][9] = 2;
			map[8][9] = 4;
			
			rand = (int)(Math.random()*100) % 5;
			map[3][7+rand] = 3;
			map[3][13] = 4;

			rand = (int)(Math.random()*100) % 5;
			map[4][7+rand] = 3;

			rand = (int)(Math.random()*100) % 5;
			map[5][7+rand] = 3;
			
			boolean flag = false;
			
			while(true){
				for(int j=4; j<14; j++){
					rand = (int)(Math.random()*100) % 5;
					if(map[6][j] !=4 && rand == 3){
						map[6][j] = 5;
						flag = true;
						break;
					}
				}
				if(flag == true)	break;
			}

		}		
		else if(randmap == 1){

			Scanner sc = null;
			try{
				sc = new Scanner(new FileReader("./src/imgs/temp.txt"));
			}
			catch (FileNotFoundException e) {
				System.out.println("Can't find the file");
			}
			
			
			for(int i=0; i<12; i++){
				String arr = sc.nextLine();
				for(int j=0; j<20; j++){
					int a = arr.charAt(j) - '0';
					map[i][j] = a; 
				}
			}
			sc.close();
			
			map[8][9] = 4;
			
			int rand = (int)(Math.random()*100) % 4;
			map[3+rand][11] = 3;
					
			for(int amount = 0; amount < 4; amount++){
				rand = (int)(Math.random()*100) % 5;
				map[2+rand][7] = 1;
			}
			
			
			rand = (int)(Math.random()*100) % 4;
			map[3+rand][6] = 3;
			
			rand = (int)(Math.random()*100) % 10;
			map[2][2+rand] = 4;
			

			boolean flag = true;
			while(flag){
				rand = (int)(Math.random()*100) % 4;
				if(map[3][9+rand] != 3){
					map[3][9+rand] = 3;
					flag = false;
				}
			}
			
			for(int amount=0; amount<4; amount++){
				rand = (int)(Math.random()*100) % 5;
				if(map[5][9+rand] != 3)
					map[5][9+rand] = 1;
			}
			
			if(map[5][11] != 3)	map[5][11] = 2;
			
			flag = true;
			while(flag){
				rand = (int)(Math.random()*100) % 8;
				if(rand != 3)	flag = false;
			}
			map[6][8+rand] = 5;
		
		}				
		else{

			Scanner sc = null;
			try{
				sc = new Scanner(new FileReader("./src/imgs/temp1.txt"));
			}
			catch (FileNotFoundException e) {
				System.out.println("Can't find the file");
			}
			
			
			for(int i=0; i<12; i++){
				String arr = sc.nextLine();
				for(int j=0; j<20; j++){
					int a = arr.charAt(j) - '0';
					map[i][j] = a; 
				}
			}
			sc.close();
			
			int rand = (int)(Math.random()*100) % 6;
			map[3+rand][15] = 3;
			
			rand = (int)(Math.random()*100) % 3;
			map[2+rand][17] = 4;
			
			for(int amount=0; amount<3; amount++){
				rand = (int)(Math.random()*100) % 5;
				map[5+rand][14] = 1;
			}

			map[10][10] = 4;

			for(int amount=0; amount<5; amount++){
				rand = (int)(Math.random()*100) % 6;
				map[5][3+rand] = 1;
			}

			rand = (int)(Math.random()*100) % 7;
			map[4][3+rand] = 3;
			
			map[2][9] = 1;
			map[1][9] = 4;

			rand = (int)(Math.random()*100) % 5;
			map[6][3+rand] = 3;
			
			for(int amount=0; amount<3; amount++){
				rand = (int)(Math.random()*100) % 3;
				map[7][10+rand] = 1;
			}

			rand = (int)(Math.random()*100) % 3;
			map[4+rand][2] = 5;
			
			map[5][8] = 2;
		}
						
		try{
			File file = new File("src/maps/auto.map");
			FileWriter output = new FileWriter(file);
			for(int i=0; i<12; i++){
				for(int j=0; j<20; j++){
					char c = (char) ((char)map[i][j] + '0');
					output.append(c);
				}
				output.append('\n');
			}	
			output.close();
		} catch(IOException e){
			System.out.println("error");
			e.printStackTrace();
		}
				
		if(randmap == 0){
			this.flagA = 1;
			this.flagB = 0;
			this.flagC = 0;
		}
		if(randmap == 1){
			this.flagB = 1;
			this.flagA = 0;
			this.flagC = 0;
		}
		if(randmap == 2){
			this.flagC = 1;
			this.flagA = 0;
			this.flagB = 0;
		}		
	}		
}
