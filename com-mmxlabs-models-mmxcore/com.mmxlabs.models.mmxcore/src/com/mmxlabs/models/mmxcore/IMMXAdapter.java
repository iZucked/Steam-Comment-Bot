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
}
