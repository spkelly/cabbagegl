public class Vector3 {
    private double x, y, z;

    private double l;
    private boolean l_calc;

    private Vector3 normalized;
    private boolean n_calc;

    public static final Vector3 ZERO = new Vector3(0,0,0);
    public static final Vector3 UNIT_X = new Vector3(1,0,0);
    public static final Vector3 UNIT_Y = new Vector3(0,1,0);
    public static final Vector3 UNIT_Z = new Vector3(0,0,1);


    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        l_calc = false;
        n_calc = false;
    }

    public double len() {
        if(!l_calc) {
            l = Math.sqrt(x*x + y*y + z*z);
            l_calc = true;
        }
        return l;
    }

    public Vector3 scale(double d) {
        return new Vector3(x*d, y*d, z*d);
    }

    public Vector3 normalize() {
        if (!n_calc) {
            double denom = invSqrt(x*x + y*y + z*z);
            normalized = scale(denom);
            n_calc = true;
            normalized.normalized = normalized;
            normalized.n_calc = true;
            normalized.l = 1.0;
            normalized.l_calc = true;
        }
        return normalized;
    }

    private static double invSqrt(double x) {
       double xhalf = 0.5d*x;
       long i = Double.doubleToLongBits(x);
       i = 0x5fe6ec85e7de30daL - (i>>1);
       x = Double.longBitsToDouble(i);
       x = x*(1.5d - xhalf*x*x);
       return x;
    }

    public Vector3 sum(Vector3 b) {
        return new Vector3(x+b.x, y+b.y, z+b.z);
    }

    public Vector3 diff(Vector3 b) {
        return new Vector3(x-b.x, y-b.y, z-b.z);
    }

    public double dot(Vector3 b) {
        return x*b.x + y*b.y + z*b.z;
    }

    public Vector3 cmul(Vector3 b) {
       return new Vector3(x*b.x, y*b.y, z*b.z);
    }

    public Vector3 cross(Vector3 b) {
        double nx = b.y*z - y*b.z;
        double ny = -(b.x*z - x*b.z);
        double nz = b.x*y - x*b.y;
        return new Vector3(nx, ny, nz);
    }

    public Vector3 clamp(double low, double high) {
       double nx = x < low?  low  :
                   (x > high? high : x);
       double ny = y < low?  low  :
                   (y > high? high : y);
       double nz = z < low?  low  :
                   (z > high? high : z);

       return new Vector3(nx, ny, nz);
    }

    public Vector3 reflect(Vector3 normal) {
       // u - 2 udotn * n
       // u is incoming vec (this)
       double twoudotn = 2 * dot(normal);
       Vector3 nscaled = normal.scale(twoudotn);
       return diff(nscaled);
    }

    public String toString() {
        return "{" + x + ", " + y + ", " + z + "}";
    }

    public double X() {
       return x;
    }

    public double Y() {
       return y;
    }

    public double Z() {
       return z;
    }

    public boolean equals(Vector3 o) {
       return x == o.x && y == o.y && z == o.z;
    }


}
