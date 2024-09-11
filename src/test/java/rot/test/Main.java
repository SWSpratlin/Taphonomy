package rot.test;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class Main extends PApplet {

    public static PImage a;
    public static PImage bg;
    public static PImage map;
    public static Utils utils;
    public static CamHandler handler;
    public static ArrayRot rot;
    public int d = 5;
    public static int max;
    public static int[] temp;

    public void settings() {
        size(1280, 720);
        utils = new Utils();
        utils.sysInfo();
        System.out.println(utils.sysInfo());
        System.out.println("max: " + width * height);
        handler = new CamHandler(utils, width, height);
        try {
            CamHandler.initCam();
        } catch (TimeoutException e) {
            e = new TimeoutException("Shit Timed Out");
        }

        int w = CamHandler.cam.getViewSize().width;
        int h = CamHandler.cam.getViewSize().height;
        a = createImage(w, h, ARGB);
        a.loadPixels();
        max = a.pixels.length;
        System.out.println(max);
        Arrays.fill(a.pixels, 0xFF000000);

        bg = loadImage("data/bg.jpg");
        map = loadImage("data/map.png");
        map.loadPixels();
        System.out.println("Map Loaded");
        rot = new ArrayRot(this, a, map);
        rot.drawMap();

        int i = (int) random((width * height) - 1);
        //Debug Print Statements
        System.out.println("Cam: " + CamHandler.cam.getName());
        System.out.println("Cam Size: " + CamHandler.cam.getViewSize().width + ", " + CamHandler.cam.getViewSize().height);

        temp = new int[max];
    }

    public void draw() {
        image(bg, 0, 0);

        if (frameCount % d == 0) {
            rot.grow();
        }

        image(a, 0, 0);
        handler.camFlipper(temp);

        //Raw Video Stream
//        for (int i = 0; i < max; i++) {
//            a.pixels[i] = temp[i];
//        }

        //Processed Video Stream
        for (int i = 0; i < max; i++) {
            if (Utils.osName.contains("linux")) {
                if (temp[i] < -130000 && temp[i] > -135000) {
                    a.pixels[i] = 0x00000000;
                }
            } else if (Utils.osName.contains("mac")) {
                if (temp[i] == -1) {
                    a.pixels[i] = 0x00000000;
                }
            }
        }

        a.updatePixels();

        fill(0x80000000);
        rect(0, 0, 500, 200);
        textSize(30);
        fill(255);
        text("Cam Framerate: " + CamHandler.cam.getFPS(), 40, 40);
        text("Framerate: " + frameRate, 40, 100);
        text("Pixel Value: " + Integer.toHexString(a.pixels[mouseX + mouseY * width]), 40, 150);
        text("Cam Pixel Value: " + Integer.toHexString(temp[mouseX + mouseY * width])
                + " int: " + temp[mouseX + mouseY * width], 40, 200);

        utils.printMemory(frameCount, 600);
    }

    public void keyPressed() {
        if (keyCode == ENTER || keyCode == RETURN) {
            System.out.println("Exiting");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            System.setProperty("java.library.path", "/usr/java/packages/lib/");
            System.out.println(System.getProperty("java.library.path"));
        }
        String[] processingArgs = {"Main"};
        rot.test.Main main = new rot.test.Main();
        PApplet.runSketch(processingArgs, main);
    }
}
