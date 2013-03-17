public class Sphere extends Shape {
    private Vector3 center;
    private double radius;

    public Sphere(Vector3 cen, double rad, Material mat) {
        super(mat);
        center = cen;
        radius = rad;
    }

    public HitData hitBy(Ray r) {
       HitData hd = null;
       Vector3 dir = r.getDir();
       Vector3 base = r.getBase();

       Vector3 vPrime = base.diff(center);

       // Compute quadratic coefficients
       double a = dir.dot(dir);
       double b = 2 * vPrime.dot(dir);
       double c = vPrime.dot(vPrime) - (radius * radius);

       // Compute discriminant
       double disc = b*b - 4*a*c;
       if (disc > 0) {
          // We've hit the sphere. Calculate hitpoint data
          hd = new HitData();
          double t1 = (-b + Math.sqrt(disc)) / (2*a);
          double t2 = (-b - Math.sqrt(disc)) / (2*a);

          Vector3 hit1 = base.sum(dir.scale(t1));
          Vector3 norm1 = hit1.diff(center).scale(-1).normalize();
          Vector3 hit2 = base.sum(dir.scale(t2));
          Vector3 norm2 = hit2.diff(center).normalize();
          
          hd.addHitpoint(new HitPoint(hit1, norm1, t1, this, FaceSide.BACK));
          hd.addHitpoint(new HitPoint(hit2, norm2, t2, this, FaceSide.FRONT));
       }

       return hd;
    }

    public Material materialPropsAt(Vector3 ray, FaceSide fs) {
        return mat;
    }


    public void translate(Vector3 trans) {
       center = center.sum(trans);
    }

    public void rotate(Vector3 rotation, double angle) {
       // STUBBED
    }
}
