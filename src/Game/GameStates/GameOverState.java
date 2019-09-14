package Game.GameStates;

import java.awt.Color;
import java.awt.Graphics;

import Display.DisplayScreen;
import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameOverState extends State {

    private UIManager uiManager;

    public GameOverState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(125, handler.getGame().getHeight() - 150, 128, 64, Images.Yes, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.checkScore();
            handler.getGame().playMainAudio();
			State.setState(handler.getGame().menuState);
			DisplayScreen.setMessage("It's Snake Time!");
        }));

        uiManager.addObjects(new UIImageButton(560, handler.getGame().getHeight() - 150, 128, 64, Images.No, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.checkScore();
            handler.getWorld().player.closeGame();
        }));
    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.gameOver,0,0,780,780,null);
        uiManager.Render(g);
    }
}
