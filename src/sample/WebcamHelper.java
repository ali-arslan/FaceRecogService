package sample;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.github.sarxos.webcam.*;

import javax.imageio.ImageIO;


public class WebcamHelper{
    static  boolean resDone = false;
     static BufferedImage getImage() throws IOException {
         Webcam webcam = Webcam.getDefault();
         if (!resDone) {
             webcam.setViewSize(WebcamResolution.QVGA.getSize());
             resDone = true;
         }
         webcam.open(true);
         BufferedImage bufferedImage = webcam.getImage();


//         File outputfile = new File("image.jpg");
//         ImageIO.write(bufferedImage, "jpg", outputfile);


         return bufferedImage;
    }

}