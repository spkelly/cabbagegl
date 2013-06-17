package cabbagegl;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;

public class Test {
    public static void main(String[] args) {
       // Setup the view volume
       Camera myCam = new Camera();
       Vector3 eye = new Vector3(0,0, 5);
       Vector3 view = new Vector3(0,0,0);
       Vector3 up = new Vector3(0,1,0);
       myCam.lookAt(eye, view, up);

       double fov    = 45.0;
       double aspect = 1.33333;
       double znear  = 8;
       double zfar   = 100.0;
       myCam.perspective(fov, aspect, znear, zfar);
       myCam.cel_shaded = false;

       // Render options
       RenderOptions options = new RenderOptions();
       options.AA_samples = 4;
       options.width = 640;
       options.height = 480;
       options.max_recurse = 10;

       options.focal_plane_dist = 11;
       options.lens_aperture_radius = 1.5;
       options.dof_rays = 1;

        // Construct the scene
        Scene myScene = new Scene();
        Vector3 sceneAmb = new Vector3(2, 2, 2);
        myScene.ambient = sceneAmb;

        // Setup floor/wall materials
        // Floor
        Vector3 floorcol = Vector3.ColorVector(200, 205, 58);
        Vector3 floordif = new Vector3(.8,.8,.8);
        Vector3 floorspc = Vector3.ZERO;
        Material floorMat = new Material(floorcol, floordif, floorspc);

        // walls
        Vector3 wallcol = Vector3.ColorVector(37,103,179);
        Vector3 walldif = new Vector3(.8,.8,.8);
        Vector3 wallspc = new Vector3(.2,.2,.2);
        Material wallMat = new Material(wallcol, walldif, wallspc); 

        // ceiling
        Vector3 ceilcol = Vector3.ColorVector(230,230,230);
        Vector3 ceildif = new Vector3(.8,.8,.8);
        Vector3 ceilspc = new Vector3(.5,.5,.5);
        Material ceilMat = new Material(ceilcol, ceildif, ceilspc);

        // Setup floor/walls
        Vector3 floorPt = new Vector3(0,-3.5,-7);
        Vector3 floorNrm = new Vector3(0,1,0);
        Plane floor = new Plane(floorPt, floorNrm, floorMat);
        myScene.renderables.add(floor);

        Vector3 lwallPt = new Vector3(-10,0,-7);
        Vector3 lwallNrm = new Vector3(1,0,.1);
        Plane lwall = new Plane(lwallPt, lwallNrm, wallMat);
        myScene.renderables.add(lwall);

        Vector3 rwallPt = new Vector3(10,0,-7);
        Vector3 rwallNrm = new Vector3(-1,0,-.1);
        Plane rwall = new Plane(rwallPt, rwallNrm, wallMat);
        myScene.renderables.add(rwall);

        Vector3 bwallPt = new Vector3(0,4,-15);
        Vector3 bwallNrm = new Vector3(0,0,1);
        Plane bwall = new Plane(bwallPt, bwallNrm, wallMat);
        myScene.renderables.add(bwall);

        Vector3 behWallPt = new Vector3(0,4,5);
        Vector3 behWallNrm = new Vector3(0,0,-1);
        Plane behWall = new Plane(behWallPt, behWallNrm, wallMat);
        myScene.renderables.add(behWall);

        Vector3 ceilPt = new Vector3(0,5,-7);
        Vector3 ceilNrm = new Vector3(0,-1,0);
        Plane ceil = new Plane(ceilPt, ceilNrm, ceilMat);
        myScene.renderables.add(ceil);

        // Chrome balls
        // Material
        Vector3 cbCol = Vector3.ColorVector(19,59,191);
        Vector3 cbDif = new Vector3(.8,.8,.8);
        Vector3 cbSpc = new Vector3(.8,.8,.8);
        Material cbMat = new Material(cbCol, cbDif, cbSpc);

        // Physical
        Vector3 cbCen = new Vector3(0,0,-6);
        double cbRad = 1.2;
        Sphere cb = new Sphere(cbCen, cbRad, cbMat);
        myScene.renderables.add(cb);

        Sphere cb2 = cb.copy();
        cb2.translate(new Vector3(-4.25,0,-2));
        myScene.renderables.add(cb2);

        Sphere cb3 = cb.copy();
        cb3.translate(new Vector3(4.25,0,2));
        myScene.renderables.add(cb3);

        Sphere cb4 = cb.copy();
        cb4.translate(new Vector3(8.5,0,4));
        myScene.renderables.add(cb4);


        

        // Pyramid
        // Material
        Vector3 pyCol = Vector3.ColorVector(192,6,19);
        Vector3 pyDif = new Vector3(.8,.8,.8);
        Vector3 pySpc = new Vector3(.3,.3,.3);
        double pyalpha = .5;
        double pyior = 2.33;
        Material pyMat = new Material(pyCol, pyDif, pySpc, 2, pyalpha, pyior);

        // Physical
        Vector3 low = new Vector3(0,-3.5,-5);
        Vector3 right = new Vector3(1,-3.5,-6);
        Vector3 left = new Vector3(-1,-3.5,-6);
        Vector3 top = new Vector3(0,-3.5,-7);
        Vector3 tip = new Vector3(0,-1,-6);
        Triangle t1 = new Triangle(low,right,tip,pyMat);
        Triangle t2 = new Triangle(right,top,tip,pyMat);
        Triangle t3 = new Triangle(top,left,tip,pyMat);
        Triangle t4 = new Triangle(left,low,tip,pyMat);
        myScene.renderables.add(t1);
        myScene.renderables.add(t2);
        myScene.renderables.add(t3);
        myScene.renderables.add(t4);

        // setup lights
        double c_a = 1.0;
        double l_a = .045;
        double q_a = .0075;
        Light lighta = new Light(new Vector3(5,5,5), new Vector3(1,1,1),
            new Vector3(3, 2, 2), new Vector3(c_a, l_a, q_a));
        myScene.lights.add(lighta);
        Light lightb = new Light(new Vector3(5,5,5), new Vector3(1,1,1),
            new Vector3(-2, 3, 3), new Vector3(c_a, l_a, q_a));
        myScene.lights.add(lightb);
        Light lightc = new Light(new Vector3(5,5,5), new Vector3(1,1,1),
            new Vector3(5,4,-9), new Vector3(c_a, l_a, q_a));
        myScene.lights.add(lightc);



       System.out.println("Rendering. This will take several minutes.");
       // Render the scene
       long startTime = System.currentTimeMillis();

       BufferedImage rscene = myCam.renderScene(myScene, options);
       // output the image
       try {
          File output = new File("output.png");
          ImageIO.write(rscene, "png", output);
       } catch (Exception e) {
          System.err.println("An error occurred while outputting the image.");
       }



       // Render statistics
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

       int rays = Ray.nrays.get();
       System.out.println("Rays Fired: " + rays);
    }
}
