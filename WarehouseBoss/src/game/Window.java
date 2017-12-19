package game;
import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import javax.swing.*;

import game.Window.Player;

import java.awt.*;

class Window extends JFrame implements KeyListener,ActionListener{
	private MapGenerator mp;
	private List<String> initmapData;
	private List<String> ResetData;
	private List<String> mapData;
	private Pictures imgSrc;
	private int column;
	private int row;
	private Dimension picSize;
	private BGM bgm;
	private int level;
	private List<JButton> btns;
	private Player player1;
	private Player player2;
	private Stack<List<String>> backStack1;
	private Stack<List<String>> backStack2;
	private JPanel panel;//use to put the double players and tool bar, just for two play option
	private JPanel basic;// use to store the staff on tool bar, just for two player option

	class Player extends JPanel{
		
		private Block[] blocks;
		private Block men;
		private Canva canva;
		private Vector<Block> terminals;
		private int id;
		
		/**
		 * Constructor for Player
		 * @preconditions WarehouseBoss is initiated
		 * @postconditions a player is initiated
		 * @param id an integer as the number of player
		 */
		Player(int id){
			this.id=id;
			init(initmapData);
			setPreferredSize(new Dimension(940,524));
			setLayout(new BorderLayout());
			//canva
			canva = new Canva();
			canva.addKeyListener(Window.this);
			add(canva);
		}	
		
		/**
		 * init is to initiate a new game map with a player
		 * @preconditions the player is initiated
		 * @postconditions a map come with the picture is formed
		 * @param md the list of map data
		 */
		public void init(List<String> md) {
			terminals = new Vector<>();
			mapData = md;
			imgSrc  = Pictures.getInstance();
			column = mapData.get(0).length();
			row	   = mapData.size();
			blocks  = new Block[column*row];
			picSize = new Dimension(imgSrc.getImgByIndex(0).getIconWidth(), imgSrc.getImgByIndex(0).getIconHeight());
			
			//initialize all blocks
			for(int i = 0;i<row;i++){
				for(int j = 0;j<column;j++){
					ImageIcon imageIcon;
					if(mapData.get(i).charAt(j)-'0'<=8 && mapData.get(i).charAt(j)-'0'>=5 &&id==2){
						imageIcon = imgSrc.getImgByIndex(mapData.get(i).charAt(j)-'0'+6);
					}
					else{
						imageIcon = imgSrc.getImgByIndex(mapData.get(i).charAt(j)-'0');
					}
					int type = Integer.parseInt(imageIcon.getDescription());
					int pos = i*column+j;
					blocks[pos] = new Block(new Point(j*picSize.width, i*picSize.height), picSize,imageIcon);
					if(Block.isMen(type)) men = blocks[pos];
					if(Block.TERMINAL==type)   terminals.add(blocks[pos]);
				}
			}
			setSize(column*picSize.width + 140, row*picSize.height + 45);
		}
		
		/**
		 * @preconditions player is initiated
		 * @postconditions return a boolean if the player is win or not
		 * @return return a boolean
		 */
		private boolean isWin(){
			for (Block block : terminals) {
				if(block.getIconType() != Block.BOX_DONE)
					return false;
			}
			return true;
		}
		
		private class Canva extends Canvas{
			
