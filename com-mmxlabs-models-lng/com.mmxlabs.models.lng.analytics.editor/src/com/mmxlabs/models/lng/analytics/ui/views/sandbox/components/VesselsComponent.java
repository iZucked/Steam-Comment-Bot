/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.google.common.collect.Streams;
import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView;
import com.mmxlabs.models.lng.analytics.ui.views.ResultsSetDeletionHelper;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VesselDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.VesselContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;

public class VesselsComponent extends AbstractSandboxComponent<Object, AbstractAnalysisModel> {

	private GridTreeViewer vesselViewer;
	private Object optionModellerView;

	public VesselsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<AbstractAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(final Composite parent, boolean expanded, final IExpansionListener expansionListener, Object optionModellerView) {

		this.optionModellerView = optionModellerView;
		final ExpandableComposite expandableVessels = wrapInExpandable(parent, "Vessels", p -> createVesselOptionsViewer(p).getGrid());
		expandableVessels.setExpanded(expanded);
		expandableVessels.addExpansionListener(expansionListener);
	}

	@Override
	public void refresh() {
		vesselViewer.refresh();
	}

	private GridTreeViewer createVesselOptionsViewer(final Composite vesselComposite) {
		vesselViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(vesselViewer);
		vesselViewer.getGrid().setHeaderVisible(false);

		// vesselViewer.getGrid().setHeaderVisible(true);

		createColumn(vesselViewer, "Vessels", new VesselDescriptionFormatter(), false);
		vesselViewer.setContentProvider(new VesselContentProvider(scenarioEditingLocation));
		hookDragSource(vesselViewer);
		
		
		if (optionModellerView instanceof OptionModellerView view) {
			Action deleteAction = new Action("Delete") {
				@Override
				public void run() {

					if (vesselViewer.getSelection() instanceof IStructuredSelection selection) {
						
						final Collection<EObject> c = new LinkedList<>();
						Streams.<Object>stream(selection.iterator()) //
								.map(EObject.class::cast) //
								.forEach(c::add);
						final CompoundCommand compoundCommand = new CompoundCommand("Delete option");
						if (!c.isEmpty()) {
						compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c));
						}
						if (vesselViewer.getInput() instanceof OptionAnalysisModel model) {
							final Collection<EObject> linkedResults = ResultsSetDeletionHelper.getRelatedResultSets(c, model);
							if (!linkedResults.isEmpty()) {
								compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), linkedResults));
							}
						}
						if (!compoundCommand.isEmpty()) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(compoundCommand, null, null);
						}
					}
				}
			};
			vesselViewer.getControl().addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					view.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
				}

				@Override
				public void focusGained(FocusEvent e) {
					view.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);

				}
			});
		}
		return vesselViewer;
	}

	public void setInput(IScenarioEditingLocation scenarioEditingLocation) {
		vesselViewer.setInput(scenarioEditingLocation);
	}
}
