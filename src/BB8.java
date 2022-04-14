import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class BB8 extends Rectangle {

    int speed;
    int xdir=0;
    int jumpsize=50;
    boolean jump=false;

    ImageIcon body;
    ImageIcon head;

    float mirroroc=0.1f;
    double headscale;
    int heady;
    int gamespeed;
    int type;

    int starty;
    int startx;

    public BB8(int x,int y,int width,int height,int speed,int type,ImageIcon body,ImageIcon head){
        super(x,y,width,height);
        this.speed=speed;
        this.body=body;
        this.head=head;
        this.type=type;
        setScale(type);
        startx=x;
        starty=y;
    }
    public Boolean outOfScreen(int SCREEN_WIDTH){
        return x >= SCREEN_WIDTH;
    }
    public void setScale(int type){
        if(type==4){
            headscale=4;
            heady=(int)((head.getIconHeight()/headscale)*0.83);
        }
        else{
            headscale=3.5;
            heady=64;
        }
    }
    public void setSkin(ImageIcon hskin,ImageIcon bskin,int stype){
        body=bskin;
        head=hskin;
        setScale(stype);
    }
    public void restart(){
        x=startx;
        y=starty;
    }
    public void draw(Graphics2D g2,int rotation,boolean hitground){
        AffineTransform oldXForm = g2.getTransform();
        g2.translate(x,y+35);
        g2.rotate(Math.toRadians(rotation), width/2, height/2);
        g2.translate(-x, -(y+35));
        if(hitground){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, mirroroc));
            g2.drawImage(body.getImage(),x,y+35,width,height,null);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2.setTransform(oldXForm);
        g2.translate(x,y);
        g2.rotate(Math.toRadians(rotation), width/2, height/2);
        g2.translate(-x, -y);
        g2.drawImage(body.getImage(),x,y,width,height,null);
        g2.setTransform(oldXForm);
        g2.drawImage(head.getImage(),width/2+x-(int)(head.getIconWidth()/headscale)/2,y-heady,(int)(head.getIconWidth()/headscale),(int)(head.getIconHeight()/headscale),null);
    }
    public void gravity(boolean hitground){
        if(!hitground && !jump){
            double vel=(y/10);
            y+=vel;
        }
        if(hitground){
            y=starty;
        }
    }
    public void move(){
        if(x>=-(speed*xdir) && (800-width)-(speed*xdir)>=x){
            x+=speed*xdir;
        }
        if(jump){
            double vel=(y/12);
            y-=vel;
            if(jumpsize>=y){
                jump=false;
            }
        }
    }
    public void keyPressed(KeyEvent e,boolean hitground){
        int ke=e.getKeyCode();
        if(e.getKeyCode()==KeyEvent.VK_D){
            xdir = 1;
        }
        if(e.getKeyCode()==KeyEvent.VK_A){
            xdir = -1;
        }
        if(hitground && ke==KeyEvent.VK_SPACE){
            jump=true;
        }
    }
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_A){
            xdir = 0;
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            jump=false;
        }
    }
}
