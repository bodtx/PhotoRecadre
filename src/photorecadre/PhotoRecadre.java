
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photorecadre;

import com.sun.deploy.uitoolkit.DragContext;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author bod
 */
public class PhotoRecadre extends Application {
double dragBaseX,dragBaseY,dragBase2X,dragBase2Y;

    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        final Circle circle = new Circle(50.0, Color.RED);

         circle.setOnMousePressed(new EventHandler<MouseEvent>() {

public void handle(MouseEvent event) {
    
  dragBaseX = circle.translateXProperty().get();

dragBaseY = circle.translateYProperty().get();

dragBase2X = event.getSceneX();

dragBase2Y = event.getSceneY();
}
});
        
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent me) {
                
                 circle.setTranslateX(dragBaseX + (me.getSceneX()-dragBase2X));

                circle.setTranslateY(dragBaseY + (me.getSceneY()-dragBase2Y));
            }
        });

//        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent me) {
//                System.out.println("Mouse pressed");
//            }
//        });
        StackPane root = new StackPane();
        //Scene s = new Scene(root, 300, 300, Color.AZURE);
        
        root.getChildren().add(btn);
        root.getChildren().add(circle);





















        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
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

