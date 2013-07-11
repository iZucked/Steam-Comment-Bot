/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite to show a commodity index (not including its DataIndex children)
 * @author Simon McGregor
 * 
 */
public class CommodityIndexTopLevelComposite extends DefaultTopLevelComposite {

	public CommodityIndexTopLevelComposite(final Composite parent, final int style, final IScenarioEditingLocation location) {
		super(parent, style, location);
	}

	/**
	 * Don't display DataIndex child objects for editing.
	 */
	@Override
	protected void createChildArea(final MMXRootObject root, final EObject object, final Composite parent, final EReference ref, final EObject value) {
		/*
		 * Note: the logic is implemented in the #createChildArea method because 
		 * the #shouldDisplay method does not permit the particular value of
		 * a child object to be interrogated.
		 */
		
		if ((value instanceof DataIndex) == false) {		
			super.createChildArea(root, object, parent, ref, value);
		}
	}


}
