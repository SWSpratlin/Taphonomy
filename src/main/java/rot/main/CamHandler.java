package rot.main;

//imported classes
import com.github.eduramiba.webcamcapture.drivers.NativeDriver;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.gst1.Gst1Driver;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

//Imported variables
import static rot.main.Main.max;

//Class start
public class CamHandler {

    //Global Class Objects
    public static String os;
    public static Webcam cam;
    public int[] flip;

    /**
     * Constructor, takes 2 arguments
     *
     * @param w Width of the camera image being handled
     * @param h Height of the camera image being handled
     */
    public CamHandler(int w, int h) {
        os = Utils.sysInfo();
        initDriver();
        int j = 0;

        // Creates an array that stores the reverse values of a single Width
        flip = new int[w * h];
        System.out.println(flip.length);
        Utils.flipImageArray(w, h, flip);
        System.out.println(flip.length);
    }


    /**
     * Private method to Initiate the correct driver depending on the OS
     * Loads "Native Driver" for Mac Silicon
     * Loads GST1 for Pi.
     * No Windows Driver yet.
     *
     * @throws IllegalArgumentException No compatible OS
     */
    private static void initDriver() throws IllegalArgumentException {
        String sys = os.toLowerCase();
        if (sys.contains("silicon")) {
            Webcam.setDriver(new NativeDriver());
            System.out.println("Silicon Driver loaded");
        } else if (sys.contains("intel")) {
            System.out.println("We got to the driver loader");
            Webcam.setDriver(new NativeDriver());
        } else if (sys.contains("pi")) {
            try {
                Webcam.setDriver(new Gst1Driver());
                System.out.println("Pi Driver loaded");
            } catch (IllegalArgumentException e) {
                System.err.println("Pi Driver could not be loaded");
            }
        } else if (sys.contains("windows")) {
            System.out.println("I'm still working on this one, sorry");
        } else {
            System.out.println(sys);
            throw new IllegalArgumentException("No Compatible Architecture to load Driver");
        }
    }

    /**
     * public method to Initiate the Webcam. Makes you choose on Apple Silicon, loads default on Pi
     *
     * @throws IllegalArgumentException Either there is no webcam, or no Compatible OS (Apple or Pi)
     */
    public static void initCam() throws IllegalArgumentException, TimeoutException {
        if (os.toLowerCase().contains("silicon") || os.toLowerCase().contains("intel")) {
            for (int i = 0; i < Webcam.getWebcams().size(); i++) {
                System.out.println(i + ": " + Webcam.getWebcams().get(i).getName());
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Select Webcam");
            cam = Webcam.getWebcams().get(scanner.nextInt());
            cam.setViewSize(WebcamResolution.HD.getSize());
            cam.open();
        } else if (os.toLowerCase().contains("pi")) {
            if (!Webcam.getWebcams().isEmpty()) {
                for (int i = 0; i < Webcam.getWebcams().size(); i++) {
                    System.out.println(i + ": " + Webcam.getWebcams().get(i).getName());
                }
                cam = Webcam.getDefault();
                //cam = findCam();
                //cam = Webcam.getWebcamByName("/dev/video10");
                cam.setViewSize(WebcamResolution.HD.getSize());
                //cam.
                cam.open();
            } else {
                System.out.println("Pi Cam Not Loaded");
                throw new IllegalArgumentException("No Webcam Connected");
            }
        } else {
            throw new IllegalArgumentException("No Compatible OS to load Webcam");
        }
    }

    public static Webcam findCam() throws WebcamException, TimeoutException {
        for (int i = 0; i < Webcam.getWebcams().size(); i++) {
            if (Webcam.getWebcamByName(Webcam.getWebcams(i).toString()) != null) {
                return Webcam.getWebcamByName(Webcam.getWebcams(i).toString());
            }
        }
        return Webcam.getDefault();
    }

    /**
     * In progress method to return flipped Image data to the Main method.
     *
     * @return 1D array of the horizontally flipped point from the camera image
     * @throws WebcamException if no webcam is connected (how did you get this far)
     */
    public int[] camFlipper(int[] output) throws WebcamException {
        if (cam != null) {

            BufferedImage image = cam.getImage();
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    if (j + (i * image.getWidth()) < max) {
                        output[flip[j + (i * image.getWidth())]] = image.getRGB(j, i);
                    }
                }
            }
            return output;
        } else throw new WebcamException("Webcam Cannot be Null");
    }
}
