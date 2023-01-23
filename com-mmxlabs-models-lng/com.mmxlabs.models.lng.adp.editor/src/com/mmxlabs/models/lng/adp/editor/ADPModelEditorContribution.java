/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FleetConstraint;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorViewerPane;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 */
public class ADPModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int adpEditorIndex = -1;
	private ADPEditorViewerPane adpViewer;

	@Override
	public void addPages(final Composite parent) {

		if (LicenseFeatures.isPermitted("features:adp")) {

			adpViewer = new ADPEditorViewerPane(editorPart.getSite().getPage(), editorPart, editorPart.getEditorSite().getActionBars());
			adpViewer.createControl(parent);
			adpViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			adpEditorIndex = editorPart.addPage(adpViewer.getControl());
			editorPart.setPageText(adpEditorIndex, "ADP");

			PlatformUI.getWorkbench().getHelpSystem().setHelp(adpViewer.getControl(), "com.mmxlabs.lingo.doc.Editor_ADP");
		}

	}

	@Override
	public void setLocked(final boolean locked) {
		if (adpViewer != null) {
			adpViewer.setLocked(locked);
		}

	}

	private static final Class<?>[] handledClasses = { ADPModel.class, ContractProfile.class, SubContractProfile.class, FleetProfile.class, ProfileConstraint.class, FleetConstraint.class,
			PeriodDistribution.class };

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();

			for (final Class<?> clazz : handledClasses) {
				if (clazz.isInstance(target)) {
					return true;
				}
			}
			for (final EObject o : dcsd.getObjects()) {
				for (final Class<?> clazz : handledClasses) {
					if (clazz.isInstance(o)) {
						return true;
					}
				}
			}

		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			EObject target = dcsd.getTarget();

			// Look in child items for potentially handles classes.
			{
				boolean foundTarget = false;
				for (final Class<?> clazz : handledClasses) {
					if (clazz.isInstance(target)) {
						EObject t = unwrapTarget(target);
						if (t != null) {
							target = t;
							foundTarget = true;
						}
						break;
					}
				}
				if (!foundTarget) {
					for (final EObject o : dcsd.getObjects()) {
						for (final Class<?> clazz : handledClasses) {
							if (clazz.isInstance(o)) {
								EObject t = unwrapTarget(o);
								if (t != null) {
									target = t;
									foundTarget = true;
									break;
								}
							}
						}
					}
				}
			}

			if (target != null && adpViewer != null) {
				editorPart.setActivePage(adpEditorIndex);
				adpViewer.setValidationTarget(target);
				return;
			}

		}
	}

	private EObject unwrapTarget(EObject object) {
		if (object instanceof ADPModel) {
			return object;
		}
		if (object instanceof FleetProfile) {
			return object;
		}
		if (object instanceof ContractProfile<?, ?>) {
			return object;
		}
		if (object instanceof SubContractProfile<?, ?>) {
			return unwrapTarget(object.eContainer());
		}
		if (object instanceof ProfileConstraint) {
			return unwrapTarget(object.eContainer());
		}
		if (object instanceof FleetConstraint) {
			return unwrapTarget(object.eContainer());
		}
		if (object instanceof PeriodDistribution) {
			return unwrapTarget(object.eContainer());
		}

		return null;
	}
}
