import java.awt.Color;
import java.awt.image.BufferedImage;
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
    private static final Vector3 DEFAULT_EYE = Vector3.UNIT_X;
    private static final Vector3 DEFAULT_VIEW = Vector3.ZERO;
    private static final Vector3 DEFAULT_UP = Vector3.UNIT_Y;

    private static final double DEFAULT_FOV = 45.0;
    private static final double DEFAULT_ASPECT = 1.0;
    private static final double DEFAULT_ZNEAR = 0.1;
    private static final double DEFAULT_ZFAR = 20.0;

    public Camera() {
        lookAt(DEFAULT_EYE, DEFAULT_VIEW, DEFAULT_UP);
        perspective(DEFAULT_FOV, DEFAULT_ASPECT, DEFAULT_ZNEAR, DEFAULT_ZFAR);
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

    public BufferedImage renderScene(Scene scene, RenderOptions roptions) {
        BufferedImage img = new BufferedImage(roptions.width, roptions.height,
            BufferedImage.TYPE_INT_RGB);
        // Generate several threads that render seperate parts of the scene
        int nthreads = 5;
        Thread threads[] = new Thread[nthreads];
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
          for (int j = 0; j < o.height; j++) {
             Color pixCol = renderPixel(i, j, s, o);
             img.setRGB(i, j, pixCol.getRGB());
          }
       }
    }

    private Color renderPixel(int i, int j, Scene s, RenderOptions roptions) {
        // Convert i,j to ix, jy where projection plane center is 0
        double ix = (double) i - roptions.width  / 2.0;
        double jy = (double) -j + roptions.height / 2.0;

        // Construct a ray from the eye to the proper viewpoint
        // We need a base vector and a direction vector for a ray to trace
        Vector3 base = eye;
        Vector3 dir = view.diff(eye).normalize();
        Vector3 left = up.normalize().cross(dir).normalize();
        Vector3 y = dir.cross(left).normalize();

        double d = near;
        double phi = field_of_view / 2.0;

        // Get the physical dimensions of projection plane
        double yhgt = 2 * d * Math.tan(Math.toRadians(phi));
        double xwid = yhgt * aspect_ratio;
        double theta = Math.toDegrees(Math.atan((xwid/2.0)/d));

        // Now we have phi and theta, as well as camera orthonormal coord sys
        // coord sys is left, y, dir
        // positions on projection plane are linear combination of u and v
        Vector3 u = left.scale(d * Math.tan(Math.toRadians(theta)));
        Vector3 v = y.scale(d * Math.tan(Math.toRadians(phi)));

        // Alpha and Beta are coefficients
        double alpha = 2.0*(ix+0.5) / roptions.width;
        double beta  = 2.0*(jy+0.5) / roptions.height;

        // Get position of pixel in world coords
        Vector3 P = u.scale(alpha).sum(v.scale(beta)).sum(view);

        // Now get a vector pointing to the pixel from the cam
        Vector3 pixelDir = P.diff(eye);
        double mindist = pixelDir.len();
        double maxdist = mindist * (far / near);
        pixelDir = pixelDir.normalize();

        double pixWid = yhgt / roptions.height;
        double pixHgt = xwid / roptions.width;

        Vector3 toUse = Vector3.ZERO;
        // trace our new ray through the scene to get the color of this pixel
        if (roptions.AA_samples == 1) {
           Ray toTrace = new Ray(eye, pixelDir);
           toUse = toTrace.trace(s, mindist, maxdist);
        } else {
           for(int itr = 0; itr < roptions.AA_samples; itr++) {
              // Jitter the viewpoint
              double xJit = doubleBetween(-pixWid / 4.0, pixWid / 4.0);
              double yJit = doubleBetween(-pixHgt / 4.0, pixHgt / 4.0);
              
              Vector3 nP = u.scale(alpha+xJit).sum(v.scale(beta+yJit))
                  .sum(view);
              Vector3 pixDir = nP.diff(eye).normalize();
              Ray toTrace = new Ray(eye, pixDir);
              toUse = toUse.sum(toTrace.trace(s, mindist, maxdist));
           }
           toUse = toUse.scale(1.0/roptions.AA_samples);
        }

        toUse = toUse.clamp(0.0, 1.0);
        return vectorToColor(toUse);
    }

    private static Random randGen = new Random();
    private double doubleBetween(double low, double high) {
       return randGen.nextDouble() * (high-low) + low;
    }

    private static Color vectorToColor(Vector3 in) {
       int r = (int) (in.X() * 255.0);
       int g = (int) (in.Y() * 255.0);
       int b = (int) (in.Z() * 255.0);
       return new Color(r, g, b);
    }
}
