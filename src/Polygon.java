import java.util.List;

public class Polygon extends Shape {
   private Triangle tris[];
   

   // Verts must be length 3 or greater pls :c
   public Polygon(List<Vector3> verts, Material mat) {
      super(mat);
      // turn the verts into a bunch of tris
      tris = new Triangle[verts.size()];
      
      // Compute the center point
      Vector3 sum = Vector3.ZERO;
      for (int i = 0; i < tris.length; i++)
         sum = sum.sum(verts.get(i));
      sum = sum.scale(1.0/tris.length);

      Vector3 center = sum;
      for (int i = 0; i < tris.length-1; i++) {
         Vector3 v1 = verts.get(i);
         Vector3 v2 = verts.get(i+1);
         Vector3 v3 = center;
         tris[i] = new Triangle(v1, v2, v3, mat);
      }
      Vector3 v1 = verts.get(tris.length-1);
      Vector3 v2 = verts.get(0);
      Vector3 v3 = center;
      tris[tris.length-1] = new Triangle(v1, v2, v3, mat);
   }

   public HitData hitBy(Ray r) {
      HitData hd = new HitData();
      HitData curr_hd;
      HitPoint hit;
      for (int i = 0; i < tris.length; i++) {
         curr_hd = tris[i].hitBy(r);
         if (curr_hd != null) {
            hit = curr_hd.getHitpoints().get(0);
            hd.addHitpoint(hit);
         }
      }
      if (hd.getHitpoints().isEmpty())
         hd = null;
      return hd;
   }

   public Material materialPropsAt(Vector3 ray, FaceSide fs) {
      // this should almost certainly never be called
      return mat;
   }

   public void translate(Vector3 trans) {
      for (int i = 0; i < tris.length; i++)
         tris[i].translate(trans);
   }

   public void rotate(Axis axis, double angle) {
      for (int i = 0; i < tris.length; i++)
         tris[i].rotate(axis, angle);
   }

}
