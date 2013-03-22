public class PixelRangeMessage extends CGLNetMessage {
   public int lowPixel;
   public int highPixel;

   public PixelRangeMessage(int low, int high) {
      super(CGLNetMessageType.PIX_RANGE);
      lowPixel = low;
      highPixel = high;
   }
}
