import java.io.IOException;
import java.net.*;

public class RenderServer implements Runnable {
   private int sport;
   private ServerSocket ssock;
   private boolean server_running;

   private static final int TIMEOUT_CHECK = 1000;

   public RenderServer(int port) {
      sport = port;
   }

   public void run() {
      // Try/catch makes code look ugly. I'm so sorry :S

      // Set up server socket
      server_running = true;
      try{
         ssock = new ServerSocket(sport);
         ssock.setSoTimeout(TIMEOUT_CHECK);
      } catch(Exception e) {
         System.err.println("Failed to create server socket: port in use.");
         return;
      }
      
      while(server_running) {
         // Accept connections
         try  {
            // Immediately give new connections their own thread
            Socket clSock = ssock.accept();
            RenderInstance ri = new RenderInstance(clSock);
            new Thread(ri).start();
         } catch(SocketTimeoutException ste) {
            // Do nothing. This is here so that the thread can check if the
            // server should still be running every few seconds
         } catch(IOException ioe) {
            String errStr = "Failed to accept incoming connection. Server " +
               "terminated.";
            System.err.println(errStr);
            return;
         }
      }
   }

   public void stop_accepting() {
      server_running = false;
   }


   // This class represents a connection to a client that will probably request
   // an image to be rendered.
   // It will accept all necessary data from a client, servicing requests.
   private class RenderInstance implements Runnable {
      private Socket clSock;

      public RenderInstance(Socket isock) {
         clSock = isock;
      }

      public void run() {
         // Begin receiving messages

      }
   }
}
