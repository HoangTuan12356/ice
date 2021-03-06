/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.eavp.viz.service.IVizCanvas;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.geometry.widgets.ShapeTreeView;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * <p>
 * This class is ICEFormPage that displays the GeometryEditor powered by JavaFX.
 * It automatically opens the ShapeTreeView to allow the user to add and edit
 * geometry.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEGeometryPage extends ICEFormPage
		implements IUpdateableListener {
	/**
	 * <p>
	 * The property that determines whether there is a need to Save.
	 * </p>
	 * 
	 */
	private boolean dirty;

	/**
	 * <p>
	 * The DataStructure that gives this page its data.
	 * </p>
	 * 
	 */
	private GeometryComponent geometryComp;

	/**
	 * The visualization service that is providing the graphical implementation
	 * for the geometry editor.
	 */
	private IVizService service;

	/**
	 * The IVizCanvas responsible for drawing the composite this page will
	 * display.
	 */
	private IVizCanvas vizCanvas;

	/**
	 * <p>
	 * This sets the FormEditor to be opened on, as well as the id and title
	 * Strings.
	 * </p>
	 * 
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ICEGeometryPage(FormEditor editor, String id, String title) {

		super(editor, id, title);

	}

	/**
	 * <p>
	 * Returns the dirty status of the Page.
	 * </p>
	 * 
	 * @return True if the page is dirty (needs to be saved), false otherwise.
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * 
	 * @param allowed
	 */
	public void isSaveAsAllowed(boolean allowed) {
	}

	/**
	 * <p>
	 * Returns the GeometryComponent.
	 * </p>
	 * 
	 * @return The GeometryComponent represented by the page.
	 */
	public GeometryComponent getGeometry() {
		return geometryComp;
	}

	/**
	 * <p>
	 * Sets the geometryComponent; giving the geometryEditor data.
	 * </p>
	 * 
	 * @param geometryComponent
	 */
	public void setGeometry(GeometryComponent geometryComponent) {

		// Unregister from the old component
		if (geometryComp != null) {
			geometryComp.unregister(this);
		}

		// Set the component
		geometryComp = geometryComponent;

		// Register with the new component
		if (geometryComp != null) {
			geometryComp.register(this);
		}
	}

	/**
	 * <p>
	 * Connects the ShapeTreeView with the geometryEditor.
	 * </p>
	 * 
	 */
	public void getFocus() {

		ShapeTreeView shapeTreeView = (ShapeTreeView) getSite()
				.getWorkbenchWindow().getActivePage()
				.findView(ShapeTreeView.ID);
		shapeTreeView.setRenderElementHolder(
				vizCanvas.getRenderElementHolder(geometryComp.getGeometry()));
		shapeTreeView.setGeometry(geometryComp.getGeometry());

		return;
	}

	/**
	 * <p>
	 * Provides the page with the geometryApplication's information to display
	 * geometry.
	 * </p>
	 * 
	 * @param managedForm
	 *            the managed form that handles the page
	 */
	@Override
	public void createFormContent(IManagedForm managedForm) {

		// Local Declarations
		final ScrolledForm form = managedForm.getForm();
		GridLayout layout = new GridLayout();

		// Setup the layout and layout data
		layout.numColumns = 1;
		form.getBody().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		form.getBody().setLayout(new FillLayout());

		// Opening the views in order to interact with the geometryEditor
		try {

			getSite().getWorkbenchWindow().getActivePage()
					.showView(ShapeTreeView.ID);

		} catch (PartInitException e) {
			logger.error(getClass().getName() + " Exception!", e);
		}

		// Create the geometry composite - get the parent
		org.eclipse.ui.forms.widgets.Form pageForm = managedForm.getForm()
				.getForm();
		Composite parent = pageForm.getBody();

		// Get all the extensions for the viz services
		IConfigurationElement[] configurationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"org.eclipse.eavp.viz.service.IVizService");

		// TODO Provide a better way of choosing a service
		// Iterate through the extensions, finding on with "geometry" in the
		// bundle
		for (IConfigurationElement configurationElement : configurationElements) {
			if (configurationElement.getDeclaringExtension()
					.getNamespaceIdentifier().contains("geometry")) {
				try {
					service = (IVizService) configurationElement
							.createExecutableExtension("class");
				} catch (CoreException e) {
					logger.error(
							"Problem creating IVizService from geometry extension point.");
				}
			}
		}

		geometryComp.setService(service);

		// Create and draw geometry canvas
		try {
			vizCanvas = service.createCanvas(geometryComp.getGeometry());
			vizCanvas.draw(parent);

		} catch (Exception e) {
			logger.error(
					"Error creating Geometry Canvas with Geometry Service.", e);
		}

		getFocus();

		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org
	 * .eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		// If the geometry was updated, the editor is now dirty
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				editor.setDirty(true);
			};
		});
	}

	@Override
	public String getPartName() {
		// This is the name of the page displayed on tab for selection of the
		// geometryEditor
		return super.getPartName();
	}

}