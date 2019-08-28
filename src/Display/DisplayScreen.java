package Display;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by AlexVR on 7/1/2018.
 */

public class DisplayScreen {

    private JFrame frame;
    private Canvas canvas;
    private String title;
    private int width, height;
    private Color backgroundColor;
    private static JLabel message;
    private Font messageFont;

    public DisplayScreen(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        backgroundColor = new Color(178,0,255);



        createDisplay();
    }

    private void createDisplay(){
    	
    	try {
			messageFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("res/Fonts/aerial.ttf"))).deriveFont(Font.PLAIN, 12);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FontFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	message = new JLabel("It's Snake Time!");
    	message.setFont(messageFont);
    	
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBackground(backgroundColor);
        frame.add(message, BorderLayout.SOUTH);

        try {
            frame.setIconImage(ImageIO.read(new File("res/Sheets/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);
        canvas.setBackground(backgroundColor);

        frame.add(canvas);
        frame.pack();
    }

    public Canvas getCanvas(){
        return canvas;
    }
    
    public static void setMessage(String text) {
		message.setText(text);
	}
    public static void enableMessage() {
    	message.setVisible(true);
    }
    public static void disableMessage() {
    	message.setVisible(false);;
    }

    public JFrame getFrame(){
        return frame;
    }

}
