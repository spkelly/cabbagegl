import java.awt.Color;

public class RenderedMessage extends CGLNetMessage {

   public Color rendered[];
   public int lowPixel;
   public int highPixel;

   public RenderedMessage(Color irendered[], int low, int high) {
      super(CGLNetMessageType.RENDERED);
      rendered = irendered;
      lowPixel = low;
      highPixel = high;
   }
}
