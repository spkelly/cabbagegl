package cabbagegl;
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

    public boolean cel_shaded; // wat

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

     // Get position of pixel on near plane in world coords
     Vector3 P = u.scale(alpha).sum(v.scale(beta)).sum(view);

     // Now get a vector pointing to the pixel from the cam
     Vector3 pixelDir = P.diff(eye);
     Vector3 pixDirNorm = pixelDir.normalize();

     double mindist = near;
     double maxdist = far;

     // Construct the focal plane
     Vector3 toNear = view.diff(eye);
     Vector3 toFocal = toNear.normalize().scale(
        roptions.dist_to_focal_plane);
     Vector3 focalCenter = eye.sum(toFocal);
     Plane focal = new Plane(focalCenter, toFocal.scale(-1).normalize(), null);

     // We want where pixeldir intersects the focal plane to be the new P
     Ray rtoNear = new Ray(eye, pixelDir);
     Vector3 focPoint = focal.hitBy(rtoNear).getHitpoints().get(0).getHitpoint();
     P = focPoint; // Success! P is the view point
     pixelDir = P.diff(eye);
     Vector3 pixelDirNorm = pixelDir.normalize();

     double pixWid = yhgt / roptions.height;
     double pixHgt = xwid / roptions.width;


     Vector3 toUse = Vector3.ZERO;
     Vector3 curr = Vector3.ZERO;
     Vector3 neye = eye;
     // trace our new ray through the scene to get the color of this pixel
     for (int ditr = 0; ditr < roptions.dof_samples; ditr++) {
        for(int itr = 0; itr < roptions.AA_samples; itr++) {
           Ray toTrace = new Ray(neye, pixelDirNorm);
           if(!cel_shaded)
           curr = curr.sum(toTrace.trace(s, mindist, maxdist,
              roptions.max_recurse));
           else
           curr = curr.sum(toTrace.cellShadedTrace(s, mindist, maxdist));

           // Compute new viewpoint
           // Jitter the viewpoint
           double xJit = doubleBetween(-pixWid / 4.0, pixWid / 4.0);
           double yJit = doubleBetween(-pixHgt / 4.0, pixHgt / 4.0);

           // Get position of pixel on near plane in world coords
           P = u.scale(alpha+xJit).sum(v.scale(beta=yJit)).sum(view);

           // Now get a vector pointing to the pixel from the cam
           pixelDir = P.diff(eye);
           pixDirNorm = pixelDir.normalize();

           // We want where pixeldir intersects the focal plane to be the new P
           rtoNear = new Ray(eye, pixelDir);
           focPoint = focal.hitBy(rtoNear).getHitpoints().get(0).getHitpoint();
           P = focPoint; // Success! P is the view point
           pixelDir = P.diff(eye);
           pixelDirNorm = pixelDir.normalize();
        }
        curr = curr.scale(1.0/roptions.AA_samples);

        toUse = toUse.sum(curr);
        // Jitter the eye point over space
        // We know u and v cover the horizontal and vertical lines on the proj plane
        // Generate random points in a circle in this plane, centered around
        // the eye
        double jangle = Math.toRadians(doubleBetween(0, 360));
        double center_dist = doubleBetween(0, roptions.aperture_radius);

        Vector3 jit = u.scale(Math.cos(jangle)).sum(v.scale(Math.sin(jangle)));
        jit = jit.normalize().scale(center_dist);
        neye = eye.sum(jit);
     }
     toUse = toUse.scale(1.0/roptions.dof_samples);

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
