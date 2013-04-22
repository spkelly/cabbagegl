package cabbagegl;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

public class Camera {
    // Modelview
    private Vector3 eye;
    private Vector3 view;
    private Vector3 up;

    // Projection
    private double field_of_view;
    private double aspect_ratio;
    private double near;
    private double far;

    // Defaults
    private final Vector3 DEFAULT_EYE = Vector3.UNIT_X;
    private final Vector3 DEFAULT_VIEW = Vector3.ZERO;
    private final Vector3 DEFAULT_UP = Vector3.UNIT_Y;

    private final double DEFAULT_FOV = 45.0;
    private final double DEFAULT_ASPECT = 1.0;
    private final double DEFAULT_ZNEAR = 0.1;
    private final double DEFAULT_ZFAR = 20.0;

    public boolean cel_shaded; // wat

    private AtomicInteger pixelsRendered;

    public Scene scene;  // yeah, yeah, I know...


    public Camera() {
        lookAt(DEFAULT_EYE, DEFAULT_VIEW, DEFAULT_UP);
        perspective(DEFAULT_FOV, DEFAULT_ASPECT, DEFAULT_ZNEAR, DEFAULT_ZFAR);
        cel_shaded = false;
    }


    public void lookAt(Vector3 ieye, Vector3 iview, Vector3 iup) {
        eye = ieye;
        view = iview;
        up = iup;
    }

    public void perspective(double fov, double aspect, double znear,
            double zfar) {
        field_of_view = fov;
        aspect_ratio = aspect;
        near = znear;
        far = zfar;
    }
}

        /*
    public BufferedImage renderScene(Scene scene, RenderOptions roptions) {
        BufferedImage img = new BufferedImage(roptions.width, roptions.height,
            BufferedImage.TYPE_INT_RGB);
        // Generate several threads that render seperate parts of the scene
        // XXX that one is a bit important to be an hardcoded constant
        int nthreads = 1;
        Thread threads[] = new Thread[nthreads];
        pixelsRendered = new AtomicInteger(0);
        for (int i = 0; i < nthreads; i++) {
           // Calculate the current thread's range to render
           int low = i * (roptions.width / nthreads);
           int high;
           if (i == nthreads) {
              high = roptions.width;
           } else {
              high = low + (roptions.width / nthreads);
           }
           RenderThread rt = new RenderThread(scene, roptions, low, high, img);
           Thread toRun = new Thread(rt);
           toRun.start();
           threads[i] = toRun;
        }
        for (int i = 0; i < nthreads; i++) {
           try {
              threads[i].join();
           } catch(Exception e) {}
        }

        return img;
    }

    public class RenderThread implements Runnable {
       private Scene scene;
       private RenderOptions roptions;
       private int low, high;
       private BufferedImage image;
       public RenderThread(Scene s, RenderOptions ro, int l, int h,
            BufferedImage img) {
          scene = s;
          roptions = ro;
          low = l;
          high = h;
          image = img;
       }
       public void run() {
          renderInRange(scene, roptions, low, high, image);
       }
    }

    private void renderInRange(Scene s, RenderOptions o, int il, int ih,
         BufferedImage img) {
       for (int i = il; i < ih; i++) {
          System.out.println("i: " + i + " / ih: " + ih);
          for (int j = 0; j < o.height; j++) {
//;             System.out.println("j: " + j + " / o: " + o.height);
             Color pixCol = renderPixel(i, j, s, o);
             img.setRGB(i, j, pixCol.getRGB());
          }
       }
    }
    */

