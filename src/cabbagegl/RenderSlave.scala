package cabbagegl
import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.OutputChannel
import java.awt.image.BufferedImage
import java.awt.Color

class Message

case class AskPixel(i: Int, j: Int) extends Message

case class GetPixel(i: Int, j: Int, color: Int) extends Message { 

  override def toString = i + ", " + j + ", " + color
}

case class ImgWrapper(img: BufferedImage) extends Message

case class SemiEnd extends Message

case class TotalEnd extends Message

class BuildAskPixel(var iter: IndexedSeq[(Int, Int)]) extends Message {

  // def this(a: Int, b: Int) = this(for (i <- 0 to (a - 1); j <- 0 to (b - 1)) yield (i, j))
  var njobs = 0
  def next = if (iter.isEmpty) {
      if (njobs == 0) TotalEnd else SemiEnd
    } 
    else {
    val buffer = AskPixel(iter.head._1, iter.head._2)
    njobs += 1
    iter = iter.tail
    buffer 
    }
  def gotPixel = njobs -= 1
}




class RenderSlave(cam: Camera) extends Actor {
  def act() {
    while (true) {
      receive {
        case AskPixel(i, j) => 
          val pixel = GetPixel(i, j, cam.renderPixel(i, j).getRGB())
          sender ! pixel
          // println("wot: " + pixel)
        case SemiEnd => exit()
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
          slave ! askFactory.next // just can't be like anything but an askPixel. Except you have more actors than pixels. Right? 
        }
        case GetPixel(a, b, color) => {
          println("master: got GetPixel; i: " + a + " , j: " + b)
          img.setRGB(a, b, color)
          askFactory.gotPixel
          askFactory.next match {
            case toAsk: AskPixel => sender ! toAsk
            case TotalEnd => {  
              loopPixels = false
              sender ! SemiEnd
              returnTo.get ! ImgWrapper(img)
              println("master: totalEnd")
              exit()
            }
            case SemiEnd => {
              askFactory.gotPixel
              sender ! SemiEnd
              println("master: semiEnd")
            }
            case _ => println("master: wtf")
          }
        }
      }
    }
  } 
}
