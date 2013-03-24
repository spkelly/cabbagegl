public class DepthBufferMessage extends CGLNetMessage {
   public double depthBuffer[];
   public int lowPixel;
   public int highPixel;

   public DepthBufferMessage(double idBuff[], int low, int high) {
      super(CGLNetMessageType.DEPTH_BUFF);
      depthBuffer = idBuff;
      lowPixel = low;
      highPixel = high;
   }
}
