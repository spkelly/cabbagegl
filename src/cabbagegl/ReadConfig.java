package cabbagegl;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadConfig {
   public RenderOptions readFile(RenderOptions options) {
        try {
            File configFile = new File("../src/cabbagegl/config.xml");
            DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuild = dbFac.newDocumentBuilder();
            Document doc = dbBuild.parse(configFile);

            doc.getDocumentElement().normalize();

            Node someNode  = doc.getDocumentElement();
            Element someElem = (Element) someNode; 

            options.AA_samples = Integer.parseInt(someElem.getElementsByTagName("AA_samples").item(0).getTextContent());
            options.width = Integer.parseInt(someElem.getElementsByTagName("width").item(0).getTextContent());
            options.height = Integer.parseInt(someElem.getElementsByTagName("height").item(0).getTextContent());
            options.max_recurse = Integer.parseInt(someElem.getElementsByTagName("max_recurse").item(0).getTextContent());
            options.focal_plane_dist = Double.parseDouble(someElem.getElementsByTagName("focal_plane_dist").item(0).getTextContent());
            options.lens_aperture_radius = Double.parseDouble(someElem.getElementsByTagName("lens_aperture_radius").item(0).getTextContent());
            options.dof_rays = Integer.parseInt(someElem.getElementsByTagName("dof_rays").item(0).getTextContent());
               
            return options;
        } catch (Exception e) {
            e.printStackTrace();
            return options;
        }
    }
}
