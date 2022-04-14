import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Repair extends Rectangle {

    ImageIcon repair=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("repair.png")));
    boolean onscreen=false;

    public Repair(int x,int y,int width,int height){
        super(x,y,width,height);
    }
    public void draw(Graphics2D g){
        g.drawImage(repair.getImage(),x,y,width,height,null);
    }
    public void newRepair(int screenwidth){
        x=screenwidth;
        onscreen=true;
    }
    public void clearRepair(int screenwidth){
        x=screenwidth;
        onscreen=false;
    }
    public void move(int gamespeed){
        x-=gamespeed;
        if(-width>=x){
            clearRepair(800);
        }
    }
}
