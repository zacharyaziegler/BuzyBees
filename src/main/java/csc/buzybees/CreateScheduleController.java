/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package csc.buzybees;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import csc.buzybees.Shift;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author zach
 */
public class CreateScheduleController {

    private Firestore firestore;
    private FirestoreContext firestoreContext;
    @FXML
    private ComboBox<String> employeeComboBox;
    // List to hold the pending shifts
    private final ObservableList<Shift> pendingShiftsList = FXCollections.observableArrayList();
    private Map<String, String> employeeNameToIdMap = new HashMap<>();

    @FXML
    private TableView<Shift> pendingShiftsTable;
    @FXML
    private TableColumn<Shift, String> dateColumn;
    @FXML
    private TableColumn<Shift, String> startTimeColumn;
    @FXML
    private TableColumn<Shift, String> endTimeColumn;
    @FXML
    private TableColumn<Shift, String> detailsColumn;
    @FXML
    private ComboBox<String> startHoursComboBox;
    @FXML
    private ComboBox<String> startMinutesComboBox;
    @FXML
    private ComboBox<String> endHoursComboBox;
    @FXML
    private ComboBox<String> endMinutesComboBox;
    @FXML
    private DatePicker shiftDatePicker;
    @FXML
    private TextField detailsTextField;
    @FXML
    private Button submitScheduleBtn;

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<Shift, String>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Shift, String>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Shift, String>("endTime"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<Shift, String>("details"));
        firestoreContext = new FirestoreContext();
        firestore = firestoreContext.firebase(); // This will get the Firestore instance
        // Populate the ComboBox when the UI initializes
        populateEmployeeComboBox();
        initializeHoursAndMinutesComboBox();
    }

    @FXML
    private void submitSchedule() {
        if (employeeComboBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("You MUST select an employee to assign this schedule to!");
            alert.show();
        } else {
            String selectedEmployeeName = employeeComboBox.getValue();
            String selectedEmployeeId = employeeNameToIdMap.get(selectedEmployeeName); // get user ID from map

            // Convert the pendingShiftsList to a List of Maps
            List<Map<String, Object>> shiftsToSave = new ArrayList<>();
            for (Shift shift : pendingShiftsList) {
                Map<String, Object> shiftMap = new HashMap<>();
                shiftMap.put("date", shift.getDate());
                shiftMap.put("startTime", shift.getStartTime());
                shiftMap.put("endTime", shift.getEndTime());
                shiftMap.put("details", shift.getDetails());
                shiftsToSave.add(shiftMap);
            }

            // Save the shifts to Firestore under the subcollection 'shifts' for the selected employee
            for (Map<String, Object> singleShift : shiftsToSave) {
                String shiftId = generateShiftId(); // generates shift ID for firestore based on system time
                firestore.collection("users").document(selectedEmployeeId)
                        .collection("shifts").document(shiftId)
                        .set(singleShift);
            }

            // Clear the pending shifts after saving
            pendingShiftsList.clear();
        }
    }

    private String generateShiftId() {
        return String.valueOf(System.currentTimeMillis());
    }

    @FXML
    private void addShift() {
        if (startHoursComboBox.getSelectionModel().isEmpty() || startMinutesComboBox.getSelectionModel().isEmpty()
                || endHoursComboBox.getSelectionModel().isEmpty() || endMinutesComboBox.getSelectionModel().isEmpty()
                || shiftDatePicker.getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("All fields except for details MUST be filled out");
            alert.show();
        } else {
            LocalDate localDate = shiftDatePicker.getValue();
            String date = localDate.toString();

            String startHour = startHoursComboBox.getSelectionModel().getSelectedItem();
            String startMinute = startMinutesComboBox.getSelectionModel().getSelectedItem();
            String endHour = endHoursComboBox.getSelectionModel().getSelectedItem();
            String endMinute = endMinutesComboBox.getSelectionModel().getSelectedItem();

            String startTime = startHour + ":" + startMinute;
            String endTime = endHour + ":" + endMinute;

            String details = detailsTextField.getText();

            Shift newShift = new Shift(date, startTime, endTime, details);
            pendingShiftsList.add(newShift);
            pendingShiftsTable.setItems(pendingShiftsList);
        }
    }

    // Method to remove the selected shift from the list
    @FXML
    private void removeShift() {
        Shift selectedShift = pendingShiftsTable.getSelectionModel().getSelectedItem();
        if (selectedShift != null) {
            pendingShiftsList.remove(selectedShift);
        }
    }

    private void initializeHoursAndMinutesComboBox() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        startHoursComboBox.setItems(hours);
        endHoursComboBox.setItems(hours);

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i += 5) { // 5-minute intervals
            minutes.add(String.format("%02d", i));
        }
        startMinutesComboBox.setItems(minutes);
        endMinutesComboBox.setItems(minutes);
    }

    public void populateEmployeeComboBox() {
        // Create a background task for fetching user data from Firestore
        Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                ObservableList<String> names = FXCollections.observableArrayList();

                // Query Firestore to get users
                ApiFuture<QuerySnapshot> query = firestore.collection("users").get();
                // Wait for the query to complete and process the result
                QuerySnapshot querySnapshot = query.get();

                querySnapshot.getDocuments().forEach(document -> {
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");
                    String fullName = firstName + " " + lastName;
                    names.add(firstName + " " + lastName);
                    // Populate the map with the full name as key and document ID as value
                    employeeNameToIdMap.put(fullName, document.getId());
                });

                return names;
            }
        };

        // When the task successfully completes, update the ComboBox
        task.setOnSucceeded(e -> employeeComboBox.setItems(task.getValue()));

        // Run the task using a new thread
        new Thread(task).start();
    }
    
    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
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
    private void switchToMyInfo() throws IOException {
        App.setRoot("myInfo");
    }

}
