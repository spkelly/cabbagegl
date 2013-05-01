package cabbagegl
import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.OutputChannel
import java.awt.image.BufferedImage
import java.awt.Color

case class AskPixel(i: Int, j: Int) 

class BuildAskPixel(var iter: IndexedSeq[(Int, Int)]) {
  // def this(a: Int, b: Int) = this(for (i <- 0 to (a - 1); j <- 0 to (b - 1)) yield (i, j))
  def next = if (iter.isEmpty) None
    else {
    val buffer = Some(AskPixel(iter.head._1, iter.head._2))
    iter = iter.tail
    buffer 
    }
}

case class GetPixel(i: Int, j: Int, color: Int) {
  override def toString = i + ", " + j + ", " + color
}

case class ImgWrapper(img: BufferedImage)

case class End


class RenderSlave(cam: Camera) extends Actor {
  def act() {
    while (true) {
      receive {
        case AskPixel(i, j) => 
          val pixel = GetPixel(i, j, cam.renderPixel(i, j).getRGB())
          sender ! pixel
          // println("wot: " + pixel)
        case End => exit()
        case _ => println("wtf")
      }
    }
  }
}

class RenderMaster(cam: Camera, slave: RenderSlave) extends Actor {
  var returnTo: Option[OutputChannel[Any]] = None // nulls are bad and wrong I know 
  var loopPixels = true 
  var askFactory: BuildAskPixel = null
  var img: BufferedImage = null
  def act() {
    while (loopPixels) {
      receive {
        case ImgWrapper(image) => {
          println("master: got ImgWrapper")
          returnTo = Some(sender)
          img = image
          askFactory = new BuildAskPixel(for (i <- 0 to (cam.roptions.width - 1); j <- 0 to (cam.roptions.height - 1)) yield (i, j))
          slave ! askFactory.next.get // just can't be a none // I should do something anyways
        }
        case GetPixel(a, b, color) => {
          println("master: got GetPixel; i: " + a + " , j: " + b)
          img.setRGB(a, b, color)
          askFactory.next match {
            case Some(toAsk) => sender ! toAsk
            case None => {  
              loopPixels = false
              sender ! End
              returnTo.get ! ImgWrapper(img)
            }
          }
        }
      }
    }
  } 
}
