/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */
package scenario.presentation.cargoeditor;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Editor for enums
 * @author hinton
 *
 */
public class EnumAttributeManipulator extends BasicAttributeManipulator {
	private final EEnum eenum;

	public EnumAttributeManipulator(final EAttribute field,
			final EditingDomain editingDomain) {
		super(field, editingDomain);
		
		// get the enum 
		this.eenum = (EEnum) field.getEAttributeType();
		
	}

	@Override
	public CellEditor getCellEditor(final Composite c, final Object object) {
		final String [] values = new String[eenum.getELiterals().size()];
		for (final EEnumLiteral literal : eenum.getELiterals()) {
			values[literal.getValue()] = literal.getName();
		}
		return new ComboBoxCellEditor(c, values);
	}

	@Override
	public void setValue(final Object object, final Object value) {
		// value is an Integer
		final int intValue = ((Integer)value).intValue();
		final EEnumLiteral literal = eenum.getEEnumLiteral(intValue);
		// lookup enum value for int value
		super.setValue(object, literal.getInstance());
	}

	@Override
	public Object getValue(final Object object) {
		return ((Enumerator)(super.getValue(object))).getValue();
	}

	@Override
	public String render(final Object object) {
		return eenum.getEEnumLiteral((Integer) getValue(object)).getName();
	}
	
	
}
