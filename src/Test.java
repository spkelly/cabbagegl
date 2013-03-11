import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Test {
    public static void main(String[] args) {
        Scene myScene = new Scene();
        Vector3 sceneAmb = new Vector3(2, 2, 2);
        myScene.ambient = sceneAmb;

        Vector3 colora = new Vector3(1.0, 0, 0);
        Material mata = new Material(colora, colora, colora);

        Vector3 colorb = new Vector3(0, 0, 1.0);
        Material matb = new Material(colorb, colorb, colorb);

        Vector3 colorc = new Vector3(0, 1.0, 0);
        Material matc = new Material(colorc, colorc, colorc);

        Sphere a = new Sphere(new Vector3(0,0,-5), 2, mata);
        myScene.renderables.add(a);

        Plane b = new Plane(new Vector3(0,-.5,-5), new Vector3(0,1,.2), matc);
        myScene.renderables.add(b);

        Light lighta = new Light(new Vector3(4,4,4), new Vector3(-2, 3, -3.3));
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
       options.AA_samples = 1;
       options.width = 1920;
       options.height = 1080;
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
