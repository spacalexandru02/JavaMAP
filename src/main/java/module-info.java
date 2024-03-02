module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jakarta.xml.bind;

    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
}