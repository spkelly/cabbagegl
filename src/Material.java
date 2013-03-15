
// Defines the various material properties of an object
public class Material {
   public Material(Vector3 icol, Vector3 ispec, Vector3 idiff) {
      this(icol, ispec, idiff, 2.0);
   }

   public Material(Vector3 icol, Vector3 ispec, Vector3 idiff, double ish) {
      this(icol, ispec, idiff, ish, 0.0, 1.5);
   }

   public Material(Vector3 icol, Vector3 ispec, Vector3 idiff, double ish,
         double ialph, double iior) {
      color = icol;
      specular = ispec;
      diffuse = idiff;
      shininess = ish;

      alpha = ialph;
      indexOfRefraction = iior;
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
