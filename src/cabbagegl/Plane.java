package cabbagegl;


class Plane extends Shape {
    private Vector3 point;
    private Vector3 normal;

    public Plane(Vector3 pnt, Vector3 norm, Material mat) {
        super(mat);
        point = pnt;
        normal = norm;
    }

    public Plane(Plane arg) {
        // XXX compiler's probably gonna murder me right there
        super((Shape) arg);
        this.point = new Vector3(arg.point);
        this.normal = new Vector3(arg.normal);
    }

    public HitData hitBy(Ray r) {
        HitData hd = null;

        Vector3 base = r.getBase();
        Vector3 dir = r.getDir();

        if (normal.dot(dir) != 0) {
           double t = (normal.dot(point) - normal.dot(base)) / normal.dot(dir);
           if (t > 0) {
              Vector3 hitpoint = dir.scale(t).sum(base);
              Vector3 retNorm = normal;
              hd = new HitData();


              // Determine if hitpoint is on front or back of plane
              Vector3 negDir = dir.scale(-1);
              FaceSide side = FaceSide.FRONT;
              if (negDir.dot(normal) < 0) {
                 side = FaceSide.BACK;
                 retNorm = normal.scale(-1.0);
              }

              retNorm = retNorm.normalize();
              hd.addHitpoint(new HitPoint(hitpoint, retNorm, t, this, side));
           }
        }

        return hd;
    }

    public Material materialPropsAt(Vector3 ray, FaceSide fs) {
        return mat;
    }


    public void translate(Vector3 trans) {
       point = point.sum(trans);
    }

    public void rotate(Axis axis, double angle) {
       point = point.rotateAbout(axis, angle);
       normal = normal.rotateAbout(axis, angle);
    }
}



