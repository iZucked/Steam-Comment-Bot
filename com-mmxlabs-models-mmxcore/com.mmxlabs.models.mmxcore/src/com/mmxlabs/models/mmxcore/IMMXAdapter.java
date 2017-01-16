/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.common.notify.Adapter;

/**
 * Adapters which implement this interface will be switched off during save operations.
 * 
 * @author hinton
 *
 */
public interface IMMXAdapter extends Adapter {
	/**
	 * Called when a save is about to happen
	 */
	public void disable();
	
	/**
	 * Called after a save has finished
	 */
	public void enable();
	
	/**
	 * @since 2.2
	 */
	public void enable(boolean loseNotifications);
}
