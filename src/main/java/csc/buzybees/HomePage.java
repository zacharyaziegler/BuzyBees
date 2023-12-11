package csc.buzybees;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class HomePage {

    @FXML
    private Label userName;
    @FXML
    private Button homeButton1;
    @FXML
    private Button myInfoButton1;
    @FXML
    private Button tasksButton1;
    @FXML
    private Button scheduleButton1;
    @FXML
    private Button scheduleButton11;
    @FXML
    private Button leaveButton2;
    @FXML
    private Button leaveButton1;
   
    private boolean isUserManager;
    
    public void initialize() {
        loadUserFirstName();
    }

      private void loadUserFirstName() {
    String userId = SessionManager.getInstance().getUserId();
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<DocumentSnapshot> future = db.collection("users").document(userId).get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                String firstName = document.getString("firstName");
                if (document.contains("manager") && document.getBoolean("manager") != null) {
                    isUserManager = document.getBoolean("manager");
                } else {
                    isUserManager = false; // default to false if field doesn't exist or is not a boolean
                }
                javafx.application.Platform.runLater(() -> userName.setText(firstName + "!"));
            } else {
                // Handle case where the user doesn't exist
            }
            return null;
        }
    };
    new Thread(task).start();
}
       

    @FXML
    private void switchToTasks(ActionEvent event) throws IOException {
        if (isUserManager) {
            App.setRoot("tasks");
        } else {
            showAlertAccessDenied();
        }
    }    
        
    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void switchToLeaveRequestPage(ActionEvent event) throws IOException {
        App.setRoot("leave");
    }

    private void switchToTest(ActionEvent event) throws IOException {
        App.setRoot("test");
    }

    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }
    
    @FXML
    private void switchToSchedule(ActionEvent event) throws IOException {
        App.setRoot("ScheduleView");
    }
    
    @FXML
    private void switchToCreateSchedule(ActionEvent event) throws IOException {
        if (isUserManager) {
            App.setRoot("create_schedule");
        } else {
            showAlertAccessDenied();
        }
    }
    
    private void showAlertAccessDenied() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setHeaderText(null);
        alert.setContentText("You do not have permission to access this page.");
        alert.showAndWait();
    }
    
    @FXML
    private void switchToMyInfo() throws IOException {
        App.setRoot("myInfo");
    }
}