import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Test {
    public static void main(String[] args) {
        Scene myScene = new Scene();
        Vector3 sceneAmb = new Vector3(2, 2, 2);
        myScene.ambient = sceneAmb;

        Vector3 colora = new Vector3(0, 0, .8);
        Vector3 diffa = new Vector3(.7,.7,.7);
        Vector3 speca = new Vector3(.2, .2, .2);
        Vector3 diffc = new Vector3(.9,.9,.9);
        Material mata = new Material(colora, speca, diffa, 8, .7, 1.333);
        Vector3 colorb = new Vector3(0.0, 1.0, 0.0);
        Material matb = new Material(colorb, colorb, colorb);

        Vector3 colorc = new Vector3(0, 0, .7);
        Vector3 specc = new Vector3(.75,.75,.75);
        Material matc = new Material(colorc, Vector3.ZERO, diffc);

        Sphere a = new Sphere(new Vector3(-7,1.5,-7), 2, mata);
        myScene.renderables.add(a);

        Vector3 mats2Spec = new Vector3(.5, .5, .5);
        Vector3 mats2Col = new Vector3(.882, .929, .812);
        Material mats2 = new Material(mats2Col, mats2Spec, diffc, 2, .4, 1.00045);

        Sphere s2 = new Sphere(new Vector3(1.1,-1.5,-6), 1.5, mats2);
        myScene.renderables.add(s2);

        
        Material t1Mat = new Material(colorb, Vector3.ZERO, diffc, 6);
        Vector3 t1v1 = new Vector3(-1, 2, -7);
        Vector3 t1v2 = new Vector3(3, 2, -7);
        Vector3 t1v3 = new Vector3(0, 4, -5);
        Triangle t1 = new Triangle(t1v1, t1v2, t1v3, t1Mat);
        myScene.renderables.add(t1);


        Plane b = new Plane(new Vector3(0,-2,-5), new Vector3(0,1,.2), matc);
        myScene.renderables.add(b);

        Material p2Mat = new Material(colorb, Vector3.ZERO, colorb);
        Material p3Mat = new Material(new Vector3(1,1,.6), Vector3.ZERO, new Vector3(1,1,.6));

        Plane p2 = new Plane(new Vector3(-8,0,-5), new Vector3(1,0,.2),p2Mat);
        myScene.renderables.add(p2);

        Plane p3 = new Plane(new Vector3(0,0,-10), new Vector3(0,0,1),p3Mat);
        myScene.renderables.add(p3);


        Material p4Mat = new Material(new Vector3(.7,.7,0), Vector3.ZERO, new Vector3(.7,.7,0));
        Plane p4 = new Plane(new Vector3(0,0,10), new Vector3(0,0,-1), p4Mat);
        myScene.renderables.add(p4);

        double c_a = 1.0;
        double l_a = .045;
        double q_a = .0075;
        Light lighta = new Light(new Vector3(5,5,5), new Vector3(1,1,1),
            new Vector3(3, 2, 2), new Vector3(c_a, l_a, q_a));
        myScene.lights.add(lighta);
        Light lightb = new Light(new Vector3(5,5,5), new Vector3(1,1,1),
            new Vector3(-2, 3, 3), new Vector3(c_a, l_a, q_a));
        myScene.lights.add(lightb);


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
       myCam.cel_shaded = false;


       // Test render
       RenderOptions options = new RenderOptions();
       options.AA_samples = 4;
       options.width = 1920;
       options.height = 1080;
       options.max_recurse = 10;

       long startTime = System.currentTimeMillis();

       BufferedImage rscene = myCam.renderScene(myScene, options);
       long endTime = System.currentTimeMillis();

       long millis = endTime - startTime;
       long seconds = millis / 1000;
       millis = millis % 1000;
       long minutes = seconds / 60;
       seconds = seconds % 60;
       System.out.println("Render completed.");
       System.out.println("Render time: " + minutes + " min " + seconds + " sec " +
          millis + " millis");
       System.out.println("Resolution: " + options.width + " by " + options.height);
       System.out.println(options.AA_samples + "x Antialiasing.");




       // output the image
       try {
           File output = new File("output.png");
           ImageIO.write(rscene, "png", output);
       } catch (Exception e) {
           System.err.println("An error occurred while outputting the image.");
       }
    }
}
