
public class Light {
   private Vector3 intensity;
   private Vector3 position;

   public Light(Vector3 iint, Vector3 ipos) {
      intensity = iint;
      position = ipos;
   }

   public Vector3 computeDiffuse(Scene s, HitPoint hp) {
      Vector3 diffuseIllum = Vector3.ZERO;
      Vector3 hit_pnt  = hp.getHitpoint();
      Vector3 hit_norm = hp.getNormal();
      double  hit_dist = hp.getDistTo();
      Shape   hit_shp  = hp.getHitShape();

      // Make sure we aren't self occluding
      Vector3 oppLightVec = position.diff(hit_pnt).normalize();

      if (oppLightVec.dot(hit_norm) >= 0) {
         // Make sure some other object isn't occluding
         Vector3 fromLight = hit_pnt.diff(position).normalize();
         Ray lightRay = new Ray(position, fromLight);
         HitData hpoints = lightRay.getAllHitpoints(s, 0.0, Double.MAX_VALUE);
         // Make sure some other object isn't occluding

         HitPoint first_hit;
         first_hit = lightRay.findClosestHitpoint(hpoints);
         if (first_hit != null &&  first_hit.getHitShape() == hit_shp) {
            // Calculate light intensity
            Material mat = hit_shp.materialPropsAt(hit_pnt);
            Vector3 s_diff = mat.getDiffuse();

            double cos = hit_norm.dot(oppLightVec);
            double dist = first_hit.getDistTo();
            diffuseIllum = s_diff.cmul(intensity).scale(cos/dist);
         }
      }

      return diffuseIllum;
   }
}
