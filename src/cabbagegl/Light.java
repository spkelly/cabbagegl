package cabbagegl;

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

    public Light(Light arg) {
        this.intensity = arg.intensity;
        this.specular = arg.specular;
        this.position = arg.position;
        this.constant_attenuation = new Vector3(arg.constant_attenuation);
        this.linear_attenuation = new Vector3(arg.linear_attenuation);
        this.quadratic_attenuation = new Vector3(arg.quadratic_attenuation);
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
      Double distLight;

      distLight = distFromLight(s, hp);
      if (distLight != null) {
         diffuseIllum = computeDiffuse(s, hp, distLight);
         specIllum = computeSpecular(s, hp);
         attenuation = computeAttenuation(s, hp);

      }

      return diffuseIllum.sum(specIllum).scale(attenuation);
   }

   public Double distFromLight(Scene s, HitPoint hp) {
      Double dist = null;
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
         if (first_hit != null &&  first_hit.getHitShape() == hit_shp)
            dist = first_hit.getDistTo();
      }
      return dist;
   }

   public Vector3 computeDiffuse(Scene s, HitPoint hp, double distToLight) {
      Vector3 diffuseIllum = Vector3.ZERO;
      
      Vector3 hit_pnt  = hp.getHitpoint();
      Vector3 hit_norm = hp.getNormal();
      Shape   hit_shp  = hp.getHitShape();

      Vector3 oppLightVec = position.diff(hit_pnt);
      oppLightVec = oppLightVec.normalize();

      // Calculate light intensity
      Vector3 L = oppLightVec;
      Vector3 N = hit_norm;
      double dist = distToLight;

      // Start with diffuse illumination
      Material mat = hit_shp.materialPropsAt(hit_pnt, hp.getSideHit());
      Vector3 s_diff = mat.getDiffuse();

      double diff_scale = diffScaleAtPoint(hp);
      diffuseIllum = s_diff.cmul(intensity).scale(diff_scale);
      return diffuseIllum;
   }

   public double diffScaleAtPoint(HitPoint hp) {
      Vector3 hit_pnt  = hp.getHitpoint();
      Vector3 hit_norm = hp.getNormal();

      Vector3 oppLightVec = position.diff(hit_pnt).normalize();

      // Calculate light intensity
      Vector3 L = oppLightVec;
      Vector3 N = hit_norm;
      double diff_scale = Math.max(N.dot(L), 0.0);
      return diff_scale;
   }

   public Vector3 computeSpecular(Scene s, HitPoint hp) {
      Vector3 specIllum = Vector3.ZERO;

      Vector3 hit_pnt  = hp.getHitpoint();
      Vector3 hit_norm = hp.getNormal();
      Shape   hit_shp  = hp.getHitShape();

      Vector3 oppLightVec = position.diff(hit_pnt);
      oppLightVec = oppLightVec.normalize();

      Vector3 L = oppLightVec;
      Vector3 N = hit_norm;

      Material mat = hit_shp.materialPropsAt(hit_pnt, hp.getSideHit());

      // Now get specular highlights using Blinn-Phong distribution
      Vector3 s_spec = mat.getSpecular();
      Vector3 l_spec = specular;
      double shine = mat.getShininess();

      // Calculate halfway vector H for blinn-phong
      Vector3 V = hit_pnt.scale(-1.0);
      Vector3 H = L.sum(V).normalize();

      Vector3 ps = s_spec.cmul(l_spec);
      double  blinn_phong = Math.pow(Math.max(H.dot(N), 0.0), shine);
      blinn_phong /= (8 * Math.PI / (shine+2));
      
      specIllum = ps.scale(blinn_phong);
      return specIllum;
   }

   public double computeAttenuation(Scene s, HitPoint hp) {
      Vector3 hit_pnt  = hp.getHitpoint();
      Vector3 oppLightVec = position.diff(hit_pnt);
      
      double d = oppLightVec.len();
      double c_a = constant_attenuation;
      double l_a = linear_attenuation;
      double q_a = quadratric_attenuation;
      double attenuation = 1.0/(c_a + d*l_a + d*d*q_a);
      return attenuation;
   }
   
   private Vector3 computeCellShadedIllum(Scene s, HitPoint hp) {
      double grayVals[] = {0, .5, 1.0};
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
            double dist = first_hit.getDistTo();

            // Calculate attenuation
            double c_a = constant_attenuation;
            double l_a = linear_attenuation;
            double q_a = quadratric_attenuation;
            double d = dist;
            attenuation = 1.0/(c_a + d*l_a + d*d*q_a);


            // Calculate light intensity
            Vector3 L = oppLightVec;
            Vector3 N = hit_norm;

            // Start with diffuse illumination
            Material mat = hit_shp.materialPropsAt(hit_pnt, first_hit.getSideHit());
            Vector3 s_diff = mat.getDiffuse();

            double diff_scale = Math.max(N.dot(L), 0.0) * attenuation;
            if (diff_scale > 1) diff_scale = 1;
            int g_ndx = (int) (grayVals.length * diff_scale);
            if (g_ndx == grayVals.length) g_ndx--;
            double g_val = grayVals[g_ndx];
            Vector3 gray = new Vector3(g_val, g_val, g_val);

            // Combine gray and color
            diffuseIllum = s_diff.cmul(intensity).cmul(gray);

            // Now get specular highlights using Blinn-Phong distribution
            Vector3 s_spec = mat.getSpecular();
            Vector3 l_spec = specular;
            double shine = mat.getShininess();

            // Calculate halfway vector H for blinn-phong
            Vector3 V = hit_pnt.scale(-1.0);
            Vector3 H = L.sum(V).normalize();

            Vector3 ps = s_spec.cmul(l_spec);
            double  blinn_phong = Math.pow(Math.max(H.dot(N), 0.0), shine);
            blinn_phong /= (8 * Math.PI / (shine+2));
            
            specIllum = ps.scale(blinn_phong);
            specIllum = specIllum.scale(attenuation);
         }
      }
      return diffuseIllum.sum(specIllum);
   }
}
