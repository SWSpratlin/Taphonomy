package rot.main;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class Main extends PApplet {

    //Global objects
    public static PImage a;
    public static PImage bg;
    public static PImage map;
    public static CamHandler handler;
    public static ArrayRot rot;
    public int d = 1;
    public static int max;
    public static int[] temp;
    PGraphics graphics;

    //Setting up the display window. REQUIRES the Processing library from Jitpack to work properly.
    //Cannot run with only core.jar
    public void settings() {

        //Administrative setup
        fullScreen(P2D);
        windowRatio(1280,720);
        size(displayWidth, displayHeight, P2D);
        graphics = new PGraphics();
        this.g = graphics;
        background(0);
        String s = Utils.sysInfo();
        System.out.println("OS and Arch is: " + s);

        //Initializing camera Object
        handler = new CamHandler(rwidth, rheight);
        try {
            CamHandler.initCam();
        } catch (TimeoutException e) {
            e = new TimeoutException("Shit Timed Out");
        }
        int w = CamHandler.cam.getViewSize().width;
        int h = CamHandler.cam.getViewSize().height;

        //Setting Up Images to process later.
        a = createImage(w, h, ARGB);
        a.loadPixels();
        max = a.pixels.length;
        Arrays.fill(a.pixels, 0xFF000000);
        try{
            bg = loadImage("data/Taphonomy.jpg");
            map = loadImage("data/Rot_Alpha.png");
            map.loadPixels();
            System.out.println("Images Loaded");
            rot = new ArrayRot(this, a, map);
            rot.drawMap();
        } catch (Exception e){
            System.out.println("File Not Found, Images not Loaded");
        }

        //Debug Print Statements
        System.out.println("Cam: " + CamHandler.cam.getName());
        System.out.println("Cam Size: " + CamHandler.cam.getViewSize().width + ", " + CamHandler.cam.getViewSize().height);

        //Holder array for calling the camera image.
        temp = new int[max];

        if (Utils.osName.contains("silicon")){
            d = 10;
        } else if (Utils.osName.contains("linux")){
            d = 5;
        } else {
            d = 8;
        }
    }

    public void draw() {
        //Draw the BG Image.
        //May try to move to Settings() if possible to lower overhead.
        image(bg, 0, 0);

        //Grow on a set interval.
        if (frameCount % d == 0) {
            rot.grow();
        }

        //Draw Black Image. Used for erasing/growing
        image(a, 0, 0);

        //Call the flipped camera image.
        handler.camFlipper(temp);

        //Processed Video Stream
        for (int i = 0; i < max; i++) {
            if (Utils.osName.contains("linux")) {
                if (temp[i] > -100000 && temp[i] < -65810) {
                    a.pixels[i] = 0x00000000;
                }
            } else if (Utils.osName.contains("mac")) {
                if (temp[i] == -1) {
                    a.pixels[i] = 0x00000000;
                }
            }
        }

        //Raw Video Stream for Calibration
        //if (max >= 0) System.arraycopy(temp, 0, a.pixels, 0, max);

        if (frameCount % 30 == 0){
            for (int i = 0; i < 5; i++) {
                a.pixels[i] = 0xFFff2D00;
            }
        } else {
            for (int i = 0; i < 5; i++) {
                a.pixels[i] = 0xFF000000;
            }
        }

        a.updatePixels();
        //textSize(50);
        //text(a.pixels[rmouseX + (rmouseY * rwidth)], 40, 40);
    }

    //standard runSketch main method
    public static void main(String[] args) {
        String[] processingArgs = {"Main"};
        rot.main.Main main = new rot.main.Main();
        PApplet.runSketch(processingArgs, main);
    }
}
