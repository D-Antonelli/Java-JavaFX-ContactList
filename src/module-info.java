module SimpleContactApplication {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml;

    opens controller;
    opens userinterface;
    opens datamodel;
    opens main;
}