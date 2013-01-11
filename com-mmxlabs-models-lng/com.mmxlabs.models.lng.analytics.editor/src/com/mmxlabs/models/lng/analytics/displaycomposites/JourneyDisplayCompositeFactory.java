/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class JourneyDisplayCompositeFactory extends DefaultDisplayCompositeFactory {
	@Override
	public IDisplayComposite createSublevelComposite(Composite parent,EClass eClass, IScenarioEditingLocation location) {
		final DefaultDetailComposite s = (DefaultDetailComposite) super.createSublevelComposite(parent, eClass, location);

		s.setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {
			@Override
			public Layout createDetailLayout(MMXRootObject root, EObject value) {
				return new GridLayout(4, false);
			}
		});
		
		return s;
	}
}
