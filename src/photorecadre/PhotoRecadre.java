
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photorecadre;

import com.sun.deploy.uitoolkit.DragContext;
import com.sun.javafx.geom.transform.Affine3D;
import java.awt.Transparency;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author bod
 */
public class PhotoRecadre extends Application {

    double dragBaseX, dragBaseY, dragBase2X, dragBase2Y;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");
        Group root = new Group();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        final Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        Button btn = new Button();
        root.getChildren().add(btn);

        primaryStage.setScene(scene);
        primaryStage.show();


        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        final Rectangle rectMain = new Rectangle();
        final Rectangle rectUpLeft = new Rectangle();
        final Rectangle rectUpRight = new Rectangle();
        final Rectangle rectDownRight = new Rectangle();
        final Rectangle rectDownLeft = new Rectangle();
        rectMain.setX(50);
        rectMain.setY(50);
        rectMain.setWidth(200);
        rectMain.setHeight(100);
        rectMain.setFill(null);
        rectMain.setStroke(Color.BLUE);
        root.getChildren().add(rectMain);

        rectUpLeft.setX(50);
        rectUpLeft.setY(50);
        rectUpLeft.setWidth(10);
        rectUpLeft.setHeight(10);
        rectUpLeft.setStroke(Color.BLUE);
        root.getChildren().add(rectUpLeft);

        rectUpLeft.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                scene.setCursor(Cursor.CLOSED_HAND);
                dragBaseX = rectUpLeft.translateXProperty().get();

                dragBaseY = rectUpLeft.translateYProperty().get();

                dragBase2X = event.getSceneX();

                dragBase2Y = event.getSceneY();
            }
        });

        rectUpLeft.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {

                rectUpLeft.setTranslateX(dragBaseX + (me.getSceneX() - dragBase2X));

                rectUpLeft.setTranslateY(dragBaseY + (me.getSceneY() - dragBase2Y));
                   double rectMainoldX = rectMain.getX();
                   double rectMainoldY = rectMain.getY();
                rectMain.setX(rectUpLeft.getX());
                rectMain.setY(rectUpLeft.getY());
                rectMain.setWidth(200 - (rectMainoldX - rectMain.getX()));
                rectMain.setHeight(100 - (rectMainoldY - rectMain.getY()));
                rectMain.resize(100, 100);
                rectMain.getTransforms().add(new Scale(10, 10)); 
           }
        });

//        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent me) {
//                System.out.println("Mouse pressed");
//            }
//        });




    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
