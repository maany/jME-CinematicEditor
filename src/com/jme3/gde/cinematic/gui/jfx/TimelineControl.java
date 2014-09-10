/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import com.jme3.gde.cinematic.CinematicEditorManager;
import com.jme3.gde.cinematic.core.DurationChangeListener;
import com.jme3.gde.cinematic.core.Event;
import com.jme3.gde.cinematic.core.Layer;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author MAYANK
 */
public class TimelineControl extends VBox implements DurationChangeListener {
    
    @FXML
    private Group timebarTimesliderGroup;
    @FXML
    private Slider timebar;
    @FXML
    private Separator timeslider;
    @FXML
    private ScrollPane timelineScrollPane;
    @FXML
    private VBox timelineScrollPaneVBox;
    @FXML
    private Slider zoom;
    @FXML
    private ToggleButton snapToggle;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Label durationInput;
    @FXML
    private StackPane timebarTimeSliderStackPane;
    @FXML
    private Group timebarTimesliderSuperGroup;
    @FXML
    private Label currentTimeLabel;
    private DoubleProperty currentTime = new SimpleDoubleProperty(0);
    private DoubleProperty magnification = new SimpleDoubleProperty();
    private DoubleProperty maxMagnification = new SimpleDoubleProperty();
    private DoubleProperty duration = new SimpleDoubleProperty();
    private DoubleProperty frameRate = new SimpleDoubleProperty();
    private DoubleProperty frameWidth = new SimpleDoubleProperty();
    private CinematicEditorUI cinematicEditor;
    private double endAdjustment = 8; // timebar extra width due to circular thumb of slider
    private double startAdjustment = 6.5;

