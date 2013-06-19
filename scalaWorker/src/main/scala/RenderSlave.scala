package scalaActors 
import akka.actor._
import akka.routing.RoundRobinRouter
import akka.routing.Broadcast
import akka.util.Duration
import akka.util.duration._
import java.awt.image.BufferedImage
import java.awt.Color
import cabbagegl._
import java.io._
import javax.imageio.ImageIO
//import java.util._

// LOCAL

class Message
case class AskPixel(i: Int, j: Int) extends Message
case class GetPixel(i: Int, j: Int, color: Int) extends Message { 
  override def toString = i + ", " + j + ", " + color
}
case class ImgWrapper(img: BufferedImage, duration: Duration) extends Message
case class CamWrapper(cam: Camera) extends Message
case class AskWorkers extends Message
case class GetWorkers(worker: List[ActorRef]) extends Message


class Worker(var cam: Camera) extends Actor {
  def receive = {
    case AskPixel(i, j) => 
      val pixel = GetPixel(i, j, cam.renderPixel(i, j).getRGB())
      sender ! pixel
    case CamWrapper(someCam) => cam = someCam
  }
}

class MidExec extends Actor {
  val nWorkers = 3
  val listeWorkers = (1 to nWorkers).map({x => context.actorOf(Props(new Worker(null)), name = ("remoteWorker" + x))})
  def receive = {
    case AskWorkers => 
      println("Exec: AskWorkers")
      sender ! GetWorkers(listeWorkers.toList)
    case y: String => sender ! "exec: " + y
  }
}

/*
class Master(nWorkers: Int, cam: Camera, listener: ActorRef) extends Actor {  
  val nPixels = cam.roptions.width * cam.roptions.height
  var image: BufferedImage = _
  var nResults: Int = 0
  val start: Long = System.currentTimeMillis

  val workerRouter = context.actorOf(Props(new Worker(cam)).withRouter(RoundRobinRouter(nWorkers)), name = "WorkerRouter")
  
  def receive = {
    case camWrapper: CamWrapper => workerRouter ! Broadcast(camWrapper)
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
*/

class Listener extends Actor {
  def receive = {
    case ImgWrapper(imgResultat, duration) => 
      val output = new File("output.png")
      ImageIO.write(imgResultat, "png", output)
      context.system.shutdown()
      println("\n\tTemps de render: \t" + duration)
    case _ => println("apres-render: wtf")
  }
}

object Server extends App {
  val system = ActorSystem("workerSystem")
  val exec = system.actorOf(Props[MidExec], name = "exec")
  val input = readLine("stop>")
  system.shutdown
}

/*
object Render extends App {
  work()
  def work() {
    val test = new Test() 
    val read = new ReadConfig()
    test.runMe()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
    val img = new BufferedImage(test.myCam.roptions.width, test.myCam.roptions.height, BufferedImage.TYPE_INT_RGB)
    val system = ActorSystem("RenderSystem")
    val listener = system.actorOf(Props[Listener], name = "listener")
    val master = system.actorOf(Props(new Master(4, new Camera(test.myCam), listener)), name = "Master")
    master ! ImgWrapper(img, System.currentTimeMillis.millis)
  } 
}
*/
