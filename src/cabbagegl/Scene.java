package cabbagegl;
import java.util.*;

public class Scene {
    public List<Shape> renderables;
    public List<Light> lights;
    public Vector3 ambient;

    public boolean cel_shaded;

    public Scene(Scene arg) {
        renderables = new LinkedList<Shape>();
        for (Shape toAdd : arg.renderables) {
            renderables.add(toAdd);
        }
        lights = new LinkedList<Light>();
        for (Light toAdd2 : arg.lights) {
            lights.add(toAdd2);
        }
        ambient = arg.ambient;
    }


    public Scene() {
        renderables = new LinkedList<Shape>();
        lights = new LinkedList<Light>();
        ambient = Vector3.ZERO;
    }
}
