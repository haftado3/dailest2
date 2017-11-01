package Dailiest;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddEventController implements Initializable {
    private LocalTime time, timeEnd;
    private String name, description;
    private LocalDate date;
    @FXML
    private Pane addPane;

    @FXML
    private JFXTextField new_event_name;

    @FXML
    private JFXComboBox<String> new_event_occurrence;

    @FXML
    private JFXTimePicker new_event_start;

    @FXML
    private JFXTimePicker new_event_end;

    @FXML
    private JFXDatePicker new_event_date;

    @FXML
    private JFXButton new_event_add;

    @FXML
    private JFXTextField new_event_describe;

    @FXML
    private JFXCheckBox new_real_time;

    @FXML
    private JFXCheckBox new_no_des;

    @FXML
    private JFXButton new_event_cancel;

    @Override // This method is called by the FXMLLoader when initialization is complete--------------------------------
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> myOccurrences = FXCollections.observableArrayList("daily", "every other day", "weekly", "monthly", "once a year");
        new_event_occurrence.getItems().addAll(myOccurrences);
        new_event_occurrence.setValue(myOccurrences.get(0));
        lockOthers();
        new_event_name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue, "")) {
                lockOthers();
            } else {
                unlockOthers();
                name = new_event_name.getText();
            }
        });
        new_event_start.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                new_event_add.setDisable(true);
            } else {
                new_event_add.setDisable(false);
                time = new_event_start.getValue();
            }
        });
        new_event_end.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null) {
                timeEnd = new_event_end.getValue();
            }
        });

        new_event_describe.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                description = new_event_describe.getText();
            }
        });
        new_event_date.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                date = new_event_date.getValue();
            }
        });
    }
    //---------------------------------------------------------------------------------------------

    //making date disable when occurrence is daily and enable it otherwise--------------------------
    @FXML
    void boxChanged(ActionEvent event) {
        if (!Objects.equals(new_event_occurrence.getValue(), "daily")) {
            new_event_date.setDisable(false);
        } else {
            new_event_date.setDisable(true);
        }

    }

    //----------------------------------------------------------------------------------------------
    // adding data to the database -----------------------------------------------------------------
    @FXML
    void addToDB(ActionEvent event) throws SQLException {
        postgresDB db = new postgresDB();
        String idQuery = "SELECT event_id FROM public_table.event ORDER BY event_id DESC LIMIT 1";
        String addQuery = "INSERT INTO public_table.event(event_id, event_name, event_occurrence, event_start, event_end, event_description, event_date) VALUES(?,?,?,?,?,?,?)";
        String showResult = "SELECT event_name FROM public_table.event";
        ResultSet rs = db.execQuery(idQuery);
        int topID;
        if (rs.next()) topID = rs.getInt(1);
        else topID = 1;
        topID++;
        String occurrence = new_event_occurrence.getValue();
        PreparedStatement preparedStmt = db.getConnection().prepareStatement(addQuery);
        preparedStmt.setInt(1, topID);
        preparedStmt.setString(2, name);
        preparedStmt.setString(3, occurrence);
        preparedStmt.setTime(4, Time.valueOf(time));
        if (new_event_end.getValue() == null) {
            preparedStmt.setTime(5, null);
        } else {
            preparedStmt.setTime(5, Time.valueOf(timeEnd));
        }
        if (new_event_describe.getText() == null) {
            preparedStmt.setString(6, null);
        } else {
            preparedStmt.setString(6, description);
        }
        if (new_event_date.getValue() == null) {
            preparedStmt.setDate(7, null);
        } else {
            preparedStmt.setDate(7, Date.valueOf(date));
        }
        //-------------------------------------------------------
        preparedStmt.execute();
        ResultSet displayResult = db.execQuery(showResult);
        while (displayResult.next()) {
            System.out.println(displayResult.getString("event_name"));
        }
        ResultSet successResult= db.execQuery(idQuery);
        int i=0;
        if(successResult.next()) i= successResult.getInt(1);
        if (i==topID){
            NotificationType notification=NotificationType.SUCCESS;
            TrayNotification tray = new TrayNotification("ADD EVENT","Succeed.",notification);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
        }
        close();
    }
    //------------------------------------------------------------------------------------------------------

    // close listener method for opened scenes -------------------------------------------------------------
    @FXML
    void close(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void close() {
        Stage stage = (Stage) addPane.getScene().getWindow();
        stage.close();
    }

    // -----------------------------------------------------------------------------------------------------
    // enabling add event stage to edit --------------------------------------------------------------------
    private void unlockOthers() {
        new_event_occurrence.setDisable(false);
        new_event_start.setDisable(false);
        new_no_des.setDisable(false);
        new_real_time.setDisable(false);
    }

    //----------------------------------------------------------------------------------------------------
    // disabling view of nodes inside add event ----------------------------------------------------------
    private void lockOthers() {
        new_event_occurrence.setDisable(true);
        new_event_start.setDisable(true);
        new_event_end.setDisable(true);
        new_event_describe.setDisable(true);
        new_event_date.setDisable(true);
        new_event_add.setDisable(true);
        new_no_des.setDisable(true);
        new_real_time.setDisable(true);
    }

    //-----------------------------------------------------------------------------------------
    @FXML
    void noDes(ActionEvent event) {
        if (new_no_des.isSelected()) {
            new_event_describe.setDisable(true);
            new_event_describe.setText(null);
        } else {
            new_event_describe.setDisable(false);
        }
    }

    @FXML
    void realTime(ActionEvent event) {
        if (new_real_time.isSelected()) {
            new_event_end.setDisable(true);
            new_event_end.setValue(null);
        } else {
            new_event_end.setDisable(false);
        }
    }

}
