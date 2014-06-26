/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.cinematic.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author MAYANK
 */
public class TableSplitter implements ChangeListener, PropertyChangeListener{
    
    private JTable main;
	private JTable layerContainer;
	private JScrollPane scrollPane;

	/*
	 *  Specify the number of columns to be layerContainer and the scroll pane
	 *  containing the table.
	 */
	public TableSplitter(int layerContainerColumns, JScrollPane scrollPane)
	{
		this.scrollPane = scrollPane;

		main = ((JTable)scrollPane.getViewport().getView());
		main.setAutoCreateColumnsFromModel( false );
		main.addPropertyChangeListener( this );

		//  Use the existing table to create a new table sharing
		//  the DataModel and ListSelectionModel

		int totalColumns = main.getColumnCount();

		layerContainer = new JTable();
		layerContainer.setAutoCreateColumnsFromModel( false );
		layerContainer.setModel( main.getModel() );
		layerContainer.setSelectionModel( main.getSelectionModel() );
		layerContainer.setFocusable( false );

		//  Remove the layerContainer columns from the main table
		//  and add them to the layerContainer table

		for (int i = 0; i < layerContainerColumns; i++)
		{
	        TableColumnModel columnModel = main.getColumnModel();
	        TableColumn column = columnModel.getColumn( 0 );
    	    columnModel.removeColumn( column );
			layerContainer.getColumnModel().addColumn( column );
		}
                System.out.println("Layer COntainer Width from inside : " + layerContainer.getWidth() + " main :" + main.getWidth());
		//  Add the layerContainer table to the scroll pane

        layerContainer.setPreferredScrollableViewportSize(layerContainer.getPreferredSize());
		scrollPane.setRowHeaderView( layerContainer );
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, layerContainer.getTableHeader());

		// Synchronize scrolling of the row header with the main table

		scrollPane.getRowHeader().addChangeListener( this );
	}

	/*
	 *  Return the table being used in the row header
	 */
	public JTable getLayerContainer()
	{
		return layerContainer;
	}
//
//  Implement the ChangeListener
//
	public void stateChanged(ChangeEvent e)
	{
		//  Sync the scroll pane scrollbar with the row header

		JViewport viewport = (JViewport) e.getSource();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}
//
//  Implement the PropertyChangeListener
//
	public void propertyChange(PropertyChangeEvent e)
	{
		//  Keep the layerContainer table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName()))
		{
			layerContainer.setSelectionModel( main.getSelectionModel() );
		}

		if ("model".equals(e.getPropertyName()))
		{
			layerContainer.setModel( main.getModel() );
		}
	}
}
