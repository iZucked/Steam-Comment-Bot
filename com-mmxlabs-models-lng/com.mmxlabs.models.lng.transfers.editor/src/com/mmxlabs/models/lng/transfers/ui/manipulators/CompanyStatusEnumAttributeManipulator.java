package com.mmxlabs.models.lng.transfers.ui.manipulators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transfers.CompanyStatus;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

/**
 * Manipulator for Transfer Company Status Enum
 * @author FM
 *
 */
public class CompanyStatusEnumAttributeManipulator extends ValueListAttributeManipulator {

	public CompanyStatusEnumAttributeManipulator(EAttribute field, ICommandHandler commandHandler) {
		super(field, commandHandler, getValues((EEnum) field.getEAttributeType()));
	}
	
	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		values.add(new Pair<String, Object>("Intra-company", CompanyStatus.INTRA));
		values.add(new Pair<String, Object>("Inter-company", CompanyStatus.INTER));
		return values;
	}

}
