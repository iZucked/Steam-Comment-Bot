package com.mmxlabs.models.ui;

/**
 * Instances of this interface are used to populate AbstractDetailComposite
 * 
 * @author hinton
 *
 */
public interface IComponentHelper {
	/**
	 * Called when a composite is being filled with editors for a particular type.
	 */
	public void addEditorsToComposite(IInlineEditorContainer detailComposite);
}
