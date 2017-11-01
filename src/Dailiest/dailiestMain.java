package Dailiest;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class dailiestMain extends Application {
    private double x, y;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // stage init : ----------------------------------------------------------------------------------------
        // you need to define FXMLLoader and Parent separately (loader,root) so you can add components
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        Parent root = loader.load();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Dailiest");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        Pane topPane = (Pane) loader.getNamespace().get("top_pane");
        //-----------------------------------------------------------
        //------------------------------------------------------------
        try {
            addDragListener(topPane, primaryStage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addDragListener(final Node n, Stage primaryStage) {

        n.setOnMousePressed(e -> {
            this.x = n.getScene().getWindow().getX() - e.getScreenX();
            this.y = n.getScene().getWindow().getY() - e.getScreenY();
        });

        n.setOnMouseDragged(e -> {
            primaryStage.setX(e.getScreenX() + this.x);
            primaryStage.setY(e.getScreenY() + this.y);
        });
    }

}