    /**
     * Constructor to load the TimelineControl from fxml file. Initialization
     * operations are performed by {@link #initTimeline() }
     */
    public TimelineControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timeline_ui_control.fxml"));
        loader.setClassLoader(this.getClass().getClassLoader());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }
    
    public void initTimeline() {
        initView();
        initListeners();
        initActions();
    }

    /**
     * durationInput changes CinematicClip's duration, which changes
     * TimelineControl's duration
     */
    @Override
    public void durationChanged() {
        duration.setValue(CinematicEditorManager.getInstance().getCurrentClip().getDuration());
        double timesliderPos = currentTime.doubleValue() * timebar.getPrefWidth() / duration.doubleValue();
        timeslider.setLayoutX(startAdjustment + timesliderPos - 3);
    }

    private void initListeners() {
        /*
         * DurationChangeListener
         */
        CinematicEditorManager.getInstance().getCurrentClip().getDurationChangeListeners().add(this);
        /*
         * Magnification bindings and change listener
         */
        maxMagnification.bindBidirectional(zoom.maxProperty());
        magnification.bindBidirectional(zoom.valueProperty());
        magnification.addListener(new ChangeListener<Number>() {
            //TODO Zoom Issues . see notebook
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number mag) {
                if (mag.doubleValue() > 0) {

                    // change current width
                    double currentWidth = mag.doubleValue() * timelineScrollPane.getWidth();
                    timelineScrollPaneVBox.setPrefWidth(currentWidth);
                    timebar.setPrefWidth(currentWidth);
                    currentTime.setValue(timebar.getValue());
                    System.out.println("Current Width : " + currentWidth);

                    //sync timeslider
                    double timesliderPos = currentWidth * timebar.getValue() / timebar.getMax();
                    timesliderPos += startAdjustment - 3;
                    timeslider.setLayoutX(timesliderPos);

                    // change ticks
                    Integer majorTickUnit = new Integer((int) (zoom.getMax() + zoom.getMin() - mag.floatValue()));
                    timebar.setMajorTickUnit(majorTickUnit);
                    int durationNearestSecond = (int) duration.doubleValue() - (((int) duration.doubleValue()) % ((int) zoom.getValue()));
                    timebar.setMinorTickCount(100 / majorTickUnit);
                    // System.out.println("Majot Tick Unit : " + majorTickUnit + " Minor Tick Count "+ timebar.getMinorTickCount() + "Position : " + anchor.getTranslateX());

                    /*
                     * Resize and update X positions of all the eventControls
                     */
                    for (Node strip : timelineScrollPaneVBox.getChildren()) {
                        EventStrip eventStrip = (EventStrip) strip;
                        for (Node control : eventStrip.getChildren()) {
                            EventControl eventControl = (EventControl) control;
                            eventControl.refactorDisplay(mag.doubleValue());
                        }
                    }
                }
            }
        });
        /*
         *  timelineScrollPane horizontal scroll listener 
         */
        timelineScrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double extraDistance = timelineScrollPaneVBox.getPrefWidth() - timelineScrollPane.getPrefWidth();
                double timebarHVal = -1 * t1.doubleValue() * extraDistance;
                timebarTimesliderSuperGroup.setTranslateX(timebarHVal);
                //System.out.println("Margins : " +  getMargin(zoom));
                System.out.println("Positions : " + timebarTimesliderSuperGroup.getTranslateX());
            }
        });
        /*
         *  timebar value change listener. i.e update current time, which invokes the change 
         *  listener on current time to update the timeslider X position
         */
        timebar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                currentTime.setValue(t1);
            }
        });
        /*
         * updates the timeslider's X position when currentTime is changed
         */
        currentTime.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                if (timebar.getValue() != t1) {
                    timebar.setValue(t1.doubleValue());
                }
                boolean isTimesliderSync;
                double expectedPosition = (timebar.getWidth() - endAdjustment - startAdjustment) * timebar.getValue() / timebar.getMax();
                expectedPosition += startAdjustment;
                isTimesliderSync = timeslider.getLayoutX() == expectedPosition;
                System.out.println("Expected Position : " + expectedPosition + " timebar layout : " + timebar.getLayoutX() + " timebar trans" + timebar.getTranslateX() + "Anchor pane " + anchor.getLayoutX() + " : " + anchor.getTranslateX());
                if (!isTimesliderSync) {
                    timeslider.setLayoutX(expectedPosition);
                }
                if (expectedPosition == startAdjustment) {
                    timeslider.setLayoutX(0);
                }
            }
        });
        /*
         * bind currentTimeLabelText 
         */
        StringBinding currentTimeLabelTextBinding = new StringBinding() {
            {
                super.bind(currentTime);
            }
            
            @Override
            protected String computeValue() {
                
                Double val = currentTime.doubleValue();
                /*
                 * rounding to 2 decimal places (as there are 2 zeroes in 100)
                 */
                val = (double) Math.round(val * 10) / 10;
                return val.toString();
            }
        };
        currentTimeLabel.textProperty().bind(currentTimeLabelTextBinding);
        
        enableTimesliderDrag();
        
        
        
        
    }
    
    class Delta {
        
        double x, currentTime;
    }
    
    private void enableTimesliderDrag() {
        final Delta dragDelta = new Delta();
        
        timeslider.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                timeslider.getScene().setCursor(Cursor.H_RESIZE);
            }
        });
        
        timeslider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = timeslider.getLayoutX() - mouseEvent.getX();
                
            }
        });
        
        timeslider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double newPos = mouseEvent.getX() + dragDelta.x;
                dragDelta.currentTime = timebar.getMax() * newPos / (timebar.getWidth() - endAdjustment - startAdjustment);
                currentTime.set(dragDelta.currentTime);
                System.out.println("Mouse Dragged");
            }
        });
        timeslider.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                timeslider.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }
    
    public void resetView() {
        magnification.set(timebar.getPrefWidth() / timelineScrollPane.getPrefWidth());
        timelineScrollPaneVBox.setPrefWidth(timelineScrollPaneVBox.getPrefWidth() / magnification.doubleValue());
        timebar.setPrefWidth(timebar.getPrefWidth() / magnification.doubleValue());
        timeslider.setTranslateX(timeslider.getTranslateX() / magnification.doubleValue());
        magnification.set(1);
        
        duration.setValue(CinematicEditorManager.getInstance().getCurrentClip().getDuration());
        durationInput.setText(duration.getValue().toString());
        timebar.setMax(duration.doubleValue());
        Integer majorTickUnit = new Integer((int) (zoom.getMax()));
        System.out.println("Major Tick Unit : " + majorTickUnit);
        timebar.setMajorTickUnit(majorTickUnit);
        timebar.setMinorTickCount(100 / majorTickUnit);
    }

    public final void initView() {
        resetView();
        Rectangle timebarClip = new Rectangle(0, 0, 660, 190);
        //timebarTimeSliderStackPane.getChildren().add(timebarClip);
        timebarTimeSliderStackPane.setClip(timebarClip);
        //timebarTimesliderSuperGroup
    }
    
    private void initActions() {
        timebar.snapToTicksProperty().set(false);
        snapToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                
                if (!timebar.snapToTicksProperty().getValue()) {
                    System.out.println("Snap On");
                    timebar.snapToTicksProperty().set(true);
                } else {
                    System.out.println("Snap Off");
                    timebar.snapToTicksProperty().set(false);
                }
            }
        });
        
        durationInput.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                final Node tempGraphic = durationInput.getGraphic();
                final TextField durationInputTextField = new TextField(null);
                durationInputTextField.setPrefWidth(35);
                durationInput.setGraphic(durationInputTextField);
                durationInputTextField.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        try {
                            CinematicEditorManager.getInstance().getCurrentClip().setDuration(new Double(durationInputTextField.getText()));
                            /*
                             * DurationChangeListener will change the value of duration (DoubleProperty) of this TimelineControl
                             */
                            System.out.println("duration : " + duration.getValue());
                            timebar.setMax(duration.get());
                            durationInput.setText(duration.getValue().toString());
                        } catch (Exception ex) {
                            System.out.println("invalid duration entered in durationInput");
                            durationInput.setText(duration.getValue().toString());
                        } finally {
                            durationInput.setGraphic(tempGraphic);
                        }
                        
                    }
                });
            }
            
        });


        /*
         * when user clicks on currentTimeLabel it converts into a text field
         * allowing to enter the time to go-to.
         */
        currentTimeLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                final TextField currentTimeTextField = new TextField(null);
                final Node graphic = currentTimeLabel.getGraphic();
                System.out.println("Graphic is instance of Label : " + (graphic instanceof Label));
                currentTimeTextField.setPrefWidth(currentTimeLabel.getMinWidth());
                // currentTimeLabel.textProperty().set(null);
                currentTimeLabel.setGraphic(currentTimeTextField);
                
                currentTimeTextField.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        try {
                            
                            Double val = new Double(currentTimeTextField.getText());
                            timebar.setValue(val);
                            
                        } catch (Exception ex) {
                            //TODO see jme convention
                            ex.printStackTrace();
                            System.out.println("Please enter a valid number value for current time");
                        } finally {
                            currentTimeLabel.setGraphic(graphic);
                        }
                    }
                });
            }
        });
    }

    /**
     * Creates a layer view in the timeline for the given layer at the given
     * index
     *
     * @param index
     * @param layer
     */
    public void addLayerView(int index, Layer layer) {
        EventStrip eventStrip = new EventStrip();
        eventStrip.setTimeline(this);
        eventStrip.setLayer(layer);
        eventStrip.setPrefHeight(CinematicEditorUI.ROW_HEIGHT);
        eventStrip.setMinHeight(USE_PREF_SIZE);
        eventStrip.setMaxHeight(USE_PREF_SIZE);
        /*
         * TODO Add code for rendering events
         */
        for (Event event : layer.getEvents()) {
            addEventControl(event, eventStrip);
        }
        System.out.println("Adding eventStrip in Timeline for " + layer.getName());
        timelineScrollPaneVBox.getChildren().add(index, eventStrip);
    }

    public void removeLayerView(int index) {
        timelineScrollPaneVBox.getChildren().remove(index);
        System.out.println("Removed from timeline T index : " + index);
    }

    public void removeLayer(Layer layer) {
        int index = cinematicEditor.getIndex(layer);
        removeLayerView(index);
        layer.getParent().getChildren().remove(layer);
        
    }
    
    private void addEventControl(Event event, EventStrip eventStrip) {
        EventControl eventControl = new EventControl(event);
        eventStrip.getChildren().add(eventControl);
        System.out.println("Event " + event.getName()
                + "Start Pos : " + event.getStartPoint()
                + "duration : " + event.getDuration());
        eventControl.render(magnification.doubleValue());
        eventControl.refactorDisplay(magnification.doubleValue());
    }

    /**
     * Creates an Event, attaches it to the layer. It then finds and updates the
     * layer view and eventstrip by implicitly calling {@link #addEventControl(com.jme3.gde.cinematic.core.Event)
     * }
     *
     * @param event
     * @param layer
     */
    public void addEvent(Event event, Layer layer) {
        if (!layer.getEvents().contains(event)) {
            layer.getEvents().add(event);
        }
        int index = cinematicEditor.getIndex(layer);
        EventStrip eventStrip = null;
        try {
            eventStrip = (EventStrip) timelineScrollPaneVBox.getChildren().get(index);
        } catch (Exception ex) {
            eventStrip = new EventStrip();
            eventStrip.setTimeline(this);
            eventStrip.setLayer(layer);
            eventStrip.setPrefHeight(CinematicEditorUI.ROW_HEIGHT);
            eventStrip.setMinHeight(USE_PREF_SIZE);
            eventStrip.setMaxHeight(USE_PREF_SIZE);
        } finally {
            addEventControl(event, eventStrip);
        }
    }

    /**
     * removes the event from the UI and data structure
     *
     * @param event
     */
    public void removeEvent(Event event) {
        int index = cinematicEditor.getIndex(event.getLayer());
        EventStrip eventStrip = (EventStrip) timelineScrollPaneVBox.getChildren().get(index);
        for (Node control : eventStrip.getChildren()) {
            EventControl eventControl = (EventControl) control;
            if (eventControl.getEvent() == event) {
                eventStrip.getChildren().remove(eventControl);
                event.getLayer().getChildren().remove(event);
                break;
            }
        }
        
        
    }

    public DoubleProperty getDuration() {
        return duration;
    }
    
    public DoubleProperty getFrameRate() {
        return frameRate;
    }
    
    public ScrollPane getTimelineScrollPane() {
        return timelineScrollPane;
    }
    
    public void setTimelineScrollPane(ScrollPane timelineScrollPane) {
        this.timelineScrollPane = timelineScrollPane;
    }
    
    public VBox getTimelineScrollPaneVBox() {
        return timelineScrollPaneVBox;
    }
    
    public void setTimelineScrollPaneVBox(VBox timelineScrollPaneVBox) {
        this.timelineScrollPaneVBox = timelineScrollPaneVBox;
    }
    
    public DoubleProperty getMagnification() {
        return magnification;
    }
    
    public void setMagnification(DoubleProperty magnification) {
        this.magnification = magnification;
    }
    
    public CinematicEditorUI getCinematicEditor() {
        return cinematicEditor;
    }
    
    public void setCinematicEditor(CinematicEditorUI cinematicEditor) {
        this.cinematicEditor = cinematicEditor;
    }

    public DoubleProperty getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(DoubleProperty currentTime) {
        this.currentTime = currentTime;
    }
    
}
