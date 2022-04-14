import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;
import java.util.Random;

public class Game extends JPanel implements ActionListener,MouseListener {

    final int SCREEN_WIDTH=800;
    final int SCREEN_HEIGHT=600;
    final int BB_SIZE=150;
    final int BB_SPEED=14;
    final int COIN_SIZE=50;

    int GAME_SPEED=2;
    int hp;
    int coins;
    int gamestartanim=SCREEN_HEIGHT;
    int animy=0;
    int highscore;
    int currenthighscore;
    int secretskins=1;

    JFrame frame=new JFrame();

    Graphics graphics;
    Image image;

    BackGround backGround=new BackGround();
    BB8 bb;
    Ground ground;
    Obstacle obstacle;
    Coin coin;
    Repair repair;
    SpeedUp speedUp;

    int bbtype;

    ImageIcon bgimage=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("background.png")));
    ImageIcon coinimage=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("coin.png")));
    ImageIcon game_over=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("game_over.png")));
    ImageIcon replayimage=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("replay.png")));
    ImageIcon replayhover=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("replay_hover.png")));
    ImageIcon close=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("close.png")));
    ImageIcon closehover=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("close_hover.png")));
    ImageIcon back=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("back.png")));
    ImageIcon backhover=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("back_hover.png")));
    ImageIcon home=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("home.png")));
    ImageIcon homehover=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("home_hover.png")));
    ImageIcon menulogo=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("mainmenulogo.png")));
    ImageIcon skinsbg=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("skinsbg.png")));
    ImageIcon locked=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("locked.png")));
    ImageIcon speedup=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("speedup.png")));

    int skinamount=7;

    ImageIcon[] hskins=new ImageIcon[skinamount];
    ImageIcon[] bskins=new ImageIcon[skinamount];

    boolean[] owned=new boolean[skinamount];
    int selected;
    int[] dupliers={1,2,2,3,5,8,8};
    int[] prices={0,50,115,200,450,850,0};
    int[] hps={100,110,120,150,175,200,200};

    String[] bbnames={"BB-8","RED BB-8","DARK BB-8","WHITE BB-8","BB-9E","TITANIUM BB-8", "BB-8 FE"};
    String[] skinrarity={"COMMON","UNCOMMON","RARE","RARE","EPIC","LEGENDARY", "LEGENDARY"};
    Color[] namecolors=new Color[skinamount];

    JProgressBar hpbar=new JProgressBar();

    int rotation=0;
    int distance=0;
    int score=0;
    int duplier=1;

    int buttonSource=-1;

    Timer timer;

    boolean gameover=false;
    boolean inmenu=true;
    boolean inshop=false;
    boolean screenpause=false;
    boolean newhighscore;

//MENU
    JLabel newGame=new JLabel();
    JLabel shop=new JLabel();
    JLabel exit=new JLabel();
//SHOP
    JLabel backbutton=new JLabel();
    JLabel rbutton=new JLabel();
    JLabel lbutton=new JLabel();
    JLabel skinbg=new JLabel();
    JLabel buybutton=new JLabel();
    JProgressBar coinbar=new JProgressBar();
    JProgressBar hbar=new JProgressBar();
