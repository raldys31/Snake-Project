package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class PauseState extends State {

	private int count = 0;
	private UIManager uiManager;

	public PauseState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUimanager(uiManager);

		uiManager.addObjects(new UIImageButton(33, handler.getGame().getHeight() - 150, 200, 128, Images.Resume, () -> {
			handler.getMouseManager().setUimanager(null);
			State.setState(handler.getGame().gameState);
		}));

		uiManager.addObjects(new UIImageButton(33 + 287,  handler.getGame().getHeight() - 150, 200, 128, Images.Options, () -> {
			handler.getMouseManager().setUimanager(null);
			State.setState(handler.getGame().menuState);
		}));

		uiManager.addObjects(new UIImageButton(33 + 575,  handler.getGame().getHeight() - 150, 200, 128, Images.BTitle, () -> {
			handler.getMouseManager().setUimanager(null);
			State.setState(handler.getGame().menuState);
		}));





	}

	@Override
	public void tick() {
		handler.getMouseManager().setUimanager(uiManager);
		uiManager.tick();
		count++;
		if( count>=30){
			count=30;
		}
		if(handler.getKeyManager().pbutt && count>=30){
			count=0;

			State.setState(handler.getGame().gameState);
		}


	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Images.Pause,0,0,780,780,null);
		uiManager.Render(g);

	}
}