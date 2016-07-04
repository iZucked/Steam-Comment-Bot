/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for PortCost instances
 * 
 * @generated
 */
public class PortCostComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public PortCostComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated
	 */
	public PortCostComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}

	/**
	 * add editors to a composite, using PortCost as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.PORT_COST);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_portsEditor(detailComposite, topClass);
		add_entriesEditor(detailComposite, topClass);
		add_referenceCapacityEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the ports feature on PortCost
	 * 
	 * @generated
	 */
	protected void add_portsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PORT_COST__PORTS));
	}

	/**
	 * Create the editor for the entries feature on PortCost
	 * 
	 * @generated NOT
	 */
	protected void add_entriesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PORT_COST__ENTRIES));

		// add an inline editor for each port capability
		for (final PortCapability capability : PortCapability.values()) {

			if (capability != PortCapability.TRANSFER || LicenseFeatures.isPermitted("features:shiptoship")) {

				final NumberInlineEditor numberEditor = new NumberInlineEditor(PricingPackage.eINSTANCE.getPortCostEntry_Cost());
				detailComposite.addInlineEditor(new IInlineEditor() {

					@Override
					public void setLabel(Label label) {
						label.setText(capability.getName() + " cost");
					}

					@Override
					public void setCommandHandler(ICommandHandler handler) {
						numberEditor.setCommandHandler(handler);
					}

					@Override
					public void processValidation(IStatus status) {
						numberEditor.processValidation(status);
					}

					@Override
					public EStructuralFeature getFeature() {
						return numberEditor.getFeature();
					}

					@Override
					public void display(final IDialogEditingContext dialogContext, MMXRootObject scenario, EObject object, final Collection<EObject> range) {
						// mangle
						if (object instanceof PortCost) {
							final PortCost pc = (PortCost) object;
							for (final PortCostEntry entry : pc.getEntries()) {
								if (entry.getActivity() == capability) {
									numberEditor.display(dialogContext, scenario, entry, range);
									return;
								}
							}
							final PortCostEntry entry = PricingFactory.eINSTANCE.createPortCostEntry();
							entry.setActivity(capability);
							pc.getEntries().add(entry);
							numberEditor.display(dialogContext, scenario, entry, range);
						}
					}

					@Override
					public Control createControl(Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
						return numberEditor.createControl(parent, dbc, toolkit);
					}

					@Override
					public void setEditorLocked(boolean locked) {
						numberEditor.setEditorLocked(locked);
					}

					@Override
					public boolean isEditorLocked() {
						return numberEditor.isEditorLocked();
					}

					@Override
					public void setEditorEnabled(boolean enabled) {
						numberEditor.setEditorEnabled(enabled);
					}

					@Override
					public boolean isEditorEnabled() {
						return numberEditor.isEditorEnabled();
					}

					@Override
					public void setEditorVisible(boolean visible) {
						numberEditor.setEditorVisible(visible);
					}

					@Override
					public boolean isEditorVisible() {
						return numberEditor.isEditorVisible();
					}

					@Override
					public EObject getEditorTarget() {
						return numberEditor.getEditorTarget();
					}

					@Override
					public Label getLabel() {
						return numberEditor.getLabel();
					}

					@Override
					public void addNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
						// TODO Auto-generated method stub

					}

					@Override
					public void removeNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean hasLabel() {
						return true;
					}

					@Override
					public Object createLayoutData(MMXRootObject root,
							EObject value, Control control) {
						// TODO Auto-generated method stub
						return null;
					}					
				
				});
			}
		}
	}

	/**
	 * Create the editor for the referenceCapacity feature on PortCost
	 * 
	 * @generated
	 */
	protected void add_referenceCapacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PORT_COST__REFERENCE_CAPACITY));
	}
}