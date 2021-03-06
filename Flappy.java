import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/*
 * Created by Aaron on 8/23/2016.
 * Modified by:
 * Jerrod Pope and Jack Jeffers 11/20/2018
 * 		This is a modified flap style game involving Bret Kavaunaugh
 * 	Any resemblance to persons, living or dead, is entirely coincidental or is intended purely as a satire,
 *  parody or spoof of such persons and is not intended to communicate any true or factual information about that person.
 *  This game is intended for a mature and discerning audience.
 */

public class Flappy implements ActionListener, KeyListener{
	public static Flappy instance;	//game instance
    
    private final int WIDTH = 800;		//Can't these two be moved outside the class
    private final int HEIGHT = 600;		//default HEIGHT and WIDTH was 800 x 600, def speed = 10
    //private int score = 0, gameSpeed = 10, objectCount = 0, beerCollected = 0;
    private int score, gameSpeed, objectCount, beerCollected;
    
    private int beerHeight = 80, beerWidth = 40;
    
    //default HEIGHT and WIDTH was 800 x 600, def speed = 10
    
    private Renderer renderer;		//also confused by this
    private Rectangle character;

    private ArrayList<Rectangle> cloud;
    private Random rand;

    private boolean start = false, 
    				gameover = false,
    				isDrunk = false;

    private int gameTick, 
    			drunkCooldown = 0;

    public Flappy() {	//constructor

        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();
        cloud = new ArrayList<Rectangle>();
        character = new Rectangle(200, 220, 20, 20);
        
        jFrame.setTitle("Flappy Green");
        jFrame.add(renderer);
        
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.addKeyListener(this);
        
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(int i = 0; i < 5; i++) {
        	addObject();
        }

        timer.start();
    }

