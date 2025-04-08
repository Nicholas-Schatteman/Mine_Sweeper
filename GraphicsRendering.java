import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.JPanel;

public class GraphicsRendering extends JPanel{
    public MineField board = new MineField(10, 10, 20);
    private BufferedImage tile, flag, bomb;
    private BufferedImage number[] = new BufferedImage[9];
    private AudioSystem dig;
    private boolean isClicked = false;
    private boolean isCtrlHeld = false;
    private boolean isLost = false;

    final public int TILE_HEIGHT = 15;
    final public int TILE_WIDTH = 15;

    public GraphicsRendering(){
        super();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                int space[] = pointToSpace(getMousePosition());
                if (!board.isWon() && !isLost){
                    if (space[0] < board.rowLength() && space[1] < board.columnLength() && !board.isFlaged(space[0], space[1]) && (e.getButton() == MouseEvent.BUTTON1)){
                        //If a bomb is pressed
                        isLost = board.check(space[0], space[1]);
                        repaint();
                    }
                    else if (space[0] < board.rowLength() && space[1] < board.columnLength() && !board.isSeen(space[0], space[1]) && (e.getButton() == MouseEvent.BUTTON3)){
                        board.addFlag(space[0], space[1]);
                        repaint();
                    }
                }
            }
            @Override
            public void mouseDragged(MouseEvent e){
                System.out.println("a");
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                System.out.println("Key press");
                if (e.isControlDown()){
                    isCtrlHeld = true;
                    System.out.println("ctrl pressed");
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                if (!e.isControlDown()){
                    isCtrlHeld = false;
                    System.out.println("ctrl unpressed");
                }
            }
        });
        try{
            tile = ImageIO.read(new File("Images\\Tile.png"));
            flag = ImageIO.read(new File("Images\\Flag.png"));
            bomb = ImageIO.read(new File("Images\\Bomb.png"));
            number[0] = ImageIO.read(new File("Images\\Number0.png"));
            number[1] = ImageIO.read(new File("Images\\Number1.png"));
            number[2] = ImageIO.read(new File("Images\\Number2.png"));
            number[3] = ImageIO.read(new File("Images\\Number3.png"));
            number[4] = ImageIO.read(new File("Images\\Number4.png"));
            number[5] = ImageIO.read(new File("Images\\Number5.png"));
            number[6] = ImageIO.read(new File("Images\\Number6.png"));
            number[7] = ImageIO.read(new File("Images\\Number7.png"));
            number[8] = ImageIO.read(new File("Images\\Number8.png"));

            
        }catch (IOException e){
            throw new IOError(e.getCause());
        }
    }

    private int[] pointToSpace(Point p){
        int space[] = new int[2];
        space[0] = (int)(p.getX() / TILE_WIDTH);
        space[1] = (int)(p.getY() / TILE_HEIGHT);
        return space;
    }

    @Override
    public void paint(Graphics g) {
        for (int y = 0; y < board.columnLength(); y++){
            for (int x = 0; x < board.rowLength(); x++){
                if (board.isFlaged(x, y)){
                    g.drawImage(flag, TILE_HEIGHT * x, TILE_WIDTH * y, TILE_WIDTH, TILE_HEIGHT,null);
                }
                else if (!board.isSeen(x, y) && isClicked){

                }
                else if (!board.isSeen(x, y)){
                    g.drawImage(tile, TILE_HEIGHT * x, TILE_WIDTH * y, TILE_WIDTH, TILE_HEIGHT,null);
                }
                else if (!board.isMine(x, y)){
                    g.drawImage(number[board.getNumber(x, y)], TILE_HEIGHT * x, TILE_WIDTH * y, TILE_WIDTH, TILE_HEIGHT,null);
                }
                else{
                    g.drawImage(bomb, TILE_HEIGHT * x, TILE_WIDTH * y, TILE_WIDTH, TILE_HEIGHT,null);
                }
            }
        }
    }
}