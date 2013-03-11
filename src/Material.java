
// Defines the various material properties of an object
public class Material {
   public Material(Vector3 icol, Vector3 ispec, Vector3 idiff) {
      color = icol;
      specular = ispec;
      diffuse = idiff;
   }
   private Vector3 color;
   private Vector3 specular;
   private Vector3 diffuse;

   public Vector3 getColor() {
      return color;
   }

   public Vector3 getSpecular() {
      return specular;
   }

   public Vector3 getDiffuse() {
      return diffuse;
   }
}
