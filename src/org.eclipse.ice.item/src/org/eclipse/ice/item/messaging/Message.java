/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.messaging;

/**
 * The Message class is a simple Java Bean that contains information from an ICE
 * subsystem, a remote ICE subsystem or an external third process about the
 * progress of worked performed by ICE.
 * 
 * Messages cannot be copied or cloned.
 * 
 * Messages are used, for example, to post real time updates.
 * 
 * @author Jay Jay Billings
 */
public class Message {
	
	/**
	 * An optional id for this message.
	 */
	private int id;
	
	/**
	 * The id of the Item which which this Message should be associated.
	 */
	private int itemId;
	
	/**
	 * The type of the message. One of FILE_CREATED, FILE_MODIFIED,
	 * FILE_DELETED, MESSAGE_POSTED, PROGRESS_UPDATED, CONVERGENCE_UPDATED,
	 * UPDATER_STARTED, or UPDATER_STOPPED.
	 */
	private String type;
	
	/**
	 * The contents of the Message.
	 */
	private String message;

	/**
	 * The constructor.
	 */
	public Message() {

		id = 0;
		itemId = 0;
		type = "";
		message = "";

	}

	/**
	 * This operation returns the id of the Message.
	 * 
	 * @return The id
	 */
	public int getId() {
		return id;
	}

	/**
	 * This operation returns the id of the Item with which the Message is
	 * associated.
	 * 
	 * @return The ItemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * This operation returns the type of the Message.
	 * 
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * This operation returns the content of the Message.
	 * 
	 * @return The content
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * This operation sets the id of the Message.
	 * 
	 * @param id The id
	 */
	public void setId(int id) {
		this.id = id;
		return;
	}

	/**
	 * This operation sets the id of the Item with which the Message is
	 * associated.
	 * 
	 * @param itemId The ItemId
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
		return;
	}

	/**
	 * This operation sets the type of the Message.
	 * 
	 * @param type The type
	 */
	public void setType(String type) {
		this.type = type;
		return;
	}

	/**
	 * This operation sets the content of the Message.
	 * 
	 * @param content The content
	 */
	public void setMessage(String content) {
		message = content;
		return;
	}
}