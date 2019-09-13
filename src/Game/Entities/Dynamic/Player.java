package Game.Entities.Dynamic;

import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import Display.DisplayScreen;
import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	private Handler handler;
	
	public int lenght;
	public int xCoord;
	public int yCoord;
	public int score;
	public int steps;
	private int removeTailX;
	private int removeTailY;
	public int moveCounter;
	private int speedAdjust;

	public String direction;
	private String highscore;
	private String eatSoundEffect;
	private String deathSoundEffect;
	private String bananaSoundEffect;
	private Color playerColor;
	private Color appleColor;
	private Color rottenAppleColor;
	private Color bananaColor;
	private Tail tail;

	public boolean justAte;
	public boolean justAteBanana;
	private boolean soundLoop;
	private boolean tailRemoved;
	private boolean beatHighscore;


	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		score = 0;
		steps = 0;
		highscore = this.getHighScore();
		moveCounter = 0;
		direction= "Right";
		justAte = false;
		justAteBanana = false;
		lenght= 1;
		playerColor = Color.green;
		appleColor = Color.red;
		rottenAppleColor = Color.black;
		bananaColor = Color.yellow;
		speedAdjust = 20;
		eatSoundEffect = "res/music/bite.wav";
		deathSoundEffect = "res/music/sound-frogger-dead.wav";
		bananaSoundEffect = "res/music/Powerup.wav";
		soundLoop = true;
		tailRemoved = false;
		beatHighscore = false;

	}

	public void tick(){
		moveCounter += 2;
		if(moveCounter>=speedAdjust) {
			checkCollisionAndMove();
			checkSteps();
			verifyScore();
			moveCounter=0;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_W) && direction != "Down"){
			direction="Up";
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S) && direction != "Up"){
			direction="Down";
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A) && direction != "Right"){
			direction="Left";
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D) && direction != "Left"){
			direction="Right";
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){
			speedAdjust-=2;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)){
			speedAdjust+=2;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
			this.setJustAte(false);
			handler.getWorld().body.addLast(new Tail(xCoord, yCoord, handler));
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
			handler.getGame().playAudio("res/music/pause.wav", false);
			State.setState(handler.getGame().pauseState);
		}

	}

	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		steps++;
		switch (direction){
		case "Left":
			if(xCoord==0){
				xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				xCoord = 0;
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				yCoord = 0;
			}else{
				yCoord++;
			}
			break;
		}

		handler.getWorld().playerLocation[xCoord][yCoord]=true;

		if (handler.getWorld().body.size() > 0) {
			for (int i = 0; i < handler.getWorld().body.size(); i++){
				//Kill player when collides with itself.
				if(xCoord == handler.getWorld().body.get(i).x && yCoord == handler.getWorld().body.get(i).y) {
					kill();
				} 
			}
		}

		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			this.setJustAte(true);
			eatAndAddTail();
			removeTail();
			adjustScore();
			this.setSteps(0);
		}
		
		if(handler.getWorld().bananaLocation[xCoord][yCoord]) {
			this.setJustAteBanana(true);
			slowSnake();
		}

		//Displays the score on the bottom as soon as the game starts and gets updated whenever the snake eats a dot.
		if((xCoord > 0 || yCoord > 0) || justAte==true){
			if (score < 0) {
				score = 0;
				kill();
			}
			DisplayScreen.setMessage(String.format("Current Score: %d; %s", score, highscore)); 
		}

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));
		}

		System.out.println(steps);

	}

	//If the snake has made 80 or more steps then the apple becomes rotten.
	public void checkSteps() {
		if (steps >= 80) {
			handler.getWorld().apple.setIsGood(false);
		}
	}

	public void render(Graphics g,Boolean[][] playeLocation){
		//Changes the color of the snake.
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(playerColor);

				//Removes the tail of the snake if the apple is rotten.
				if (tailRemoved) {
					g.clearRect(removeTailX, removeTailY, handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);
					tailRemoved = false;
				}

				if(playeLocation[i][j]||handler.getWorld().appleLocation[i][j]){
					g.fillOval((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}

				else {
					g.clearRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
			}
		}

		//Changes the color of the apple.
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				if (handler.getWorld().apple.isGood) {
					g.setColor(appleColor);
				}
				else {
					g.setColor(rottenAppleColor);
				}

				if(handler.getWorld().appleLocation[i][j]){
					g.fillOval((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);

				}
			}
		}
		
		//Sets the color of the banana.
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(bananaColor);
				
				//Made the banana appears only when the speed in the game becomes difficult.
				if(handler.getWorld().bananaLocation[i][j] && speedAdjust <= 8){
					g.fillOval((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
			}
		}
	}

	public void removeTail() {
		if(justAte == true && handler.getWorld().apple.isGood == false && !handler.getWorld().body.isEmpty()) {
			removeTailX = handler.getWorld().body.getLast().x;
			removeTailY = handler.getWorld().body.getLast().y;
			handler.getWorld().playerLocation[removeTailX][removeTailY] = false;
			handler.getWorld().body.removeLast();
			tailRemoved = true;
			lenght--;
		}
		else if (justAte == true && handler.getWorld().apple.isGood == false && handler.getWorld().body.isEmpty()) {
			kill();
		}
	}

	public void eatAndAddTail(){
		lenght++;
		//Kills the snake if it eats a rotten apple and has no tail.
		if(justAte == true && handler.getWorld().apple.isGood == false && lenght == 0){
			kill(); 
		}
		else if (justAte == true && handler.getWorld().apple.isGood) {

			handler.getGame().playAudio(eatSoundEffect, false);
			handler.getWorld().appleLocation[xCoord][yCoord]=false;
			handler.getWorld().appleOnBoard=false;

			switch (direction){
			case "Left":
				if(handler.getWorld().body.isEmpty()){
					if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail = new Tail(this.xCoord+1,this.yCoord,handler);
					}
					else {
						if(this.yCoord!=0){
							tail = new Tail(this.xCoord,this.yCoord-1,handler);
						}
						else{
							tail =new Tail(this.xCoord,this.yCoord+1,handler);
						}
					}
				}
				else{
					if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
					}
					else {
						if(handler.getWorld().body.getLast().y!=0){
							tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
						}
						else {
							tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);
						}
					}
				}
				break;

			case "Right":
				if(handler.getWorld().body.isEmpty()){
					if(this.xCoord!=0){
						tail=new Tail(this.xCoord-1,this.yCoord,handler);
					}
					else {
						if(this.yCoord!=0){
							tail=new Tail(this.xCoord,this.yCoord-1,handler);
						}
						else {
							tail=new Tail(this.xCoord,this.yCoord+1,handler);
						}
					}
				}
				else {
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}
					else {
						if(handler.getWorld().body.getLast().y!=0){
							tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
						}
						else
						{
							tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
						}
					}
				}
				break;

			case "Up":
				if(handler.getWorld().body.isEmpty()){
					if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail=(new Tail(this.xCoord,this.yCoord+1,handler));
					}
					else {
						if(this.xCoord!=0){
							tail=(new Tail(this.xCoord-1,this.yCoord,handler));
						}
						else
						{
							tail=(new Tail(this.xCoord+1,this.yCoord,handler));
						}
					}
				} 
				else {
					if (handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					} 
					else {
						if(handler.getWorld().body.getLast().x!=0){
							tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
						}
						else{
							tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
						}
					}
				}
				break;

			case "Down":
				if(handler.getWorld().body.isEmpty()){
					if(this.yCoord!=0){
						tail=(new Tail(this.xCoord,this.yCoord-1,handler));
					}
					else{
						if(this.xCoord!=0){
							tail=(new Tail(this.xCoord-1,this.yCoord,handler));
						}
						else{
							tail=(new Tail(this.xCoord+1,this.yCoord,handler));
						}
					}
				}
				else {
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}
					else{
						if(handler.getWorld().body.getLast().x!=0){
							tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
						}
						else{
							tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
						}
					}
				}
				break;
			}

			handler.getWorld().body.addLast(tail);
			handler.getWorld().playerLocation[tail.x][tail.y] = true;
		}

		else if (justAte == true && handler.getWorld().apple.isGood == false) {
			handler.getWorld().appleLocation[xCoord][yCoord]=false;
			handler.getWorld().appleOnBoard=false;
		}

		else {
			handler.getWorld().body.addLast(new Tail(xCoord, yCoord, handler));
		}
	}
	
	//Power-Up: Eating a banana decreases the speed and increases the score by half the original formula.
	//Appears only when the speed in the game becomes difficult.
	public void slowSnake() {
		speedAdjust +=2;
		score += Math.sqrt(2*score + 1)/2;
		handler.getGame().playAudio(bananaSoundEffect, false);
		handler.getWorld().bananaLocation[xCoord][yCoord] = false;
		handler.getWorld().bananaOnBoard = false;
	}

	//Scoring system.
	public void adjustScore() {
		if (justAte == true) {
			if (handler.getWorld().apple.isGood) {
				//Score formula when eating a good apple.
				score += Math.sqrt(2*score + 1);
				//Increases the speed of the snake everytime it eats a dot.
				speedAdjust -= 5;
			}
			else if (handler.getWorld().apple.isGood == false) {
				//Score formula when eating a rotten apple.
				score -= Math.sqrt(2*score + 1);
			}
		}
	}
	
	//Game Over if the score gets negative.
	public void verifyScore() {
		if (score < 0) {
			handler.getGame().stopMainAudio();
			handler.getGame().playAudio(deathSoundEffect, false);
			State.setState(handler.getGame().gameOverState); 
		}
	}

	public void kill(){
		lenght = 0;
		handler.getGame().stopMainAudio();
		handler.getGame().playAudio(deathSoundEffect, false);

		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				handler.getWorld().playerLocation[i][j]=false;
				State.setState(handler.getGame().gameOverState); 
			}
		}
	}

	public String getHighScore(){
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader("highscore.dat"));
			return reader.readLine();
		}catch(Exception e){
			return "High score: 0 (by nobody)";
		}
		finally{
			try {
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void checkScore(){
		if(score > Integer.parseInt(highscore.substring(highscore.indexOf(":") + 2, highscore.indexOf("(")-1))){
			this.setBeatHighscore(true);
			handler.getGame().stopAudio();
			handler.getGame().playAudio("res/music/cheering.wav", false);
			String name = (String) JOptionPane.showInputDialog(null, "You set a new high score.\nPlease enter your name: ", "Congratulations!", JOptionPane.INFORMATION_MESSAGE, Images.icon, null, "");
			if(name == null){
				name = "somebody";
			}
			highscore = String.format("High Score: %d (by %s)", score, name); 

			File scoreFile = new File("highscore.dat");
			BufferedWriter writer;
			if(!scoreFile.exists()){
				try {
					scoreFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			try {
				writer = new BufferedWriter(new FileWriter(scoreFile));
				writer.write(highscore);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void closeGame() {
		if (score < Integer.parseInt(highscore.substring(highscore.indexOf(":") + 2, highscore.indexOf("(")-1)) || this.beatHighscore == false) {
			System.exit(0);
		}
	}

	public void setEatSoundEffect(String eatSoundEffect) {
		this.eatSoundEffect = eatSoundEffect;
	}

	public void setDeathSoundEffect(String deathSoundEffect) {
		this.deathSoundEffect = deathSoundEffect;
	}

	public void setAudioLoop(boolean toLoop) {
		this.soundLoop = toLoop;
	}

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public void setJustAteBanana(boolean justAteBanana) {
		this.justAteBanana = justAteBanana;
	}

	public void setBeatHighscore(boolean beatHighscore) {
		this.beatHighscore = beatHighscore;
	}
}
