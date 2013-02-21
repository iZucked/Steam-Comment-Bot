package com.mmxlabs.models.lng.analytics.transformer.ui.editors;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.transformer.ui.views.ShippingCostRowViewerPane;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.rcp.common.editors.IPartGotoTarget;

public class ShippingCostPlanModelEditorContribution implements IJointModelEditorContribution, IPartGotoTarget {

	private ShippingCostRowViewerPane editorPane;

	private int editorPage;

	private JointModelEditorPart editorPart;

	/**
	 * Temporary switch to enable/disable this editor during devel
	 */
	private static final boolean TMP_ENABLE_EDITOR = false;

	public ShippingCostPlanModelEditorContribution() {
	}

	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject) {
		this.editorPart = editorPart;

	}

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		if (TMP_ENABLE_EDITOR) {
			editorPane = new ShippingCostRowViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			editorPane.createControl(sash);
			editorPane.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows()), editorPart.getAdapterFactory());

			editorPane.getViewer().setInput(Collections.emptyList());

			editorPage = editorPart.addPage(sash);
			editorPart.setPageText(editorPage, "Shipping");
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

		if (object instanceof ShippingCostPlan) {
			if (editorPane != null) {
				editorPart.setActivePage(editorPage);
				editorPane.getViewer().setInput(object);
			}
		}
	}

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof ShippingCostPlan) {
				return true;
			} else if (dcsd.getTarget() instanceof ShippingCostRow) {
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
			ShippingCostPlan plan = null;
			ShippingCostRow row = null;
			if (dcsd.getTarget() instanceof ShippingCostPlan) {
				plan = (ShippingCostPlan) dcsd.getTarget();
				editorPart.setActivePage(editorPage);
				editorPane.getViewer().setInput(plan);
			} else if (dcsd.getTarget() instanceof ShippingCostRow) {
				row = (ShippingCostRow) dcsd.getTarget();
				editorPart.setActivePage(editorPage);
				editorPane.getViewer().setInput(row.eContainer());
				editorPane.getViewer().setSelection(new StructuredSelection(row), true);
			}
		}
	}
}
