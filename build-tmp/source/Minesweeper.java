import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {

int SCREEN_WID = 400;
int SCREEN_HEI = 400;

int NUM_ROWS = 20;
int NUM_COLS = 20;
public boolean gameOver = false;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton>(); //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(SCREEN_HEI, SCREEN_HEI);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    buttons = new MSButton[NUM_ROWS][NUM_COLS];
    for(int rows = 0; rows < NUM_ROWS; rows++){
        for(int cols = 0; cols < NUM_COLS; cols++){buttons[rows][cols]=new MSButton(rows,cols);}}
    setBombs();
}
public void setBombs()
{
    int i = 0;
    while(i < 40)
    {
        int bombRow = (int)(Math.random()*NUM_ROWS);
        int bombCol = (int)(Math.random()*NUM_COLS);
        if(bombs.contains(buttons[bombRow][bombCol])==false)
        {
            bombs.add(buttons[bombRow][bombCol]);
            i++;
        }
    }
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    //your code here
    return false;
}
public void displayLosingMessage()
{
    noStroke();
    //fill(50,50,50,12);
    fill(100,30,30,5);
    rect(0,170,400,80);
    fill(200,0,0);
    textSize(60);
    text("GAME OVER",200,200);
    textSize(12);
}
public void displayWinningMessage()
{
    noStroke();
    fill(50,50,50,12);
    rect(0,170,400,80);
    fill(0,0,200);
    textSize(60);
    text("LEVEL COMPLETED",200,200);
    textSize(12);
}
public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = SCREEN_HEI/NUM_COLS;
        height = SCREEN_HEI/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = false;
        clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
        if((keyPressed==true || mouseButton==RIGHT)&&clicked==false){marked = !marked;}
        else if(countBombs(r,c)>0)
        {
            clicked = true;
            setLabel(""+countBombs(r,c)+"");
        }
    }
    public void draw ()
    {    
        stroke(0,0,150);
        //noStroke();
        //stroke(0);
        if (marked)
            fill(0,0,100);
        else if( clicked && bombs.contains(this) ){
            fill(200,0,0);
            gameOver=true;
        }
        else if(clicked)
            fill(0);
        else 
            fill(50);

        rect(x, y, width, height);
        fill(0,0,200);
        text(label,x+width/2,y+height/2);
        if(gameOver==true){displayLosingMessage();}
    }
    public void setLabel(String newLabel)
    {
        if(newLabel=="0"){label = "";}
        else{label = newLabel;}
    }
    public boolean isValid(int r, int c)
    {
        if(r<0 || r>=NUM_ROWS || c<0 || c>=NUM_COLS){return false;}
        return true;
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        for(int i = -1; i <=1; i++)
        {
            for(int j = -1; j <=1; j++)
            {
                if(isValid(row+i, col+j)==true && bombs.contains(buttons[row+i][col+j])==true)
                {
                    numBombs++;
                }
            }
        }
        if(numBombs==0)
        {
            for(int r = -1; r <= 1; r++)
            {
                for(int c = -1; c <= 1; c++)
                {
                    if(isValid(row+r, col+c)==true && buttons[row+r][col+c].isClicked()==false)
                    {
                        buttons[row+r][col+c].mousePressed();
                    }
                }
            }
        }
        return numBombs;
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
