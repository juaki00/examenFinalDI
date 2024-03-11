module com.jrs.examen {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jrs.examen to javafx.fxml;
    exports com.jrs.examen;
}