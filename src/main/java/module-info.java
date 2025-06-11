module ru.vsu.cs.dplatov.vvp.task6.task8 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens ru.vsu.cs.dplatov.vvp.task8 to javafx.fxml;
    exports ru.vsu.cs.dplatov.vvp.task8;
}