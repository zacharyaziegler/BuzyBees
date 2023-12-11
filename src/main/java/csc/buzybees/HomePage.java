package csc.buzybees;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class HomePage {

    @FXML
    private Button homeButton;
    @FXML
    private Button testBtn;
    @FXML
    private ImageView logo;
    @FXML
    private Button myInfoButton;
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
    @FXML
    private ImageView profilePic;
    @FXML
    private Button tasksButton;
    @FXML
    private Button scheduleButton;
    @FXML
    private Button leaveButton;

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void switchToLeaveRequestPage(ActionEvent event) throws IOException {
        App.setRoot("leave");
    }

    @FXML
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
    
    @FXML
    private void switchToMyInfo() throws IOException {
        App.setRoot("myInfo");
    }
    
}