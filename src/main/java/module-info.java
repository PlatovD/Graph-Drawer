module ru.vsu.cs.dplatov.vvp.task {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens ru.vsu.cs.dplatov.vvp.task8 to javafx.fxml;
    exports ru.vsu.cs.dplatov.vvp.task8.logic;
    exports ru.vsu.cs.dplatov.vvp.task8.graphic.utils;
    exports ru.vsu.cs.dplatov.vvp.task8.graphic.elements;
    exports ru.vsu.cs.dplatov.vvp.task8;
}