package org.tekkotsu.stateMachine.parts;
import java.awt.*;
public class Square {
	 int x;
	    int y;
	 
	    public Square(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }
	 
	    public int getX() {
	        return x;
	    }
	 
	    public void setX(int x) {
	        this.x = x;
	    }
	 
	    public int getY() {
	        return y;
	    }
	 
	    public void setY(int y) {
	        this.y = y;
	    }
	 
	    public void draw(Graphics g) {
	        g.setColor(Color.RED);
	        g.fillRect(x, y, 10, 10);
	    }

}
