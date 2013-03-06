/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.SWT;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.editor.editors.CargoTypeInlineEditor;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Cargo instances
 * 
 * @generated
 */
public class CargoComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public CargoComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated
	 */
	public CargoComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.ACARGO));
	}

	/**
	 * add editors to a composite, using Cargo as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CARGO);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated NOT
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers)
			helper.addEditorsToComposite(detailComposite, topClass);

		// This line is not auto-generated
//		add_cargoTypeEditor(detailComposite, topClass);
		add_loadSlotEditor(detailComposite, topClass);
		add_dischargeSlotEditor(detailComposite, topClass);
		add_allowRewiringEditor(detailComposite, topClass);
		add_allowedVesselsEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the cargoType operation on Cargo
	 * 
	 * @generated NO
	 */
	protected void add_cargoTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new CargoTypeInlineEditor(SWT.NONE));
	}

	/**
	 * Create the editor for the loadSlot feature on Cargo
	 * 
	 * @generated NO
	 */
	protected void add_loadSlotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__LOAD_SLOT));
	}

	/**
	 * Create the editor for the dischargeSlot feature on Cargo
	 * 
	 * @generated NO
	 */
	protected void add_dischargeSlotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__DISCHARGE_SLOT));
	}

	/**
	 * Create the editor for the allowRewiring feature on Cargo
	 * 
	 * @generated NOT
	 */
	protected void add_allowRewiringEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__ALLOW_REWIRING);

		editor.addNotificationChangedListener(new IInlineEditorExternalNotificationListener() {

			private IScenarioEditingLocation location;

			private EObject input;

			@Override
			public void postDisplay(final IInlineEditor editor, final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
				this.location = location;
				this.input = object;
			}

			@Override
			public void notifyChanged(final Notification notification) {
				if (notification.getFeature() == InputPackage.eINSTANCE.getElementAssignment_Locked()) {
					editor.setEditorEnabled(!notification.getNewBooleanValue());
					if (notification.getNewBooleanValue()) {
						final ICommandHandler handler = location.getDefaultCommandHandler();
						handler.handleCommand(SetCommand.create(location.getEditingDomain(), input, CargoPackage.eINSTANCE.getCargo_AllowRewiring(), false), input,
								CargoPackage.eINSTANCE.getCargo_AllowRewiring());
					}
				}

			}
		});

		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the allowedVessels feature on Cargo
	 * 
	 * @generated NOT
	 */
	protected void add_allowedVesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__ALLOWED_VESSELS);
		editor.addNotificationChangedListener(new SlotInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}
}