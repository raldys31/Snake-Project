package Main;

import Display.DisplayScreen;
import Game.GameStates.GameInfoState;
import Game.GameStates.GameOverState;
import Game.GameStates.GameState;
import Game.GameStates.MenuState;
import Game.GameStates.OptionsState;
import Game.GameStates.PauseState;
import Game.GameStates.State;
import Input.KeyManager;
import Input.MouseManager;
import Resources.Images;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by AlexVR on 7/1/2018.
 */

public class GameSetUp implements Runnable {
    private DisplayScreen display;
    private int width, height;
    public String title;

    private boolean running = false;
    private Thread thread;

    private BufferStrategy bs;
    private Graphics g;



    //Input
    private KeyManager keyManager;
    private MouseManager mouseManager;

    //Handler
    private Handler handler;

    //States
    public State gameState;
    public State menuState;
    public State pauseState;
    public State gameOverState;
    public State optionsState;
    public State gameInfoState;

    //Res.music
    private InputStream audioFile;
    private AudioInputStream audioStream;
    private AudioFormat format;
    private DataLine.Info info;
    private String audioName;
    private Clip audioClip;
    private Clip audioPlayer; //To play sound effects other than the main audio
    private boolean soundEffectMute;
    private boolean backgroundMusicMute;
    private long clipTime = 0;

    private BufferedImage loading;

    public GameSetUp(String title, int width, int height){

        this.width = width;
        this.height = height;
        this.title = title;
        keyManager = new KeyManager();
        mouseManager = new MouseManager();

    }

    private void init(){
        display = new DisplayScreen(title, width, height);
        display.getFrame().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);

        Images img = new Images();


        handler = new Handler(this);

        gameState = new GameState(handler);
        menuState = new MenuState(handler);
        pauseState = new PauseState(handler);
        gameOverState = new GameOverState(handler);
        optionsState = new  OptionsState(handler);
        gameInfoState = new GameInfoState(handler);

        State.setState(menuState);

        try {

            audioFile = getClass().getResourceAsStream("/music/Megalovania.wav");
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            format = audioStream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            
            audioName = "/music/Megalovania.wav";

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopAudio(){
    	audioPlayer.stop();
    }
	public void stopMainAudio(){
    	audioClip.stop();
    }
	public void playAudio(String fileLocation, boolean soundLoop){
		try {
			if(!soundEffectMute){
				audioPlayer = AudioSystem.getClip();
				audioPlayer.open(AudioSystem.getAudioInputStream(new File(fileLocation)));
				if(!soundLoop){
					audioPlayer.start();
				}else{
					audioPlayer.loop(Clip.LOOP_CONTINUOUSLY);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}
	public void playMainAudioAs(String fileLocation){
		try {
			if(!backgroundMusicMute){
			audioStream = AudioSystem.getAudioInputStream(new File(fileLocation));
			audioClip = AudioSystem.getClip();
			audioClip.open(audioStream);
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
			audioName = fileLocation;
			}
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resumeBackgroundMusic(){
		audioClip.setMicrosecondPosition(clipTime);
		audioClip.start();
	}

	public void restartBackgroundMusic() {
		clipTime = 0;
		resumeBackgroundMusic();
	}
	
	public void playMainAudio(){
		audioClip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
    public void reStart(){
        gameState = new GameState(handler);
    }

    public synchronized void start(){
        if(running)
            return;
        running = true;
        //this runs the run method in this  class
        thread = new Thread(this);
        thread.start();
    }

    public void run(){

        //initiallizes everything in order to run without breaking
        init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while(running){
            //makes sure the games runs smoothly at 60 FPS
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if(delta >= 1){
                //re-renders and ticks the game around 60 times per second
                tick();
                render();
                ticks++;
                delta--;
            }

            if(timer >= 1000000000){
                ticks = 0;
                timer = 0;
            }
        }

        stop();

    }

    private void tick(){
        //checks for key types and manages them
        keyManager.tick();

        //game states are the menus
        if(State.getState() != null)
            State.getState().tick();
    }

    private void render(){
        bs = display.getCanvas().getBufferStrategy();
        if(bs == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //Clear Screen
        g.clearRect(0, 0, width, height);

        //Draw Here!

        g.drawImage(loading ,0,0,width,height,null);
        if(State.getState() != null)
            State.getState().render(g);


        //End Drawing!
        bs.show();
        g.dispose();
    }

    public synchronized void stop(){
        if(!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void setSoundEffectMute(boolean soundEffectMute) {
  		this.soundEffectMute = soundEffectMute;
  	}
      
      public void setBackgroundMusicMute(boolean backgroundMusicMute) {
  		this.backgroundMusicMute = backgroundMusicMute;
  	}
      public String getAudioName() {
  		return audioName;
  	}

    public KeyManager getKeyManager(){
        return keyManager;
    }

    public MouseManager getMouseManager(){
        return mouseManager;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}

