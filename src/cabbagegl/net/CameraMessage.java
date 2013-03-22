import cabbagegl.Camera;

public class CameraMessage extends CGLNetMessage {
   public Camera camera;

   public CameraMessage(Camera cam) {
      super(CGLNetMessageType.CAMERA);
      camera = cam;
   }
}
