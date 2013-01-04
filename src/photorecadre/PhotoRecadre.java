
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photorecadre;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author bod
 */
public class PhotoRecadre extends Application {

    double dragBaseCX, dragBaseCY, dragBase2CX, dragBase2CY;
    private Logger logger = Logger.getLogger(PhotoRecadre.class.getName());
    final int resizedWidth = 400;
    Image image = new Image(getClass().getClassLoader().getResourceAsStream("resources/ernest_et_celestine.jpg"));
    double ratioResize;
    Rectangle rectMain = new Rectangle();
    final Rectangle rectCenter = new Rectangle();
    private final ImageView sourceImageView = new ImageView();
    File prefDirectory = null;
    static HashMap<String, Image> imageMap = new HashMap<>();
    final ImageView imageView = new ImageView();
    final double ratioOldPicture = (10.0 / 15.0);

    @Override
    public void start(Stage primaryStage) {


        StackPane stackPane = new StackPane();
        final FileChooser fChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Photos", "*.JPG", "*.jpg", "*.PNG", "*.png");
        fChooser.getExtensionFilters().add(filter);
//        primaryStage.setTitle("Save image");
//        Group root = new Group();
//        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//        final Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        Button btn = new Button();
        Button btnFile = new Button("Choose Pictures");
        image = getWorkingImage(image, resizedWidth, imageView);
        btn.setText("Save cropped image");
        rectMain.setX(0);
        rectMain.setY(0);
        rectMain.setWidth(Math.round(image.getWidth() * 100) / 100 - 2);
        rectMain.setHeight(Math.round((image.getWidth() - 1) * ratioOldPicture * 100) / 100 - 1);
        rectMain.setFill(null);
        rectMain.setStroke(Color.GREEN);
        rectCenter.setY(rectMain.getHeight() / 2.0);
        rectCenter.setX(rectMain.getWidth() / 2.0);
        rectCenter.setWidth(10);
        rectCenter.setHeight(10);
        rectCenter.setStroke(Color.BLUE);

        final ListView<String> list = new ListView<>();
        final ObservableList<String> items = FXCollections.observableArrayList();
        list.setItems(items);
        list.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ImageCell();
            }
        });
        FlowPane box = new FlowPane();
        imageView.setX(0);
        imageView.setY(0);
        stackPane.getChildren().add(imageView);
        stackPane.getChildren().add(rectMain);
        stackPane.getChildren().add(rectCenter);
        box.getChildren().addAll(stackPane, list, btn, btnFile);

        primaryStage.setScene(new Scene(box, 800, 600));
        primaryStage.show();

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String pathToImage = list.getSelectionModel().getSelectedItem();
                showImage(pathToImage);




            }
        });
        btnFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (prefDirectory != null) {
                    fChooser.setInitialDirectory(prefDirectory);
                }
                list.getItems().clear();
                List<File> showOpenMultipleDialog = fChooser.showOpenMultipleDialog(null);
                imageMap.clear();
                for (File file : showOpenMultipleDialog) {
                    final String imagePath = file.toURI().toString();
                    list.getItems().add(imagePath);
                    imageMap.put(imagePath, new Image(imagePath, 200, 0, true, false, true));
                }
                prefDirectory = showOpenMultipleDialog.get(0).getParentFile();
            }
        });
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Rectangle2D viewportRect;
                final ImageView saveImageView = new ImageView();
                if (!rectMain.getTransforms().isEmpty()) {
                    viewportRect = new Rectangle2D((rectMain.getBoundsInParent().getMinX() - imageView.getBoundsInParent().getMinX()) * ratioResize,
                            (rectMain.getBoundsInParent().getMinY() - imageView.getBoundsInParent().getMinY()) * ratioResize,
                            (rectMain.getHeight()) * ratioResize,
                            (rectMain.getWidth()) * ratioResize);
                } else {
                    viewportRect = new Rectangle2D((rectMain.getBoundsInParent().getMinX() - imageView.getBoundsInParent().getMinX()) * ratioResize,
                            (rectMain.getBoundsInParent().getMinY() - imageView.getBoundsInParent().getMinY()) * ratioResize,
                            (rectMain.getWidth()) * ratioResize,
                            (rectMain.getHeight()) * ratioResize);

                }
                saveImageView.setImage(sourceImageView.getImage());
                saveImageView.setViewport(viewportRect);
                logger.log(Level.INFO, "iViewCoord {0}/{1}", new Object[]{imageView.getBoundsInParent().getMinX(), imageView.getBoundsInParent().getMinY()});
                final String filePath = list.getSelectionModel().getSelectedItem();
                try {
                    File imageToDelete = new File(new URI(filePath));
                    imageToDelete.delete();
                    final WritableImage snapshot = saveImageView.snapshot(null, null);
                    final BufferedImage croppedARGBImage = SwingFXUtils.fromFXImage(
                                                      snapshot, null);

                    BufferedImage imageRGB = new BufferedImage((int)viewportRect.getWidth(), (int)viewportRect.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = imageRGB.createGraphics();
                    //Color.WHITE set the background to white. You can use any other color
                    g.drawImage(croppedARGBImage, 0, 0, imageRGB.getWidth(), imageRGB.getHeight(), java.awt.Color.WHITE, null);
                    
                    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                    ImageWriter writer = (ImageWriter) writers.next();
                    ImageWriteParam param = writer.getDefaultWriteParam();
                    ImageOutputStream ios;
                    try (OutputStream os = new FileOutputStream(new File(new URI(filePath + ".jpg")))) {
                        ios = ImageIO.createImageOutputStream(os);
                        writer.setOutput(ios);
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(1.0f);
                        writer.write(null, new IIOImage(imageRGB, null, null), param);
                    }
                    
                    imageMap.remove(filePath);
                    final String newPicFile = filePath + ".jpg";
                    imageMap.put(newPicFile, new Image(newPicFile, 200, 0, true, false, false));
                    items.set(list.getSelectionModel().getSelectedIndex(), newPicFile);
                    showImage(newPicFile);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(PhotoRecadre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        stackPane.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent t) {
                        final int step = 10;
                        //TODO prevent scroll if scroll + step > imageview otherwise it will be over
                        if (!(rectMain.getBoundsInParent().getMaxY() > imageView.getBoundsInParent().getMaxY())
                                && !(rectMain.getBoundsInParent().getMaxX() > imageView.getBoundsInParent().getMaxX())
                                && !(rectMain.getBoundsInParent().getMinY() < imageView.getBoundsInParent().getMinY())
                                && !(rectMain.getBoundsInParent().getMinX() < imageView.getBoundsInParent().getMinX())) {
                            //TODO: handle rectMain size <= imageView size
                            if (t.getDeltaY() > 0) {
                                rectMain.setX(rectMain.getX() - (step / 2));
                                rectMain.setWidth(rectMain.getWidth() + step);
                                rectMain.setY(rectMain.getY() - ((step * ratioOldPicture) / 2));
                                rectMain.setHeight(rectMain.getWidth() * ratioOldPicture);
                            } else {
                                rectMain.setX(rectMain.getX() + (step / 2));
                                rectMain.setWidth(rectMain.getWidth() - step);
                                rectMain.setY(rectMain.getY() + ((step * ratioOldPicture) / 2));
                                rectMain.setHeight(rectMain.getWidth() * ratioOldPicture);
                            }
                            updateRecCOlor(rectMain, ratioResize);
                        }
                    }
                });
        updateRecCOlor(rectMain, ratioResize);

        rectCenter.setOnMousePressed(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
//                scene.setCursor(Cursor.CLOSED_HAND);
                        dragBaseCX = rectCenter.translateXProperty().get();
                        dragBaseCY = rectCenter.translateYProperty().get();
                        dragBase2CX = event.getSceneX();
                        dragBase2CY = event.getSceneY();
                    }
                });

        rectCenter.setOnMouseDragged(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent me) {
                        final double dragWidth = dragBaseCX + (me.getSceneX() - dragBase2CX);
                        final double dragHeight = dragBaseCY + (me.getSceneY() - dragBase2CY);

                        rectCenter.setTranslateX(dragWidth);
                        rectCenter.setTranslateY(dragHeight);
                        rectMain.setTranslateX(dragWidth);
                        rectMain.setTranslateY(dragHeight);

                        if (rectMain.getBoundsInParent().getMaxY() > imageView.getBoundsInParent().getMaxY()) {
                            rectCenter.setTranslateY(dragHeight - (rectMain.getBoundsInParent().getMaxY() - imageView.getBoundsInParent().getMaxY() + 1));
                            rectMain.setTranslateY(dragHeight - (rectMain.getBoundsInParent().getMaxY() - imageView.getBoundsInParent().getMaxY() + 1));
                        }
                        if (rectMain.getBoundsInParent().getMaxX() > imageView.getBoundsInParent().getMaxX()) {
                            rectCenter.setTranslateX(dragWidth - (rectMain.getBoundsInParent().getMaxX() - imageView.getBoundsInParent().getMaxX() + 1));
                            rectMain.setTranslateX(dragWidth - (rectMain.getBoundsInParent().getMaxX() - imageView.getBoundsInParent().getMaxX() + 1));
                        }
                        if (rectMain.getBoundsInParent().getMinY() < imageView.getBoundsInParent().getMinY()) {
                            rectCenter.setTranslateY(dragHeight - (rectMain.getBoundsInParent().getMinY() - imageView.getBoundsInParent().getMinY()));
                            rectMain.setTranslateY(dragHeight - (rectMain.getBoundsInParent().getMinY() - imageView.getBoundsInParent().getMinY()));
                        }
                        if (rectMain.getBoundsInParent().getMinX() < imageView.getBoundsInParent().getMinX()) {
                            rectCenter.setTranslateX(dragWidth - (rectMain.getBoundsInParent().getMinX() - imageView.getBoundsInParent().getMinX()));
                            rectMain.setTranslateX(dragWidth - (rectMain.getBoundsInParent().getMinX() - imageView.getBoundsInParent().getMinX()));
                        }

                    }
                });

    }

    /**
     * rectMain The main() method is ignored in correctly deployed JavaFX
     * application. main() serves only as fallback in case the application can
     * not be launched through deployment artifacts, e.g., in IDEs with limited
     * FX support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void showImage(String pathToImage) {
        Image selectedWorkingImage = new Image(pathToImage);

        image = getWorkingImage(selectedWorkingImage, resizedWidth, imageView);
        rectMain.setTranslateX(0.0);
        rectMain.setTranslateY(0.0);
        rectMain.setX(0.0);
        rectMain.setY(0.0);
        //FIXME why need -1 ? because at startup the bound in parents is not equals to the rect's width...
        rectMain.setWidth(Math.round(image.getWidth() * 100) / 100 - 2);
        rectMain.setHeight(Math.round((image.getWidth() - 2) * ratioOldPicture * 100) / 100);

        rectCenter.setTranslateX(0.0);
        rectCenter.setTranslateY(0.0);
//   TODO: clear the transforms and suppress else if      rectCenter.getTransforms().clear();
        if (image.getWidth() < image.getHeight() && rectMain.getTransforms().isEmpty()) {
            rectMain.getTransforms().add(new Rotate(90.0, rectMain.getWidth() / 2.0, rectMain.getHeight() / 2.0));
        } else if (image.getWidth() > image.getHeight() && !rectMain.getTransforms().isEmpty()) {
            rectMain.getTransforms().clear();
        }
    }

    private void updateRecCOlor(final Rectangle rectMain, final double ratioResize) {
        if (rectMain.getWidth() * ratioResize < 1600) {
            rectMain.setStroke(Color.RED);
        } else {
            rectMain.setStroke(Color.GREEN);
        }
    }

    private Image getWorkingImage(final Image image, final int resizedWidth, final ImageView imageView) {
        sourceImageView.setImage(image);
        sourceImageView.setPreserveRatio(true);
        sourceImageView.setSmooth(true);
        sourceImageView.setFitWidth(resizedWidth);
        //TODO: wrong way to resize an image load the image with correct constructor
        final Rectangle2D sourceViewportRect = new Rectangle2D(0, 0, sourceImageView.getFitWidth(), sourceImageView.getFitHeight());
        sourceImageView.setViewport(sourceViewportRect);
        final Image workingImage = sourceImageView.snapshot(null, null);
        imageView.setImage(workingImage);
        final Rectangle2D viewportRect = new Rectangle2D(0, 0, workingImage.getWidth(), workingImage.getHeight());
        imageView.setViewport(viewportRect);
        ratioResize = image.getWidth() / resizedWidth;
        updateRecCOlor(rectMain, ratioResize);
        return workingImage;


    }

    static class ImageCell extends ListCell<String> {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                final Image imageCell = PhotoRecadre.imageMap.get(item);
                ImageView imageView = new ImageView(imageCell);
                imageView.setPreserveRatio(true);
                setGraphic(imageView);
            }
        }
    }
}
