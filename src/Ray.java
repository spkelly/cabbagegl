import java.awt.Color;
import java.util.*;

public class Ray {
    private Vector3 base;
    private Vector3 dir;

    public Ray(Vector3 ibase, Vector3 idir) {
        base = ibase;
        dir = idir;
    }

    public Vector3 getBase() {
        return base;
    }

    public Vector3 getDir() {
        return dir;
    }

    public Vector3 trace(Scene s, double mindist, double maxdist) {
       return trace(s, mindist, maxdist, 0);
    }

    // Compute color of ray fired through scene. ignore objects that are hit
    // that are closer to the viewer than mindist or further than maxdist
    private Vector3 trace(Scene s, double mindist, double maxdist, double sofar) {
       Vector3 retColor = Vector3.ZERO;

       // Find the closest hitpoint
       HitData allHitpoints = getAllHitpoints(s, mindist, maxdist);
       if (!allHitpoints.isEmpty()) {
         HitPoint closest = findClosestHitpoint(allHitpoints);
         sofar += closest.getDistTo();

         Vector3 hitPos = closest.getHitpoint();
         Shape hitShape = closest.getHitShape();

         Material mat = hitShape.getMaterial();

         Vector3 matC = mat.getColor();
         Vector3 ambient = s.ambient;

         // Compute ambient contribution
         retColor = matC.cmul(ambient);

         // Compute diffuse contribution
         Vector3 diff = diffuseLighting(s, closest);
         retColor = retColor.sum(diff);

         // Scale the resulting color using the dist travelled so far
         retColor = retColor.scale(1.0/sofar);
       }


       retColor = retColor.clamp(0.0, 1.0);
       return retColor;
    }


    public HitData getAllHitpoints(Scene s, double mindist, double maxdist) {
       HitData allhit = new HitData();
       // go through the object list and see if we hit anything
       for (Shape i : s.renderables) {
          HitData data = i.hitBy(this);
          if (data != null)
             for (HitPoint j : data.getHitpoints()) {
                double dist = j.getDistTo();
                // Ignore hitpoints that aren't in the view volume
                if (dist > mindist && dist < maxdist)
                   allhit.addHitpoint(j);
             }
       }
       return allhit;
    }

    public HitPoint findClosestHitpoint(HitData hitpoints) {
       return findClosestHPointsExclude(hitpoints, null);
    }

    public HitPoint findClosestHPointsExclude(HitData hitpoints, Shape ex) {
       HitPoint ret = null;
       if (!hitpoints.isEmpty()) {
          hitpoints.sort();
          for (HitPoint i : hitpoints.getHitpoints()) {
             if (i.getHitShape() == ex)
                continue;
             else {
                ret = i;
                break;
             }
          }
       }
       return ret;
    }

    private Vector3 diffuseLighting(Scene s, HitPoint hp) {
       Vector3 diffuseAdded = Vector3.ZERO;
       for (Light l : s.lights)
          diffuseAdded = diffuseAdded.sum(l.computeDiffuse(s, hp));
       return diffuseAdded;
    }

}
