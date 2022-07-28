package com.mmxlabs.models.lng.transfers.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.ui.editorpart.TransferRecordsViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

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
	}
}
