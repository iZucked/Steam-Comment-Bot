/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.composite_inject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;

public class BaseFuelPriceInjector extends BaseComponentHelper {
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, null);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject rootObject, final EObject value) {
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
		final CostModel costModel = ScenarioModelUtil.getCostModel(scenarioModel);
		for (final BaseFuelCost cost : costModel.getBaseFuelCosts()) {
			if (cost.getFuel() == value) {
				return Collections.singletonList((EObject) cost);
			}
		}
		return super.getExternalEditingRange(rootObject, value);
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass displayedClass) {
		final ReferenceInlineEditor numberEditor = new ReferenceInlineEditor(PricingPackage.eINSTANCE.getBaseFuelCost_Index());
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
			public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
				for (final EObject r : range) {
					if (r instanceof BaseFuelCost && ((BaseFuelCost) r).getFuel() == object) {
						numberEditor.display(dialogContext, scenario, r, range);
						return;
					}
				}
			}

			@Override
			public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
				return numberEditor.createControl(parent, dbc, toolkit);
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
			public void setEditorLocked(final boolean locked) {
				numberEditor.setEditorLocked(locked);
			}

			@Override
			public boolean isEditorLocked() {
				return numberEditor.isEditorLocked();
			}

			@Override
			public void setEditorEnabled(final boolean enabled) {
				numberEditor.setEditorEnabled(enabled);
			}

			@Override
			public boolean isEditorEnabled() {
				return numberEditor.isEditorEnabled();
			}

			@Override
			public void setEditorVisible(final boolean visible) {
				numberEditor.setEditorVisible(visible);
			}

			@Override
			public boolean isEditorVisible() {
				return numberEditor.isEditorVisible();
			}

			@Override
			public void addNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean hasLabel() {
				return true;
			}
		});
	}

	@Override
	public int getDisplayPriority() {
		return 10;
	}
}
