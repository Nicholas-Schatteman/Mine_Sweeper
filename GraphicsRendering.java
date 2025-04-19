import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

public class GraphicsRendering extends JPanel{
    public MineField board = new MineField(20, 20, 20);
    private BufferedImage tile, flag, falseFlag, bomb, hitBomb;
    private BufferedImage number[] = new BufferedImage[9];
    private Clip dig;
    private Clip hit;
    private int mouseOveredTile[];
    private int lossingTile[];
    private boolean isM1Pressed;
    private boolean isLost = false;

    final private double TOP_X_TO_Y_MULTIPLIER = 0.25;
    final private int BORDER_WIDTH = 12;

    public GraphicsRendering(){
        super();

        mouseOveredTile = new int[2];
        mouseOveredTile[0] = -1;
        mouseOveredTile[1] = -1;

        lossingTile = new int[2];

        MouseAdapter mouseReader = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e){
                int space[] = pointToSpace(getMousePosition());
                if (e.getButton() == MouseEvent.BUTTON1){
                    mouseOveredTile[0] = -1;
                    mouseOveredTile[1] = -1;
                    isM1Pressed = false;
                }

                if (!board.isWon() && !isLost){
                    if (space[0] < board.rowLength() 
                    && space[1] < board.columnLength() 
                    && !board.isFlaged(space[0], space[1]) 
                    && !e.isControlDown() 
                    && !board.isSeen(space[0], space[1]) 
                    && (e.getButton() == MouseEvent.BUTTON1)){
                        //If a bomb is pressed
                        isLost = board.check(space[0], space[1]);
                        if (!isLost){
                            dig.start();
                            dig.setFramePosition(0);
                        }
                        else{
                            hit.start();
                            lossingTile = space;
                        }
                        repaint();
                    }
                    else if (space[0] < board.rowLength() 
                    && space[1] < board.columnLength() 
                    && !board.isSeen(space[0], space[1]) 
                    && (e.isControlDown() || (e.getButton() == MouseEvent.BUTTON3))){
                        board.addFlag(space[0], space[1]);
                        repaint();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int tileOver[] = pointToSpace(getMousePosition());
                if (e.getButton() == MouseEvent.BUTTON1 && !board.isWon() && !isLost){
                    isM1Pressed = true;
                }
                if ((tileOver[0] != mouseOveredTile[0] || tileOver[1] != mouseOveredTile[1]) 
                && e.getButton() == MouseEvent.BUTTON1 
                && !board.isWon() && !isLost){
                    mouseOveredTile = tileOver;
                    repaint();
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e){
                int tileOver[] = pointToSpace(getMousePosition());
                
                if ((tileOver[0] != mouseOveredTile[0] || tileOver[1] != mouseOveredTile[1]) && isM1Pressed){
                    mouseOveredTile = tileOver;
                    repaint();
                }
            }
        };

        addMouseListener(mouseReader);
        addMouseMotionListener(mouseReader);
        try{
            tile = ImageIO.read(new File("Images\\Tile.png"));
            flag = ImageIO.read(new File("Images\\Flag.png"));
            falseFlag = ImageIO.read(new File("Images\\FalseFlag.png"));
            bomb = ImageIO.read(new File("Images\\Bomb.png"));
            hitBomb = ImageIO.read(new File("Images\\HitBomb.png"));
            number[0] = ImageIO.read(new File("Images\\Number0.png"));
            number[1] = ImageIO.read(new File("Images\\Number1.png"));
            number[2] = ImageIO.read(new File("Images\\Number2.png"));
            number[3] = ImageIO.read(new File("Images\\Number3.png"));
            number[4] = ImageIO.read(new File("Images\\Number4.png"));
            number[5] = ImageIO.read(new File("Images\\Number5.png"));
            number[6] = ImageIO.read(new File("Images\\Number6.png"));
            number[7] = ImageIO.read(new File("Images\\Number7.png"));
            number[8] = ImageIO.read(new File("Images\\Number8.png"));

            File audioDig = new File("Audio\\Dig.wav");
            dig = AudioSystem.getClip();
            dig.open(AudioSystem.getAudioInputStream(audioDig));

            File audioHit = new File("Audio\\Hit.wav");
            hit = AudioSystem.getClip();
            hit.open(AudioSystem.getAudioInputStream(audioHit));
        }catch (IOException|LineUnavailableException|UnsupportedAudioFileException e){
            throw new IOError(e.getCause());
        }
    }

    private int[] pointToSpace(Point p){
        int space[] = new int[2];
        space[0] = (int)((p.getX() - BORDER_WIDTH) / getTileDimension());
        space[1] = (int)((p.getY() - 2 * BORDER_WIDTH) / getTileDimension() - TOP_X_TO_Y_MULTIPLIER * board.rowLength());
        return space;
    }

    private int getTileDimension(){
        int dimensions;
        if ((getWidth() - 2 * BORDER_WIDTH) / board.rowLength() >= (int)((getHeight() - 3 * BORDER_WIDTH) / (TOP_X_TO_Y_MULTIPLIER * board.rowLength() + board.columnLength()))){
            //dimensions = (getHeight() - 3 * BORDER_WIDTH - (int)(TOP_X_TO_Y_MULTIPLIER * board.rowLength())) / board.columnLength();
            dimensions = (int)((getHeight() - 3 * BORDER_WIDTH) / (TOP_X_TO_Y_MULTIPLIER * board.rowLength() + board.columnLength()));
        }
        else{
            dimensions = (getWidth() - 2 * BORDER_WIDTH) / board.rowLength();
        }

        return dimensions;
    }

    private int tileDrawnPosX(int x){
        return getTileDimension() * x + BORDER_WIDTH;
    }

    private int tileDrawnPosY(int y){
        return (int)(getTileDimension() * (y + TOP_X_TO_Y_MULTIPLIER * board.rowLength()) + 2 * BORDER_WIDTH);
    }

    @Override
    public void paint(Graphics g) {
        for (int y = 0; y < board.columnLength(); y++){
            for (int x = 0; x < board.rowLength(); x++){
                if (board.isFlaged(x, y) && !board.isMine(x, y) && isLost){
                    g.drawImage(falseFlag, tileDrawnPosX(x), tileDrawnPosY(y), getTileDimension(), getTileDimension(),null);
                }
                else if (board.isFlaged(x, y)){
                    g.drawImage(flag, tileDrawnPosX(x), tileDrawnPosY(y), getTileDimension(), getTileDimension(),null);
                }
                else if (!board.isSeen(x, y) && x == mouseOveredTile[0] && y == mouseOveredTile[1]){
                    g.drawImage(number[0], tileDrawnPosX(x), tileDrawnPosY(y), getTileDimension(), getTileDimension(),null);
                }
                else if (!board.isSeen(x, y)){
                    g.drawImage(tile, tileDrawnPosX(x), tileDrawnPosY(y), getTileDimension(), getTileDimension(),null);
                }
                else if (!board.isMine(x, y)){
                    g.drawImage(number[board.getNumber(x, y)], tileDrawnPosX(x), tileDrawnPosY(y), getTileDimension(), getTileDimension(),null);
                }
                else if (x == lossingTile[0] && y == lossingTile[1]){
                    g.drawImage(hitBomb, tileDrawnPosX(x), tileDrawnPosY(y), getTileDimension(), getTileDimension(),null);
                }
                else{
                    g.drawImage(bomb, tileDrawnPosX(x), tileDrawnPosY(y), getTileDimension(), getTileDimension(),null);
                }
            }
        }
    }
}