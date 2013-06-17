// I'l have to split that file into a few more logically... Logic subdivisions. 
// master / slave, for starters. And perhaps another file for the messages. 

package scalaActors 
import akka.actor._
import akka.routing.RoundRobinRouter
import akka.util.Duration
import akka.util.duration._
import java.awt.image.BufferedImage
import java.awt.Color
import cabbagegl._
import java.io._
import javax.imageio.ImageIO
import java.util._

// ovbiously, those are passed around. 
// If I recall correctly... 
// the main method creates the whole shebang of actors, then send an ImgWrapper to the master, which is the "start" signal. 
// AskPixel is sent by the master to a slave (is that how it's still called?), which sends back a GetPixel. 
// once it's all done, Master sends back the ImgWrapper to the listnener, which bounces it to .png and displays the render time and such meaningless trivia

// Perhaps the listener stuff could be better done by asking the master for a future, instead. I have a nasty tendency to cargo-cult, sometimes.


class Message

case class AskPixel(i: Int, j: Int) extends Message

case class GetPixel(i: Int, j: Int, color: Int) extends Message { 

  override def toString = i + ", " + j + ", " + color
}

case class ImgWrapper(img: BufferedImage, duration: Duration) extends Message

// that's a wrapper over your cam, which also happens to be a data container for the scene details and such. Yeah, that's ugly. 
// it takes an ask, computes, sends back a pixel.
class Worker(cam: Camera) extends Actor {
  def receive = {
    case AskPixel(i, j) => 
      val pixel = GetPixel(i, j, cam.renderPixel(i, j).getRGB())
      sender ! pixel
  }
}

// as I said, this guy takes an ImgWrapper, splits the task (via the automagic router) then sends back an image. 
// 
class Master(nWorkers: Int, cam: Camera, listener: ActorRef) extends Actor {  
  val nPixels = cam.roptions.width * cam.roptions.height
  var image: BufferedImage = _
  var nResults: Int = 0
  val start: Long = System.currentTimeMillis

  val workerRouter = context.actorOf(Props(new Worker(cam)).withRouter(RoundRobinRouter(nWorkers)), name = "WorkerRouter")
  
  def receive = {
    case ImgWrapper(_image, _) => 
      image = _image
      for (i <- 0 to (cam.roptions.width - 1); j <- 0 to (cam.roptions.height - 1)) workerRouter ! AskPixel(i, j)
    case GetPixel(a, b, color) => 
      image.setRGB(a, b, color)
      nResults += 1
      if (nResults == nPixels) {
        listener ! ImgWrapper(image, (System.currentTimeMillis - start).millis)
        context.stop(self)
      }
  }
}

// waits for an ImgWrapper. 
class Listener extends Actor {
  def receive = {
    case ImgWrapper(imgResultat, duration) => 
      val output = new File("output.png")
      ImageIO.write(imgResultat, "png", output)
      context.system.shutdown()
      println("\n\tRender Time: \t" + duration)
    case _ => println("Houston we have a problem")
  }
}

object Render extends App {
  work()
  def work() {
    // a shitload of stupid boilerplate to load the config file and the scene description. This is seriously ugly. 
    val test = new Test() 
    val read = new ReadConfig()
    test.runMe()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
    val img = new BufferedImage(test.myCam.roptions.width, test.myCam.roptions.height, BufferedImage.TYPE_INT_RGB)
    // my boilerplate ends. This is Akka boilerplate. 
    val system = ActorSystem("RenderSystem")
    val listener = system.actorOf(Props[Listener], name = "listener")
    val master = system.actorOf(Props(new Master(4, new Camera(test.myCam), listener)), name = "Master")
    // aaand here we go. 
    master ! ImgWrapper(img, System.currentTimeMillis.millis)
  } 
}

