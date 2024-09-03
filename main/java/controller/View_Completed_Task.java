package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Task;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class View_Completed_Task implements Initializable {

    @FXML
    private TableColumn<?, ?> colComDate;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    TableView<Task> tblView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colComDate.setCellValueFactory(new PropertyValueFactory<>("completeDate"));

        loadTable();
    }

    public void loadTable(){
        ObservableList<Task> taskObservableList = FXCollections.observableArrayList();

        tblView.setItems(taskObservableList);

        try {
            String SQL = "SELECT task_title, task_description, created_date FROM complete_task";

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task","root","12345");
            PreparedStatement psTm = connection.prepareStatement(SQL);

            ResultSet resultSet = psTm.executeQuery();

            while (resultSet.next()){
                Task task = new Task(
                        resultSet.getString("task_title"),
                        resultSet.getString("task_description"),
                        resultSet.getDate("created_date").toLocalDate()
                );

                taskObservableList.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void btnReloadOnAction(ActionEvent actionEvent) {
        loadTable();
    }
}
