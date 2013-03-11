import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Test {
    public static void main(String[] args) {
        Scene myScene = new Scene();
        Vector3 sceneAmb = new Vector3(2, 2, 2);
        myScene.ambient = sceneAmb;

        Vector3 colora = new Vector3(1.0, 0, 0);
        Vector3 speca = new Vector3(.75, .75, .75);
        Material mata = new Material(colora, speca, colora);

        Vector3 colorb = new Vector3(0.0, 1.0, 0.0);
        Material matb = new Material(colorb, colorb, colorb);

        Vector3 colorc = new Vector3(0, 0, .7);
        Vector3 specc = new Vector3(.75,.75,.75);
        Vector3 diffc = new Vector3(.9,.9,.9);
        Material matc = new Material(colorc, Vector3.ZERO, diffc);

        Sphere a = new Sphere(new Vector3(-3,1.5,-7), 2, mata);
        myScene.renderables.add(a);

        Material mats2 = new Material(colora, speca, diffc, 6);
        Sphere s2 = new Sphere(new Vector3(1.1,.7,-6), 1.5, mats2);
        myScene.renderables.add(s2);

        Plane b = new Plane(new Vector3(0,-2,-5), new Vector3(0,1,.2), matc);
        myScene.renderables.add(b);

        Material p2Mat = new Material(colorb, Vector3.ZERO, diffc);
        Material p3Mat = new Material(new Vector3(.3,.3,.3), Vector3.ZERO, diffc);

        Plane p2 = new Plane(new Vector3(-8,0,-5), new Vector3(1,0,.2),p2Mat);
        myScene.renderables.add(p2);

        Plane p3 = new Plane(new Vector3(0,0,-10), new Vector3(0,0,1),p3Mat);
        myScene.renderables.add(p3);


        Material p4Mat = new Material(new Vector3(.7,.7,0), Vector3.ZERO, diffc);
        Plane p4 = new Plane(new Vector3(0,0,10), new Vector3(0,0,-1), p4Mat);
        myScene.renderables.add(p4);

        Light lighta = new Light(new Vector3(6,6,6), new Vector3(1,1,1),
            new Vector3(4, 4, 0));
        //   new Vector3(-2, 3, -3.3));
        myScene.lights.add(lighta);

        // Setup the view volume
        Camera myCam = new Camera();
        Vector3 eye = new Vector3(0,0, 5);
        Vector3 view = new Vector3(0,0,0);
        Vector3 up = new Vector3(0,1,0);
        myCam.lookAt(eye, view, up);

       double fov    = 45.0;
       double aspect = 16.0/9.0;
       double znear  = 5.0;
       double zfar   = 100.0;
       myCam.perspective(fov, aspect, znear, zfar);


       // Test render
       RenderOptions options = new RenderOptions();
       options.AA_samples = 8;
       options.width = 1920;
       options.height = 1080;
       options.max_recurse = 10;
       BufferedImage rscene = myCam.renderScene(myScene, options);

       // output the image
       try {
           File output = new File("output.png");
           ImageIO.write(rscene, "png", output);
       } catch (Exception e) {
           System.err.println("An error occurred while outputting the image.");
       }
    }
}
