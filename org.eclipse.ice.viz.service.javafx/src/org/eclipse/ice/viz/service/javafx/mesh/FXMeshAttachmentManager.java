/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.mesh;

import java.util.ArrayList;

import org.eclipse.ice.viz.service.javafx.canvas.AbstractAttachmentManager;
import org.eclipse.ice.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.ice.viz.service.javafx.scene.model.IAttachment;

/**
 * Manages Mesh Editor attachments.
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXMeshAttachmentManager extends AbstractAttachmentManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.javafx.internal.model.geometry.
	 * AbstractAttachmentManager#allocate()
	 */
	@Override
	public IAttachment allocate() {
		if (active == null) {
			active = new ArrayList<>();
		}

		// Create a geometry attachment and add it to the list of active
		// attachments
		FXAttachment attach = new FXMeshAttachment(this);
		active.add(attach);

		return attach;
	}
}
