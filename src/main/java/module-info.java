module com.hdr.demo.demojavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;

    opens com.hdr.demo.javafx to javafx.fxml;
    exports com.hdr.demo.javafx;
}