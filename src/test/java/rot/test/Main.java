package rot.test;

import org.opencv.core.Core;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Arrays;

public class Main extends PApplet{

    public static PImage a;
    public static PImage bg;
    public static PImage map;
    public static Utils utils;
    public static CamHandler handler;
    public static ArrayRot rot;
    public int d = 5;
    public static int max;
    public static int[] temp;

    public void settings(){
        size(1280, 720);
        utils = new Utils();
        System.out.println(utils.sysInfo());
        System.out.println("max: " + width * height);
        handler = new CamHandler(utils, width, height);
        CamHandler.initCam();

        int w = CamHandler.cam.getViewSize().width;
        int h = CamHandler.cam.getViewSize().height;
        a = createImage(w, h, ARGB);
        a.loadPixels();
        max = a .pixels.length;
        System.out.println(max);
        Arrays.fill(a.pixels, 0xFF000000);

        bg = loadImage("data/bg.jpg");
        map = loadImage("data/map.png");
        map.loadPixels();
        System.out.println("Map Loaded");
        rot = new ArrayRot(this, a, map);
        rot.drawMap();

        int i = (int)random((width*height) -1);
        //Debug Print Statements
        System.out.println("Cam: " + CamHandler.cam.getName());
        System.out.println("Cam Size: " + CamHandler.cam.getViewSize().width + ", " + CamHandler.cam.getViewSize().height);

        temp = new int[max];
    }

    public void draw(){
        image(bg,0,0);

        if(frameCount % d == 0){
            rot.grow();
        }

        image(a, 0, 0);
        handler.camFlipper(temp);
        for (int i = 0; i < max; i++) {
            if(temp[i] == -1){
                a.pixels[i] = 0x00000000;
            }
        }
        a.updatePixels();

        fill(0x80000000);
        rect(0, 0, 350, 200);
        textSize(30);
        fill(255);
        text("Cam Framerate: " + CamHandler.cam.getFPS(), 40, 40);
        text("Framerate: " + frameRate, 40, 100);
        text("Pixel Value" + a.pixels[mouseX + mouseY * width], 40, 150);

        utils.printMemory(frameCount, 600);
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String[] processingArgs = {"Main"};
        rot.test.Main main = new rot.test.Main();
        PApplet.runSketch(processingArgs, main);
    }
}
