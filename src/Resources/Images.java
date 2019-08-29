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
    public static BufferedImage[] Resume;
    public static BufferedImage[] BTitle;
    public static BufferedImage[] Options;
    public static BufferedImage[] Yes;
    public static BufferedImage[] No;
    public static BufferedImage gameOver;
    public static ImageIcon icon;

    public Images() {

        butstart = new BufferedImage[3];
        Resume = new BufferedImage[2];
        BTitle = new BufferedImage[2];
        Options = new BufferedImage[2];
        Yes = new BufferedImage[2];
        No = new BufferedImage[2];

        try {

            title = ImageIO.read(getClass().getResourceAsStream("/Sheets/Java Snake G Theme.jpg"));
            Pause = ImageIO.read(getClass().getResourceAsStream("/Sheets/PauseState.png"));
            gameOver = ImageIO.read(getClass().getResourceAsStream("/Sheets/GameOverR.jpg"));
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
