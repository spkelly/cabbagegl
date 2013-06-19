package cabbagegl;
import java.io.Serializable;

// Defines the various material properties of an object
public class Material implements Serializable {
   public Material(Vector3 icol, Vector3 idiff, Vector3 ispec) {
      this(icol, idiff, ispec, 2.0);
   }

   public Material(Vector3 icol, Vector3 idiff, Vector3 ispec, double ish) {
      this(icol, idiff, ispec, ish, 0.0, 1.5);
   }

   public Material(Vector3 icol, Vector3 idiff, Vector3 ispec, double ish,
         double ialph, double iior) {
      color = icol;
      diffuse = idiff;
      specular = ispec;
      shininess = ish;

      alpha = ialph;
      indexOfRefraction = iior;
   }

    public Material(Material arg) {
        this.color = new Vector3(arg.color);
        this.specular = new Vector3(arg.specular);
        this.diffuse = new Vector3(arg.diffuse);
        this.shininess = arg.shininess;
        this.alpha = arg.alpha;
        this.indexOfRefraction = arg.indexOfRefraction;
    }

   private Vector3 color;
   private Vector3 specular;
   private Vector3 diffuse;
   private double shininess;

   private double alpha;
   private double indexOfRefraction;

   public Vector3 getColor() {
      return color;
   }

   public Vector3 getSpecular() {
      return specular;
   }

   public Vector3 getDiffuse() {
      return diffuse;
   }

   public double getShininess() {
      return shininess;
   }

   public double getAlpha() {
      return alpha;
   }

   public double getIndexOfRefraction() {
      return indexOfRefraction;
   }
}