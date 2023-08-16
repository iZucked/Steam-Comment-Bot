package com.mmxlabs.models.lng.cargo.ui.filters;

import java.time.YearMonth;
import java.util.Objects;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;

public class TimePeriodFilter extends ViewerFilter {
	private final CargoEditorFilterUtils.TimeFilterOption option;
	private final YearMonth choice;
	private final int promptMonth;

	@Override
	public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
		if (element instanceof final TradesRow row) {
			return CargoEditorFilterUtils.timePeriodFilter(getOption(), row.getLoadSlot(), row.getDischargeSlot(), getChoice(), getPromptMonth());
		} else {
			return false;
		}
	}

	CargoEditorFilterUtils.TimeFilterOption getOption() {
		return option;
	}

	public YearMonth getChoice() {
		return choice;
	}



	public int getPromptMonth() {
		return promptMonth;
	}
	
	public TimePeriodFilter(int promptMonth, CargoEditorFilterUtils.TimeFilterOption option, YearMonth choice) {
		this.option = option;
		this.choice = choice;
		this.promptMonth = promptMonth;
	}

	public TimePeriodFilter(int promptMonth, CargoEditorFilterUtils.TimeFilterOption option) {
		this.option = option;
		this.choice = null;
		this.promptMonth = promptMonth;
	}

	public TimePeriodFilter(int promptMonth, YearMonth choice) {
		this.option = CargoEditorFilterUtils.TimeFilterOption.NONE;
		this.choice = choice;
		this.promptMonth = promptMonth;
	}

	public TimePeriodFilter(int promptMonth) {
		this.option = CargoEditorFilterUtils.TimeFilterOption.NONE;
		this.choice = null;
		this.promptMonth = promptMonth;
	}

	public TimePeriodFilter(CargoEditorFilterUtils.TimeFilterOption option, YearMonth choice) {
		this.option = option;
		this.choice = choice;
		this.promptMonth = 3;
	}

	public TimePeriodFilter(CargoEditorFilterUtils.TimeFilterOption option) {
		this.option = option;
		this.choice = null;
		this.promptMonth = 3;
	}

	public TimePeriodFilter(YearMonth choice) {
		this.option = CargoEditorFilterUtils.TimeFilterOption.NONE;
		this.choice = choice;
		this.promptMonth = 3;
	}

	public TimePeriodFilter() {
		this.option = CargoEditorFilterUtils.TimeFilterOption.NONE;
		this.choice = null;
		this.promptMonth = 3;
	}

	@Override
	public int hashCode() {
		return Objects.hash(choice, option, promptMonth);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof TimePeriodFilter other) {
			return Objects.equals(choice, other.choice) && option == other.option && promptMonth == other.promptMonth;
		}
		return false;
	}
}