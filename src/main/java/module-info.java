module com.vls.crud_usuarios_java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires java.dotenv;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.vls.crud_usuarios_java to javafx.fxml;
    opens com.vls.crud_usuarios_java.controller to javafx.fxml;
    opens com.vls.crud_usuarios_java.model to javafx.base;

    exports com.vls.crud_usuarios_java;
}