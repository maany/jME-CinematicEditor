/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui.jfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author MAYANK
 */
public class TimelineUI extends VBox {

    @FXML
    private Slider timebar;
    @FXML
    private StackPane timelineStackPane;
    @FXML
    private ScrollPane timelineScrollPane;
    @FXML
    private VBox timelineVBox;
    @FXML
    private Separator timeslider;   
    
    DoubleProperty duration = new SimpleDoubleProperty();
    DoubleProperty currentTime = new SimpleDoubleProperty();
    public TimelineUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timeline_ui.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        initListeners();
        
    }

    public void test() {
        System.out.println("Connected to TimelineUI");
    }
    /*********** Listeners ***************/
    public final void initListeners() {
        /**
         * bind timelineScrollPane horizontal scrollbar to change timebar's
         * horizontal position so that the conttents of timelineVBox and
         * timebar move together when scrollbar is used
         *
         */
        timelineScrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double extraDistance = timebar.getPrefWidth() - timelineScrollPane.getPrefWidth();
                double timebarHVal = -1 * t1.doubleValue() * extraDistance;
                double timesliderRefVal = (timebar.getPrefWidth() / timebar.getMax()) * timebar.getValue();
                double timesliderHVal = timebarHVal + timesliderRefVal;
                timebar.setLayoutX(timebarHVal);
                timeslider.setLayoutX(timesliderHVal);
                System.out.println("slider Reference : " + timesliderRefVal );
            }
        });
        

        /**
         * *** current time changes timebar's value ****
         */
        timebar.valueProperty().bindBidirectional(currentTime);

        /**
         * ** currentTime changes timeslider's x position ****
         */
        currentTime.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                double newPos = (timebar.getWidth() / timebar.getMax()) * t1.doubleValue();
                timeslider.setLayoutX(newPos);

            }
        });



    }
 
    
    /***************** DURATION ******************/
    public void setDuration(double duration)
    {
        timebar.setMax(duration);
    }
    public double getDuration()
    {
        return timebar.getMax();
    }
    public DoubleProperty getCurrentTime() {
        return currentTime;
    }
}

