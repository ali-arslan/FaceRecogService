package sample;

import java.net.URL;
import java.util.ResourceBundle;

import com.facepp.error.FaceppParseException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.json.JSONException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView camviewer;

    @FXML
    private TextField userid_field;

    @FXML
    private Button startbutton;

    @FXML
    private Button detect_button;

    @FXML
    private TextField label_field;

    @FXML
    private Button register_button;

    @FXML
    private Text detected_age;

    @FXML
    private Text deteced_name;

    @FXML
    private Text detected_race;

    @FXML
    private Text detected_status;

    @FXML
    private Text detected_sex;

    @FXML
    private Pane detected_info_pane;

    @FXML
    private Rectangle information_container_rectangle_for_style;

    @FXML
    private CheckBox precision_mode;

    @FXML
    void onRegisterButton(ActionEvent event) throws JSONException, FaceppParseException, IOException {
        final String userid = userid_field.getText();
        final String label = label_field.getText();
//        BufferedImage img = getCurrentFrame();


        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            public void run() {
                try {
                    FaceRecognition.registerFace(userid, label);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
    }

    @FXML
    void on_detect_button(ActionEvent event) throws JSONException, FaceppParseException, IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(8000);
//                        while (true)
                    FaceRecognition.detectFace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    private void handleStartButton(ActionEvent event) {
        if (WebcamStreamControl) {
            stopWebcamStream();
            startbutton.setText("Start");
        } else {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    startWebcamStream();
                }
            });
            t.start();
            startbutton.setText("Stop");
        }
    }



    @FXML
    void initialize() {
        assert camviewer != null : "fx:id=\"camviewer\" was not injected: check your FXML file 'sample.fxml'.";
        assert startbutton != null : "fx:id=\"startbutton\" was not injected: check your FXML file 'sample.fxml'.";
        camviewer.setImage(new Image("Untitled.jpg"));
        updateDetectedInfo = false;
        WebcamStreamControl = false;
        precisionMode = false;
    }

    static boolean WebcamStreamControl;

    static BufferedImage CurrFrame;

    void startWebcamStream() {
        WebcamStreamControl = true;
        BufferedImage bf = null;
        while (WebcamStreamControl) {
            try {
                bf = WebcamHelper.getImage();
                CurrFrame = bf;
            } catch (IOException e) {
                System.err.println("Error: Failed to fetch frame from webcam");
            }
            WritableImage wr = null;
            if (bf != null) {
                wr = new WritableImage(bf.getWidth(), bf.getHeight());
                PixelWriter pw = wr.getPixelWriter();
                for (int x = 0; x < bf.getWidth(); x++) {
                    for (int y = 0; y < bf.getHeight(); y++) pw.setArgb(x, y, bf.getRGB(x, y));
                }
            }
            final ImageView imageView = new ImageView(wr);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    camviewer.setImage(imageView.getImage());
                    setDetectedInformation();
                }
            });
            if (precision_mode.isSelected()) {
                precisionMode = true;
            }
        }
    }

    void stopWebcamStream() {
        WebcamStreamControl = false;
        camviewer.setImage(new Image("Untitled.jpg"));
    }

    static BufferedImage getCurrentFrame() {
        if (WebcamStreamControl)
            return CurrFrame;
        System.out.println("Webcam capture is off; press start before method call");
        return null;
    }
    static String detectedName;
    static String detectedAge;
    static String detectedSex;
    static String detectedStatus;
    static String detectedRace;
    static boolean updateDetectedInfo;

    static boolean precisionMode;

    void setDetectedInformation() {
        if (updateDetectedInfo) {
            deteced_name.setText(detectedName);
            detected_age.setText(detectedAge);
            detected_status.setText(detectedStatus);
            detected_sex.setText(detectedSex);
            detected_race.setText(detectedRace);
            updateDetectedInfo = false;
        }
    }


}
