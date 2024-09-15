package rot.main;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;

public class Utils {

    private static final long MEGABYTE = 1024L * 1024L;
    private static MemoryMXBean runtime = ManagementFactory.getMemoryMXBean();
    public static MemoryUsage memory = runtime.getHeapMemoryUsage();
    public static String[] opacity;
    public static String osName;
    public static String osArch;
    private static final RuntimeMXBean up = ManagementFactory.getRuntimeMXBean();

    public Utils() {
        opacityArray();
    }

    /**
     * Working on this to detect the system information (mostly OS and Architecture)
     * Used mostly for camera handling purposes.
     *
     * @return String that gives the OS name, and Chip architecture (silicon vs Intel).
     */
    public static String sysInfo() {
        osName = System.getProperty("os.name").toLowerCase();
        osArch = System.getProperty("os.arch").toLowerCase();
        if (osName.contains("mac") && osArch.contains("aarch64")) {
            return "Mac Silicon";
        } else if (osName.contains("mac") && !osArch.contains("aarch64")) {
            return "Mac Intel";
        } else if (osName.contains("linux") && osArch.contains("aarch64")) {
            return "Linux / Pi";
        } else if (osName.contains("windows")) {
            return "Windows";
        }
        return "No Compatible OS";
    }

    /**
     * Utility to easily log memory usage for sketches. Not necessary for deployment,
     * but helpful for optimization.
     */
    private static void printMemory() {
        memory = runtime.getHeapMemoryUsage();
        long used = memory.getUsed();
        System.out.println("Used memory in bytes: " + used);
        System.out.println("Used memory in megabytes: " + bytesToMegabytes(used));
    }

    private static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    /**
     * Automatically called in Constructor
     * Use this to populate a static String array of the Opacity numbers.
     * Combine these with a ("0x" + Utils.opacity[index] + Integer.toHexString(colordata)) to convert a color to a hex code with alpha values
     */
    private static void opacityArray() {
        opacity = new String[101];
        opacity[100] = "FF";
        opacity[99] = "FC";
        opacity[98] = "FA";
        opacity[97] = "F7";
        opacity[96] = "F5";
        opacity[95] = "F2";
        opacity[94] = "F0";
        opacity[93] = "ED";
        opacity[92] = "EB";
        opacity[91] = "E8";
        opacity[90] = "E6";
        opacity[89] = "E3";
        opacity[88] = "E0";
        opacity[87] = "DE";
        opacity[86] = "DB";
        opacity[85] = "D9";
        opacity[84] = "D6";
        opacity[83] = "D4";
        opacity[82] = "D1";
        opacity[81] = "CF";
        opacity[80] = "CC";
        opacity[79] = "C9";
        opacity[78] = "C7";
        opacity[77] = "C4";
        opacity[76] = "C2";
        opacity[75] = "BF";
        opacity[74] = "BD";
        opacity[73] = "BA";
        opacity[72] = "B8";
        opacity[71] = "B5";
        opacity[70] = "B3";
        opacity[69] = "B0";
        opacity[68] = "AD";
        opacity[67] = "AB";
        opacity[66] = "A8";
        opacity[65] = "A6";
        opacity[64] = "A3";
        opacity[63] = "A1";
        opacity[62] = "9E";
        opacity[61] = "9C";
        opacity[60] = "99";
        opacity[59] = "96";
        opacity[58] = "94";
        opacity[57] = "91";
        opacity[56] = "8F";
        opacity[55] = "8C";
        opacity[54] = "8A";
        opacity[53] = "87";
        opacity[52] = "85";
        opacity[51] = "82";
        opacity[50] = "80";
        opacity[49] = "7D";
        opacity[48] = "7A";
        opacity[47] = "78";
        opacity[46] = "75";
        opacity[45] = "73";
        opacity[44] = "70";
        opacity[43] = "6E";
        opacity[42] = "6B";
        opacity[41] = "69";
        opacity[40] = "66";
        opacity[39] = "63";
        opacity[38] = "61";
        opacity[37] = "5E";
        opacity[36] = "5C";
        opacity[35] = "59";
        opacity[34] = "57";
        opacity[33] = "54";
        opacity[32] = "52";
        opacity[31] = "4F";
        opacity[30] = "4D";
        opacity[29] = "4A";
        opacity[28] = "47";
        opacity[27] = "45";
        opacity[26] = "42";
        opacity[25] = "40";
        opacity[24] = "3D";
        opacity[23] = "3B";
        opacity[22] = "38";
        opacity[21] = "36";
        opacity[20] = "33";
        opacity[19] = "30";
        opacity[18] = "2E";
        opacity[17] = "2B";
        opacity[16] = "29";
        opacity[15] = "26";
        opacity[14] = "24";
        opacity[13] = "21";
        opacity[12] = "1F";
        opacity[11] = "1C";
        opacity[10] = "1A";
        opacity[9] = "17";
        opacity[8] = "14";
        opacity[7] = "12";
        opacity[6] = "0F";
        opacity[5] = "0D";
        opacity[4] = "0A";
        opacity[3] = "08";
        opacity[2] = "05";
        opacity[1] = "03";
        opacity[0] = "00";
    }

    /**
     * Method to convert opacity array values to color data for use in BufferedImage or PImage
     *
     * @param alpha the desired opacity
     * @param i     the current color converted to a hex string
     * @return color data in the 0xAARRGGBB format
     */
    public static int convertToAlphaHex(int alpha, int i) {
        String hex = "0x" + opacity[alpha] + Integer.toHexString(i);
        return Integer.parseInt(hex, 16);
    }

    /**
     * Used to create a 1D array that corresponds with the pixel positions of a horizontally flipped image.
     *
     * @param w      width of the image to flip
     * @param h      height of the image to flip
     * @param output the array to store these new values. Usually a reference array. Can be FINAL
     * @return a populated 1D array of a horizonally flipped version of whatever image.
     */
    public static int[] flipImageArray(int w, int h, int[] output) {
        int i = 0;
        for (int y = 0; y < h; y++) {
            for (int x = w - 1; x >= 0; x--) {
                output[i] = x + (y * w);
                i++;
            }
        }
        return output;
    }

    /**
     * Creates a hash map for coordinates to use with BufferedImage or any other 2D image handling
     *
     * @param w Width of the image
     * @param h Height of the image
     * @return A Hash Map containing every 1D pixel value, and its corresponding coordinates.
     */
    public static HashMap<Integer, Point> coordArray(int w, int h) {
        HashMap<Integer, Point> coords = new HashMap<Integer, Point>(w * h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = x + (y * w);
                coords.put(i, (new Point(x, y)));
            }
        }
        return coords;
    }

    /**
     * Uptime counter.
     */
    private static int seconds = 50;
    private static int minutes = 0;
    private static int hours = 0;
    private static long lastTimeCounter = 0;

    public static String uptime() {
        long timeCounter = up.getUptime() / 1000;
        if (timeCounter != lastTimeCounter) {
            lastTimeCounter = timeCounter;
            seconds++;
            if (seconds >= 60) {
                minutes++;
                printMemory();
                System.out.println("Total Memory: " + bytesToMegabytes(memory.getCommitted()) + ", Init Memory: " + bytesToMegabytes(memory.getInit()));
                if (seconds == 60) {
                    seconds = 0;
                }
                if (minutes >= 60) {
                    hours++;
                    if (minutes == 60) {
                        minutes = 0;
                    }
                }
            }
        }
        return hours + ":" + minutes + ":" + seconds;
    }
}