			/**
			 * @preconditions Warehouse Boss is initiated
			 * @postconditions the graph shows
			 * @param g the graph
			 */
			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D)g;
				for (Block block : blocks) {
					g2d.draw(block);
					g2d.drawImage(block.getIcon().getImage(), block.getLocation().x, block.getLocation().y,this);
				}
			}	
		}
		
		/**
		 * @preconditions the Canva is initiated
		 * @postconditions get a boolean if the block move or not
		 * @param point the coordinate of the block
		 * @param keyCode an integer when the kep pressed
		 * @return return a boolean
		 */
		private boolean blockMove(Point point,int keyCode){
			int x = point.x, y = point.y;
			int currPos = (y/picSize.height)*column + (x/picSize.width);
			int men_type = Block.MEN_DOWN;
			switch (keyCode) {
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					x+=picSize.width;
					if(id==1) men_type = Block.MEN_RIGHT;
					if(id==2) men_type = Block.MEN_RIGHT_2;
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:	
					y+=picSize.height;
					if(id==1) men_type = Block.MEN_DOWN;
					if(id==2) men_type = Block.MEN_DOWN_2;
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					x-=picSize.width;
					if(id==1) men_type = Block.MEN_LEFT;
					if(id==2) men_type = Block.MEN_LEFT_2;
					break;
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:	
					y-=picSize.height;
					if(id==1) men_type = Block.MEN_UP;
					if(id==2) men_type = Block.MEN_UP_2;
					break;
				default:	
					break;
			}
			int nextPos = (y/picSize.height)*column + (x/picSize.width);
			System.out.println("currPos:"+ currPos+"  "+"newtPos:" +nextPos);
			int curr_box_type =blocks[currPos].getIconType(),
				next_box_type =blocks[nextPos].getIconType();
			//reach border
			if(Block.BORDER==next_box_type) 
				return false;
			//two box side by side
			if(Block.isBox(curr_box_type) && Block.isBox(next_box_type))
				return false;
			//box move
			if(Block.isBox(curr_box_type) && Block.isRoad(next_box_type)){
				blocks[nextPos].setIcon(imgSrc.getImgByIndex(next_box_type == Block.TERMINAL?Block.BOX_DONE:Block.BOX));//goal position or not
				blocks[currPos].setIcon(imgSrc.getImgByIndex(blocks[currPos].getBlockType()));
				return true;
			}
			//next is box
	        if(Block.isBox(next_box_type)){
	        	if(!blockMove(new Point(x, y), keyCode))//box can be push or not
	        		return false;
	        }	
	        next_box_type =blocks[nextPos].getIconType();
			if(Block.isRoad(next_box_type)) {
				System.out.println("check road");
				blocks[nextPos].setIcon(imgSrc.getImgByIndex(men_type));
				blocks[currPos].setIcon(imgSrc.getImgByIndex(blocks[currPos].getBlockType()));
				men = blocks[nextPos];
				return true;
			}
			return false;
		}

	}
	
	/**
	 * Constructor for Window
	 * @preconditions the Warehouse Boss is initiated
	 * @postconditions the Window is initiated
	 * @param mapName the name of map
	 */
	public Window(String mapName) {
		backStack1 = new Stack<List<String>>();
		backStack2 = new Stack<List<String>>();
		initmapData = new Map(mapName).getMapData();
		backStack1.push(initmapData);
		backStack2.push(initmapData);
		HomePage();
		initBGM();
	}
	
	/**
	 * GameStart is to generate the game Screen, and also settle down
	 * the data for the game, including the map data, the player and
	 * store the player's step. To prepare for the use of bottoms
	 * @preconditions the window is initiated and number of player exists
	 * @postconditions a new GameStart is created. A new game start
	 * @param numPlayer an integer as the number of player
	 */
	public void GameStart(int numPlayer){
		mp = new MapGenerator();
		mp.create();
		backStack1 = new Stack<List<String>>();
		backStack2 = new Stack<List<String>>();
		player1 = null;
		player2 = null;
		this.level = 0;
		setResizable(false);

		setSize(500*numPlayer + 140, 300 + 45);
		setMenu(numPlayer);
		setPlayerToolBar(numPlayer);


        setTitle("WarehouseBoss");
        setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);//system exit
		setVisible(true);
		this.ResetData = this.mapData;
		backStack1.push(mapData);
		backStack2.push(mapData);
	}
	
	/**
	 * keyPressed is a keyboard listener to manage the different key
	 * pressed. Including the to players pressed and the change of 
	 * levels. It also recognizes when the player win the right level
	 * @preconditions the Window is initiated
	 * @postconditions some specific key press can be handled
	 */
	//keyboard listening
	@SuppressWarnings("deprecation")
	@Override
	public void keyPressed(KeyEvent e) {
		Point point=null;
		if(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyCode()==KeyEvent.VK_DOWN||
		   e.getKeyCode()==KeyEvent.VK_LEFT||e.getKeyCode()==KeyEvent.VK_UP){
			point = player1.men.getLocation();
			if(player1.blockMove(point, e.getKeyCode())){
				System.out.println(player1.blocks);
				backStack1.push(Map.creatMapData(player1.blocks, row, column));
				player1.canva.repaint();
				setVisible(true);
			}
		}
		if(player2!=null && (e.getKeyCode()==KeyEvent.VK_A||e.getKeyCode()==KeyEvent.VK_W||
				             e.getKeyCode()==KeyEvent.VK_S||e.getKeyCode()==KeyEvent.VK_D)){
			point = player2.men.getLocation();
			if(player2.blockMove(point, e.getKeyCode())){
				backStack2.push(Map.creatMapData(player2.blocks, row, column));
				player2.canva.repaint();
				setVisible(true);
			}
		}
		if(player1.isWin()||(player2!=null && player2.isWin())){
			int result = 0;
			boolean on = bgm.isMUSIC_ON();
			if(on){
				bgm.BGM();
			}
			bgm.winMusic();
			ImageIcon img;
			if(player1.isWin()){
				img = new ImageIcon("src/imgs/10.png");
			}
			else{
				img = new ImageIcon("src/imgs/15.png");
			}
			result = JOptionPane.showConfirmDialog(null, "You win!!", "Congratulations!!", JOptionPane.DEFAULT_OPTION, 
					  result, img);
			if (result != -1 && on) {
				String next;
				bgm.BGM();
				if(this.level == -1){
					mp.create();
					next = "auto.map";
				}else{
					if(Map.getAllMapName().size()-1 < this.level+2) return;
					this.level += 1;
					if(this.level < 10){
						next = "0" + Integer.toString(this.level) + ".map";
					}else{
						next = Integer.toString(this.level) + ".map";
					}
				}
				backStack1 = new Stack<>();
				this.ResetData = new Map(next).getMapData();
				player1.init(new Map(next).getMapData()); 
				player1.canva.repaint();
				if(player2 != null){
					backStack2 = new Stack<>();
					player2.init(new Map(next).getMapData()); 
					player2.canva.repaint();
				}
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}	
	
	/**
	 * actionPerformed is a bottom listener. There are two tool bar which is 
	 * on the top of screen and another is on the west.
	 * @preconditions the setPlayerToolbar and set menuBar is initiated
	 * @postconditions the bottom can manage the game
	 */	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand()=="Back"){
			System.out.print("back");
			if(backStack1.size()==0){
				backStack1.push(mapData);
				return;
			}
			List<String> temp1 = backStack1.pop();
			if (!temp1.equals(Map.creatMapData(player1.blocks, row, column))) {
				player1.init(temp1);//stack return top
			} else {
				if (backStack1.size()==0) {
					backStack1.push(mapData);
					return;
				}
				player1.init(backStack1.pop());
			}
		}else if(e.getActionCommand()=="Reset"){
			backStack1 = new Stack<>();
			player1.init(this.ResetData);
			if(player2 != null){
				player2.init(this.ResetData);
			}
		}else if(e.getActionCommand()=="Continue"){
			if(ResetData == null) return;
			for(int i=0; i<btns.size();i++){
				this.remove((Component) this.btns.get(i));
			}
			if(backStack1.isEmpty()) return;
			List<String> temp1 = backStack1.pop();
			if(player2 != null){
				List<String> temp2 = backStack2.pop();
				GameStart(2);
				player2.init(temp2);
				player1.init(temp1);
			}else{
				GameStart(1);
				player1.init(temp1);
			}
		}else if(e.getActionCommand()=="Back  2"){
			if(backStack2.size()==0){
				backStack2.push(mapData);
				return;
			}
			List<String> temp2 = backStack2.pop();
			if (!temp2.equals(Map.creatMapData(player2.blocks, row, column))) {
				player2.init(temp2);//stack return top
			} else {
				if (backStack2.size()==0) {
					backStack2.push(mapData);
					return;
				}
				player2.init(backStack2.pop());
			}
		}else if(e.getActionCommand()=="Single  Player"){
			for(int i=0; i<btns.size();i++){
				this.remove((Component) this.btns.get(i));
			}
			GameStart(1);
		}else if(e.getActionCommand()=="Two  Players"){
			for(int i=0; i<btns.size();i++){
				this.remove((Component) this.btns.get(i));
			}
			GameStart(2);
		}else if(e.getActionCommand()=="Home  Page"){
			setJMenuBar(null);
			HomePage();
		}else if(e.getActionCommand()=="On"){
			if (!bgm.isMUSIC_ON()) bgm.BGM();
			return;
		}else if(e.getActionCommand()=="Off"){
			if (bgm.isMUSIC_ON()) bgm.BGM();
			return;
		}else{//select menu
			backStack1 = new Stack<>();
			mp.create();
			setLevel(e.getActionCommand());
			player1.init(new Map(e.getActionCommand()).getMapData());
			this.ResetData = new Map(e.getActionCommand()).getMapData();
			if(player2 != null){
				backStack2 = new Stack<>();
				player2.init(new Map(e.getActionCommand()).getMapData());
			}
		}
		player1.canva.repaint();
		if(player2!=null){
			player2.canva.repaint();
		}
		setVisible(true);
	}	
	
	/**
	 * Homepage is to initiated the home page, set the size and the buttom font
	 * @preconditions the WarehouseBoss is initiated
	 * @postconditions the home page is initiated
	 */
	public void HomePage(){
		setLayout(new BorderLayout());
		setTitle("WarehouseBoss");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(500+140, 300+45));
		setHomePageMenu();
        try {
        	setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/imgs/Home.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton single = new JButton("Single  Player");
        single.setSize(150, 75);
        single.setLocation(130, 50);
        single.addActionListener(this);
        single.setFont(new Font("Bradley Hand", Font.BOLD, 18));
        JButton two = new JButton("Two  Players");
        two.setSize(150, 75);
        two.setLocation(330, 50);
        two.setFont(new Font("Bradley Hand", Font.BOLD, 18));
        two.addActionListener(this);
        add(single);
        add(two);
        btns = new ArrayList<JButton>();
        btns.add(single);
        btns.add(two);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
	}	
	
	/**
	 * setHomePageMenu is to set up the menu and the top tool bar, which is 
	 * control the music and choose the players model
	 * @preconditions the WarehouseBoss is initiated
	 * @postconditions the home page menu bar is settle down
	 */
	public void setHomePageMenu(){
		setJMenuBar(null);
		JMenu music = new JMenu("Music");
		music.setFont(new Font("Bradley Hand", Font.BOLD, 16));
		JMenuItem on = new JMenuItem("On");
		JMenuItem off = new JMenuItem("Off");
		on.setFont(new Font("Bradley Hand", Font.BOLD, 14));
		off.setFont(new Font("Bradley Hand", Font.BOLD, 14));
		on.addActionListener(this);
		off.addActionListener(this);
		music.add(on);
		music.add(off);
		
		JMenu game = new JMenu("Game");
		game.setFont(new Font("Bradley Hand", Font.BOLD, 16));
		JMenuItem single = new JMenuItem("Single  Player");
		JMenuItem two = new JMenuItem("Two  Players");
		JMenuItem con = new JMenuItem("Continue");
		single.setFont(new Font("Bradley Hand", Font.BOLD, 14));
		two.setFont(new Font("Bradley Hand", Font.BOLD, 14));
		con.setFont(new Font("Bradley Hand", Font.BOLD, 14));
		con.addActionListener(this);
		single.addActionListener(this);
		two.addActionListener(this);
		game.add(con);
		game.add(single);
		game.add(two);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(game);
		menuBar.add(music);
		setJMenuBar(menuBar);
	}
	
	/**
	 * setPlayerToolBar is to set up the tool bar on the side
	 * @precondition the gamePlay is initiated
	 * @postcondition the player tool bar is settle down
	 * @param numPlayer an integer as the number of players, 1 or 2
	 */
	public void setPlayerToolBar(int numPlayer) {
		//canva
        if(numPlayer==1){
    		//button
    		JButton reset = new JButton("Reset");
    		reset.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		reset.addActionListener(this);
    		JButton ret = new JButton("Home  Page"); //return to home page
    		ret.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		ret.addActionListener(this);
    		JButton back = new JButton("Back");
    		back.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		back.addActionListener(this);
    		JButton quit = new JButton("Exit");
    		quit.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		quit.addActionListener((ActionEvent event) -> {
    			System.exit(0);
    		});
    		//set tool bar
            JPanel bottom = new JPanel(new GridLayout(4, 1, 0, 50));
    		bottom.add(back);
    		bottom.add(reset);
    		bottom.add(ret);
    		bottom.add(quit);
    		        
    		JPanel basic = new JPanel(new GridBagLayout());
    		GridBagConstraints gbc = new GridBagConstraints();
    		gbc.anchor = GridBagConstraints.CENTER;
    		basic.add(bottom, gbc);
    		
        	player1 = new Player(1);
    		setLayout(new BorderLayout());//border with no gap between components
    		add(basic,BorderLayout.EAST);//menu on top
    		add(player1,BorderLayout.CENTER);//map in center
        } else {
    		//button
    		setBounds(100, 100, 500+155, (300+45)*numPlayer);
    		setLayout(new BorderLayout(0, 0));
    		JPanel contentPane = new JPanel();
    		contentPane.setLayout(new BorderLayout(0, 0));
    		setContentPane(contentPane); 		
    		//button
    		JButton reset = new JButton("Reset");
    		reset.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		reset.addActionListener(this);
    		JButton ret = new JButton("Home  Page"); //return to home page
    		ret.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		ret.addActionListener(this);
    		JButton back = new JButton("Back");
    		back.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		back.addActionListener(this);
    		JButton quit = new JButton("Exit");
    		quit.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		quit.addActionListener((ActionEvent event) -> {
    			System.exit(0);
    		});
    		
    		JButton back2 = new JButton("Back  2");
    		back2.setFont(new Font("Bradley Hand", Font.BOLD, 18));
    		back2.addActionListener(this);
    		
    		// set tool bar
            JPanel bottom = new JPanel(new GridLayout(6, 1, 0, 70));
    		bottom.add(back);
    		bottom.add(back2);
    		bottom.add(reset);
    		bottom.add(ret);
    		bottom.add(quit);      
    		basic = new JPanel(new GridBagLayout());
    		GridBagConstraints gbc = new GridBagConstraints();
    		gbc.anchor = GridBagConstraints.CENTER;
    		basic.add(bottom, gbc);
    		
    		JPanel gameP = new JPanel();
    		gameP.setLayout(new BoxLayout(gameP, BoxLayout.Y_AXIS));   		
    		player1 = new Player(1);
    		gameP.add(player1);   		
    		player2 = new Player(2);
        	gameP.add(player2);  
    		 		
    		//final one
    		panel = new JPanel();
    		panel.setLayout(new BorderLayout());
    		add(gameP,BorderLayout.CENTER);
        	add(basic,BorderLayout.EAST);
        }
	}
	
	/**
	 * setMenu is to set up the top tool bar for the game start which including
	 * the music control and level choose
	 * @preconditions Music, Map, MusicGenerator is initiated
	 * @postconditions the Meun is settle down
	 * @param numPlayer an integer as the number of players, 1 or 2
	 */
	public void setMenu(int numPlayer){
		setJMenuBar(null);
		//menu
		JMenu music = new JMenu("Music");
		JMenuItem on = new JMenuItem("On");
		JMenuItem off = new JMenuItem("Off");
		music.setFont(new Font("Bradley Hand", Font.BOLD, 16));
		on.setFont(new Font("Bradley Hand", Font.BOLD, 14));
		off.setFont(new Font("Bradley Hand", Font.BOLD, 14));
		on.addActionListener(this);
		off.addActionListener(this);
		music.add(on);
		music.add(off);
		JMenu pick = new JMenu("Choose  Map");
		pick.setFont(new Font("Bradley Hand", Font.BOLD, 16));
		List<String> map = Map.getAllMapName();
		for(String name:map){
			JMenuItem item = new JMenuItem(name);
			item.setFont(new Font("Bradley Hand", Font.BOLD, 14));
			item.addActionListener(this);
			pick.add(item);
		}	
		//menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(pick);
		menuBar.add(music);
		if (numPlayer == 2) {
			
		}
		setJMenuBar(menuBar);				
	}

	/**
	 * setLevel is for choose map according its name
	 * @preconditions the map exists
	 * @postconditions the new map is choosen
	 * @param map the name of the map
	 */
	private void setLevel(String map){
		if(map.equals("auto.map")){
			this.level = -1;
			return;
		}
		String[] parts = map.split("\\.");

		if(parts[0].length() == 2 && parts[0].substring(0, 1).equals("0")){
			this.level =  Integer.parseInt(parts[0].substring(1, 2));
			return;
		}
		this.level = Integer.parseInt(parts[0]);

	}
	
	/**
	 * initBGM is to manage the back ground music
	 * @preconditions BGM is initiated
	 * @postconditions the BGM works
	 */
	public void initBGM(){
		bgm = new BGM();
		bgm.newBGM();
		bgm.Get_Music();
		bgm.BGM();
	}
}