package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class MainPage_Controller {
    @FXML
    public ListView<CheckBox> listView;
    public ObservableList<CheckBox> ListActive = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        loadTasks();
    }

    public void loadTasks() {
       // ListActive.clear();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/task", "root", "12345");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT task_id, task_title FROM active_task");

            while (rs.next()) {
                String taskTitle = rs.getString("task_title");
                int taskId = rs.getInt("task_id");
                CheckBox checkBox = new CheckBox(taskTitle);

                checkBox.setOnAction(e -> handleTaskCompletion(checkBox, taskId));
                ListActive.add(checkBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setItems(ListActive);
    }

    private void handleTaskCompletion(CheckBox checkBox, int taskId) {
        if (checkBox.isSelected()) {
            new Alert(Alert.AlertType.INFORMATION,"Task Marked as Completed !").show();
            moveTaskToCompletedTasks(taskId, checkBox.getText());
            ListActive.remove(checkBox);
        }
    }

    private void moveTaskToCompletedTasks(int taskId, String taskTitle) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/task", "root", "12345");
            Statement stmt = conn.createStatement();

            String sqlInsert = "INSERT INTO complete_task (task_title, task_description) " +
                    "SELECT task_title, task_description FROM active_task WHERE task_id = " + taskId;
            stmt.executeUpdate(sqlInsert);

            String sqlDelete = "DELETE FROM active_task WHERE task_id = " + taskId;
            stmt.executeUpdate(sqlDelete);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddTaskButtonListener(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/addTask.fxml"));
            Scene scene = new Scene(loader.load());

            AddTask addTask = loader.getController();
            addTask.setMainPageController(this);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnAddCompleteList(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.show();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/View_Completed_Task.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
