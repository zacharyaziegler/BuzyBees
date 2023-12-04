package csc.buzybees;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;

public class LoginPage {

    @FXML
    private Button btnLogin;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtEmail;

    private Firestore firestore;
    private FirestoreContext firestoreContext;
    private FirebaseAuth auth;

    public LoginPage() {
        firestoreContext = new FirestoreContext();
        firestore = firestoreContext.firebase(); // This will get the Firestore instance
    }

    @FXML
    void loginButton(ActionEvent event) throws IOException {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        // Validate the fields are not empty
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Email and password cannot be empty.");
            return;
        }

        // Firebase REST API URL for authentication
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyBXMtMeMr9BZShTYV-dHF4Yyca-52q7Qds";
        // Attempt to login
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            String urlParameters = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"returnSecureToken\":true}";

            // Send POST request
            con.setDoOutput(true);
            con.getOutputStream().write(urlParameters.getBytes(StandardCharsets.UTF_8));

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("HTTP_OK Accepted");
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response to JSON
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Get the ID token and localID
                String idToken = jsonResponse.getString("idToken");
                String localId = jsonResponse.getString("localId"); // This is the user's Firebase ID
                // Set the ID token and user ID in the SessionManager
                SessionManager.getInstance().setIdToken(idToken);
                SessionManager.getInstance().setUserId(localId);

                App.setRoot("home");

                // Store ID token HERE to manage session (Zach will implement this)
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Log or print the error response
                System.out.println("Error response: " + response.toString());

                // Parse the response to JSON for more specific error handling (optional)
                JSONObject jsonResponse = new JSONObject(response.toString());
                String errorMessage = jsonResponse.getJSONObject("error").getString("message");
                System.out.println("Error message: " + errorMessage);

                showAlert("Login Failed", "Error: " + errorMessage); // debugging purposes
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Login Error", "An error occurred while trying to log in.");
        }

//        String username = txtUsername.getText();
//        String password = txtPassword.getText();
//
//        // Validate the fields are not empty
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert("Login Failed", "Username and password cannot be empty.");
//            return;
//        }
//        // Attempt to login
//        ApiFuture<QuerySnapshot> future = firestore.collection("users").whereEqualTo("username", username).get();
//
//        try {
//            QuerySnapshot querySnapshot = future.get(); // This line blocks and waits for the query to complete.
//            if (!querySnapshot.isEmpty()) {
//                // User found, now check the password
//                String storedPassword = querySnapshot.getDocuments().get(0).getString("password");
//                if (password.equals(storedPassword)) {
//                    // Password matches, login successful
//                    App.setRoot("home");
//                } else {
//                    // Password does not match
//                    showAlert("Login Failed", "Invalid username or password.");
//                }
//            } else {
//                // User not found
//                showAlert("Login Failed", "Invalid username or password.");
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            // Handle exceptions
//            showAlert("Login Error", "An error occurred while trying to log in.");
//        }
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
