package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameInfoState extends State {

    private UIManager uiManager;

    public GameInfoState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(160, handler.getHeight()/20 + 595, 138, 54, Images.Back, ()-> {
            handler.getMouseManager().setUimanager(null); 
            State.setState(handler.getGame().menuState);
        }));
           
    }
    
    
    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.gameInfo,0,0,780,780, null);
        uiManager.Render(g);

    }
}
