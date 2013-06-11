package cabbagegl;
import java.util.*;
import java.io.Serializable;

public class Scene implements Serializable {
    public List<Shape> renderables;
    public List<Light> lights;
    public Vector3 ambient;

    public boolean cel_shaded;

    public String toString() {
        return "\nScene: \nn renderables: " + renderables.size() + "\n" +
        "n lights: " + lights.size() + "\n" +
        "ambient: " + ambient + "\n";
    }

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
