public class Matrix33 {
   Vector3 r1, r2, r3;

   public Matrix33(Vector3 ir1, Vector3 ir2, Vector3 ir3) {
      r1 = ir1;
      r2 = ir2;
      r3 = ir3;
   }

   public static Matrix33 generateRotationMatrix(Axis a, double angle) {
      double sin = Math.sin(Math.toRadians(angle));
      double cos = Math.cos(Math.toRadians(angle));

      Vector3 v1 = null;
      Vector3 v2 = null;
      Vector3 v3 = null;

      switch(a) {
         case XAXIS:
            v1 = new Vector3(1,0,0);
            v2 = new Vector3(0,cos,-sin);
            v3 = new Vector3(0,sin,cos);
            break;
         case YAXIS:
            v1 = new Vector3(cos,0,sin);
            v2 = new Vector3(0,1,0);
            v3 = new Vector3(-sin,0,cos);
            break;
         case ZAXIS:
            v1 = new Vector3(cos,-sin,0);
            v2 = new Vector3(sin,cos,0);
            v3 = new Vector3(0,0,1);
            break;
      }
      return new Matrix33(v1,v2,v3);
   }

   
   public Vector3 mul(Vector3 right) {
      double x = r1.dot(right);
      double y = r2.dot(right);
      double z = r3.dot(right);
      return new Vector3(x,y,z);
   }
}
