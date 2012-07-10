/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.composite_inject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;

public class BaseFuelPriceInjector extends BaseComponentHelper {
	@Override
	public void addEditorsToComposite(IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, null);
	}

	@Override
	public List<EObject> getExternalEditingRange(MMXRootObject root,
			EObject value) {
		final PricingModel pricingModel = root.getSubModel(PricingModel.class);
		for (final BaseFuelCost cost : pricingModel.getFleetCost().getBaseFuelPrices()) {
			if (cost.getFuel() == value) {
				return Collections.singletonList((EObject) cost);
			}
		}
		return super.getExternalEditingRange(root, value);
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass displayedClass) {
		final NumberInlineEditor numberEditor = new NumberInlineEditor(PricingPackage.eINSTANCE.getBaseFuelCost_Price());
		detailComposite.addInlineEditor(new IInlineEditor() {
			@Override
			public void setLabel(final Label label) {
				numberEditor.setLabel(label);
			}
			
			@Override
			public void setCommandHandler(final ICommandHandler handler) {
				numberEditor.setCommandHandler(handler);
			}
			
			@Override
			public void processValidation(final IStatus status) {
				numberEditor.processValidation(status);
			}
			
			@Override
			public EStructuralFeature getFeature() {
				return numberEditor.getFeature();
			}
			
			@Override
			public void display(final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
				for (final EObject r : range) {
					if (r instanceof BaseFuelCost && ((BaseFuelCost) r).getFuel() == object) {
						numberEditor.display(location, scenario, r, range);
						return;
					}
				}
			}
			
			@Override
			public Control createControl(final Composite parent) {
				return numberEditor.createControl(parent);
			}

			@Override
			public void setEnabled(boolean enabled) {
				numberEditor.setEnabled(enabled);
			}

			@Override
			public EObject getEditorTarget() {
				return numberEditor.getEditorTarget();
			}
		});
	}

	@Override
	public int getDisplayPriority() {
		return 10;
	}
}
