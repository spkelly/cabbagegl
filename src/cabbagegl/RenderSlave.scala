package cabbagegl
import scala.actors.Actor
import scala.actors.Actor._
import java.awt.image.BufferedImage
import java.awt.Color

case class AskPixel(i: Int, j: Int) 

class BuildAskPixel(var iter: IndexedSeq[(Int, Int)]) {
  def this(a: Int, b: Int) = this(for (i <- 0 to (a - 1); j <- 0 to (b - 1)) yield (i, j))
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


class RenderMaster(cam: Camera, slave: List[RenderSlave]) extends Actor {
  def act {
    loop {
      react {
        case ImgWrapper(img) => {
          println("banane")
          /*
          for (i <- 0 to cam.roptions.width - 1) {
            println("i: " + i)
            for (j <- 0 to cam.roptions.height - 1) {
              // println("attention attention: i " + i + ", j: " + j)
              slave ! AskPixel(i, j)
              receive {
                case GetPixel(a, b, color) => {
                  img.setRGB(i, j, color)
                  // println("i: " + i + ", j: " + j + ", color: " + color)
                }
                case _ => println("master: wtf")
              } 
            }
          }
          */
          val askFactory new BuildAskPixel(cam.roptions.width, cam.roptions.height)
          var toAsk = askFactory next
          slave map (x => {
            x ! toAsk next get
          })
          var nSlaves = slave.size
          while (nSlaves > 0)
            receive {
              case GetPixel(a, b, color) => {
                img.setRGB(a, b, color)
                // println("i: " + i + ", j: " + j + ", color: " + color)
                toAsk next match {
                  case Some(ask) => sender ! ask
                  case None => {
                    sender ! End
                    nSlaves = nSlaves - 1
                  }
                }
    
              }
              case _ => println("master: wtf")
            }   
          }
          sender ! ImgWrapper(img)
          // sender ! img
          // sender ! "banane"
          exit()
        }
      case _ => println("wtf")
      }
    }
  }
}
