
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photorecadre;

import com.sun.deploy.uitoolkit.DragContext;
import com.sun.javafx.geom.transform.Affine3D;
import java.awt.Transparency;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javax.imageio.ImageIO;

/**
 *
 * @author bod
 */
public class PhotoRecadre extends Application {

    double dragBaseULX, dragBaseULY, dragBase2ULX, dragBase2ULY;
    double dragBaseDRX, dragBaseDRY, dragBase2DRX, dragBase2DRY;
    double dragBaseCX, dragBaseCY, dragBase2CX, dragBase2CY;
    double dragBaseDLX, dragBaseDLY, dragBase2DLX, dragBase2DLY;
    private double oldRectMainH;
    private double oldRectMainW;
    private double oldRectMainTX;
    private double oldRectMainTY;
    private double rectCenterMinX;
    private double rectCenterMinY;
    private double rectMainMaxX;
    private double rectMainMaxY;
    private double rectMainMinX;
    private double rectMainMinY;
            private double rectULMinX;
            private double rectULMinY;
            private double startULY;
            private double startULX;
    private Logger logger = Logger.getLogger(PhotoRecadre.class.getName());

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Save image");
        Group root = new Group();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        final Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        Button btn = new Button();
        btn.setTranslateX(600);
        root.getChildren().add(btn);

        primaryStage.setScene(scene);
        primaryStage.show();

        // Create Image and ImageView objects
        final Image image = new Image("file:///home/bod/Images/1009398_fr_ernest_et_celestine_1342095795304.jpg");
        final ImageView sourceImageView = new ImageView();
        sourceImageView.setImage(image);
        sourceImageView.setPreserveRatio(true);
        final int resizedWidth = 400;
        sourceImageView.setFitWidth(resizedWidth);
        final Rectangle2D sourceViewportRect = new Rectangle2D(0, 0, sourceImageView.getFitWidth(), sourceImageView.getFitHeight());
        sourceImageView.setViewport(sourceViewportRect);

        final Image workingImage = sourceImageView.snapshot(null, null);
        final ImageView imageView = new ImageView();
        imageView.setImage(workingImage);
        final Rectangle2D viewportRect = new Rectangle2D(0, 0, workingImage.getWidth(), workingImage.getHeight());
        imageView.setViewport(viewportRect);
        root.getChildren().add(imageView);




        btn.setText("Say 'Hello World'");


        final Rectangle rectMain = new Rectangle();
        final Rectangle rectUpLeft = new Rectangle();
        final Rectangle rectCenter = new Rectangle();
        final double ratioOldPicture = (11.0 / 15.0);
        rectMain.setWidth(workingImage.getWidth());
        rectMain.setHeight(workingImage.getHeight() * ratioOldPicture);
        rectMain.setFill(null);
        rectMain.setStroke(Color.BLUE);
        root.getChildren().add(rectMain);

//        rectUpLeft.setX(50);
//        rectUpLeft.setY(50);
        rectUpLeft.setWidth(10);
        rectUpLeft.setHeight(10);
        rectUpLeft.setStroke(Color.BLUE);
        root.getChildren().add(rectUpLeft);


        rectCenter.setX(rectMain.getWidth() / 2.0 - 10);
        rectCenter.setY(rectMain.getHeight() / 2.0);
        rectCenterMinX = rectCenter.getX();
        rectCenterMinY = rectCenter.getY();
        rectMainMinX = rectMain.getX();
        rectMainMinY = rectMain.getY();
        rectMainMaxX = rectMain.getWidth();
        rectMainMaxY = rectMain.getHeight();
        
        rectULMinX = rectUpLeft.getX();
        rectULMinY = rectUpLeft.getY();

        rectCenter.setWidth(10);
        rectCenter.setHeight(10);
        rectCenter.setStroke(Color.BLUE);
        root.getChildren().add(rectCenter);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double ratioResize = image.getWidth() / resizedWidth;
                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getBoundsInParent().getMinX() * ratioResize,
                        rectUpLeft.getBoundsInParent().getMinY() * ratioResize,
                        (rectMain.getWidth()) * ratioResize,
                        (rectMain.getHeight()) * ratioResize);
                final ImageView saveImageView = new ImageView();
                saveImageView.setImage(image);
                saveImageView.setViewport(viewportRect);
                try {
                    ImageIO.write(
                            SwingFXUtils.fromFXImage(
                            saveImageView.snapshot(null, null), null),
                            "png",
                            new File("celestin.png"));
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        });

