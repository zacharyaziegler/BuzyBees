/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package csc.buzybees;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
 * @author zach
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
    @FXML
    private Button createScheduleBtn;

    private YearMonth currentYearMonth = YearMonth.now();
//    private YearMonth currentYearMonth = YearMonth.of(2024, Month.DECEMBER); // debugging purposes
    private GridPane scheduleGrid;
    private Label monthLabel = new Label();
    private Firestore firestore;
    private FirestoreContext firestoreContext;
    private FirebaseAuth auth;

    
    @FXML
    public void initialize() {
        String[] dayNames = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"}; // day labels on top of schedule grid
        GridPane dayLabels = new GridPane();
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints(150); // The width should match the scheduleGrid's column width
            dayLabels.getColumnConstraints().add(column);
        }
        for (int i = 0; i < dayNames.length; i++) {
            Label lblDay = new Label(dayNames[i]);
            lblDay.setAlignment(Pos.BOTTOM_LEFT);
            lblDay.setMaxWidth(Double.MAX_VALUE); // Allows the label to grow to fill the cell
            GridPane.setHalignment(lblDay, HPos.CENTER); // Center align the day labels horizontally
            GridPane.setMargin(lblDay, new Insets(0, 0, 0, 0));
            // Set the label to fill the entire cell for consistent alignment
            lblDay.setStyle("-fx-font-weight: bold; -fx-text-alignment: center;");
            dayLabels.add(lblDay, i, 0); // Add the label to the corresponding column
        }
        dayLabels.setGridLinesVisible(true);
        dayLabels.setVgap(0);
        dayLabels.setHgap(0);

        VBox scheduleLayout = new VBox(); // no spacing between elements in order to create grid lines
        updateMonthLabel(currentYearMonth);

        // Initialize the calendar grid and other components
        scheduleGrid = new GridPane();

        for (int i = 0; i < 7; i++) { // set contraints on rows and columns of GridPane
            ColumnConstraints column = new ColumnConstraints(150); // Set the width of columns
            RowConstraints row = new RowConstraints(75); // Set the height of rows
            scheduleGrid.getColumnConstraints().add(column);
            scheduleGrid.getRowConstraints().add(row);
        }

        scheduleGrid.setVgap(0);
        scheduleGrid.setHgap(0);
        dayLabels.setPadding(new Insets(0, 0, 0, 0));
        scheduleGrid.setPadding(new Insets(0, 0, 0, 0));
