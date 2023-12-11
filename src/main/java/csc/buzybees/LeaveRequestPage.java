package csc.buzybees;

import java.io.IOException;
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
    private ComboBox<?> leaveTypeComboBox;
    private Label swapLabel;
    @FXML
    private ComboBox<?> employeeComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea reasonField;
    @FXML
    private Label employeeText;
    @FXML
    private Button homeButton;
    @FXML
    private Button scheduleButton;
    @FXML
    private Button leaveButton;
    @FXML
    private ImageView logo;
    @FXML
    private Button myInfoButton;
    @FXML
    private Button tasksButton;
    @FXML
    private Button filesButton;
    @FXML
    private Button payrollButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button settingsButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private ImageView profilePicture;

    public void initialize() {
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
    
    @FXML
    private void switchToLeave() throws IOException {
        App.setRoot("leave");
    }
    
    @FXML
    private void switchToCreateSchedule(ActionEvent event) throws IOException {
        App.setRoot("create_schedule");
    }
    
    @FXML
    private void switchToMyInfo() throws IOException {
        App.setRoot("myInfo");
    }
}

