/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.VesselFillVolumeInlineEditor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;


public class FillCapacityDetailComposite extends DefaultDetailComposite implements IDisplayComposite {
	class FillCapacityDisplayCompositeLayoutProvider extends DefaultDisplayCompositeLayoutProvider {
		@Override
		public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
			return new GridLayout(4, false);
		}
		
		@Override
		public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
			final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
			
			// Special case for fill capacity.
			final var feature = editor.getFeature();
			if (feature == FleetPackage.Literals.VESSEL__FILL_CAPACITY || editor.getProxy() instanceof VesselFillVolumeInlineEditor) {
				if (editor.getProxy() instanceof VesselFillVolumeInlineEditor) {
					final Label label = editor.getLabel();
					if (label != null) {
						label.setText("");
						label.setToolTipText("");
					}
					editor.setLabel(null);
				}
				gd.horizontalSpan = 1;
			}
			else {
				gd.horizontalSpan = 3;
			}
			return gd;
		}
	}

	public FillCapacityDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new FillCapacityDisplayCompositeLayoutProvider();
	}
}
