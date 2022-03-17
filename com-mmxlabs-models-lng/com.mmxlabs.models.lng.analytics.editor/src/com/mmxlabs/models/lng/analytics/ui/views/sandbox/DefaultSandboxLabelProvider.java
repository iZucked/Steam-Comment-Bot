/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.SandboxUIHelper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class DefaultSandboxLabelProvider extends CellFormatterLabelProvider {
	protected final Map<Object, IStatus> validationErrors;
	private final String name;
	protected SandboxUIHelper sandboxUIHelper;

	public DefaultSandboxLabelProvider(SandboxUIHelper sandboxUIHelper, final ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, final ETypedElement... pathObjects) {
		super(renderer, new EMFPath(true, pathObjects));
		this.sandboxUIHelper = sandboxUIHelper;
		this.validationErrors = validationErrors;
		this.name = name;
	}

	public DefaultSandboxLabelProvider(SandboxUIHelper sandboxUIHelper, ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, @Nullable EMFPath path) {
		super(renderer, path);
		this.sandboxUIHelper = sandboxUIHelper;
		this.validationErrors = validationErrors;
		this.name = name;
	}
 
	@Override
	protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

		if (validationErrors.containsKey(element)) {
			final IStatus status = validationErrors.get(element);
			Image img = sandboxUIHelper.getValidationImageForStatus(status);
			if (img != null) {
				return null;
			}
		} else {
			if (element instanceof RoundTripShippingOption) {
				return sandboxUIHelper.imgShippingRoundTrip;
			} else if (element instanceof SimpleVesselCharterOption) {
				return sandboxUIHelper.imgShippingFleet;
			}
		}
		return null;
	}

	@Override
	public String getToolTipText(final Object element) {

		final Set<Object> targetElements = getTargetElements(name, element);

		final StringBuilder sb = new StringBuilder();
		for (final Object target : targetElements) {
			if (validationErrors.containsKey(target)) {
				final IStatus status = validationErrors.get(target);
				if (!status.isOK()) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(status.getMessage());
				}
			}
		}
		if (sb.length() > 0) {
			return sb.toString();
		}
		return super.getToolTipText(element);
	}

	private Set<Object> getTargetElements(final String name, final Object element) {
		final Set<Object> targetElements = new HashSet<>();
		targetElements.add(element);
		// FIXME: Hacky!
		if (element instanceof BaseCaseRow) {
			final BaseCaseRow baseCaseRow = (BaseCaseRow) element;
			if (name == null || "Buy".equals(name)) {
				targetElements.add(baseCaseRow.getBuyOption());
			}
			if (name == null || "Sell".equals(name)) {
				targetElements.add(baseCaseRow.getSellOption());
			}
			if (name == null || "Shipping".equals(name)) {
				targetElements.add(baseCaseRow.getShipping());
			}
		}
		if (element instanceof PartialCaseRow) {
			final PartialCaseRow row = (PartialCaseRow) element;
			if (name == null || "Buy".equals(name)) {
				targetElements.addAll(row.getBuyOptions());
			}
			if (name == null || "Sell".equals(name)) {
				targetElements.addAll(row.getSellOptions());
			}
			if (name == null || "Shipping".equals(name)) {
				targetElements.add(row.getShipping());
			}
		}
		targetElements.remove(null);
		return targetElements;
	}

	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);

		final GridItem item = (GridItem) cell.getItem();
		item.setHeaderText("");
		item.setHeaderImage(null);
		cell.setBackground(null);
		final Object element = cell.getElement();

		final Set<Object> targetElements = getTargetElements(null, element);
		IStatus s = org.eclipse.core.runtime.Status.OK_STATUS;
		for (final Object e : targetElements) {
			if (validationErrors.containsKey(e)) {
				final IStatus status = validationErrors.get(e);
				if (!status.isOK()) {
					if (status.getSeverity() > s.getSeverity()) {
						s = status;
					}
				}
			}
		}
		
		sandboxUIHelper.updateGridItem(cell, s);
		 

		if (element instanceof BaseCaseRow || element instanceof PartialCaseRow) {
			if (validationErrors.containsKey(element)) {
				final IStatus status = validationErrors.get(element);
				sandboxUIHelper.updateGridHeaderItem(cell, status);
			}
		}
		setFont(cell, element);
	}

	protected void setFont(ViewerCell cell, Object element2) {
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}