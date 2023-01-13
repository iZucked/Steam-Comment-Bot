/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.ui.manipulators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transfers.TransferStatus;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

/**
 * Manipulator for Transfer Status Enum
 * @author FM
 *
 */
public class TransferStatusEnumAttributeManipulator extends ValueListAttributeManipulator {

	public TransferStatusEnumAttributeManipulator(EAttribute field, ICommandHandler commandHandler) {
		super(field, commandHandler, getValues((EEnum) field.getEAttributeType()));
	}
	
	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		values.add(new Pair<String, Object>("Confirmed", TransferStatus.CONFIRMED));
		values.add(new Pair<String, Object>("Draft", TransferStatus.DRAFT));
		values.add(new Pair<String, Object>("Cancelled", TransferStatus.CANCELLED));
		return values;
	}

}
