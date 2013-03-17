

public class Triangle extends Plane {
   private Vector3 verts[]; // v1, v2, v3
   private Vector3 edges[]; // v1->v2, v2->v3, v3->v1


   public Triangle(Vector3 v1, Vector3 v2, Vector3 v3, Material mat) {
      super(v1, v2.diff(v1).cross(v3.diff(v1)).normalize(), mat);
      verts = new Vector3[3];
      edges = new Vector3[3];
      // Store verts, Compute edges
      verts[0] = v1; edges[0] = v2.diff(v1);
      verts[1] = v2; edges[1] = v3.diff(v2);
      verts[2] = v3; edges[2] = v1.diff(v3);

   }

   public HitData hitBy(Ray r) {
      HitData ret = null;
      HitData hd = super.hitBy(r);

      if (hd != null) {
         HitPoint hp = hd.getHitpoints().get(0);
         // The ray definitely hit the plane this tri exists on
         // Find out if the hitpoint is in the triangle
         Vector3 htpnt = hp.getHitpoint();
         Vector3 htnorm = hp.getNormal().normalize();
         int sign;

         // Walk the edges, take cross product with edge and to-point
         boolean ansFound = true;
         boolean ltz = true, gtz = true;
         for (int i = 0; i < 3; i++) {
            Vector3 tohitpt = htpnt.diff(verts[i]).normalize();
            Vector3 edge = edges[i].normalize();
            Vector3 cross = edge.cross(tohitpt).normalize();

            // uhhh
            if (cross.len() == 0) {
               // There's no intersection
               ansFound = false;
               break;
            }

            double norm_cross_ang = htnorm.dot(cross);
            ltz = ltz && norm_cross_ang < 0;
            gtz = gtz && norm_cross_ang > 0;

            
         }
         if (ansFound && (ltz || gtz)) {
            // The intersection was in the triangle!
            ret = hd;
         }
      }

      return ret;
   }


    public Material materialPropsAt(Vector3 ray, FaceSide fs) {
        return mat;
    }

}
