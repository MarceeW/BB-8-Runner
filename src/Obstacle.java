import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Obstacle extends Rectangle {

    int groundy;
    int anim;

    ImageIcon[] animation=new ImageIcon[30];

    public Obstacle(int x,int y,int width,int height){
        super(x,y,width,height);
        for (int i = 1; i <= 30; i++) {
            if(10>i){
                System.out.println("frame_00"+i+".png");
                animation[i-1]=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("blasterbolt/frame_00"+i+".png")));
            }
            else{
                System.out.println("frame_0"+i+".png");
                animation[i-1]=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("blasterbolt/frame_0"+i+".png")));
            }
        }
        newRandom(10);
        groundy=y;
    }
    public void draw(Graphics2D g){
        g.drawImage(animation[anim].getImage(),x-width/2,y,width*2,height,null);
        anim++;
        if(anim>=30){
            anim=0;
        }
    }
    public void newRandom(int gamespeed){
        x=800+gamespeed*2;
        Random r=new Random();
        this.height=((r.nextInt(5))*30)+150;
        this.y=groundy-height;
    }
    public void move(int gamespeed,boolean speedactivated){
        this.y=groundy-height;
        if(-width>=x && !speedactivated){
            newRandom(gamespeed);
        }
        x-=gamespeed;
    }
    public void restart(){
        x=1250;
    }
}
