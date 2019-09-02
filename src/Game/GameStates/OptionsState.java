package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class OptionsState extends State {

    private int count = 0;
    private UIManager uiManager;

    public OptionsState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(168, 480, 69, 27, Images.Back, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().pauseState);
        }));
        uiManager.addObjects(new UIImageButton(75, 290, 256, 128, Images.MuteBM, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getGame().stopMainAudio();
            handler.getGame().setBackgroundMusicMute(true);
            }));
        uiManager.addObjects(new UIImageButton(75, 130, 256, 128, Images.MuteSE, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getGame().setSoundEffectMute(true);
            }));
        uiManager.addObjects(new UIImageButton(75, 370, 256, 128, Images.UnmuteBM, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getGame().playMainAudio();
            handler.getGame().setBackgroundMusicMute(false);
            }));
        uiManager.addObjects(new UIImageButton(75, 210, 256, 128, Images.UnmuteSE, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getGame().setSoundEffectMute(false);
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
        g.drawImage(Images.optionsState,0,0,780,780, null);
        uiManager.Render(g);

    }
}
