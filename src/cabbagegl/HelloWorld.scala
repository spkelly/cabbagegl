package cabbagegl
import java.awt.Color
import java.awt.image.BufferedImage
import java.io._
import javax.imageio.ImageIO
import java.util._

object HelloWorld {
  var test = new Test
  var img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
  def wot() = {

    println("Hello, Scala world!")
    
    test = new Test() 
    val read = new ReadConfig()
    test.runMe()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
  }
  
  def main(args: Array[String]): Unit = {
    test = new Test() 
    val read = new ReadConfig()
    test.runMe()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
    img = new BufferedImage(test.myCam.roptions.width, test.myCam.roptions.height, BufferedImage.TYPE_INT_RGB)
/*
    val slave = RenderSlave(new Camera(test.myCam))
    val master = RenderMaster(new Camera(test.myCam), slave, img)
    slave.start
    master.start
*/
    val slave = new RenderSlave(new Camera(test.myCam))
    slave.start
    val master = new RenderMaster(new Camera(test.myCam, slave))
    master.start
    master !? ImgWrapper(img) match {
      case ImgWrapper(img) => 
      // illegal FUCKING character right fucking there, for the gazilionth time today. I'm going to bed. 
        file output = new File("output.png")
        ImageIO.write(img, "png", output)
      case _ => println("apres render: wtf")
    } 
  }
}

