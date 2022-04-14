import java.awt.*;
import java.util.Random;

public class Coin extends Rectangle {

    Image coin;
    boolean onscreen=false;

    public Coin(int x,int y,int width,int height,Image image){
        super(x,y,width,height);
        this.coin=image;
    }
    public void draw(Graphics2D g){
        g.drawImage(coin,x,y,width,height,null);
    }
    public void newCoin(int screenwidth){
       x=screenwidth;
       Random r=new Random();
       y=r.nextInt(200)+50;
       onscreen=true;
    }
    public void move(int gamespeed){
        x-=gamespeed;
    }
    public void clearCoin(int screenwidth){
        x=screenwidth;
        onscreen=false;
    }
}
