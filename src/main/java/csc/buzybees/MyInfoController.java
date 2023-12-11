package csc.buzybees;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author catherinearevalo
 */
public class MyInfoController {
    
    @FXML
    private Label labelAddr;

    @FXML
    private Label labelCity;

    @FXML
    private Label labelDOB;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelPosition;

    @FXML
    private Label labelState;

    @FXML
    private Label labelZip;
    
    @FXML
    private Label labelBigName;
    
     private Firestore firestore;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
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

    // This method will be called by the FXMLLoader when initialization is complete
    public void initialize() {
        loadUserFirstName();
        // Set labels to be invisible initially
        setVisibleLabels(false);
    }

    @FXML
    private void onViewInformationClicked() {
        loadUserData();
    }

    private void loadUserFirstName() {
        String userId = SessionManager.getInstance().getUserId(); // Retrieve the user ID from SessionManager
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Firestore db = FirestoreClient.getFirestore();
                ApiFuture<DocumentSnapshot> future = db.collection("users").document(userId).get();
                DocumentSnapshot document = future.get();
                if (document.exists()) {
                    String fullName = document.getString("firstName") + " " + document.getString("lastName");
                    javafx.application.Platform.runLater(() -> labelBigName.setText(fullName));

                } else {
                    // Handle case where the user doesn't exist
                }
                return null;
            }
        };
        new Thread(task).start();
    }
    private void loadUserData() {
        String userId = SessionManager.getInstance().getUserId(); // Retrieve the user ID from SessionManager
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Firestore db = FirestoreClient.getFirestore();
                DocumentSnapshot document = db.collection("users").document(userId).get().get();
                if (document.exists()) {
                    // Extract data from the document
                    String fullName = document.getString("firstName") + " " + document.getString("lastName");
                    String address = document.getString("streetAddress");
                    String city = document.getString("city");
                    String state = document.getString("state");
                    String zipCode = document.getString("zipCode");
                    String dateOfBirth = document.getString("dateOfBirth");
                    String jobPosition = document.getString("jobPosition");  
                    String phoneNumber = document.getString("phoneNumber");

                    // Update labels on the JavaFX application thread
                    javafx.application.Platform.runLater(() -> {
                        labelName.setText(fullName);
                        labelAddr.setText(address);
                        labelCity.setText(city);
                        labelState.setText(state);
                        labelZip.setText(zipCode);
                        labelDOB.setText(dateOfBirth);
                        labelPosition.setText(jobPosition);
                        labelPhone.setText(phoneNumber);

                        // Make the labels visible after updating
                        setVisibleLabels(true);
                    });
                } else {
                    // Handle case where the user doesn't exist
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void setVisibleLabels(boolean visible) {
        labelAddr.setVisible(visible);
        labelCity.setVisible(visible);
        labelDOB.setVisible(visible);
        labelName.setVisible(visible);
        labelPhone.setVisible(visible);
        labelPosition.setVisible(visible);
        labelState.setVisible(visible);
        labelZip.setVisible(visible);
    }

    private String getCurrentUserId() {
        // This method should return the ID of the currently logged-in user
        // For now, it's just a placeholder. Replace this with your actual logic.
        return "user-id-from-authentication";
    }
    
    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }
    
    @FXML
    private void switchToMyInfo() throws IOException {
        App.setRoot("myInfo");
    }
    
    @FXML
    private void switchToViewSchedule(ActionEvent event) throws IOException {
        App.setRoot("ScheduleView");
    }
    
    @FXML
    private void switchToCreateSchedule(ActionEvent event) throws IOException {
        App.setRoot("create_schedule");
    }

    @FXML
    private void switchToLeaveRequestPage(ActionEvent event) throws IOException {
        App.setRoot("leave");
    }
    
    @FXML
    private void Logout() throws IOException {
        App.setRoot("login");
    }
    
    @FXML
    private void switchToTasks(ActionEvent event) throws IOException {
            App.setRoot("tasks");
      
        }
}

