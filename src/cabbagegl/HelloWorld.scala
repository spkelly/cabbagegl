package cabbagegl
import java.awt.Color
import java.awt.image.BufferedImage

object HelloWorld {
  def main(args: Array[String]) = {
    println("Hello, Scala world!")
    
    val test = new Test()
    test runMe()
    println(test.myCam.cel_shaded)
    val read = new ReadConfig()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
    var img = new BufferedImage(test.myCam.roptions.width, test.myCam.roptions.height, BufferedImage.TYPE_INT_RGB)
    for (var i <- 0 to test.myCam.roptions.width) {
      for (var j <- 0 to test.myCam.roptions.height) {
        println("attention attention: " + i + ", " + j) 
        img.setRGB(i, j, test.myCam.renderPixel(i, j).getRGB)
        println("great success")
      }
    }
  }
}
