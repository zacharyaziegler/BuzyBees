module csc.buzybees {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens csc.buzybees to javafx.fxml;
    exports csc.buzybees;
}
