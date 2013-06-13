public class UpdateMessage extends CGLNetMessage {
   public double percentComplete;

   public UpdateMessage(double ipc) {
      super(CGLNetMessageType.UPDATE);
      percentComplete = ipc;
   }
}
