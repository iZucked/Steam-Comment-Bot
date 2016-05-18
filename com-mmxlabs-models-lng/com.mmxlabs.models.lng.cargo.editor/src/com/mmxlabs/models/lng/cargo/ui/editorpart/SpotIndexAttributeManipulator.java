/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;

/**
 * 
 * An implementation of {@link BasicAttributeManipulator} to handle boolean objects. By default this will show a Y/N choice, but this can changed using the alternative constructor,
 * 
 * @author Simon Goodall
 */
public class SpotIndexAttributeManipulator extends BasicAttributeManipulator {

	private final String nominalOption = "Nominal";
	private int[] optionValues;
	private String[] options;

	public SpotIndexAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	protected CellEditor createCellEditor(final Composite parent, final Object object) {

		String[] options = null;
		int[] optionValues = null;
		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;
			final int currentIndex = assignableElement.getSpotIndex();

			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {

				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				final int spotCharterCount = charterInMarket.getSpotCharterCount();

				final int diff = currentIndex == -1 ? 0 : (currentIndex >= spotCharterCount ? 1 : 0);
				options = new String[1 + spotCharterCount + diff];
				optionValues = new int[options.length];

				// Install nominal option
				options[0] = nominalOption;
				optionValues[0] = -1;

				for (int i = 0; i < spotCharterCount; ++i) {
					options[1 + i] = String.format("%d", 1 + i);
					optionValues[1 + i] = i;
				}
				if (currentIndex >= spotCharterCount) {
					final int idx = options.length - 1;
					options[idx] = String.format("%d", 1 + currentIndex);
					optionValues[idx] = currentIndex;
				}
			} else {
				options = new String[] { String.format("%d", 1 + currentIndex) };
				optionValues = new int[] { currentIndex };
			}
		}

		if (options == null) {
			options = new String[] { "Unknown" };
			optionValues = new int[1];
		}

		this.options = options;
		this.optionValues = optionValues;

		return new ComboBoxCellEditor(parent, options);
	}

	@Override
	public void doSetValue(final Object object, final Object value) {

		if (value instanceof Integer) {
			int idx = (Integer) value;
			int internalValue = optionValues[idx];
			super.doSetValue(object, internalValue);
		} else {
			super.doSetValue(object, value);
		}
	}

	@Override
	public Object getValue(final Object object) {

		final Object object2 = super.getValue(object);
		if (object2 instanceof Integer) {
			int interalValue = (Integer) object2;
			for (int i = 0; i < optionValues.length; ++i) {
				if (optionValues[i] == interalValue) {
					return optionValues[i];
				}
			}
		}

		return 1;
	}

	@Override
	public boolean canEdit(final Object object) {
		return true;
	}

	@Override
	public String render(final Object object) {
		if (object instanceof Integer) {
			int interalValue = (Integer) object;
			for (int i = 0; i < optionValues.length; ++i) {
				if (optionValues[i] == interalValue) {
					return options[i];
				}
			}
		}
		return "Unknown";
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		return render(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return render(object);
	}
}