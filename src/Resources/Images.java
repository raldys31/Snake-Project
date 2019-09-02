package Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class Images {


    public static BufferedImage[] butstart;
    public static BufferedImage title;
    public static BufferedImage Pause;
    public static BufferedImage optionsState;
    public static BufferedImage[] Resume;
    public static BufferedImage[] BTitle;
    public static BufferedImage[] Options;
    public static BufferedImage[] Yes;
    public static BufferedImage[] No;
    public static BufferedImage[] Info;
    public static BufferedImage[] Back;
    public static BufferedImage[] MuteBM;
    public static BufferedImage[] MuteSE;
    public static BufferedImage[] UnmuteBM;
    public static BufferedImage[] UnmuteSE;
    public static BufferedImage gameOver;
    public static BufferedImage gameInfo;
    public static ImageIcon icon;
    

    public Images() {

        butstart = new BufferedImage[3];
        Resume = new BufferedImage[2];
        BTitle = new BufferedImage[2];
        Options = new BufferedImage[2];
        Yes = new BufferedImage[2];
        No = new BufferedImage[2];
        Info = new BufferedImage[2];
        Back = new BufferedImage[2];
        MuteBM = new BufferedImage[2];
        MuteSE = new BufferedImage[2];
        UnmuteBM = new BufferedImage[2];
        UnmuteSE = new BufferedImage[2];

        try {

            title = ImageIO.read(getClass().getResourceAsStream("/Sheets/Java Snake G Theme.jpg"));
            Pause = ImageIO.read(getClass().getResourceAsStream("/Sheets/PauseState.png"));
            gameInfo = ImageIO.read(getClass().getResourceAsStream("/Sheets/Game Info.jpg"));
            gameOver = ImageIO.read(getClass().getResourceAsStream("/Sheets/GameOverR.jpg"));
            optionsState = ImageIO.read(getClass().getResourceAsStream("/Sheets/OptionsP.jpg"));
            Resume[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/resumebv.png"));
            Resume[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/ResumeB2.png"));
            BTitle[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/titlebv.png"));
            BTitle[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/TitleB2.png"));
            Options[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/optionsbv.png"));
            Options[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/OptionsB2.png"));
            butstart[0]= ImageIO.read(getClass().getResourceAsStream("/Buttons/startbblack.png"));//normbut
            butstart[1]= ImageIO.read(getClass().getResourceAsStream("/Buttons/startbwhite2.png"));//hoverbut
            butstart[2]= ImageIO.read(getClass().getResourceAsStream("/Buttons/StartB3.png"));//clickbut
            Yes[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/yesB.png"));
            Yes[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/yesB2.png"));
            No[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/noB.png"));
            No[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/noB2.png"));
            Info[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Info.png"));
            Info[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Info2.png"));
            Back[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/back.png"));
            Back[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/back2.png"));
            MuteBM[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Mutebm.png"));
            MuteBM[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Mutebm2.png"));
            MuteSE[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Mutese.png"));
            MuteSE[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Mutese2.png"));
            UnmuteBM[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Unmutebm.png"));
            UnmuteBM[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Unmutebm2.png"));
            UnmuteSE[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Unmutese.png"));
            UnmuteSE[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Unmutese2.png"));

            icon =  new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/Sheets/pixelsnake.png")));


        }catch (IOException e) {
        e.printStackTrace();
    }


    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Images.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
