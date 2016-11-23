package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class DefaultSandboxLabelProvider extends CellFormatterLabelProvider {
	protected final Map<Object, IStatus> validationErrors;
	private final String name;

	public DefaultSandboxLabelProvider(final ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, final ETypedElement... pathObjects) {
		super(renderer, new EMFPath(true, pathObjects));
		this.validationErrors = validationErrors;
		this.name = name;
	}
	public DefaultSandboxLabelProvider(ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, @Nullable EMFPath path) {
		super(renderer, path);
		this.validationErrors = validationErrors;
		this.name = name;
	}

	Image imgError = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/error.gif").createImage();
	Image imgWarn = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/warning.gif").createImage();
	Image imgInfo = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/information.gif").createImage();
	Image imgShippingRoundTrip = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/roundtrip.png").createImage();
	Image imgShippingFleet = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/fleet.png").createImage();
	Image imgModel = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/console_view.gif").createImage();

	Color colour_error = new Color(Display.getDefault(), new RGB(255, 100, 100));
	Color colour_warn = new Color(Display.getDefault(), new RGB(255, 255, 200));
	Color colour_info = new Color(Display.getDefault(), new RGB(200, 240, 240));

	@Override
	protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

		if (validationErrors.containsKey(element)) {
			final IStatus status = validationErrors.get(element);
			if (!status.isOK()) {
				if (status.matches(IStatus.ERROR)) {
					return imgError;
				}
				if (status.matches(IStatus.WARNING)) {
					return imgWarn;
				}
				if (status.matches(IStatus.INFO)) {
					return imgWarn;
				}
			}
		} else {
			if (element instanceof RoundTripShippingOption) {
				return imgShippingRoundTrip;
			} else if (element instanceof FleetShippingOption) {
				return imgShippingFleet;
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
		if (!s.isOK()) {
			if (s.matches(IStatus.ERROR)) {
				cell.setBackground(colour_error);
				item.setBackground(colour_error);
			} else if (s.matches(IStatus.WARNING)) {
				cell.setBackground(colour_warn);
				item.setBackground(colour_warn);
			} else if (s.matches(IStatus.INFO)) {
				cell.setBackground(colour_info);
				item.setBackground(colour_info);
			}
		}

		if (element instanceof BaseCaseRow || element instanceof PartialCaseRow) {
			if (validationErrors.containsKey(element)) {
				final IStatus status = validationErrors.get(element);
				if (!status.isOK()) {
					if (status.matches(IStatus.ERROR)) {
						item.setHeaderImage(imgError);
					} else if (status.matches(IStatus.WARNING)) {
						item.setHeaderImage(imgWarn);
					} else if (status.matches(IStatus.INFO)) {
						item.setHeaderImage(imgInfo);
					}
				}
			}
		}
		setFont(cell, element);
	}

	protected void setFont(ViewerCell cell, Object element2) {
	}
	
	@Override
	public void dispose() {
		imgError.dispose();
		imgWarn.dispose();
		imgInfo.dispose();
		imgShippingRoundTrip.dispose();
		imgShippingFleet.dispose();
		imgModel.dispose();
		colour_error.dispose();
		colour_info.dispose();
		colour_warn.dispose();
		super.dispose();
	}
}