//GAMEOVER
    JLabel restart=new JLabel();
    JLabel closelabel=new JLabel();
    JLabel homelabel=new JLabel();

    JLabel cheatbutton=new JLabel();

    saveData sd= new saveData();

    public Game() throws IOException {

        sd.Read();
        hp=hps[selected];
        owned[0]=true;

        namecolors[0]=new Color(0xBEFFCA7A, true);
        namecolors[1]=new Color(0xB751FF91, true);
        namecolors[2]=new Color(0xA97AFFD9, true);
        namecolors[3]=new Color(0xB0A97AFF, true);
        namecolors[4]=new Color(0xADE54FFF, true);
        namecolors[5]=new Color(0xBEFF0B3C, true);
        namecolors[6]=new Color(0xBEFF0B73, true);

        for (int i = 0; i < hskins.length; i++) {
            hskins[i]=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(i + "h.png")));
        }
        for (int i = 0; i < bskins.length; i++) {
            bskins[i]=new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(i + "b.png")));
        }

        int maxdupliers=0;
        for (int k : dupliers) {
            if (k > maxdupliers) {
                maxdupliers = k;
            }
        }
        coinbar.setBounds(SCREEN_WIDTH/2-100,160+SCREEN_HEIGHT,200,15);
        coinbar.setMaximum(maxdupliers);
        coinbar.setValue(dupliers[bbtype]);
        coinbar.setBorderPainted(false);
        coinbar.setBackground(new Color(0x0FFFFFF, true));
        coinbar.setVisible(true);
        this.add(coinbar);

        int maxhps=0;
        for (int j : hps) {
            if (j > maxhps) {
                maxhps = j;
            }
        }
        hbar.setBounds(SCREEN_WIDTH/2-100,200+SCREEN_HEIGHT,200,15);
        hbar.setMaximum(maxhps);
        hbar.setValue(hps[bbtype]);
        hbar.setBorderPainted(false);
        hbar.setBackground(new Color(0x0FFFFFF, true));
        hbar.setForeground(new Color(0xAB13FC68, true));
        hbar.setVisible(true);
        this.add(hbar);

        hpbar.setValue(hp);
        hpbar.setMaximum(hps[selected]);
        hpbar.setBounds(SCREEN_WIDTH-275,40,200,25);
        hpbar.setBorderPainted(false);
        hpbar.setBackground(new Color(0x1B000000, true));
        hpbar.setForeground(new Color(0xAB13FC68, true));
        hpbar.setVisible(false);
        this.add(hpbar);

        ground=new Ground(0,SCREEN_HEIGHT-30,SCREEN_WIDTH,30);

        bb=new BB8(30,(int)ground.getY()-BB_SIZE+1,BB_SIZE,BB_SIZE,BB_SPEED,selected,bskins[selected],hskins[selected]);
        bb.gamespeed=GAME_SPEED;

        obstacle=new Obstacle(SCREEN_WIDTH+250,SCREEN_HEIGHT-ground.height,75,400);

        coin=new Coin(SCREEN_WIDTH,0,COIN_SIZE,COIN_SIZE,coinimage.getImage());

        repair=new Repair(SCREEN_WIDTH,150,40,40);

        speedUp=new SpeedUp(SCREEN_WIDTH+150,SCREEN_HEIGHT-200,speedup.getIconWidth(),speedup.getIconHeight(),speedup);

        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setLayout(null);

        frame.setTitle("BB Runner");
        frame.setIconImage(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setLocation(283,84);
        frame.addKeyListener(new kListener());
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

        newGame.addMouseListener(this);
        this.add(newGame);

        shop.addMouseListener(this);
        this.add(shop);

        exit.addMouseListener(this);
        this.add(exit);

        backbutton.setBounds(50,SCREEN_HEIGHT+200,back.getIconWidth()/8,back.getIconHeight()/8);
        backbutton.addMouseListener(this);
        this.add(backbutton);

        rbutton.setBounds(550,SCREEN_HEIGHT+200,30,120);
        rbutton.addMouseListener(this);
        this.add(rbutton);

        lbutton.setBounds(220,SCREEN_HEIGHT+200,30,120);
        lbutton.addMouseListener(this);
        this.add(lbutton);

        skinbg.setBounds(0,SCREEN_HEIGHT+200,skinsbg.getIconWidth(),skinsbg.getIconHeight());
        this.add(skinbg);

        buybutton.setBounds(SCREEN_WIDTH/2-125,SCREEN_HEIGHT+400,250,50);
        buybutton.addMouseListener(this);
        this.add(buybutton);

        restart.setBounds(SCREEN_WIDTH/2-200,250,100,100);
        restart.addMouseListener(this);
        this.add(restart);

        closelabel.setBounds(restart.getX()+150,restart.getY(),95,95);
        closelabel.addMouseListener(this);
        this.add(closelabel);

        homelabel.setBounds(closelabel.getX()+150,closelabel.getY(),100,100);
        homelabel.addMouseListener(this);
        this.add(homelabel);

        cheatbutton.setBounds(SCREEN_WIDTH-10,SCREEN_HEIGHT-10,10,10);
        cheatbutton.addMouseListener(this);
        this.add(cheatbutton);

        timer=new Timer(10,this);
        timer.start();

    }
    public void newGameAnimation(Graphics g){
        g.setColor(new Color(0x81000000, true));
        g.fillRect(0,gamestartanim-SCREEN_HEIGHT,SCREEN_WIDTH,SCREEN_HEIGHT);
        gamestartanim-=10;
        if(50>=gamestartanim){
            hpbar.setVisible(true);
        }
    }
    public void MainMenu(Graphics2D g){

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(0x81000000, true));
        g.fillRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);

        Color buttoncolor=new Color(0xABFFFFFF, true);

        g.drawImage(menulogo.getImage(),SCREEN_WIDTH/2-menulogo.getIconWidth()/2,50+animy,this);
        g.setColor(new Color(0xD2FFFFFF, true));
        g.setFont(new Font("Century Gothic",Font.PLAIN,35));
        int width = g.getFontMetrics().stringWidth("N E W   G A M E");
        int height = g.getFontMetrics().getHeight();
        newGame.setBounds(SCREEN_WIDTH/2-200,340+animy,width+40,height);
        g.drawString("N E W   G A M E",newGame.getX()+40,newGame.getY()+height-10);

        width = g.getFontMetrics().stringWidth("S K I N S");
        height = g.getFontMetrics().getHeight();
        shop.setBounds(newGame.getX(),newGame.getY()+50,width+40,height);
        g.drawString("S K I N S",shop.getX()+40,shop.getY()+height-10);

        width = g.getFontMetrics().stringWidth("E X I T");
        height = g.getFontMetrics().getHeight();
        exit.setBounds(newGame.getX(),shop.getY()+50,width+40,height);
        g.drawString("E X I T",exit.getX()+40,exit.getY()+height-10);

        g.drawImage(back.getImage(),backbutton.getX(), backbutton.getY(),backbutton.getWidth(), backbutton.getHeight(),this);

        g.setColor(buttoncolor);
        g.fillRect(SCREEN_WIDTH/2-menulogo.getIconWidth()/2+5,newGame.getY(),6,newGame.getHeight());
        g.fillRect(SCREEN_WIDTH/2-menulogo.getIconWidth()/2+5,shop.getY(),6,shop.getHeight());
        g.fillRect(SCREEN_WIDTH/2-menulogo.getIconWidth()/2+5,exit.getY(),6,exit.getHeight());

        // SHOP
        if(inshop){

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawImage(coinimage.getImage(),backbutton.getX(),backbutton.getY()+100,COIN_SIZE,COIN_SIZE,this);
            g.setColor(new Color(0xD2FFFFFF, true));
            g.drawString(String.valueOf(coins),backbutton.getX()+60, backbutton.getY()+135);
            g.setColor(new Color(0xFFFFFFFF, true));
            g.setFont(new Font("Century Gothic",Font.ITALIC,15));
            g.drawString("C O I N   M U L T I P L I E R:   "+dupliers[bbtype]+" X",coinbar.getX(),coinbar.getY()-5);
            g.drawString("H E A L T H:  "+hps[bbtype],hbar.getX(),hbar.getY()-5);

            g.setColor(buttoncolor);
            if(bbtype>0){
                g.fillPolygon(new int[]{lbutton.getX(), lbutton.getX() + lbutton.getWidth(), lbutton.getX() + lbutton.getWidth()}, new int[]{rbutton.getY() + rbutton.getHeight() / 2, rbutton.getY(), rbutton.getY() + rbutton.getHeight()}, 3);
            }
            if(hskins.length-1-secretskins>bbtype){
                g.fillPolygon(new int[]{rbutton.getX(),rbutton.getX(),rbutton.getX()+rbutton.getWidth()},new int[]{rbutton.getY(),rbutton.getY()+rbutton.getHeight(),rbutton.getY()+rbutton.getHeight()/2},3);
            }
            g.drawImage(skinsbg.getImage(),skinbg.getX(),skinbg.getY(),this);

            g.setFont(new Font("Century Gothic",Font.BOLD,20));
            width = g.getFontMetrics().stringWidth(String.valueOf(skinrarity[bbtype]));
            g.setPaint(namecolors[bbtype]);
            g.drawString(skinrarity[bbtype],SCREEN_WIDTH/2-width/2,SCREEN_HEIGHT+130+animy);

            g.setFont(new Font("Century Gothic",Font.PLAIN,70));
            width = g.getFontMetrics().stringWidth(String.valueOf(bbnames[bbtype]));
            height = g.getFontMetrics().getHeight();
            int y=SCREEN_HEIGHT+100+animy;
            g.setPaint(namecolors[bbtype]);
            g.drawString(bbnames[bbtype],SCREEN_WIDTH/2-width/2,y);
            GradientPaint gp = new GradientPaint(
                    SCREEN_WIDTH/2-width/2,
                    y,
                    new Color(0x9CFFFFFF, true),
                    SCREEN_WIDTH/2+width/2,
                    y+height,
                    new Color(0x0FFFFFF, true));
            g.setPaint(gp);
            g.drawString(bbnames[bbtype],SCREEN_WIDTH/2-width/2,y);
            bb.draw(g,rotation,bb.intersects(ground));

            if(!owned[bbtype]){
                g.drawImage(locked.getImage(),SCREEN_WIDTH/2- locked.getIconWidth()/6,SCREEN_HEIGHT/2-locked.getIconHeight()/6+60+SCREEN_HEIGHT+animy,locked.getIconWidth()/3,locked.getIconHeight()/3,this);
                if(coins>=prices[bbtype]){
                    g.setColor(new Color(0x8861FF96, true));
                }
                else{
                    g.setColor(new Color(0x88F8F8F8, true));
                }
                g.fillRect(buybutton.getX(),buybutton.getY(),buybutton.getWidth(),buybutton.getHeight());
                g.setFont(new Font("Century Gothic",Font.PLAIN,35));
                width = g.getFontMetrics().stringWidth(String.valueOf(prices[bbtype]));
                height = g.getFontMetrics().getHeight();
                g.drawImage(coinimage.getImage(),SCREEN_WIDTH/2+20,buybutton.getY()+5,height-5,height-5,this);
                g.drawString(String.valueOf(prices[bbtype]),SCREEN_WIDTH/2-50,buybutton.getY()+37);
            }
            else{
                g.setColor(new Color(0x6AC9FFFE, true));
                g.fillRect(buybutton.getX(),buybutton.getY(),buybutton.getWidth(),buybutton.getHeight());
                g.setColor(new Color(0x88FFFFFF, true));
                if(bbtype==selected){
                    g.setFont(new Font("Century Gothic",Font.PLAIN,30));
                    width = g.getFontMetrics().stringWidth("S E L E C T E D");
                    height = g.getFontMetrics().getHeight();
                    g.drawString("S E L E C T E D",SCREEN_WIDTH/2-width/2,buybutton.getY()+37);
                }
                else{
                    g.setFont(new Font("Century Gothic",Font.PLAIN,30));
                    width = g.getFontMetrics().stringWidth("S E L E C T");
                    height = g.getFontMetrics().getHeight();
                    g.drawString("S E L E C T",SCREEN_WIDTH/2-width/2,buybutton.getY()+37);
                }
            }
        }
        switch (buttonSource){
            case 0:
                g.fillRect(SCREEN_WIDTH / 2 - menulogo.getIconWidth() / 2 + 5, newGame.getY()+animy, 24, newGame.getHeight());
                break;
            case 1:
                g.fillRect(SCREEN_WIDTH / 2 - menulogo.getIconWidth() / 2 + 5, shop.getY()+animy, 24, shop.getHeight());
                break;
            case 2:
                g.fillRect(SCREEN_WIDTH / 2 - menulogo.getIconWidth() / 2 + 5, exit.getY()+animy, 24, exit.getHeight());
                break;
            case 3:
                g.drawImage(backhover.getImage(),backbutton.getX(), backbutton.getY(),backbutton.getWidth(), backbutton.getHeight(),this);
                break;
            case 4:
                if(bbtype>0){
                    g.fillPolygon(new int[]{lbutton.getX(),lbutton.getX()+lbutton.getWidth(),lbutton.getX()+lbutton.getWidth()},new int[]{rbutton.getY()+rbutton.getHeight()/2,rbutton.getY(),rbutton.getY()+rbutton.getHeight()},3);
                }
                break;
            case 5:
                if(hskins.length-1-secretskins>bbtype){
                    g.fillPolygon(new int[]{rbutton.getX(),rbutton.getX(),rbutton.getX()+rbutton.getWidth()},new int[]{rbutton.getY(),rbutton.getY()+rbutton.getHeight(),rbutton.getY()+rbutton.getHeight()/2},3);
                }
                break;
            case 6:
                if(bbtype!=selected){
                    g.setColor(new Color(0x3BF8F8F8, true));
                    g.fillRect(buybutton.getX(),buybutton.getY(),buybutton.getWidth(),buybutton.getHeight());
                }
                if(!owned[bbtype]){
                    g.drawImage(coinimage.getImage(),SCREEN_WIDTH/2+20,buybutton.getY()+5,height-5,height-5,this);
                }
                break;
        }
    }
    public void resetSkins(){
        for (int i = 1; i < skinamount; i++) {
            owned[i]=false;
        }
        coins=2000;
        selected=0;
        secretskins=1;
        bbtype=selected;
        highscore=0;
        bb.setSkin(hskins[bbtype],bskins[bbtype],bbtype);
    }
    public void GameOverScreen(Graphics2D g){

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.setColor(new Color(0x81000000, true));
        g.fillRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawImage(game_over.getImage(),SCREEN_WIDTH/2-game_over.getIconWidth()/2,100,this);

        if(!newhighscore){
            g.setFont(new Font("Century Gothic",Font.PLAIN,25));
            g.setPaint(Color.white);
            int width = g.getFontMetrics().stringWidth("Y O U R   S C O R E:");
            g.drawString("Y O U R   S C O R E:",SCREEN_WIDTH/2-width/2-150,400);
            width = g.getFontMetrics().stringWidth("H I G H S C O R E:");
            g.drawString("H I G H S C O R E:",SCREEN_WIDTH/2-width/2+150,400);
            g.setFont(new Font("Century Gothic",Font.BOLD,70));
            width = g.getFontMetrics().stringWidth(String.valueOf(currenthighscore));
            g.drawString(String.valueOf(currenthighscore),SCREEN_WIDTH/2-width/2-150,470);
            width = g.getFontMetrics().stringWidth(String.valueOf(highscore));
            g.drawString(String.valueOf(highscore),SCREEN_WIDTH/2-width/2+150,470);
        }
        else{
            g.setFont(new Font("Century Gothic",Font.PLAIN,25));
            g.setPaint(Color.white);
            int width = g.getFontMetrics().stringWidth("Y O U R   S C O R E:");
            g.drawString("Y O U R   S C O R E:",SCREEN_WIDTH/2-width/2,450);
            g.setFont(new Font("Century Gothic",Font.BOLD,75));
            width = g.getFontMetrics().stringWidth(String.valueOf(highscore));
            g.drawString(String.valueOf(highscore),SCREEN_WIDTH/2-width/2,520);
            g.setFont(new Font("Century Gothic",Font.BOLD,35));
            width = g.getFontMetrics().stringWidth("N E W  H I G H S C O R E !");
            g.drawString("N E W  H I G H S C O R E !",SCREEN_WIDTH/2-width/2,400);
        }

        g.drawImage(replayimage.getImage(),restart.getX(),restart.getY(),restart.getWidth(),restart.getHeight(),this);
        g.drawImage(close.getImage(),closelabel.getX(),closelabel.getY(),closelabel.getWidth(),closelabel.getHeight(),this);
        g.drawImage(home.getImage(),homelabel.getX(),homelabel.getY(),homelabel.getWidth(),homelabel.getHeight(),this);

        if(buttonSource==0){
            g.drawImage(replayhover.getImage(),restart.getX(),restart.getY(),restart.getWidth(),restart.getHeight(),this);
        }
        else if(buttonSource==1){
            g.drawImage(closehover.getImage(),closelabel.getX(),closelabel.getY(),closelabel.getWidth(),closelabel.getHeight(),this);
        }
        else if(buttonSource==2){
            g.drawImage(homehover.getImage(),homelabel.getX(),homelabel.getY(),homelabel.getWidth(),homelabel.getHeight(),this);
        }
    }
    public void PauseScreen(Graphics2D g){
        g.setColor(new Color(0x81000000, true));
        g.fillRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        g.setColor(new Color(0x81FFFFFF, true));
        g.fillRect(SCREEN_WIDTH/2-25-50,SCREEN_HEIGHT/2-100,50,200);
        g.fillRect(SCREEN_WIDTH/2-25+50,SCREEN_HEIGHT/2-100,50,200);
    }
    public void gameRestart(){
        repair.clearRepair(SCREEN_WIDTH);
        ground.restart();
        bb.restart();
        obstacle.restart();
        distance=0;
        score=0;
        duplier=1;
        rotation=0;
        GAME_SPEED=2;
        currenthighscore=score;
        hp=hps[selected];
        speedUp.clear();
        hpbar.setVisible(false);
        hpbar.setValue(hp);
        coin.clearCoin(SCREEN_WIDTH);
        gamestartanim=SCREEN_HEIGHT;
        gameover=false;
        this.setVisible(true);
        timer.start();
    }
    public void draw(Graphics2D g){
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if(coin.onscreen){
            coin.draw(g);
        }
        if(repair.onscreen){
            repair.draw(g);
        }
        if(speedUp.moving){
            speedUp.draw(g);
        }
        bb.draw(g,rotation,bb.intersects(ground));
        obstacle.draw(g);
        if(gamestartanim>=0 && !inmenu){
            newGameAnimation(g);
        }
        if(gameover){
            GameOverScreen(g);
        }
        else if(inmenu){
            MainMenu(g);
        }
    }

    public void paintComponent(Graphics g){
        image=createImage(SCREEN_WIDTH,SCREEN_HEIGHT);
        graphics=image.getGraphics();
        Graphics2D g2=(Graphics2D)graphics;
        backGround.draw(g2);
        if(!gameover && 70>=gamestartanim && !inmenu){
            g2.setPaint(new Color(0x1E000000, true));
            g2.fillRect(0,0,SCREEN_WIDTH,100);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.drawImage(coinimage.getImage(),25,30,COIN_SIZE,COIN_SIZE,this);
            g2.setFont(new Font("Century Gothic",Font.BOLD,30));
            g2.setColor(new Color(0x5B312E2E,true));
            g2.drawString(String.valueOf(coins),90,65);
            g2.setFont(new Font("Century Gothic",Font.BOLD,70));
            g2.drawString(String.valueOf(score),SCREEN_WIDTH/2-120,80);
            g2.setColor(new Color(0xFFFFFFFF,true));
            g2.setFont(new Font("Century Gothic",Font.BOLD,30));
            g2.drawString(String.valueOf(coins),90-3,68);
            g2.setFont(new Font("Century Gothic",Font.BOLD,70));
            g2.drawString(String.valueOf(score),SCREEN_WIDTH/2-120-3,83);
            frame.setTitle("BB Runner           Health: "+hp);
        }
        draw(g2);
        if(screenpause){
            PauseScreen(g2);
        }
        g.drawImage(image,0,0,this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        rotation+=GAME_SPEED;
        if(inshop){
            if(bb.getX()>-bb.getWidth() && bb.getY()!=animy+SCREEN_HEIGHT+SCREEN_HEIGHT/2){
                bb.setLocation((int)bb.getX()-5,(int)bb.getY());
            }
            else if(SCREEN_HEIGHT>-animy){
                animy-=10;
                newGame.setLocation(newGame.getX(),newGame.getY()-10);
                shop.setLocation(shop.getX(),shop.getY()-10);
                exit.setLocation(exit.getX(),exit.getY()-10);

                backbutton.setLocation(backbutton.getX(),animy+SCREEN_HEIGHT+50);
                rbutton.setLocation(rbutton.getX(),animy+SCREEN_HEIGHT+(SCREEN_HEIGHT/2-rbutton.getHeight()/2));
                lbutton.setLocation(lbutton.getX(),animy+SCREEN_HEIGHT+(SCREEN_HEIGHT/2-lbutton.getHeight()/2));
                skinbg.setLocation(skinbg.getX(),animy+SCREEN_HEIGHT+(SCREEN_HEIGHT/2-skinbg.getHeight()/2));
                buybutton.setLocation(buybutton.getX(),animy+2*SCREEN_HEIGHT-100);
                bb.setLocation((int)(SCREEN_WIDTH/2-bb.getWidth()/2),animy+SCREEN_HEIGHT+SCREEN_HEIGHT/2);

                coinbar.setVisible(true);
                coinbar.setLocation(coinbar.getX(),animy+SCREEN_HEIGHT+160);
                hbar.setVisible(true);
                hbar.setLocation(hbar.getX(),animy+SCREEN_HEIGHT+200);
            }
        }
        else if(0>animy){
            animy+=10;
            newGame.setLocation(newGame.getX(),newGame.getY()+10);
            shop.setLocation(shop.getX(),shop.getY()+10);
            exit.setLocation(exit.getX(),exit.getY()+10);

            backbutton.setLocation(backbutton.getX(),animy+SCREEN_HEIGHT+50);
            rbutton.setLocation(rbutton.getX(),animy+SCREEN_HEIGHT+(SCREEN_HEIGHT/2-rbutton.getHeight()/2));
            lbutton.setLocation(lbutton.getX(),animy+SCREEN_HEIGHT+(SCREEN_HEIGHT/2-lbutton.getHeight()/2));
            skinbg.setLocation(skinbg.getX(),animy+SCREEN_HEIGHT+(SCREEN_HEIGHT/2-skinbg.getHeight()/2));
            buybutton.setLocation(buybutton.getX(),animy+2*SCREEN_HEIGHT-100);

            coinbar.setVisible(false);
            coinbar.setLocation(coinbar.getX(),animy+SCREEN_HEIGHT+160);
            hbar.setVisible(false);
            hbar.setLocation(hbar.getX(),animy+SCREEN_HEIGHT+200);

            if(!bb.outOfScreen(SCREEN_WIDTH)){
                bb.setLocation((int)bb.getX()+10,(int)bb.getY());
            }
            else if(bb.outOfScreen(SCREEN_WIDTH) && bb.getX()>SCREEN_WIDTH){
                bb.setLocation((int)-bb.getWidth(),(int)ground.getY()-BB_SIZE+1);
            }
        }
        else if(20>bb.getX() && inmenu){
            bb.setLocation((int)bb.getX()+10,(int)bb.getY());
        }
        if(!inmenu){
            if(0>=hp){
                gameover=true;
                timer.stop();
                hpbar.setVisible(false);
                frame.setTitle("BB Runner");
            }
            if(0>bb.getX()){
                bb.x=0;
            }
            obstacle.move(GAME_SPEED,speedUp.activated);
            if(distance%300==0 && score>10 && !speedUp.moving && !speedUp.activated){
                speedUp.Generate();
            }
            if(speedUp.moving){
                speedUp.move(GAME_SPEED,(int)obstacle.getX()+300);
            }
            if(speedUp.intersects(bb) && !speedUp.activated){
                speedUp.clear();
                speedUp.moving=false;
                speedUp.activated=true;
                speedUp.startdistance=distance;
                speedUp.startSpeed=GAME_SPEED;
                GAME_SPEED+=20;
            }
            if(distance>=speedUp.startdistance+100 && speedUp.activated){
                GAME_SPEED=speedUp.startSpeed;
                obstacle.restart();
                speedUp.activated=false;
            }
            if(hps[selected]*0.8>=hp && !(repair.onscreen) && distance%175==0){
                Random r=new Random();
                int chance=r.nextInt(hps[selected]);
                if(hps[selected]-hp>=chance){
                    repair.newRepair(SCREEN_WIDTH);
                }
            }
            if(repair.onscreen){
                repair.move(GAME_SPEED);
            }
            if(repair.intersects(bb)){
                repair.clearRepair(SCREEN_WIDTH);
                hp+=20;
                hpbar.setValue(hp);
            }
            if(distance%100==0){
                Random r=new Random();
                int chance=r.nextInt(100);
                if(30>=chance){
                    coin.newCoin(SCREEN_WIDTH);
                }
            }
            if(coin.onscreen){
                coin.move(GAME_SPEED);
            }
            if(coin.intersects(bb)){
                coin.clearCoin(SCREEN_WIDTH);
                coins+=dupliers[selected];
            }
            if(!(bb.intersects(obstacle))){
                distance++;
                if(speedUp.activated){
                    score+=GAME_SPEED/4;
                }
                else{
                    score+=duplier;
                }
                if(distance%300==0 && !speedUp.activated && 2000>score){
                    GAME_SPEED+=2;
                    bb.gamespeed=GAME_SPEED;
                }
                else if(distance%300==0 && !speedUp.activated && score>2000 && 20>GAME_SPEED){
                    GAME_SPEED+=4;
                    bb.gamespeed=GAME_SPEED;
                }
                else if(distance%500==0){
                    duplier++;
                }
            }
            if(obstacle.intersects(bb) && !speedUp.activated){
                distance++;
                hp-=6;
                duplier=1;
                hpbar.setValue(hp);
                if(score>currenthighscore){
                    currenthighscore=score;
                }
                if(currenthighscore>=highscore){
                    newhighscore=true;
                    highscore=currenthighscore;
                }
                else{
                    newhighscore=false;
                }
                score-=score/10;
                if(GAME_SPEED>10){
                    GAME_SPEED--;
                }
                else{
                    GAME_SPEED=10;
                }
                bb.gamespeed=GAME_SPEED;
            }
            bb.move();
            bb.gravity(bb.intersects(ground));
            if(gamestartanim==SCREEN_HEIGHT/2){
                GAME_SPEED=10;
                bb.gamespeed=GAME_SPEED;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!screenpause){
            if(e.getSource().equals(newGame)){
                inmenu=false;
            }
            else if(e.getSource().equals(cheatbutton)){
                String cheatCode=JOptionPane.showInputDialog("Enter Your CheatCODE:");
                if(cheatCode==null){
                    JOptionPane.showMessageDialog(null,"Invalid Cheat Code!");
                }
                else if(cheatCode.equals("Szeretem_a_Manit!<3")){
                    JOptionPane.showMessageDialog(null,"BB-8 Fannika Edition has been unlocked!");
                    owned[skinamount-1]=true;
                    secretskins=0;
                }
                else if(cheatCode.equals("Tell_me_a_Secret")){
                    JOptionPane.showMessageDialog(null,"Vasárnap kirándulunk! :3");
                }
            }
            else if(e.getSource().equals(backbutton)){
                inshop=false;
                hp=hps[selected];
                bb.setSkin(hskins[selected],bskins[selected],selected);
            }
            else if(e.getSource().equals(shop)){
                inshop=true;
                bbtype=selected;
                hbar.setValue(hps[bbtype]);
                coinbar.setValue(dupliers[bbtype]);
                coinbar.setForeground(namecolors[bbtype]);
            }
            else if(inshop && e.getSource().equals(lbutton) && bbtype>0){
                bbtype--;
                hbar.setValue(hps[bbtype]);
                coinbar.setValue(dupliers[bbtype]);
                bb.setSkin(hskins[bbtype],bskins[bbtype],bbtype);
                coinbar.setForeground(namecolors[bbtype]);
            }
            else if(inshop && e.getSource().equals(rbutton) && hskins.length-1-secretskins>bbtype){
                bbtype++;
                hbar.setValue(hps[bbtype]);
                coinbar.setValue(dupliers[bbtype]);
                bb.setSkin(hskins[bbtype],bskins[bbtype],bbtype);
                coinbar.setForeground(namecolors[bbtype]);
            }
            else if(inshop && e.getSource().equals(buybutton) && coins>=prices[bbtype] && !owned[bbtype]){
                coins-=prices[bbtype];
                owned[bbtype]=true;
            }
            else if(inshop && e.getSource().equals(buybutton) && owned[bbtype]){
                selected=bbtype;
                hpbar.setMaximum(hps[selected]);
                hpbar.setValue(hps[selected]);
                hp=hps[selected];
            }
            else if(e.getSource().equals(restart) && gameover){
                gameRestart();
            }
            else if(e.getSource().equals(closelabel) && gameover){
                try {
                    sd.Save();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                frame.dispose();
            }
            else if(e.getSource().equals(homelabel) && !inmenu){
                inmenu=true;
                gameRestart();
            }
            else if(e.getSource().equals(exit)){
                timer.stop();
                try {
                    sd.Save();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                frame.dispose();
            }
        }
        buttonSource=-1;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(!screenpause){
            if(e.getSource().equals(newGame)){
                buttonSource=0;
            }
            else if(e.getSource().equals(shop)){
                buttonSource=1;
            }
            else if(e.getSource().equals(exit)){
                buttonSource=2;
            }
            else if(e.getSource().equals(backbutton)){
                buttonSource=3;
            }
            else if(e.getSource().equals(lbutton)){
                buttonSource=4;
            }
            else if(e.getSource().equals(rbutton)){
                buttonSource=5;
            }
            else if(e.getSource().equals(buybutton)){
                buttonSource=6;
            }
            else if(e.getSource().equals(restart) && !inmenu){
                buttonSource=0;
                repaint();
            }
            else if(e.getSource().equals(closelabel) && !inmenu){
                buttonSource=1;
                repaint();
            }
            else if(e.getSource().equals(homelabel) && !inmenu){
                buttonSource=2;
                repaint();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(!screenpause){
            buttonSource=-1;
            repaint();
        }
    }

    public class saveData{
        String myDocumentPath = System.getProperty("user.home") + "//bbrunner.dat";
        public void Save() throws IOException {
            DataOutputStream outputStream=new DataOutputStream(new FileOutputStream(myDocumentPath));
            for (boolean b : owned) {
                outputStream.writeBoolean(b);
            }
            outputStream.writeInt(coins);
            outputStream.writeInt(highscore);
            outputStream.writeInt(selected);
            outputStream.writeInt(secretskins);
        }
        public void Read() throws IOException {
            File f=new File(myDocumentPath);
            if(f.exists()){
                DataInputStream inputStream=new DataInputStream(new FileInputStream(myDocumentPath));
                for (int i = 0; i < owned.length; i++) {
                    owned[i]=inputStream.readBoolean();
                }
                coins=inputStream.readInt();
                highscore=inputStream.readInt();
                selected=inputStream.readInt();
                bbtype=selected;
                hp=hps[selected];
                hpbar.setMaximum(hps[selected]);
                secretskins=inputStream.readInt();
            }
        }
    }

    public class kListener extends KeyAdapter{
        @Override
        public void keyReleased(KeyEvent e) {
            bb.keyReleased(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            bb.keyPressed(e,bb.intersects(ground));
            if(e.getKeyCode()==KeyEvent.VK_ENTER){
                resetSkins();
            }
            if(e.getKeyCode()==KeyEvent.VK_ESCAPE && !gameover){
                if(inmenu){
                    inshop=false;
                    hp=hps[selected];
                }
                else{
                    screenpause=!screenpause;
                    if(screenpause){
                        timer.stop();
                    }
                    else{
                        timer.start();
                    }
                    repaint();
                }
            }
            if(inshop && e.getKeyCode()==KeyEvent.VK_LEFT && bbtype>0){
                bbtype--;
                hbar.setValue(hps[bbtype]);
                coinbar.setValue(dupliers[bbtype]);
                bb.setSkin(hskins[bbtype],bskins[bbtype],bbtype);
                coinbar.setForeground(namecolors[bbtype]);
            }
            if(inshop && e.getKeyCode()==KeyEvent.VK_RIGHT && hskins.length-1-secretskins>bbtype){
                bbtype++;
                hbar.setValue(hps[bbtype]);
                coinbar.setValue(dupliers[bbtype]);
                bb.setSkin(hskins[bbtype],bskins[bbtype],bbtype);
                coinbar.setForeground(namecolors[bbtype]);
            }
        }
    }
    public class BackGround{
        int x=0;
        public void draw(Graphics2D g){
           g.drawImage(bgimage.getImage(),x,0, null);
            if(-766>=x){
                x=0;
            }
            if(!gameover){
                x-=GAME_SPEED;
            }
        }
    }
    public static void main(String[]args) throws IOException {
        new Game();
    }
}
