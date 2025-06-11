module ru.vsu.cs.dplatov.vvp.task {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens ru.vsu.cs.dplatov.vvp.task8 to javafx.fxml;
    exports ru.vsu.cs.dplatov.vvp.task8;
    exports ru.vsu.cs.dplatov.vvp.task8.logic;
}