import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SpeedUp extends Rectangle {
    boolean moving=false;
    boolean activated=false;
    int startdistance;
    int startSpeed;
    ImageIcon image;
    public SpeedUp(int x,int y,int width,int height,ImageIcon image){
        super(x,y,width/2,height/2);
        this.image=image;
    }
    public void draw(Graphics2D g){
        g.drawImage(image.getImage(),x,y,width,height,null);
    }
    @Override
    public void move(int gamespeed,int obstaclex){
        x-=gamespeed;
        if(-width>=x){
            x=obstaclex+width;
            moving=false;
        }
    }
    public void clear(){
        x=1125;
    }
    public void Generate(){
        Random r=new Random();
        int chance=r.nextInt(2);
        if(chance==1){
            moving=true;
        }
    }
}
