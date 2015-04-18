package sample;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ali Arslan on 3/30/2015.
 */
public class FaceRecognitionCore {

    static HttpRequests httpRequests;
    static JSONObject result;
    static void connect() {
        httpRequests = new HttpRequests("2088166d0c8c4cb93f5cec0f14101891", "SCgzycGwsVXFWX7FP2WlE9bGRW-oYPAy ", true, true);
        result = null;
    }

    static void generalPersonAdd(String Name) throws IOException, JSONException {
        try {
            addPerson(Name);
//            performDetection();
//            addFace((Name));
            getPersonInfo(Name);
            addToGroup("general", Name);
        } catch (FaceppParseException e) {
            e.printStackTrace();
//            System.out.println(e.getErrorCode());
        }
    }

    static void generalFaceAdder(String Name, BufferedImage bufferedImage) throws FaceppParseException, IOException, JSONException {
        performDetection(bufferedImage);
        addFace(Name);
        train_identify("general");
        train_verify(Name);
    }

    static void performDetection(BufferedImage bufferedImage) throws FaceppParseException, IOException, JSONException {
        ImageIO.write(bufferedImage, "jpg", new File("image.jpg"));
        result = httpRequests.detectionDetect(new PostParameters().setImg(new File("image.jpg")));
        System.out.println("Detection done. Result dump:");

    }
    //    static void performDetection() throws FaceppParseException, IOException, JSONException {
//        ImageIO.write(ImageIO.read(new File("ali2.jpg")), "jpg", new File("image.jpg"));
//        result = httpRequests.detectionDetect(new PostParameters().setImg(new File("image.jpg")));
//        System.out.println("Detection done. Result dump:");
//
//    }
    static String performDetectionRes(BufferedImage bufferedImage) throws FaceppParseException, IOException, JSONException { // return gender, race and age in comma separated string
        ImageIO.write(bufferedImage, "jpg", new File("image.jpg"));
        result = httpRequests.detectionDetect(new PostParameters().setImg(new File("image.jpg")));
        System.out.println("Detection done. Result dump:");
        System.out.println(result);
        String gender = result.getJSONArray("face").getJSONObject(0).getJSONObject("attribute").getJSONObject("gender").getString("value"); // race, age
        String race = result.getJSONArray("face").getJSONObject(0).getJSONObject("attribute").getJSONObject("race").getString("value");
        int age = result.getJSONArray("face").getJSONObject(0).getJSONObject("attribute").getJSONObject("age").getInt("value");
        String resS = gender + "," + race + "," + age;
        System.out.println(resS);
        return resS;
    }

    static void addPerson(String Name) throws FaceppParseException {
        System.out.println(httpRequests.personCreate(new PostParameters().setPersonName(Name)));
    }

    static void addFace(String Name) throws JSONException, FaceppParseException {
        System.out.println(httpRequests.personAddFace(new PostParameters().setPersonName(Name).setFaceId(
                result.getJSONArray("face").getJSONObject(0).getString("face_id"))));
    }

    static void getPersonInfo(String Name) throws FaceppParseException {
        System.out.println(httpRequests.personGetInfo(new PostParameters().setPersonName(Name)));
    }

    static void createGroup(String Name) throws FaceppParseException {
        System.out.println(httpRequests.groupCreate(new PostParameters().setGroupName(Name)));
    }

    static void addToGroup(String GName, String Name) throws FaceppParseException {
        System.out.println(httpRequests.groupAddPerson(new PostParameters().setGroupName(GName).setPersonName(Name)));
    }

    static void getGroupInfo(String Name) throws FaceppParseException {
        System.out.println(httpRequests.groupGetInfo(new PostParameters().setGroupName(Name)));
    }

    static void train_identify(String GName) throws JSONException, FaceppParseException {
        JSONObject syncRet = null;
        syncRet = httpRequests.trainIdentify(new PostParameters().setGroupName(GName));
        System.out.println(syncRet);
        System.out.println(httpRequests.getSessionSync(syncRet.get("session_id").toString()));
    }

    static void train_verify(String Name) throws JSONException, FaceppParseException {
        JSONObject syncRet = null;
        syncRet = httpRequests.trainVerify(new PostParameters().setPersonName(Name));
        System.out.println(httpRequests.getSessionSync(syncRet.get("session_id").toString()));
    }

    static String recognitionRecognize(String GName, BufferedImage bufferedImage) throws FaceppParseException, IOException, JSONException {
        ImageIO.write(bufferedImage, "jpg", new File("image.jpg"));
        JSONObject res = httpRequests.recognitionIdentify(new PostParameters().setGroupName(GName).setImg(new File("image.jpg")));
//        System.out.println((String)res.get("person_name"));
//        System.out.println(res);
        JSONObject data = res.getJSONArray("face").getJSONObject(0);
//        System.out.println(data.getString("person_name"));
        System.out.println(res);
        String MatchName = data.getJSONArray("candidate").getJSONObject(0).getString("person_name");
        Double MatchConfidence = data.getJSONArray("candidate").getJSONObject(0).getDouble("confidence");
//        System.out.println(MatchName);
//        System.out.println(MatchConfidence);
        if (MatchConfidence > 20.0)
            return MatchName;
        return "No Match Found";
    }

    static Boolean recognitionVerify(String Name) throws JSONException, FaceppParseException {
        JSONObject res = httpRequests.recognitionVerify(new PostParameters().setPersonName(Name).setFaceId(
                result.getJSONArray("face").getJSONObject(0).getString("face_id")));
        Boolean isSame = res.getBoolean("is_same_person");
        Double Confidence = res.getDouble("confidence");
        System.out.println(Confidence);
        return isSame;
    }

    static void removeFace(String Name) throws JSONException, FaceppParseException {
        System.out.println(httpRequests.personRemoveFace(
                new PostParameters().setPersonName(Name).setFaceId(
                        result.getJSONArray("face").getJSONObject(0).getString("face_id"))));
    }

    static void deletePerson(String Name) throws FaceppParseException {
        System.out.println(httpRequests.personDelete(new PostParameters().setPersonName(Name)));
    }

    static void deleteGroup(String GName) throws FaceppParseException {
        System.out.println(httpRequests.groupDelete(new PostParameters().setGroupName(GName)));
    }

}
