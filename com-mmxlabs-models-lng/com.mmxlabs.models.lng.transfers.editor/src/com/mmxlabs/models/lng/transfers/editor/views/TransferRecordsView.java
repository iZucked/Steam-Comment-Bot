/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.editor.views;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.ui.editorpart.TransferRecordsViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.WrappedSelectionProvider;

/**
 * View to spawn the pane to show the TransferRecords list
 * Spawns the {@link TransferRecordsViewerPane}
 * @author FM
 *
 */
public class TransferRecordsView extends ScenarioTableViewerView<TransferRecordsViewerPane>{

	private TransferRecordsViewerPane pane;
	
	@Override
	protected @NonNull TransferRecordsViewerPane createViewerPane() {
		pane = new TransferRecordsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
		return pane;
	}

	@Override
	protected void initViewerPane(TransferRecordsViewerPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_TransferModel(),
					TransfersPackage.eINSTANCE.getTransferModel_TransferRecords()}), getAdapterFactory(), getModelReference());
			pane.getViewer().setInput(getRootObject());
		}
		
		// Wrap the viewer to map internal data to external data for the rest of the application.
		final ISelectionProvider wrappedSelectionProvider = new WrappedSelectionProvider(this::convertSelection);
		pane.getViewer().addSelectionChangedListener(e -> wrappedSelectionProvider.setSelection(e.getSelection()));
		getSite().setSelectionProvider(wrappedSelectionProvider);
	}
	
	private ISelection convertSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection sel) {
			final List<Object> newSelection = new LinkedList<>();
			for (final Object obj : sel.toList()) {
				if (obj instanceof TransferRecord tr && tr.getLhs() != null) {
					newSelection.add(tr.getLhs());
				}
			}
			return SelectionHelper.adaptSelection(newSelection);
		}
		return selection;
	}
}
