/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author alex
 * 
 */
public class VesselAvailabilityDetailComposite extends DefaultDetailComposite {

	public VesselAvailabilityDetailComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				// //return new FillLayout();
				// return super.createDetailLayout(root, value);

				// TODO: replace this with a GridBagLayout or GroupLayout; for editors without a label,
				// we want the editor to take up two cells rather than one.
				return new GridLayout(4, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				final EStructuralFeature feature = editor.getFeature();
				if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL || feature == CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_NUMBER) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);

					if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Vessel");
						}
						editor.setLabel(null);
					} else {
						gd.widthHint = 16;
						editor.setLabel(null);
					}

					return gd;
				}

				// Anything else needs to fill the space.
				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 3;
				return gd;
			}
		};
	}

}