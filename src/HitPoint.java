public class HitPoint implements Comparable<HitPoint> {
   private Vector3 hitpoint;
   private Vector3 normal;
   private double distTo;
   private Shape hitShape;

   public HitPoint(Vector3 ih, Vector3 in, double dist, Shape iHS) {
      hitpoint = ih;
      normal = in;
      distTo = dist;
      hitShape = iHS;
   }

   public int compareTo(HitPoint o) {
      double diff = distTo - o.distTo;
      return diff < 0? -1 : 
             diff > 0?  1 : 0;
   }
   
   public Vector3 getHitpoint() {
      return hitpoint;
   }

   public Vector3 getNormal() {
      return normal;
   }

   public double getDistTo() {
      return distTo;
   }

   public Shape getHitShape() {
      return hitShape;
   }
}
