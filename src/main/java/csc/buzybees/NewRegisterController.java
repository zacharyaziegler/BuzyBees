/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package csc.buzybees;
//trying to commit lol
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author catherinearevalo
 */
public class NewRegisterController {
    
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtDob;
    @FXML
    private TextField txtJobPosition;
    @FXML
    private TextField txtStreetAddr;
    @FXML
    private TextField txtState;
    @FXML
    private TextField txtZipCode;
    @FXML
    private TextField txtPhoneNum;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button registerButton;
    
    private Firestore firestore;
    private FirestoreContext firestoreContext;

    public NewRegisterController() {
        firestoreContext = new FirestoreContext();
        firestore = firestoreContext.firebase(); // This will get the Firestore instance
    }

    
    @FXML
    void switchToNewMember(ActionEvent event) throws IOException {
        // Check if any field is empty
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty()
                || txtDob.getText().isEmpty() || txtJobPosition.getText().isEmpty()
                || txtStreetAddr.getText().isEmpty() || txtCity.getText().isEmpty()
                || txtState.getText().isEmpty() || txtZipCode.getText().isEmpty()
                || txtPhoneNum.getText().isEmpty() || txtEmail.getText().isEmpty()
                || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {

            // Show alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText(null);
            alert.setContentText("Please fill out all fields.");
            alert.showAndWait();
            return; // Stop further execution
        }
        // Create a new User object with the data from the text fields
        User newUser = new User(
            txtFirstName.getText(),
            txtLastName.getText(),
            txtDob.getText(),
            txtJobPosition.getText(),
            txtStreetAddr.getText(),
            txtCity.getText(),
            txtState.getText(),
            txtZipCode.getText(),
            txtPhoneNum.getText(),
            txtEmail.getText(),
            txtUsername.getText(),
            txtPassword.getText()
        );

        
     // Serialize newUser to a Map to store in Firestore
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", newUser.getFirstName());
        userMap.put("lastName", newUser.getLastName());
        userMap.put("dateOfBirth", newUser.getDateOfBirth());
        userMap.put("jobPosition", newUser.getJobPosition());
        userMap.put("streetAddress", newUser.getStreetAddress());
        userMap.put("city", newUser.getCity());
        userMap.put("state", newUser.getState());
        userMap.put("zipCode", newUser.getZipCode());
        userMap.put("phoneNumber", newUser.getPhoneNumber());
        userMap.put("email", newUser.getEmail());
        userMap.put("username", newUser.getUsername());
        userMap.put("password", newUser.getPassword()); // Make sure to hash the password before this step

        // Now, use Firestore to store the newUser object
        ApiFuture<WriteResult> writeResult = firestore.collection("users").document(newUser.getUsername()).set(userMap);

        // Handle the result of the write operation
        try {
            // Block and wait for the Firestore operation to complete and log the result
            WriteResult result = writeResult.get();
            System.out.println("User registration successful. Write result: " + result);
            App.setRoot("newMember");
            // Here you can call switchToHome() or any other method to proceed
        } catch (InterruptedException | ExecutionException e) {
            // This will be called if there is an error during the write
            e.printStackTrace();
            // Handle the exception, such as providing user feedback
        }

    }
}
