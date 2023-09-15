module synical.careerplanningapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    opens synical.careerplanningapp to javafx.fxml;
    exports synical.careerplanningapp;
}