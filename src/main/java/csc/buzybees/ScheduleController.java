/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package csc.buzybees;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 *
 * @author zman1
 */
public class ScheduleController extends AnchorPane {

    @FXML
    private AnchorPane scheduleContainer;
    @FXML
    private Button homeButton;
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
    private ImageView profilePic;
    @FXML
    private Button tasksButton;
    @FXML
    private Button scheduleButton;
    @FXML
    private Button leaveButton;

    private YearMonth currentYearMonth = YearMonth.now();
    private GridPane scheduleGrid;
    private Label monthLabel = new Label();

    @FXML
    public void initialize() {

        VBox scheduleLayout = new VBox(10); // 10 is the spacing between elements
        updateMonthLabel(currentYearMonth);
        // Initialize the calendar grid and other components
        scheduleGrid = new GridPane();
        scheduleGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        scheduleGrid.setPrefSize(1100, 400); // Set preferred size as needed
        scheduleGrid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        for (int i = 0; i < 7; i++) { // set contraints on rows and columns of GridPane
            ColumnConstraints column = new ColumnConstraints(150); // Set the preferred width of columns
            RowConstraints row = new RowConstraints(50); // Set the preferred height of rows
            scheduleGrid.getColumnConstraints().add(column);
            scheduleGrid.getRowConstraints().add(row);
        }

        //scheduleGrid.setAlignment(Pos.CENTER); // Center the GridPane within its container
        // scheduleGrid.setPadding(new Insets(10, 10, 10, 10)); // Add padding as needed 
        scheduleGrid.setGridLinesVisible(true);
        scheduleGrid.setVgap(10);
        scheduleGrid.setHgap(10);
        scheduleGrid.setStyle("-fx-border-color: red;");
        updateSchedule();
        scheduleLayout.getChildren().addAll(monthLabel, scheduleGrid);
        AnchorPane.setTopAnchor(scheduleLayout, 100.0); // Adjust this value as needed
        AnchorPane.setLeftAnchor(scheduleLayout, 10.0);
        AnchorPane.setRightAnchor(scheduleLayout, 10.0);
        scheduleContainer.getChildren().add(scheduleLayout); // Add the calendar to the container
        //scheduleLayout.setAlignment(Pos.BOTTOM_CENTER);
    }

    private void updateSchedule() {
        scheduleGrid.getChildren().clear(); // Clear the existing calendar cells

        LocalDate calendarDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), 1);
        // Fill in the calendar grid with day numbers
        int dayOfWeekOfFirst = calendarDate.getDayOfWeek().getValue();
        int daysInMonth = currentYearMonth.lengthOfMonth();

        int column = 0;
        int row = 1;

        // Fill grid with days
        for (int i = 1; i <= daysInMonth; i++) {
            if (i == 1) {
                column = dayOfWeekOfFirst - 1; // Adjust for zero based index
            } else if (column == 6) {
                column = 0;
                row++;
            } else {
                column++;
            }
            scheduleGrid.add(new Label(String.valueOf(i)), column, row);
        }
    }

    private void updateMonthLabel(YearMonth yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(yearMonth.format(formatter));
        monthLabel.setAlignment(Pos.CENTER);
        monthLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    }

    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void switchToLeaveRequestPage(ActionEvent event) throws IOException {
        App.setRoot("leave");
    }

    @FXML
    private void switchToSchedule(ActionEvent event) throws IOException {
        App.setRoot("ScheduleView");
    }

}
