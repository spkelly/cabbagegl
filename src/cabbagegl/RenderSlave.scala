package cabbagegl
import scala.actors.Actor
import scala.actors.Actor._
import java.awt.image.BufferedImage
import java.awt.Color

case class AskPixel(i: Int, j: Int) 

case class GetPixel(i: Int, j: Int, color: Int) {
  override def toString = i + ", " + j + ", " + color
}

case class ImgWrapper(img: BufferedImage)


class RenderSlave(cam: Camera) extends Actor {
  def act() {
    while (true) {
      receive {
        case AskPixel(i, j) => 
          val pixel = GetPixel(i, j, cam.renderPixel(i, j).getRGB())

          sender ! pixel
          println("wot: " + pixel)
        case _ => println("wtf")
      }
    }
  }
}


class RenderMaster(cam: Camera, slave: RenderSlave) extends Actor {
  def act {
    loop {
      react {
        case ImgWrapper(img) => {
          println("banane")
          for (i <- 0 to cam.roptions.width - 1) {
            for (j <- 0 to cam.roptions.height - 1) {
              println("attention attention: i " + i + ", j: " + j)
              slave ! AskPixel(i, j)
              receive {
                case GetPixel(a, b, color) => {
                  img.setRGB(i, j, color)
                  println("i: " + i + ", j: " + j + ", color: " + color)
                }
                case _ => println("master: wtf")
              } 
            }
          }
          sender ! img
        }
        case _ => println("wtf")
      }
    }
  }
}