//
//        rectUpLeft.setOnMousePressed(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event) {
////                scene.setCursor(Cursor.CLOSED_HAND);
//                dragBaseULX = rectUpLeft.translateXProperty().get();
//                dragBaseULY = rectUpLeft.translateYProperty().get();
//                dragBase2ULX = event.getSceneX();
//                dragBase2ULY = event.getSceneY();
//
//                dragBaseCX = rectCenter.translateXProperty().get();
//                dragBaseCY = rectCenter.translateYProperty().get();
//                dragBase2CX = event.getSceneX();
//                dragBase2CY = event.getSceneY();
//
//
//                oldRectMainW = rectMain.getWidth();
//                oldRectMainH = rectMain.getHeight();
//                System.out.println("pressed: " + oldRectMainW + "/" + oldRectMainH);
//            }
//        });
//        
//
//        rectUpLeft.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent me) {
//                rectUpLeft.setTranslateX(dragBaseULX + (me.getSceneX() - dragBase2ULX));
//                rectUpLeft.setTranslateY(dragBaseULY + (me.getSceneY() - dragBase2ULY));
//                rectMain.setTranslateX(rectUpLeft.getTranslateX());
//                rectMain.setTranslateY(rectUpLeft.getTranslateY());
//
//                final double rectHeight = oldRectMainH + dragBaseULY - rectUpLeft.getTranslateY();
//                rectMain.setHeight(rectHeight);
//                final double rectWidth = oldRectMainW + dragBaseULX - rectUpLeft.getTranslateX();
//                rectMain.setWidth(rectWidth);
////                System.out.println("drag: " + oldRectMainW + "/" + oldRectMainH);
////                rectCenter.relocate(rectMain.getX() + rectMain.getTranslateX() + rectWidth / 2.0, rectMain.getY() + rectMain.getTranslateY() + rectHeight / 2.0);
////                rectCenter.setTranslateY(dragBaseCY + (me.getSceneY() - dragBase2ULY));
//
////                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getTranslateX(), rectUpLeft.getTranslateY(), rectWidth, rectHeight);
////                imageView.setViewport(viewportRect);
////                imageView.setTranslateX(rectMain.getX() + rectUpLeft.getTranslateX());
////                imageView.setTranslateY(rectMain.getY() + rectUpLeft.getTranslateY());
//
//            }
//        });

        //EventListener for MousePressed
        rectUpLeft.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //record the current mouse X and Y position on Node
                dragBaseULX = event.getSceneX();
                dragBaseULY = event.getSceneY();
                
                startULX = event.getSceneX();
                startULY = event.getSceneY();
                //get the x and y position measure from Left-Top
                dragBase2ULX = rectUpLeft.getLayoutX();
                dragBase2ULY = rectUpLeft.getLayoutY();
                oldRectMainW = rectMainMaxX - rectMainMinX;
                oldRectMainH = rectMainMaxY - rectMainMinY;
                
                rectMainMinX = rectMain.getBoundsInParent().getMinX();
                rectMainMinY = rectMain.getBoundsInParent().getMinY();
                rectMainMaxX = rectMain.getBoundsInParent().getMaxX();
                rectMainMaxY = rectMain.getBoundsInParent().getMaxY();



            }
        });

        //Event Listener for MouseDragged
        rectUpLeft.onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Get the exact moved X and Y
                dragBase2ULX += event.getSceneX() - dragBaseULX;

                dragBase2ULY += event.getSceneY() - dragBaseULY;

                //set the positon of Node after calculation
                rectUpLeft.setLayoutX(dragBase2ULX);
                rectUpLeft.setLayoutY(dragBase2ULY);


