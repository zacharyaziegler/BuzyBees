package csc.buzybees;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author jonb
 */
public class TasksController {

    @FXML
    private TextArea announcementArea;

    @FXML
    private ComboBox<String> employeeSelector; // Assuming the use of String for employee names

    @FXML
    private TextArea taskArea;
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

    @FXML
    private void sendAnnouncement() {
        String announcement = announcementArea.getText();
        // Logic to send announcement to all employees
    }

    @FXML
    private void assignTask() {
        String selectedEmployee = employeeSelector.getValue();
        String task = taskArea.getText();
        // Logic to assign the task to the selected employee
    }

    public void initialize() {
        // Initialize employeeSelector with employee names
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
        App.setRoot("create_schedule");

    }
}
