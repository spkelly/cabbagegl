import java.util.*;

public class Scene {
    public List<Shape> renderables;
    public List<Light> lights;
    public Vector3 ambient;

    public boolean cel_shaded;

    public Scene() {
        renderables = new LinkedList<Shape>();
        lights = new LinkedList<Light>();
        ambient = Vector3.ZERO;
    }
}
