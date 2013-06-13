import cabbagegl.RenderOptions;

public class RenderOptionsMessage extends CGLNetMessage {
   public RenderOptions render_options;

   public RenderOptionsMessage(RenderOptions options) {
      super(CGLNetMessageType.RENDER_OPTIONS);
      render_options = options;
   }
}
