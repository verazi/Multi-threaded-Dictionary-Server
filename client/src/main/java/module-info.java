module com.edu {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.edu to javafx.fxml;
    exports com.edu;
}
