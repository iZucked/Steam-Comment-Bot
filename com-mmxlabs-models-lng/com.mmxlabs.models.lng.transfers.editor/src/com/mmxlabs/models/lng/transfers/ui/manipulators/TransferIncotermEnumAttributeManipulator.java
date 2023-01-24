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
import com.mmxlabs.models.lng.transfers.TransferIncoterm;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

/**
 * Editor for Transfer Incoterm Enum
 * @author FM
 *
 */
public class TransferIncotermEnumAttributeManipulator extends ValueListAttributeManipulator {

	public TransferIncotermEnumAttributeManipulator(EAttribute field, ICommandHandler commandHandler) {
		super(field, commandHandler, getValues((EEnum) field.getEAttributeType()));
	}
	
	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		values.add(new Pair<String, Object>("FOB", TransferIncoterm.FOB));
		values.add(new Pair<String, Object>("DES", TransferIncoterm.DES));
		values.add(new Pair<String, Object>("Both", TransferIncoterm.BOTH));
		return values;
	}

}
