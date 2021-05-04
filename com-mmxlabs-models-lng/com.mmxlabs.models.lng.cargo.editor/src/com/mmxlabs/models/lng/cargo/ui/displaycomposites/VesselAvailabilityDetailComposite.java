/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author alex, FM
 * 
 */
public class VesselAvailabilityDetailComposite extends DefaultDetailComposite {
	
	private final VesselAvailabilityDetailGroup detailGroup;

	public enum VesselAvailabilityDetailGroup{
		TOP_LEFT, TOP_LEFT_EXTRA, TOP_RIGHT, TOP_RIGHT_EXTRA, GENERAL, START, END;
	}
	
	public VesselAvailabilityDetailComposite(Composite parent, int style, FormToolkit toolkit, VesselAvailabilityDetailGroup detailGroup) {
		super(parent, style, toolkit);
		this.detailGroup = detailGroup;
	}	
	
	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {
		
		if (editor == null) {
			return null;
		}

		// By default all elements are in the main tab
		VesselAvailabilityDetailGroup cdg = VesselAvailabilityDetailGroup.GENERAL;
		
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Fleet()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Vessel()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_CharterNumber()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_MinDuration()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration()) {
			cdg = VesselAvailabilityDetailGroup.TOP_LEFT;
		}
		
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Optional()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Entity()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate()) {
			cdg = VesselAvailabilityDetailGroup.TOP_RIGHT;
		}
		
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_GenericCharterContract()) {
			cdg = VesselAvailabilityDetailGroup.TOP_LEFT;
		}

		// Here the exceptions are listed for the elements which should go into the middle composite
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_StartAt()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_StartAfter()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_StartBy()) {
			cdg = VesselAvailabilityDetailGroup.START;
		}
		
		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_EndAt()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_EndAfter()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_EndBy()) {
			cdg = VesselAvailabilityDetailGroup.END;
		}
	
		// Do not add elements if they are for the wrong section.
		if (detailGroup != cdg){
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
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
					gd.widthHint = 64;

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
				
				if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER || feature == CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);

					if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("After (UTC)");
						}
						editor.setLabel(null);
					} else {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("By");
						}
						editor.setLabel(null);
					}

					return gd;
				}
				if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER || feature == CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					
					if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("After (UTC)");
						}
						editor.setLabel(null);
					} else {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("By");
						}
						editor.setLabel(null);
					}
					
					return gd;
				}

				if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__MIN_DURATION || feature == CargoPackage.Literals.VESSEL_AVAILABILITY__MAX_DURATION) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);

					if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__MIN_DURATION) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Duration");
						}
						editor.setLabel(null);
					} else {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText(" up to ");
						}
						editor.setLabel(null);
					}

					return gd;
				}

//				if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__GENERIC_CHARTER_CONTRACT) {
//					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
//
//					if (feature == CargoPackage.Literals.VESSEL_AVAILABILITY__GENERIC_CHARTER_CONTRACT) {
//						final Label label = editor.getLabel();
//						if (label != null) {
//							label.setText("Charter contract");
//						}
//						editor.setLabel(null);
//					}
//					return gd;
//				}
				
				// Anything else needs to fill the space.
				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 3;
				return gd;
			}
		};
	}

}