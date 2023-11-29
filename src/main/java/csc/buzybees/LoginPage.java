package csc.buzybees;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPage {

    @FXML
    private Button btnLogin;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;
    
    private Firestore firestore;
    private FirestoreContext firestoreContext;
    
    public LoginPage() {
        firestoreContext = new FirestoreContext();
        firestore = firestoreContext.firebase(); // This will get the Firestore instance
    }

    @FXML
    void loginButton(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // Validate the fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Username and password cannot be empty.");
            return;
        }
        // Attempt to login
        ApiFuture<QuerySnapshot> future = firestore.collection("users").whereEqualTo("username", username).get();

        try {
            QuerySnapshot querySnapshot = future.get(); // This line blocks and waits for the query to complete.
            if (!querySnapshot.isEmpty()) {
                // User found, now check the password
                String storedPassword = querySnapshot.getDocuments().get(0).getString("password");
                if (password.equals(storedPassword)) {
                    // Password matches, login successful
                    App.setRoot("home");
                } else {
                    // Password does not match
                    showAlert("Login Failed", "Invalid username or password.");
                }
            } else {
                // User not found
                showAlert("Login Failed", "Invalid username or password.");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Handle exceptions
            showAlert("Login Error", "An error occurred while trying to log in.");
        }

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void switchToHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }

    @FXML
    void switchToRegister(ActionEvent event) throws IOException {
        App.setRoot("register");
    }

}
