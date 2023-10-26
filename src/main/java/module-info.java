module csc.buzybees {
    requires javafx.controls;
    requires javafx.fxml;

    opens csc.buzybees to javafx.fxml;
    exports csc.buzybees;
}
