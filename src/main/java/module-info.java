module com.hdr.demo.demojavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hdr.demo.demojavafx to javafx.fxml;
    exports com.hdr.demo.demojavafx;
}