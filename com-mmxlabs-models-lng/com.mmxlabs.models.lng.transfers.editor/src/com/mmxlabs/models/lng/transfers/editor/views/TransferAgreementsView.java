package com.mmxlabs.models.lng.transfers.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.ui.editorpart.TransferAgreementsViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class TransferAgreementsView extends ScenarioTableViewerView<TransferAgreementsViewerPane>{

	@Override
	protected @NonNull TransferAgreementsViewerPane createViewerPane() {
		return new TransferAgreementsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(TransferAgreementsViewerPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_TransferModel(),
					TransfersPackage.eINSTANCE.getTransferModel_TransferAgreements() }), getAdapterFactory(), getModelReference());
			pane.getViewer().setInput(getRootObject());
		}
	}

}
