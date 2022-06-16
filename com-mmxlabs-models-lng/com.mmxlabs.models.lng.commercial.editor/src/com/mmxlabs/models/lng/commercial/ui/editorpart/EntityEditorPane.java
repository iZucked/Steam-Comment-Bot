/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class EntityEditorPane extends ScenarioTableViewerPane {
	public EntityEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		addNameManipulator("Name");

		defaultSetTitle("Entities");
	}

	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return new ScenarioTableViewerDeleteAction(callback) {
			@Override
			protected boolean isApplicableToSelection(final ISelection selection) {

				if (selection instanceof final IStructuredSelection ss) {
					final CommercialModel m = ScenarioModelUtil.getCommercialModel(getScenarioEditingLocation().getScenarioDataProvider());
					// We need at least 2 entities to be able to delete one
					if (m.getEntities().size() < 2) {
						return false;
					}

					// We cannot delete everything
					if (m.getEntities().size() == ss.size()) {
						return false;
					}

					// We cannot delete custom entities
					final Iterator<?> itr = ss.iterator();
					while (itr.hasNext()) {
						final Object obj = itr.next();
						if (!(obj instanceof LegalEntity)) {
							return false;
						}
					}
					return true;
				}

				return false;
			}
		};
	}
}
