/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultAttributeEditor implements IFeatureEditor {
	private final EditingDomain editingDomain;

	public DefaultAttributeEditor(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	@Override
	public IFeatureManipulator getFeatureManipulator(
			final List<EReference> path, final EStructuralFeature field) {
		return new DefaultFeatureManipulator(path, field);
	}

	/**
	 * Simple manipulator which should handle the basic EDataTypes
	 * 
	 * @author hinton
	 * 
	 */
	private class DefaultFeatureManipulator implements IFeatureManipulator {
		private final List<EReference> path;
		private final EStructuralFeature field;
		private final EDataType fieldType;
		private final EFactory factory;

		public DefaultFeatureManipulator(final List<EReference> path,
				final EStructuralFeature field) {
			this.path = path;
			this.field = field;
			this.fieldType = ((EAttribute) field).getEAttributeType();
			this.factory = fieldType.getEPackage().getEFactoryInstance();
		}

		@Override
		public String getStringValue(EObject o) {
			if (o == null)
				return "";
			for (final EReference ref : path) {
				o = (EObject) o.eGet(ref);
				if (o == null)
					return "";
			}
			Object value = o.eGet(field);
			if (value == null)
				return "";
			return value.toString();
		}

		@Override
		public void setFromEditorValue(EObject o, final Object value) {
			assert value instanceof String;
			final String vString = (String) value;

			for (final EReference ref : path) {
				o = (EObject) o.eGet(ref);
				if (o == null)
					return;
			}
			// TODO use EMF edit commands here, because doing this does
			// not appear to work nicely
			try {
				Object eValue = factory.createFromString(fieldType, vString);
				editingDomain.getCommandStack().execute(
						editingDomain.createCommand(SetCommand.class,
								new CommandParameter(o, field, eValue)));
			} catch (Exception ex) {

			}
		}

		@Override
		public boolean canModify(final EObject row) {
			return true;
		}

		@Override
		public CellEditor createCellEditor(final Composite parent) {
			final EcorePackage ecp = EcorePackage.eINSTANCE;
			if (fieldType.equals(ecp.getEInt())
					|| fieldType.equals(ecp.getELong())) {
				final TextCellEditor result = new TextCellEditor(parent);

				// checks on each keypress that the keypress triggered a number
				((Text) result.getControl())
						.addVerifyListener(new VerifyListener() {
							public void verifyText(VerifyEvent e) {
								e.doit = "0123456789".indexOf(e.text) >= 0;
							}
						});

				return result;
			} else if (fieldType.equals(ecp.getEString())) {
				return new TextCellEditor(parent);
				// TODO add in date dialog editor, for example
				// TODO how would that fit in with timezones at ports?
			} else {
				return new TextCellEditor(parent);
			}
		}

		@Override
		public Object getEditorValue(EObject row) {
			return getStringValue(row);
		}
	}
}
