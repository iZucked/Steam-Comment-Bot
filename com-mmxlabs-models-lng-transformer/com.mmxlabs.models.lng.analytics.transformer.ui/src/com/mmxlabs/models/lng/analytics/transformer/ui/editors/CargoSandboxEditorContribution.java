package com.mmxlabs.models.lng.analytics.transformer.ui.editors;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.CargoSandbox;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.transformer.ui.views.CargoSandboxesViewerPane;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.rcp.common.editors.IPartGotoTarget;

public class CargoSandboxEditorContribution implements IJointModelEditorContribution, IPartGotoTarget {

	private CargoSandboxesViewerPane editorPane;

	private int editorPage;

	private JointModelEditorPart editorPart;

	/**
	 * Temporary switch to enable/disable this editor during devel
	 */
	private static final boolean TMP_ENABLE_EDITOR = true;

	public CargoSandboxEditorContribution() {
	}

	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject) {
		this.editorPart = editorPart;

	}

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		if (TMP_ENABLE_EDITOR) {
			editorPane = new CargoSandboxesViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			editorPane.createControl(sash);
			editorPane.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getCargoSandbox_Cargoes()), editorPart.getAdapterFactory());

			editorPane.getViewer().setInput(Collections.emptyList());

			editorPage = editorPart.addPage(sash);
			editorPart.setPageText(editorPage, "Cargo Sandbox");
		}
	}

	@Override
	public void setLocked(final boolean locked) {
		if (editorPane != null) {
			editorPane.setLocked(locked);
		}
	}

	@Override
	public void dispose() {
		if (editorPane != null) {
			editorPane.dispose();
		}
	}

	@Override
	public void gotoTarget(final Object object) {

		if (object instanceof CargoSandbox) {
			if (editorPane != null) {
				editorPart.setActivePage(editorPage);
				editorPane.setInput((CargoSandbox) object);
			}
		}
	}

	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof CargoSandbox) {
				return true;
			} else if (dcsd.getTarget() instanceof ProvisionalCargo) {
				return true;
			} else if (dcsd.getTarget() instanceof BuyOpportunity) {
				return true;
			} else if (dcsd.getTarget() instanceof SellOpportunity) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (editorPane == null) {
			return;
		}
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			CargoSandbox plan = null;
			ProvisionalCargo row = null;
			if (dcsd.getTarget() instanceof CargoSandbox) {
				plan = (CargoSandbox) dcsd.getTarget();
				editorPart.setActivePage(editorPage);
				editorPane.setInput(plan);
			} else if (dcsd.getTarget() instanceof ProvisionalCargo) {
				row = (ProvisionalCargo) dcsd.getTarget();
				editorPart.setActivePage(editorPage);
				editorPane.setInput((CargoSandbox) row.eContainer());
				editorPane.getViewer().setSelection(new StructuredSelection(row), true);
			}
		}
	}
}