    void repaint(Graphics g) {//this 'repaints' the graphics to start a new game after a gameover, i'm considering renamming this to restarter
        g.setColor(Color.black);			//resets the background to black
        g.fillRect(0,0, WIDTH, HEIGHT);		
        
        g.setColor(Color.blue);
        g.fillRect(0, HEIGHT - 100, WIDTH, 100);	//resets the 'ocean' at the bottom

        g.setColor(Color.green);	//creates the 'Character'
        g.fillRect(character.x, character.y, character.width, character.height);
        
        g.setColor(Color.WHITE);	
        g.setFont(new Font("Arial", 1 ,32));	
        g.drawString("Score: " + score , 15, 35);	//draw score counter
        g.drawString("Beers enjoyed: " + beerCollected , 15, 75);	//draws beerCollected

        if (character.y >=  HEIGHT - 100) {
            gameover = true;
        }
        
        if((character.y ) < 0)
        	character.y = 0;
        
        for (Rectangle rect : cloud) {	//draws and fills clouds	
        	if(rect.width == beerWidth) {
        		g.setColor(Color.yellow);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
        	} else {
        		g.setColor(Color.white);
        		g.fillRect(rect.x, rect.y, rect.width, rect.height);
        	} 
        }
        
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1 ,32));
        g.drawString("DrunkCooldown " + drunkCooldown, 15, 105);

        g.drawString("gameSpeed " + gameSpeed , 15, 125);
        
         
        /*
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1 ,32));
        g.drawString("objectCount is" + objectCount , 15, 75);
         */

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1 ,100));
        if (!start) {
        	g.drawString("Press to start!", 75, HEIGHT / 2);
        	gameSpeed = 10;
        }
        else if (gameover) {
            g.drawString("Game Over!", 100, HEIGHT / 2);
            objectCount = 0;
        }
    }

    private void addCloud() {	//adds clouds
        if (cloud.isEmpty() ) {	//if true, the function adds a new cloud at a random height
            cloud.add(new Rectangle(WIDTH + cloud.size() * 300, rand.nextInt(HEIGHT-120), 80, 100));
        } else {	//spawning a cloud with proper spacing outside of frame
            cloud.add(new Rectangle(cloud.get(cloud.size() - 1).x + 300, rand.nextInt(HEIGHT-120), 80, 100));
        }
    }
    
    private void addBeer() {
    	cloud.add(new Rectangle(cloud.get(cloud.size() - 1).x + 300, rand.nextInt(HEIGHT-120), beerWidth, beerHeight));
    }
  
    private void addObject() {
    	int beerRandom = rand.nextInt(7) + 1;
    	if(objectCount == 0) {//initial cloud
    		addCloud();
    	}else if(objectCount % beerRandom != 0) {//recurring clouds
    	//}else if(objectCount % 4 != 0) {//recurring clouds
    		addCloud();
    	}else {
    		addBeer();
    	}
    	objectCount++;
    }
    
    private void checkDrunk() {
    	
    	if(isDrunk == true) {
    		drunkCooldown += 75;//set this to a smaller amount for binge drinking
    	} else if(beerCollected >= 6) {
    		drunkCooldown += rand.nextInt(400) +200;
    		isDrunk = true;
    	}

    	
    	//feel free to add graphical function change here	
    }
    
    private void flap() {

        if (gameover) {
        	gameSpeed = 10;
            score = 0;
            beerCollected = 0;
            isDrunk = false;
            character = new Rectangle(200, 300, 20, 20);
            cloud.clear();
            addCloud();
            
            for(int i = 0; i < 5 ; i++)
            	addObject();
       
            gameover = false;
        }
        
        if (!start) {
            start = true;
        }
        else if (!gameover) {
            character.y -= 70;
            gameTick = 0;
        }

    }

    public static void main(String args[]) {	//ok, this has me confused
        instance = new Flappy();					//this creates a game instance to run the constructor?
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Space");
        if (start) {
            for (int i = 0; i < cloud.size(); i++) {	//cloud movement
                Rectangle rect = cloud.get(i);
                rect.x -= gameSpeed;
                
                if(rect.x <= character.x  && rect.x >= character.x - gameSpeed + 1 && !gameover && rect.width != beerWidth){	//score updater for cloud passage
                	score+= 10;	
                } 
            }
    
            for (int i = 0; i < cloud.size(); i++) {	//removes a cloud after it has exited the frame and creates a new one
                Rectangle rect = cloud.get(i);

                if (rect.x + rect.width < 0) {
                    cloud.remove(rect);
                    addObject();
                }
            }
            
            for (int i = 0; i < cloud.size(); i++) {	//cloud or beer hit detection
                Rectangle rect = cloud.get(i);
                
                if (rect.intersects(character) && rect.width == beerWidth) {//adds score for hitting beer and removes token
            		score+= 20;
            		cloud.remove(rect);
            		addObject();      		
            		beerCollected++;
            		checkDrunk();
            		
            	} else if (rect.intersects(character)) { //lose if cloud was hit
                    gameover = true;
                    character.x -= gameSpeed;
                }  
            }

            
            if( isDrunk == true) {
            	if(drunkCooldown % 12 == 0)
            		gameSpeed = rand.nextInt(12) + 7;	//tinker with this
            	
            	drunkCooldown--;
            	
            	if(drunkCooldown == 0) {
            		isDrunk = false;
            		gameSpeed = 10;
            		beerCollected = 0;
            	}
            }
 
            gameTick ++;
          
           if (gameTick %2 == 0 && character.y < HEIGHT - 100)//this is for letting the character fall and setting floor height
                character.y += gameTick;

        }

        renderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) 	//increase game gameSpeed by 1 pixel per tick
    		gameSpeed = (gameSpeed > 1) ?  gameSpeed -= 1 : 1;

    	if (e.getKeyCode() == KeyEvent.VK_RIGHT)	//decrease game speed by ~1 pixel per tick
    		gameSpeed++;
    	
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            flap();
        }
        
    }
}
