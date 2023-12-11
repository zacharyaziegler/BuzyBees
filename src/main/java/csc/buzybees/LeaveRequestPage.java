package csc.buzybees;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 *
 * @author jonb
 */
public class LeaveRequestPage {

    @FXML
    private ComboBox<String> leaveTypeComboBox;
    private Label swapLabel;
    @FXML
    private ComboBox<String> employeeComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea reasonField;
    @FXML
    private Label employeeText;
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

    public void initialize() {
        populateEmployeeComboBox();
        // Add a change listener to the leaveTypeComboBox
        leaveTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Shift Swap".equals(newValue)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Choose employee to swap");
                alert.showAndWait();
                employeeComboBox.setVisible(true); // Show the employeeComboBox
                employeeText.setVisible(true);
            } else {
                employeeComboBox.setVisible(false); // Hide the employeeComboBox
                employeeText.setVisible(false);
            }
        });
    }
    
    private void populateEmployeeComboBox() {
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            Firestore db = FirestoreClient.getFirestore();
            // Assume "users" is the name of the collection where user documents are stored
            ApiFuture<QuerySnapshot> future = db.collection("users").get();
            // Block and get the query results
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if (document.exists()) {
                    // Assuming the field that contains the name is "firstName"
                    String name = document.getString("firstName");
                    javafx.application.Platform.runLater(() -> employeeComboBox.getItems().add(name));
                }
            }
            return null;
        }
    };
    new Thread(task).start();
}

    @FXML
    private void handleSubmit(ActionEvent event) {
    }

    private void handleLeaveTypeChange() {
        boolean isShiftSwap = "Shift Swap".equals(leaveTypeComboBox.getValue());
        employeeComboBox.setVisible(isShiftSwap);
        swapLabel.setVisible(isShiftSwap);

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
    private void switchToLeaveRequestPage(ActionEvent event) throws IOException {
        App.setRoot("leave");
    }
    
    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }
    
    private void switchToLeave() throws IOException {
        App.setRoot("leave");
    }
    
    @FXML
    private void switchToCreateSchedule(ActionEvent event) throws IOException {
        App.setRoot("create_schedule");
    }
}

