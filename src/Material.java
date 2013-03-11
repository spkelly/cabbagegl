
// Defines the various material properties of an object
public class Material {
   public Material(Vector3 icol, Vector3 ispec, Vector3 idiff) {
      this(icol, ispec, idiff, 2.0);
   }

   public Material(Vector3 icol, Vector3 ispec, Vector3 idiff, double ish) {
      color = icol;
      specular = ispec;
      diffuse = idiff;
      shininess = ish;
   }
   private Vector3 color;
   private Vector3 specular;
   private Vector3 diffuse;
   private double shininess;

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
}
