package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import model.Task;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddTask {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtTitle;

    private MainPage_Controller controller;

    public void setMainPageController(MainPage_Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void btnConformOnAction(ActionEvent actionEvent) {
        Task task = new Task(txtTitle.getText(), txtArea.getText(), datePicker.getValue());

        try {
            String SQL = "INSERT INTO active_task (task_title, task_description, created_date) VALUES (?, ?, ?)";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task", "root", "12345");
            PreparedStatement psTm = connection.prepareStatement(SQL);

            psTm.setString(1, task.getTitle());
            psTm.setString(2, task.getDescription());
            psTm.setDate(3, java.sql.Date.valueOf(task.getCompleteDate()));

            int i = psTm.executeUpdate();

            if (i > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Task Added Successfully!").show();

                if (controller != null) {
                    controller.loadTasks();
                }
            }
            clearText();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearText(){
        txtTitle.setText(null);
        txtArea.setText(null);
        datePicker.setValue(null);
    }

    public void btnMainPageOnAction(MouseEvent mouseEvent) {
    }
}
