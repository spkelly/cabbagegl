
public class Light {
   private Vector3 intensity;
   private Vector3 specular;
   private Vector3 position;

   private double constant_attenuation;
   private double linear_attenuation;
   private double quadratric_attenuation;

   public Light(Vector3 iint, Vector3 ispec, Vector3 ipos) {
      this(iint, ispec, ipos, new Vector3(2.0, 0, 0));
   }

   public Light(Vector3 iint, Vector3 ispec, Vector3 ipos, Vector3 atten) {
      intensity = iint;
      specular = ispec;
      position = ipos;

      constant_attenuation = atten.X();
      linear_attenuation = atten.Y();
      quadratric_attenuation = atten.Z();
   }

   public Vector3 computeIllumination(Scene s, HitPoint hp) {
      Vector3 diffuseIllum = Vector3.ZERO;
      Vector3 specIllum = Vector3.ZERO;
      double attenuation = 1.0;
      Vector3 hit_pnt  = hp.getHitpoint();
      Vector3 hit_norm = hp.getNormal();
      Shape   hit_shp  = hp.getHitShape();

      // Make sure we aren't self occluding
      Vector3 oppLightVec = position.diff(hit_pnt);
      oppLightVec = oppLightVec.normalize();

      if (oppLightVec.dot(hit_norm) >= 0) {
         // Make sure some other object isn't occluding
         Vector3 fromLight = hit_pnt.diff(position).normalize();
         Ray lightRay = new Ray(position, fromLight);
         HitData hpoints = lightRay.getAllHitpoints(s, 0.0, Double.MAX_VALUE);

         HitPoint first_hit;
         first_hit = lightRay.findClosestHitpoint(hpoints);
         if (first_hit != null &&  first_hit.getHitShape() == hit_shp) {
            // Calculate light intensity

            // Start with diffuse illumination
            Material mat = hit_shp.materialPropsAt(hit_pnt);
            Vector3 s_diff = mat.getDiffuse();

            double cos = hit_norm.dot(oppLightVec);
            double dist = first_hit.getDistTo();
            diffuseIllum = s_diff.cmul(intensity).scale(cos/dist);

            // Now get specular highlights using Blinn-Phong distribution
            Vector3 s_spec = mat.getSpecular();
            Vector3 l_spec = specular;
            double shine = mat.getShininess();

            // Calculate halfway vector H for blinn-phong
            Vector3 L = oppLightVec;
            Vector3 V = hit_pnt.scale(-1.0);
            Vector3 H = L.sum(V).normalize();
            Vector3 N = hit_norm;

            Vector3 ps = s_spec.cmul(l_spec);
            double  blinn_phong = Math.pow(Math.max(H.dot(N), 0.0), shine);
            
            specIllum = ps.scale(blinn_phong);
            
            // Now calculate the attenuation
            double c_a = constant_attenuation;
            double l_a = linear_attenuation;
            double q_a = quadratric_attenuation;
            double d = dist;
            attenuation = 1.0/(c_a + d*l_a + d*d*q_a);
         }
      }

      return diffuseIllum.sum(specIllum).scale(attenuation);
   }
}
