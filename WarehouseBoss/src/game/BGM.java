package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;


public class BGM {
	
	private boolean MUSIC_ON;
	private AudioInputStream bgm;
	private Clip bgmclip;
	
	/**
	 * @preconditions player exists
	 * @postconditions a new BGM is initiated
	 */
	public void newBGM(){
		this.MUSIC_ON = false;
		
	}
	
	/**
	 * @preconditions music file exists
	 * @postconditions music is get from file
	 */
	public void Get_Music(){
		try{
			this.bgm = AudioSystem.getAudioInputStream(new File("src/music/bgm.wav").getAbsoluteFile());
			bgmclip = AudioSystem.getClip();
	        bgmclip.open(this.bgm);
		}catch(Exception ex){
			System.out.print("bgm not found");
		}
	}
	
	/**
	 * control the on and off of BGM music
	 * @preconditions the music exists
	 * @postconditions BGM is initiated
	 */
	public void BGM(){
		if(!MUSIC_ON){
		    this.bgmclip.loop(Clip.LOOP_CONTINUOUSLY);
		    this.bgmclip.start();
			this.MUSIC_ON = true;
		}else{	
	        this.bgmclip.stop();;
			this.MUSIC_ON = false;
		}
	}
	
	/**
	 * isMUSIC_ON is to check if the music is playing on
	 * @preconditions the BGM is initiated
	 * @return return a boolean
	 */
	public boolean isMUSIC_ON() {
		return MUSIC_ON;
	}
	
	/**
	 * setter to play music on
	 * @preconditions the boolean mUSIC_ON exists
	 * @postconditions music is set to play on
	 * @param mUSIC_ON a boolean to check is music plays on
	 */
	public void setMUSIC_ON(boolean mUSIC_ON) {
		MUSIC_ON = mUSIC_ON;
	}
	
	/**
	 * winMusic is to play music when the player win the game
	 * @preconditions BGM is initiated
	 * @postconditions win music play is settle down
	 */
	public void winMusic() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/music/win.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	
}


