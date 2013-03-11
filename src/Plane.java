

class Plane extends Shape {
    private Vector3 point;
    private Vector3 normal;

    public Plane(Vector3 pnt, Vector3 norm, Material mat) {
        super(mat);
        point = pnt;
        normal = norm;
    }


    public HitData hitBy(Ray r) {
        HitData hd = null;

        Vector3 base = r.getBase();
        Vector3 dir = r.getDir();

        if (normal.dot(dir) != 0) {
           double t = (normal.dot(point) - normal.dot(base)) / normal.dot(dir);
           if (t > 0) {
              Vector3 hitpoint = dir.scale(t).sum(base);
              hd = new HitData();
              hd.addHitpoint(new HitPoint(hitpoint, normal, t, this));
           }
        }

        return hd;
    }

    public Material materialPropsAt(Vector3 ray) {
        return mat;
    }
}



