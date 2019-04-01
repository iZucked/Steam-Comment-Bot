/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.displaycomposites;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

/**
 * @author Simon Goodall
 * 
 */
public class SpotMarketsDetailComposite extends DefaultDetailComposite {

	private final DetailGroup contractDetailGroup;

	public enum DetailGroup {
		GENERAL, RESTRICTIONS, AVAILABILITY, PRICING
	}

	public SpotMarketsDetailComposite(final Composite parent, final int style, final DetailGroup contractDetailGroup, final FormToolkit toolkit) {
		super(parent, style, toolkit);

		this.contractDetailGroup = contractDetailGroup;
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {
		if (editor == null) {
			return null;
		}
		// By default all elements are in the main tab
		DetailGroup cdg = DetailGroup.GENERAL;

		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedContractsArePermissive() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedPortsArePermissive() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedVesselsArePermissive() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedContracts() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedPorts() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedVessels() //
		) {
			cdg = DetailGroup.RESTRICTIONS;
		}
		if (editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_Availability() //
		) {
			cdg = DetailGroup.AVAILABILITY;
		}

		// Do not add elements if they are for the wrong section.
		if (contractDetailGroup != cdg) {
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(6, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				// Special case for min/max volumes - ensure text box has enough width for around 7 digits.
				// Note: Should really render the font to get width - this is ok on my system, but other systems (default font & size, resolution, dpi etc) could make this wrong
				final EStructuralFeature feature = editor.getFeature();
				if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__MAX_QUANTITY || feature == SpotMarketsPackage.Literals.SPOT_MARKET__MIN_QUANTITY
						|| feature == SpotMarketsPackage.Literals.SPOT_MARKET__VOLUME_LIMITS_UNIT
				// || feature == SpotMarketsPackage.Literals.SPOT_MARKET__OPERATIONAL_TOLERANCE
				) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__MAX_QUANTITY || feature == SpotMarketsPackage.Literals.SPOT_MARKET__MIN_QUANTITY) {
						gd.widthHint = 80;
					}

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__MIN_QUANTITY) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Volume");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}

					return gd;
				}
				
				if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS 
						|| feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 2;
					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS) {
						
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Contracts");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}

					return gd;
				}
				if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS 
						|| feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 2;
					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Ports");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}

					return gd;
				}
				if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS 
						|| feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 2;
					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Vessels");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}

					return gd;
				}

				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 5;
				return gd;
			}
		};
	}
}
