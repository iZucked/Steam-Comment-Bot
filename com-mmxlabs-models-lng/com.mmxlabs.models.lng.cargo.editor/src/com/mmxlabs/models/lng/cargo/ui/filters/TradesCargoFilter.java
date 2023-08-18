/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.filters;

import java.util.Objects;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesWiringViewerPane;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;

public class TradesCargoFilter extends ViewerFilter {
	/**
	 * 
	 */
	private final Set<String> filtersOpenContracts;
	private final CargoEditorFilterUtils.CargoFilterOption option;

	/**
	 * @param tradesWiringViewerPane
	 */
	public TradesCargoFilter(Set<String> filtersOpenContracts, CargoEditorFilterUtils.CargoFilterOption option) {
		this.filtersOpenContracts = filtersOpenContracts;
		this.option = option;
	}

	@Override
	public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
		return element instanceof final TradesRow row && CargoEditorFilterUtils.cargoTradesFilter(this.getOption(), row.getCargo(), row.getLoadSlot(), row.getDischargeSlot(), filtersOpenContracts);
	}


	public CargoEditorFilterUtils.CargoFilterOption getOption() {
		return option;
	}

	@Override
	public int hashCode() {
		return Objects.hash(filtersOpenContracts, option);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof TradesCargoFilter other) {
			return Objects.equals(filtersOpenContracts, other.filtersOpenContracts) && option == other.option;
		}
		return false;
	}
}