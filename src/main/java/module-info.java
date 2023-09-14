module synical.careerplanningapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens synical.careerplanningapp to javafx.fxml;
    exports synical.careerplanningapp;
}