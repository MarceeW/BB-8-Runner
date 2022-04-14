import java.awt.*;

public class Ground extends Rectangle {

    public Ground(int x,int y,int width,int height){
        super(x,y,width,height);
    }
    public void restart(){
        x=0;
    }
}
