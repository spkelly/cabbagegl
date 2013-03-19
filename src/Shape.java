
public abstract class Shape {
    protected Material mat;

    public Shape(Material imat) {
        mat = imat;
    }

    public abstract HitData hitBy(Ray r);
    public abstract Material materialPropsAt(Vector3 ray, FaceSide fs);

    public abstract void translate(Vector3 trans);
    public abstract void rotate(Axis axis, double angle);

    public Material getMaterial() {
       return mat;
    }
}
