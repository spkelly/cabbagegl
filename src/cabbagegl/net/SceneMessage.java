import cabbagegl.Scene;

public class SceneMessage extends CGLNetMessage {
   public Scene scene;

   public SceneMessage(Scene iscene) {
      super(CGLNetMessageType.SCENE);
      scene = iscene;
   }
}
