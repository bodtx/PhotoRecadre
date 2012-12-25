
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
    double dragBaseURX, dragBaseURY, dragBase2URX, dragBase2URY;
    double dragBaseDLX, dragBaseDLY, dragBase2DLX, dragBase2DLY;
    double oldRectMainHeight, oldRectMainWidth;

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
        final Rectangle rectUpRight = new Rectangle();
        final Rectangle rectDownRight = new Rectangle();
        final Rectangle rectDownLeft = new Rectangle();
//        rectMain.setX(50);
//        rectMain.setY(50);
//        final double ratioOldPicture = (11.0/15.0);
        rectMain.setWidth(workingImage.getWidth());
        rectMain.setHeight(workingImage.getHeight() );
        rectMain.setFill(null);
        rectMain.setStroke(Color.BLUE);
        root.getChildren().add(rectMain);

//        rectUpLeft.setX(50);
//        rectUpLeft.setY(50);
        rectUpLeft.setWidth(10);
        rectUpLeft.setHeight(10);
        rectUpLeft.setStroke(Color.BLUE);
        root.getChildren().add(rectUpLeft);

        rectDownRight.setX(rectMain.getWidth() - 10);
        rectDownRight.setY(rectMain.getHeight() - 10);
        rectDownRight.setWidth(10);
        rectDownRight.setHeight(10);
        rectDownRight.setStroke(Color.BLUE);
        root.getChildren().add(rectDownRight);

        rectUpRight.setX(rectMain.getWidth() - 10);
//        rectUpRight.setY(50);
        rectUpRight.setWidth(10);
        rectUpRight.setHeight(10);
        rectUpRight.setStroke(Color.BLUE);
        root.getChildren().add(rectUpRight);