//                rectMain.setLayoutX(dragBase2ULX);
//                rectMain.setLayoutY(dragBase2ULY);

                final double rectHeight = rectMainMaxY - rectMainMinY - (event.getSceneY() - startULY);
                rectMain.setHeight(rectHeight);
                final double rectWidth = rectMainMaxX - rectMainMinX - (event.getSceneX() - startULX);
                rectMain.setWidth(rectWidth);

                logger.log(Level.INFO, event.getSceneX()+"/"+ startULX+"/"+rectUpLeft.getLayoutX() + "/" + dragBaseULX + "/" + dragBase2ULX + "/" + rectUpLeft.getTranslateX() +"/"+ rectUpLeft.getBoundsInParent().getMinX()+ "/"+rectUpLeft.getBoundsInParent().getMaxX());
//                logger.log(Level.OFF, "X property " + (rectUpLeft.getLayoutX() + rectUpLeft.getTranslateX()));
//                logger.log(Level.OFF, "X property main" + rectMain.getLayoutX() + "/" + rectMain.getTranslateX() + "/" + rectMain.getX() + "/" + rectMain.getBoundsInParent().getMinX() + "/" + rectMain.getBoundsInLocal().getMinX());
                rectMain.setX(rectUpLeft.getBoundsInParent().getMinX() - rectMain.getTranslateX());
                rectMain.setY(rectUpLeft.getBoundsInParent().getMinY() - rectMain.getTranslateY());

                //again set current Mouse x AND y position
                dragBaseULX = event.getSceneX();
                dragBaseULY = event.getSceneY();
                rectCenter.setLayoutX(dragBase2ULX / 2.0);
                rectCenter.setLayoutY(dragBase2ULY / 2.0);

            }
        });
        


        rectCenter.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                scene.setCursor(Cursor.CLOSED_HAND);
                dragBaseCX = rectCenter.translateXProperty().get();
                dragBaseCY = rectCenter.translateYProperty().get();
                dragBase2CX = event.getSceneX();
                dragBase2CY = event.getSceneY();
                oldRectMainTX = rectMain.getTranslateX();
                oldRectMainTY = rectMain.getTranslateY();


            }
        });

        rectCenter.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                rectCenter.setTranslateX(dragBaseCX + (me.getSceneX() - dragBase2CX));
                rectCenter.setTranslateY(dragBaseCY + (me.getSceneY() - dragBase2CY));
                rectCenterMinX = rectCenter.getBoundsInParent().getMinX();
                rectCenterMinY = rectCenter.getBoundsInParent().getMinY();




                rectUpLeft.setTranslateY(dragBaseCY + (me.getSceneY() - dragBase2CY));
                rectUpLeft.setTranslateX(dragBaseCX + (me.getSceneX() - dragBase2CX));
                rectULMinX = rectUpLeft.getBoundsInParent().getMinX();
                rectULMinY = rectUpLeft.getBoundsInParent().getMinY();
logger.log(Level.OFF, "X property rectUL" + rectULMinX + "/" + rectULMinY);


                rectMain.setTranslateX(dragBaseCX + (me.getSceneX() - dragBase2CX));
                rectMain.setTranslateY(dragBaseCY + (me.getSceneY() - dragBase2CY));
                rectMainMinX = rectMain.getBoundsInParent().getMinX();
                rectMainMinY = rectMain.getBoundsInParent().getMinY();
                rectMainMaxX = rectMain.getBoundsInParent().getMaxX();
                rectMainMaxY = rectMain.getBoundsInParent().getMaxY();

                logger.log(Level.OFF, "X property " + rectCenter.getLayoutX() + "/" + rectCenter.getTranslateX() + "/" + rectCenter.getX() + "/" + rectCenter.getBoundsInParent().getMinX() + "/" + rectCenter.getBoundsInLocal().getMinX());
                logger.log(Level.OFF, "X property rectmain" + rectMain.getBoundsInParent().getMinX() + "/" + rectMain.getBoundsInParent().getMaxX());





//                final double rectHeight = workingImage.getHeight() - rectUpLeft.getTranslateY() ;
//                final double rectWidth = workingImage.getWidth() - rectUpLeft.getTranslateX() ;
//                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getTranslateX(), rectUpLeft.getTranslateY(), rectWidth, rectHeight);
//                imageView.setViewport(viewportRect);
//                imageView.setTranslateX(rectMain.getX() + rectUpLeft.getTranslateX());
//                imageView.setTranslateY(rectMain.getY() + rectUpLeft.getTranslateY());
            }
        });

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
