module com.jrs.examen {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.desktop;
    requires jasperreports;
    requires java.sql;


    opens com.jrs.examen to javafx.fxml;
    exports com.jrs.examen;
    exports com.jrs.examen.controller;
    opens com.jrs.examen.controller to javafx.fxml;
    exports com.jrs.examen.model;
    opens com.jrs.examen.model to javafx.fxml;
}