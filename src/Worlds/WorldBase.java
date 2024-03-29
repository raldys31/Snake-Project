package Worlds;

import Game.Entities.Dynamic.Player;
import Game.Entities.Dynamic.Tail;
import Game.Entities.Static.Apple;
import Game.Entities.Static.Banana;
import Main.Handler;

import java.awt.*;
import java.util.LinkedList;


/**
 * Created by AlexVR on 7/2/2018.
 */
public abstract class WorldBase {

	//How many pixels are from left to right
	//How many pixels are from top to bottom
	//Must be equal
	public int GridWidthHeightPixelCount;

	//automatically calculated, depends on previous input.
	//The size of each box, the size of each box will be GridPixelsize x GridPixelsize.
	public int GridPixelsize;

	public Player player;

	protected Handler handler;


	public Boolean appleOnBoard;
	public Apple apple;
	public Boolean[][] appleLocation;
	public Boolean bananaOnBoard;
	public Banana banana;
	public Boolean[][] bananaLocation;
	private Color gridlinesColor;


	public Boolean[][] playerLocation;

	public LinkedList<Tail> body = new LinkedList<>();


	public WorldBase(Handler handler){
		this.handler = handler;

		appleOnBoard = false;
		bananaOnBoard = false;
		gridlinesColor = new Color(178,0,255);


	}
	public void tick(){



	}

	public void render(Graphics g){

		for (int i = 0; i <= 780; i = i + GridPixelsize) {

			g.setColor(gridlinesColor);
			g.drawLine(0, i, handler.getWidth() , i);
			g.drawLine(i,0,i,handler.getHeight());

		}



	}

}
