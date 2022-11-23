package com.mmxlabs.models.lng.analytics.ui.views.sandbox;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.SandboxUIHelper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class MultipleValidationStatusSandboxLabelProvider extends CellFormatterLabelProvider {

	protected final Map<Object, Collection<IStatus>> validationErrors;
	private final String name;
	protected SandboxUIHelper sandboxUiHelper;
	protected final EStructuralFeature featureOfInterest;

	public MultipleValidationStatusSandboxLabelProvider(final SandboxUIHelper sandboxUiHelper, final EStructuralFeature featureOfInterest, final ICellRenderer renderer,
			final Map<Object, Collection<IStatus>> validationErrors, final String name, @Nullable final EMFPath path) {
		super(renderer, path);
		this.sandboxUiHelper = sandboxUiHelper;
		this.name = name;
		this.validationErrors = validationErrors;
		this.featureOfInterest = featureOfInterest;
	}

	public MultipleValidationStatusSandboxLabelProvider(final SandboxUIHelper sandboxUiHelper, final EStructuralFeature featureOfInterest, final ICellRenderer renderer,
			final Map<Object, Collection<IStatus>> validationErrors, final String name, final ETypedElement... pathObjects) {
		super(renderer, new EMFPath(true, pathObjects));
		this.sandboxUiHelper = sandboxUiHelper;
		this.name = name;
		this.validationErrors = validationErrors;
		this.featureOfInterest = featureOfInterest;
	}

	@Override
	protected @Nullable Image getImage(@NonNull ViewerCell cell, @Nullable Object element) {
		final Collection<IStatus> statuses = validationErrors.get(element);
		if (statuses != null && !statuses.isEmpty()) {
			final IStatus mostSevereStatus = statuses.stream().max((s1, s2) -> Integer.compare(s1.getSeverity(), s2.getSeverity())).get();
			final Image img = sandboxUiHelper.getValidationImageForStatus(mostSevereStatus);
			if (img != null) {
				return img;
			}
		}
		return null;
	}

	@Override
	public String getToolTipText(Object element) {
		final Set<Object> targetElements = getTargetElements(name, element);

		final StringBuilder sb = new StringBuilder();
		for (final Object target : targetElements) {
			final Collection<IStatus> statuses = validationErrors.get(target);
			if (statuses != null && !statuses.isEmpty()) {
				for (final IStatus status : statuses) {
					if (!status.isOK()) {
						if (status instanceof IDetailConstraintStatus detailConstraintStatus) {
							final Collection<EStructuralFeature> features = detailConstraintStatus.getFeaturesForEObject((EObject) target);
							if (features.contains(featureOfInterest)) {
								if (sb.length() > 0) {
									sb.append("\n");
								}
								sb.append(status.getMessage());
							}
						}
					}
				}
			}
		}
		return sb.length() > 0 ? sb.toString() : super.getToolTipText(element);
	}

	private Set<Object> getTargetElements(final String name, final Object element) {
		final Set<Object> targetElements = new HashSet<>();
		targetElements.add(element);
		return targetElements;
	}

	@Override
	public void update(ViewerCell cell) {
		super.update(cell);
		final GridItem item = (GridItem) cell.getItem();
		item.setHeaderText("");
		item.setHeaderImage(null);
		cell.setBackground(null);
		final Object element = cell.getElement();

		final Set<Object> targetElements = getTargetElements(null, element);
		IStatus s = org.eclipse.core.runtime.Status.OK_STATUS;
		for (final Object e : targetElements) {
			final Collection<IStatus> statuses = validationErrors.get(e);
			if (statuses != null && !statuses.isEmpty()) {
				final Optional<IStatus> optMostSevereStasus = statuses.stream() //
						.filter(status -> status instanceof IDetailConstraintStatus) //
						.filter(status -> ((IDetailConstraintStatus) status).getFeaturesForEObject((EObject) e).contains(featureOfInterest)) //
						.max((s1, s2) -> Integer.compare(s1.getSeverity(), s2.getSeverity()));
				if (optMostSevereStasus.isPresent()) {
					final IStatus mostSevereStasus = optMostSevereStasus.get();
					if (mostSevereStasus.getSeverity() > s.getSeverity()) {
						s = mostSevereStasus;
					}
				}
			}
		}
		sandboxUiHelper.updateGridItem(cell, s);

		setFont(cell, element);
	}

	protected void setFont(ViewerCell cell, Object element2) {
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
