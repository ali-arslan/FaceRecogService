package sample;

import com.facepp.error.FaceppParseException;
import org.json.JSONException;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Ali Arslan on 2/22/2015.
 */
public class FaceRecognition {
    static boolean registerFace(String userid, String label) throws IOException, JSONException, FaceppParseException, InterruptedException {
        // this method is called when the user presses the Register button
        System.out.println("called");
        FaceRecognitionCore.connect();
        FaceRecognitionCore.generalPersonAdd(userid);

//        for (int i = 0; i < 5; i++) {
            FaceRecognitionCore.generalFaceAdder(userid, Controller.getCurrentFrame());
//            Thread.sleep(1000);
//        }
        return false;
    }

    static void detectFace() throws InterruptedException, FaceppParseException, JSONException, IOException { // this method runs in a separate thread and continually checks for matches
        BufferedImage bufferedImage = Controller.getCurrentFrame();
        // ideally, perform local detection here recognition

        FaceRecognitionCore.connect();
        String heuristics = FaceRecognitionCore.performDetectionRes(bufferedImage);
        setInfo("Initial Heuristic Detection", "", heuristics.split(",")[0], heuristics.split(",")[2], heuristics.split(",")[1]);
        String match = FaceRecognitionCore.recognitionRecognize("general", bufferedImage);
        if (!match.equals(null)) {
            if (Controller.precisionMode){
                FaceRecognitionCore.performDetection(bufferedImage);
                if (FaceRecognitionCore.recognitionVerify(match))
                    setInfo("Recognition Successful", match, heuristics.split(",")[0], heuristics.split(",")[2], heuristics.split(",")[1]);
            } else
                setInfo("Recognition Successful", match, heuristics.split(",")[0], heuristics.split(",")[2], heuristics.split(",")[1]);
        }
        Thread.sleep(5000); // to reduce resource usage
    }

    static void setInfo(String Status, String Name, String Sex, String Age, String Race) { // call this in detectFace() post detection
        Controller.detectedAge = Age;
        Controller.detectedName = Name;
        Controller.detectedSex = Sex;
        Controller.detectedStatus = Status;
        Controller.updateDetectedInfo = true;
        Controller.detectedRace = Race;
    }

}
