import java.awt.Toolkit;
import javax.swing.JFrame;

import java.io.File;
import javax.imageio.ImageIO;

import java.io.IOException;

import java.awt.image.BufferedImage;

public class MineSweeper {
    public static void main(String[]args){
        Toolkit tk = Toolkit.getDefaultToolkit();
        JFrame gameWindow = new JFrame("Mine Sweeper");
        GraphicsRendering render = new GraphicsRendering();

        gameWindow.setSize(tk.getScreenSize());
        try{
            BufferedImage icon = ImageIO.read(new File("Images\\Icon.png"));
            gameWindow.setIconImage(icon);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        gameWindow.add(render);

        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameWindow.setVisible(true);
    }
}