//        scheduleGrid.setStyle("-fx-border-color: red;"); // debugging purposes
        updateSchedule();
        scheduleLayout.getChildren().addAll(monthLabel, dayLabels, scheduleGrid);
        AnchorPane.setTopAnchor(scheduleLayout, 100.0);
        AnchorPane.setLeftAnchor(scheduleLayout, 10.0);
        AnchorPane.setRightAnchor(scheduleLayout, 10.0);
        AnchorPane.setBottomAnchor(scheduleLayout, 0.0);
        scheduleContainer.getChildren().add(scheduleLayout); // Add the calendar to the container
        // Get the current user's ID from the SessionManager
        String currentUserId = SessionManager.getInstance().getUserId();
        if (currentUserId != null && !currentUserId.isEmpty()) {
            fetchAndDisplayUserSchedule(currentUserId);
        } else {
            // Handle the case where there is no logged-in user
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No user is currently logged in");
            alert.show();
        }
    }

    private void fetchAndDisplayUserSchedule(String userId) {
        firestoreContext = new FirestoreContext();
        firestore = firestoreContext.firebase(); // This will get the Firestore instance
        // Use Firestore to get the user's shifts
        ApiFuture<QuerySnapshot> future
                = firestore.collection("users").document(userId).collection("shifts").get();

        // Add a listener to the future to handle the result asynchronously
        future.addListener(() -> {
            try {
                // Wait for the future to complete and get the result
                QuerySnapshot querySnapshot = future.get();
                // Process the query results
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    String date = document.getString("date");
                    String startTime = document.getString("startTime");
                    String endTime = document.getString("endTime");
                    String details = document.getString("details");

                    // UI updates should be run on the JavaFX application thread
                    Platform.runLater(() -> {
                        LocalDate shiftDate = LocalDate.parse(date);
                        Node cell = findCellForDate(shiftDate);
                        if (cell != null) {
                            displayShiftInCell(cell, startTime, endTime, details);
                        } else {
                            System.out.println("Cant find Cell");
                        }
                    });
                }
            } catch (InterruptedException | ExecutionException e) {
                // Handle exceptions (remember to switch back to the JavaFX thread if updating UI)
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Failed to load the schedule.");
                    alert.show();
                });
            }
        }, Executors.newSingleThreadExecutor());
    }

    private Node findCellForDate(LocalDate date) {
        if (!YearMonth.from(date).equals(currentYearMonth)) {
        return null; // If the date is not in the current month and year being displayed, return null.
    }

    for (Node node : scheduleGrid.getChildren()) {
        if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
            if (node instanceof Label) {
                Label lbl = (Label) node;
                // Assuming the Label's text is just a number representing the day.
                if (lbl.getText() != null && !lbl.getText().isEmpty()) {
                    int day = Integer.parseInt(lbl.getText());
                    int lblRow = GridPane.getRowIndex(node);
                    int lblColumn = GridPane.getColumnIndex(node);

                    // Calculate the date this cell represents
                    LocalDate cellDate = currentYearMonth.atDay(day);
                    // Now we check if the cell date matches the date we're looking for
                    if (cellDate.equals(date)) {
                        return node;
                    }
                }
            }
        }
    }
    return null; // No matching cell found
    }

    private void displayShiftInCell(Node cell, String startTime, String endTime, String details) {
    if (cell instanceof Label) {
        VBox shiftContainer = (VBox) cell.lookup("#shiftContainer");
        if (shiftContainer == null) {
            shiftContainer = new VBox(5); // Container for shift details with spacing of 5
            shiftContainer.setPadding(new Insets(5)); // Padding inside the container
            shiftContainer.setId("shiftContainer");

            // Existing text in the cell, if any
            String currentText = ((Label) cell).getText();
            Label dateLabel = new Label(currentText); // Label for the date
            dateLabel.setStyle("-fx-font-weight: bold;");

            shiftContainer.getChildren().add(dateLabel);
            ((Label) cell).setText(""); // Clear the date text, since it was added to the container

            // Place the container inside the cell
            ((Label) cell).setGraphic(shiftContainer);
        }

        // Label for the shift time and details
        Label shiftDetailsLabel = new Label(startTime + " - " + endTime + "\n" + details);
        shiftContainer.getChildren().add(shiftDetailsLabel);
    }
}

    private void updateSchedule() {
        scheduleGrid.getChildren().clear(); // Clear the existing calendar cells

        LocalDate calendarDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), 1);
        int dayOfWeekOfFirst = calendarDate.getDayOfWeek().getValue();
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Subtract 1 from dayOfWeekOfFirst so that Monday (1) becomes 0, Tuesday (2) becomes 1, ...
        int column = dayOfWeekOfFirst - 1;

        // Add empty labels for days before the first day of the month 
        for (int i = 0; i < column; i++) {
            Label emptyLabel = new Label("");
            emptyLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            emptyLabel.setStyle("-fx-border-width: 0 0 1 1; -fx-border-color: black;");
            scheduleGrid.add(emptyLabel, i, 0); // Add empty labels to the first row
        }

        // Add labels for the days of the month starting from the correct column
        for (int day = 1; day <= daysInMonth; day++) {
            Label dayOfMonth = new Label(String.valueOf(day));
            dayOfMonth.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayOfMonth.setAlignment(Pos.TOP_LEFT);
            dayOfMonth.setStyle("-fx-border-width: 0 0 1 1; -fx-border-color: black; -fx-font-weight: bold;");

            // Add day label to the correct cell, wrapping to next line if end of the week is reached
            scheduleGrid.add(dayOfMonth, column % 7, column / 7);
            column++; // Move to the next cell
        }

        // Adds empty nodes after days are completed to simulate full calendar
        while (column % 7 != 0) {
            Label emptyLabel = new Label("");
            emptyLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            emptyLabel.setStyle("-fx-border-width: 0 0 1 1; -fx-border-color: black;");
            scheduleGrid.add(emptyLabel, column % 7, column / 7);
            column++;
        }
        // cleans up unclosed nodes
        int lastDateRowIndex = getLastDateRowIndex(scheduleGrid); // prevents null pointer exception

        if (lastDateRowIndex == 4) {
            getNodeFromGridPane(scheduleGrid, 6, 4).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 3).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 2).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 1).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 0).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
        } else {
            getNodeFromGridPane(scheduleGrid, 6, 5).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 4).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 3).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 2).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 1).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
            getNodeFromGridPane(scheduleGrid, 6, 0).setStyle("-fx-font-weight: bold; -fx-border-width: 0 1 1 1; -fx-border-color: black;");
        }
    }

    private int getLastDateRowIndex(GridPane gridPane) {
        // Assuming that the dates are Label nodes, we traverse the list in reverse to find the last Label added
        for (int i = gridPane.getChildren().size() - 1; i >= 0; i--) {
            Node node = gridPane.getChildren().get(i);
            // Check if the node is a Label and if it contains a numeric value which represents a date
            if (node instanceof Label && ((Label) node).getText().matches("\\d+")) {
                // If it is a date, return the row index
                Integer rowIndex = GridPane.getRowIndex(node);
                return rowIndex != null ? rowIndex : 0; // Return 0 if the row index is null
            }
        }
        // Return -1 if no date node is found
        return -1;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
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

    @FXML
    private void switchToCreateSchedule(ActionEvent event) throws IOException {
        App.setRoot("create_schedule");
    }
    
    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

}
