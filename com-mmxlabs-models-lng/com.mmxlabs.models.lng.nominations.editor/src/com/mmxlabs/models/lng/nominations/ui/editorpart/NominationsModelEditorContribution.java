/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class NominationsModelEditorContribution extends BaseJointModelEditorContribution<NominationsModel> {

	private NominationSpecsViewerPane nsvPane;
	private AbstractNominationsViewerPane snvPane;

	private int nsvPage = -1;
	
	@Override
	public void addPages(final Composite parent) {

			final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
			nsvPane = new NominationSpecsViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			nsvPane.createControl(sash);
			nsvPane.init(Collections.singletonList(NominationsPackage.eINSTANCE.getNominationsModel_NominationSpecs()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			nsvPane.getViewer().setInput(modelObject);

			snvPane = new RelativeDateRangeNominationsViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			snvPane.createControl(sash);
			snvPane.init(Collections.singletonList(NominationsPackage.eINSTANCE.getNominationsModel_Nominations()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			snvPane.getViewer().setInput(modelObject);
		///PlatformUI.getWorkbench().getHelpSystem().setHelp(indexPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Markets");
			nsvPage = editorPart.addPage(sash);
			editorPart.setPageText(nsvPage, "Nominations");
	}

	@Override
	public void setLocked(final boolean locked) {
		if (nsvPane != null) {
			nsvPane.setLocked(locked);
		}
		if (snvPane != null) {
			snvPane.setLocked(locked);
		}
	}

	@Override
	public boolean canHandle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();
			if (target instanceof AbstractNominationSpec) {
				return true;
			}
			if (target instanceof AbstractNomination) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final Object target = dcsd.getTarget();
			if (target instanceof AbstractNomination) {
				if (snvPane != null) {
					editorPart.setActivePage(nsvPage);
					snvPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				}
				return;
			}
			if (target instanceof AbstractNominationSpec) {
				if (nsvPane != null) {
					editorPart.setActivePage(nsvPage);
					nsvPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				}
				return;
			}
		}
	}
}
