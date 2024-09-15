module org.example.coursework {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;

    opens org.example.coursework to javafx.fxml;
    exports org.example.coursework;
    exports org.example.coursework.repositories;
    opens org.example.coursework.repositories to javafx.fxml;
    exports org.example.coursework.controllers;
    opens org.example.coursework.controllers to javafx.fxml;
    opens org.example.coursework.entities to javafx.base;
}