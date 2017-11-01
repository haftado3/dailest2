package Dailiest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.management.Notification;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {
    @FXML
    Button setting, event, reminder, timeLine, exit, next_event, add_event;
    @FXML
    Pane timeLine_pane, event_pane, reminder_pane, setting_pane, top_pane, rootPane;
    @FXML
    VBox timeLine_box, event_box, reminder_box, setting_box;
    @FXML
    ProgressBar progress_bar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeLine.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ToDoList.png"))));
        event.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Notification.png"))));
        reminder.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Alarm Clock.png"))));
        setting.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Gear.png"))));
        exit.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Shutdown.png"))));
        next_event.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Time Span_100px.png"))));
        add_event.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/Add_96px.png"))));
        timeLine_pane.setVisible(true);
        timeLine_pane.setDisable(false); // setting default active pane and setting it visible.
        progress_bar.setProgress(0.5);
        timeLine.setId("button_clicked");
    }

    public void close(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        System.out.println("program exited by exit button in main menu.");
    }


    public void buttonClicked(MouseEvent e) {
        if (e.getSource() == setting) {
            setting.setId("button_clicked");
            resetButton(event, reminder, timeLine);
            setPane(setting_pane, timeLine_pane, event_pane, reminder_pane);
        }
        if (e.getSource() == event) {
            event.setId("button_clicked");
            resetButton(setting, reminder, timeLine);
            setPane(event_pane, timeLine_pane, setting_pane, reminder_pane);
        }
        if (e.getSource() == reminder) {
            reminder.setId("button_clicked");
            resetButton(event, setting, timeLine);
            setPane(reminder_pane, timeLine_pane, event_pane, setting_pane);
        }
        if (e.getSource() == timeLine) {
            timeLine.setId("button_clicked");
            resetButton(event, reminder, setting);
            setPane(timeLine_pane, setting_pane, event_pane, reminder_pane);
        }

    }

    private void resetButton(Button a, Button b, Button c) {
        a.setId("black_button");
        b.setId("black_button");
        c.setId("black_button");
    }


    private void setPane(Pane a, Pane b, Pane c, Pane d) {
        a.setDisable(false);
        b.setDisable(true);
        c.setDisable(true);
        d.setDisable(true);
        a.setVisible(true);
        b.setVisible(false);
        c.setVisible(false);
        d.setVisible(false);
    }

    public void addEvent(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addEvent.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 369, 267);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("add event");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void addLabel(ActionEvent e) {
        Label lbl = new Label("hello world");
        lbl.setStyle("-fx-text-fill: red");
        lbl.setLayoutX(100);
        lbl.setLayoutY(100);
        top_pane.getChildren().add(lbl);
    }

}
