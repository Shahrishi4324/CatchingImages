package Unit3.Animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CatchImages {
    JFrame frame; 
    DrawingPanel panel; 
    Timer timer; 
    Ball ball;
    Catcher catcher;
    ArrayList <Ball> balls = new ArrayList <Ball> (); 
    int score = 0; 
    int lives = 3;
    BufferedImage Catcher, Water;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CatchImages();
            }
        });
    }

    public CatchImages(){
        Catcher = loadImage("Unit3/Images/Bucket.png");
        Water = loadImage("Unit3/Images/WaterDroplet.png");
        frame = new JFrame("Catch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel = new DrawingPanel();

        for (int i = 0; i < 4; i++){
            balls.add(new Ball((int)(Math.random() * 1000), 10, 40));
        }
        catcher = new Catcher(500, 375, 200, 200);
        panel.addMouseMotionListener(new MouseHandler());
        frame.add(panel);

        timer = new Timer(10, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                for (Ball ball : balls){
                    ball.move();
                }
                for (Ball ball : balls){
                    if (ball.isTouchingGround()){
                        ball.y = 0;
                        ball.x = (int)(Math.random() * 1000);
                        ball.vy = (int)(Math.random() * 10) + 1;
                        ball.color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256),
                                (int) (Math.random() * 256));
                        lives--;

                    }
                    if (catcher.isTouching(ball)){
                        ball.y = 0;
                        ball.x = (int)(Math.random() * 1000);
                        ball.vy = (int)(Math.random() * 10) + 1;
                        ball.color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256),
                                (int) (Math.random() * 256));
                        score++;
                    }
                }
                panel.repaint();
                if (lives == 0){
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Game Over");
                    System.exit(0);
                }
            }
        });
        timer.start();
    }

    private class DrawingPanel extends JPanel{
        int panW, panH;
        public DrawingPanel(){  
            panW = 1000;
            panH = 600;
            this.setPreferredSize(new Dimension(panW, panH));
            this.setBackground(Color.GRAY);
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            for (Ball ball : balls){
                ball.draw(g2);
            }
            catcher.draw(g2);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("Score: " + score, 10, 20);
            g2.drawString("Lives: " + lives, 10, 40);
        }
    }

    private class Ball extends Rectangle{
        int vy;
        Color color;
        public Ball(int x, int y, int r){
            super(x, y, r, r);
            vy = (int)(Math.random() * 10) + 1;
            color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256),
                    (int) (Math.random() * 256));
        }

        public void move(){
            this.y += vy;
        }

        public void draw(Graphics2D g2){
            g2.setPaint(color);
            // g2.fillOval(x, y, width, height);
            g2.drawImage(Water, x, y, width, height, null);
        }

        public boolean isTouchingGround(){
            if (this.y + this.height >= 600){
                return true;
            }
            return false;
        }
    }

    private class Catcher extends Rectangle{
        public Catcher(int x, int y, int w, int h){
            super(x, y, w, h);
        }

        public void draw(Graphics2D g2){
            // g2.setColor(Color.BLACK);
            // g2.fill(this);
            g2.drawImage(Catcher, x, y, width, height, null);
        }

        public boolean isTouching(Ball ball){
            if (this.intersects(ball)){
                return true;
            }
            return false;
        }
    }

    private class MouseHandler extends MouseAdapter{
        
        public void mouseMoved(MouseEvent e){
            catcher.x = e.getX();
            if (catcher.x + catcher.width > 1000){
                catcher.x = 1000 - catcher.width;
            }
        }
    }

    static BufferedImage loadImage(String filename){
        BufferedImage img = null; 
        try{
            img = ImageIO.read(new File(filename));
        } catch(IOException e){
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "An image failed to load: " + filename, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return img; 
    }
}