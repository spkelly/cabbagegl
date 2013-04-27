import scala.actors.Actor
import scala.actors.Actor._
import java.awt.Color
import java.awt.image.BufferedImage

class Master(cam: Camera, slave: RenderSlave) extends Actor {
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

val hel = HelloWorld
hel.main(Array("test"))
val slave = new RenderSlave(hel.test.myCam)
slave.start
val master = new Master(hel.test.myCam, slave)
master.start
