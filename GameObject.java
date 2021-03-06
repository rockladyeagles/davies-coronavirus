import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.image.ImageObserver;

/**
 * The superclass of all visibly displayable game elements. Inidividual
 * subclasses will override methods for character-specific behavior.
 */
abstract public class GameObject implements ImageObserver,
    Comparable<GameObject> {

    protected int x, y;

    // Z-order values are from 1-20 in the Coronavirus game. A lower score
    // means it's drawn further in the background. 
    protected int z;
    protected int dx, dy;
    protected int height, width;
    protected int top, bottom, left, right;
    protected boolean infected;
    protected Image image;
    protected boolean npc;
    
    public GameObject(int startX, int startY, int w, int h, String imageName) {

        x = startX;
        y = startY;
        z = 10;      // Default: middle of z-order.
        width = w;
        height = h;
        dx = 0;
        dy = 0;
        infected = false;
        npc = true;
        updateHitbox();
        if (!imageName.equals("none")) {
            try {
                image = ImageIO.read(new File(imageName));
            }
            catch (Exception e) { e.printStackTrace(); System.exit(1); }
        }
    }
    
    public int getTop() { return top; }
    public int getBottom() { return bottom; }
    public int getLeft() { return left; }
    public int getRight() { return right; }

    public boolean getInfected(){
        return infected;
    }
    public boolean getNpc(){
        return npc;
    }
    public void speedUp(int x){
        dx*=x;
        dy*=x;
    }
    public int compareTo(GameObject o) {
        return z - o.z;
    }

    public boolean isFgObject() {
        return false;
    }
    
    public void infect() {
    	infected = true;
    }
    
    public void kill() {
    	GameEngine.instance().removeObject(this);
    }
    
    public void updateHitbox() {
        left = x-width/2;
        right = x+width/2;
        top = y-height/2;
        bottom = y+height/2;
    }
    
    public boolean collidingWith(GameObject o) {
    	return left < o.getRight() &&
            right > o.getLeft() &&
            top < o.getBottom() &&
            bottom > o.getTop();
    }

    public void touch(GameObject o){
        if(o.getInfected())
            this.infect();
    }
    
    public abstract String getName();

    /**
     * Move this object one clock tick's worth of movement. The default
     * behavior is to "stick" to the sides of the screen. Subclasses can
     * override this method to implement creature-type-specific move
     * algorithms.
     */
    public void move() {
        x += dx;
        y += dy;
        if (x < GameEngine.LEFT_SIDE || x > GameEngine.RIGHT_SIDE - 80 ||
            y < GameEngine.TOP_SIDE || y > GameEngine.BOTTOM_SIDE - 80) {
            dx = dy = 0;
        }
        updateHitbox();
    }
    

    /**
     * Draw the creature at its coordinate on the screen.
     * @param g The Java Swing Graphics object that will actually do the
     * painting.
     */
    public void draw(Graphics g) {
        g.drawImage(image, x, y, (ImageObserver)this);
    }

    /**
     * Returns the age of this GameObject.
     * @return The age in Middle Earth years (which is 1.4 of an Earth year.)
     */
    abstract public int getAge();


    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) { return false; }

}