//        rectDownLeft.setX(50);
        rectDownLeft.setY(rectMain.getHeight() - 10);
        rectDownLeft.setWidth(10);
        rectDownLeft.setHeight(10);
        rectDownLeft.setStroke(Color.BLUE);
        root.getChildren().add(rectDownLeft);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double ratioResize = image.getWidth()/resizedWidth;
                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getTranslateX() * ratioResize,
                        rectUpLeft.getTranslateY() * ratioResize,
                        (workingImage.getWidth() - rectUpLeft.getTranslateX() + rectDownRight.getTranslateX()) * ratioResize,
                        (workingImage.getHeight() - rectUpLeft.getTranslateY() + rectDownRight.getTranslateY()) * ratioResize);
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
                    Logger.getLogger(PhotoRecadre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });


        rectUpLeft.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                scene.setCursor(Cursor.CLOSED_HAND);
                dragBaseULX = rectUpLeft.translateXProperty().get();
                dragBaseULY = rectUpLeft.translateYProperty().get();
                dragBase2ULX = event.getSceneX();
                dragBase2ULY = event.getSceneY();
            }
        });

        rectUpLeft.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                rectUpLeft.setTranslateX(dragBaseULX + (me.getSceneX() - dragBase2ULX));
                rectUpLeft.setTranslateY(dragBaseULY + (me.getSceneY() - dragBase2ULY));
                rectUpRight.setTranslateY(dragBaseULY + (me.getSceneY() - dragBase2ULY));
                rectDownLeft.setTranslateX(dragBaseULX + (me.getSceneX() - dragBase2ULX));
                rectMain.setTranslateX(rectUpLeft.getTranslateX());
                rectMain.setTranslateY(rectUpLeft.getTranslateY());

                final double rectHeight = workingImage.getHeight() - rectUpLeft.getTranslateY() + rectDownRight.getTranslateY();
                rectMain.setHeight(rectHeight);
                final double rectWidth = workingImage.getWidth() - rectUpLeft.getTranslateX() + rectDownRight.getTranslateX();
                rectMain.setWidth(rectWidth);


                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getTranslateX(), rectUpLeft.getTranslateY(), rectWidth, rectHeight);
                imageView.setViewport(viewportRect);
                imageView.setTranslateX(rectMain.getX() + rectUpLeft.getTranslateX());
                imageView.setTranslateY(rectMain.getY() + rectUpLeft.getTranslateY());
                System.out.println(rectUpLeft.getTranslateX()+"/"+rectUpLeft.getTranslateY()+"/"+rectWidth+"/"+rectHeight);

            }
        });



        rectDownLeft.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                scene.setCursor(Cursor.CLOSED_HAND);
                dragBaseDLX = rectDownLeft.translateXProperty().get();
                dragBaseDLY = rectDownLeft.translateYProperty().get();
                dragBase2DLX = event.getSceneX();
                dragBase2DLY = event.getSceneY();
            }
        });

        rectDownLeft.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                rectDownLeft.setTranslateX(dragBaseDLX + (me.getSceneX() - dragBase2DLX));
                rectDownLeft.setTranslateY(dragBaseDLY + (me.getSceneY() - dragBase2DLY));
                rectUpLeft.setTranslateX(dragBaseDLX + (me.getSceneX() - dragBase2DLX));
                rectDownRight.setTranslateY(dragBaseDLY + (me.getSceneY() - dragBase2DLY));
                final double rectHeight = workingImage.getHeight() + rectDownLeft.getTranslateY() - rectUpLeft.getTranslateY();
                rectMain.setHeight(rectHeight);
                final double rectWidth = workingImage.getWidth() - rectDownLeft.getTranslateX() + rectDownRight.getTranslateX();
                rectMain.setWidth(rectWidth);
                rectMain.setTranslateX(rectDownLeft.getTranslateX());

                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getTranslateX(), rectUpLeft.getTranslateY(), rectWidth, rectHeight);
                imageView.setViewport(viewportRect);
                imageView.setTranslateX(rectMain.getX() + rectDownLeft.getTranslateX());
                System.out.println(rectUpLeft.getTranslateX()+"/"+rectUpLeft.getTranslateY()+"/"+rectWidth+"/"+rectHeight);
            }
        });










        rectDownRight.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                scene.setCursor(Cursor.CLOSED_HAND);
                dragBaseDRX = rectDownRight.translateXProperty().get();
                dragBaseDRY = rectDownRight.translateYProperty().get();
                dragBase2DRX = event.getSceneX();
                dragBase2DRY = event.getSceneY();
            }
        });

        rectDownRight.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                rectDownRight.setTranslateX(dragBaseDRX + (me.getSceneX() - dragBase2DRX));
                rectDownRight.setTranslateY(dragBaseDRY + (me.getSceneY() - dragBase2DRY));
                rectUpRight.setTranslateX(dragBaseDRX + (me.getSceneX() - dragBase2DRX));
                rectDownLeft.setTranslateY(dragBaseDRY + (me.getSceneY() - dragBase2DRY));
                final double rectIViewHeigth = workingImage.getHeight() + rectDownRight.getTranslateY() - rectUpLeft.getTranslateY();
                final double rectIViewWidth = workingImage.getWidth() + rectDownRight.getTranslateX() - rectUpLeft.getTranslateX();
                rectMain.setHeight(rectIViewHeigth);
                rectMain.setWidth(rectIViewWidth);

                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getTranslateX(), rectUpLeft.getTranslateY(), rectIViewWidth, rectIViewHeigth);
                imageView.setViewport(viewportRect);
                imageView.setTranslateX(rectMain.getX() + rectUpLeft.getTranslateX());
                imageView.setTranslateY(rectMain.getY() + rectUpLeft.getTranslateY());
            }
        });

        rectUpRight.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                scene.setCursor(Cursor.CLOSED_HAND);
                dragBaseURX = rectUpRight.translateXProperty().get();
                dragBaseURY = rectUpRight.translateYProperty().get();
                dragBase2URX = event.getSceneX();
                dragBase2URY = event.getSceneY();
            }
        });

        rectUpRight.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                rectUpRight.setTranslateX(dragBaseURX + (me.getSceneX() - dragBase2URX));
                rectUpRight.setTranslateY(dragBaseURY + (me.getSceneY() - dragBase2URY));
                rectUpLeft.setTranslateY(dragBaseURY + (me.getSceneY() - dragBase2URY));
                rectDownRight.setTranslateX(dragBaseURX + (me.getSceneX() - dragBase2URX));
                rectMain.setTranslateY(rectUpRight.getTranslateY());
                rectMain.setHeight(workingImage.getHeight() - rectUpRight.getTranslateY() + rectDownRight.getTranslateY());
                final double rectWidth = workingImage.getWidth() + rectUpRight.getTranslateX() - rectUpLeft.getTranslateX();
//                rectMain.setHeight(rectWidth*ratioOldPicture);
                rectMain.setWidth(rectWidth);

                final Rectangle2D viewportRect = new Rectangle2D(rectUpLeft.getTranslateX(), rectUpLeft.getTranslateY(), workingImage.getWidth() + rectUpRight.getTranslateX() - rectUpLeft.getTranslateX(), workingImage.getHeight() - rectUpRight.getTranslateY() + rectDownRight.getTranslateY());
                imageView.setViewport(viewportRect);
                imageView.setTranslateX(rectMain.getX() + rectUpLeft.getTranslateX());
                imageView.setTranslateY(rectMain.getY() + rectUpLeft.getTranslateY());
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
