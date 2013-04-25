package cabbagegl
import java.awt.Color
import java.awt.image.BufferedImage

object HelloWorld {
  def wot() = {

    println("Hello, Scala world!")
    
    val test = new Test() 
    val read = new ReadConfig()
    test.runMe()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
/*
    var img = new BufferedImage(test.myCam.roptions.width, test.myCam.roptions.height, BufferedImage.TYPE_INT_RGB)
    
    println(test.myCam.renderPixel(0, 0))
    for (i <- 0 to test.myCam.roptions.width.intValue) {
      for (j <- 0 to test.myCam.roptions.height.intValue) {
    /*  println("attention attention: " + i + ", " + j) 
        img.setRGB(i, j, test.myCam.renderPixel(i, j).getRGB)
        println("great success")
      println(i + "; " + j)
      }
    }
*/
  }
  def main(args: Array[String]) = {
/*
    val test = new Test()
    test runMe()
    println(test.myCam.cel_shaded)
    val read = new ReadConfig()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
    var img = new BufferedImage(test.myCam.roptions.width, test.myCam.roptions.height, BufferedImage.TYPE_INT_RGB)
    for (i <- 0 to test.myCam.roptions.width) {
      for (j <- 0 to test.myCam.roptions.height) {
        println("attention attention: " + i + ", " + j) 
        img.setRGB(i, j, test.myCam.renderPixel(i, j).getRGB)
        println("great success")
      }
    }
*/
    val test = new Test() 
    val read = new ReadConfig()
    test.runMe()
    test.myCam.roptions = new RenderOptions()
    read.readFile(test.myCam.roptions)
    var img = new BufferedImage(test.myCam.roptions.width, test.myCam.roptions.height, BufferedImage.TYPE_INT_RGB)
    
    println(test.myCam.renderPixel(0, 0))
    for (i <- 0 to test.myCam.roptions.width.intValue) {
      for (j <- 0 to test.myCam.roptions.height.intValue) {
    /*  println("attention attention: " + i + ", " + j) 
        img.setRGB(i, j, test.myCam.renderPixel(i, j).getRGB)
        println("great success")
*/
      println(i + "; " + j)
      }
    }
  }
}

