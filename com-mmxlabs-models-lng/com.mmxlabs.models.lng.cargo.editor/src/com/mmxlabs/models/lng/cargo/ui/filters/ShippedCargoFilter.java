package com.mmxlabs.models.lng.cargo.ui.filters;

import java.util.Objects;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;

public class ShippedCargoFilter extends ViewerFilter {
	private final CargoEditorFilterUtils.ShippedCargoFilterOption option;

	@Override
	public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
		return element instanceof final TradesRow row && CargoEditorFilterUtils.shippedCargoFilter(this.getOption(), row.getCargo());
	}

	public CargoEditorFilterUtils.ShippedCargoFilterOption getOption() {
		return option;
	}

	public ShippedCargoFilter(CargoEditorFilterUtils.ShippedCargoFilterOption option) {
		this.option = option;
	}

	@Override
	public int hashCode() {
		return Objects.hash(option);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof ShippedCargoFilter other) {
			return option == other.option;
		}
		return false;
	}
}