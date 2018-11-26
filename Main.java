
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * Created by Aaron on 8/23/2016.
 */
public class Main implements ActionListener, KeyListener{

    public static Main main;
    public final int WIDTH = 800;
    public final int HEIGHT = 600;

    public Renderer renderer;
    public Rectangle character;

    public ArrayList<Rectangle> cloud;
    public Random rand;

    public boolean start = false, gameover = false;

    public int tick;

    public Main() {

        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();

        jFrame.setTitle("Example");
        jFrame.add(renderer);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.addKeyListener(this);
        jFrame.setVisible(true);

        cloud = new ArrayList<Rectangle>();
        character = new Rectangle(200, 220, 20, 20);

        addCloud(true);
        addCloud(true);
        addCloud(true);
        addCloud(true);
        addCloud(true);
        addCloud(true);
        addCloud(true);
        addCloud(true);

        timer.start();

    }

    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0, WIDTH, HEIGHT);

        g.setColor(Color.blue);
        g.fillRect(0, HEIGHT - 100, WIDTH, 100);

        g.setColor(Color.green);
        g.fillRect(character.x, character.y, character.width, character.height);

        if (character.y >=  HEIGHT - 100 || character.y < 0) {
            gameover = true;
        }

        for (Rectangle rect : cloud) {
            g.setColor(Color.white);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1 ,100));
        if (!start) {
            g.drawString("Press to start!", 75, HEIGHT / 2);
        }
        else if (gameover) {
            g.drawString("Game Over!", 100, HEIGHT / 2);
        }
    }

    public void addCloud(boolean start) {
        int width = 100;
        int height = 400;

        if (start) {
            cloud.add(new Rectangle(WIDTH + width + cloud.size() * 300, rand.nextInt(HEIGHT-120), 80, 100));
    }
        else {
            cloud.add(new Rectangle(cloud.get(cloud.size() - 1).x + 300, rand.nextInt(HEIGHT-120), 80, 100));
        }
    }

    public void flap() {

        if (gameover) {
            character = new Rectangle(200, 300, 20, 20);
            cloud.clear();

            addCloud(true);
            addCloud(true);
            addCloud(true);
            addCloud(true);
            addCloud(true);
            addCloud(true);
            addCloud(true);
            addCloud(true);
            gameover = false;
        }

        if (!start) {
            start = true;
        }
        else if (!gameover) {
            character.y -= 70;
            tick = 0;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 15;
        //System.out.println("Space");

        if (start) {
            for (int i = 0; i < cloud.size(); i++) {
                Rectangle rect = cloud.get(i);
                rect.x -= speed;
            }

            for (int i = 0; i < cloud.size(); i++) {
                Rectangle rect = cloud.get(i);

                if (rect.x + rect.width < 0) {
                    cloud.remove(rect);

                    addCloud(false);

                }
            }

            for (Rectangle rect : cloud) {
                if (rect.intersects(character)) {
                    gameover = true;
                    character.x -= speed;
                }
            }


            tick ++;

            if (tick %2 == 0 && character.y < HEIGHT-100)
                character.y += tick;

            if (gameover && character.y >= HEIGHT - 100) {
                character.x -= speed;
            }
        }

        renderer.repaint();
    }

    public static void main(String args[]) {
        main = new Main();
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            flap();
        }
    }
}
