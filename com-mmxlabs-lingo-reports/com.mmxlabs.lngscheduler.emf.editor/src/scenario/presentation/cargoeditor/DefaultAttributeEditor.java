/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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

import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.presentation.cargoeditor.celleditors.DateTimeCellEditor;

public class DefaultAttributeEditor implements IFeatureEditor {
	private final EditingDomain editingDomain;

	public DefaultAttributeEditor(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	@Override
	public IFeatureManipulator getFeatureManipulator(
			final List<EReference> path, final EStructuralFeature field) {
		final EDataType dataType = (EDataType) field.getEType();
		if (dataType.equals(EcorePackage.eINSTANCE.getEDate())) {
			return new DateFeatureManipulator(path, field);
		} else {
			return new DefaultFeatureManipulator(path, field);
		}
	}

	/**
	 * Manipulator which handles dates and times. If the date and time is
	 * contained in an EObject with a port reference, it will display and edit
	 * in local time.
	 * 
	 * @author hinton
	 * 
	 */
	private class DateFeatureManipulator implements IFeatureManipulator {
		private final EReference portReference;
		private final EStructuralFeature field;
		private final List<EReference> path;

		public DateFeatureManipulator(List<EReference> path,
				final EStructuralFeature field) {
			final EClass container = field.getEContainingClass();
			// check for associated port reference
			EReference portReference = null;
			for (final EReference ref : container.getEAllReferences()) {
				if (ref.isMany())
					continue;
				if (ref.getEReferenceType().equals(
						PortPackage.eINSTANCE.getPort())) {
					portReference = ref;
					break;
				}
			}
			this.portReference = portReference;
			this.field = field;
			this.path = path;
		}

		private TimeZone getTimeZone(final EObject target) {
			String timeZoneCode = "UTC";

			if (portReference != null) {
				final Port port = (Port) target.eGet(portReference);
				if (port != null) {
					if (port.getTimeZone() != null) {
						timeZoneCode = port.getTimeZone();
					}
				}
			}

			return TimeZone.getTimeZone(timeZoneCode);
		}

		private Calendar getCalendar(final EObject target) {
			if (target == null)
				return null;
			final Date date = (Date) target.eGet(field);
			if (date == null)
				return null;

			final Calendar calendar = Calendar.getInstance(getTimeZone(target));

			calendar.setTime(date);

			return calendar;
		}

		private EObject getTarget(EObject o) {
			final Iterator<EReference> it = path.iterator();
			while (o != null && it.hasNext()) {
				o = (EObject) o.eGet(it.next());
			}
			return o;
		}

		@Override
		public String getStringValue(final EObject o) {
			final EObject target = getTarget(o);
			final Calendar calendar = getCalendar(target);
			if (calendar == null)
				return "";
			
			return String.format("%02d/%02d/%d %02d:%02d %s", 
					calendar.get(Calendar.DAY_OF_MONTH),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					calendar.getTimeZone().getDisplayName(false, TimeZone.SHORT));
		}

		@Override
		public void setFromEditorValue(final EObject o, final Object value) {
			final EObject target = getTarget(o);
			final TimeZone tz = getTimeZone(target);
			final Calendar calendar = (Calendar) value;
			calendar.setTimeZone(tz);
			final Date newValue = calendar.getTime();
			
			System.err.println("Updating " + field.getName() + " on " + target);
			System.err.println("Value is " + target.eGet(field) + ", will become " + newValue);
			
			editingDomain.getCommandStack().execute(
					editingDomain.createCommand(SetCommand.class,
							new CommandParameter(target, field, newValue)));
		}

		@Override
		public boolean canModify(final EObject row) {
			return true;
		}

		@Override
		public CellEditor createCellEditor(final Composite parent) {
			return new DateTimeCellEditor(parent);
		}

		@Override
		public Object getEditorValue(final EObject row) {
			return getCalendar(getTarget(row));
		}

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
				final Text control = (Text) result.getControl();
				// checks on each keypress that the keypress triggered a number
				control.addVerifyListener(new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						final String s = control.getText() + e.text;
						try {
							int i = Integer.parseInt(s);
							e.doit = true;
						} catch (final NumberFormatException ex) {
							e.doit = false;
						}
					}
				});

				return result;
			} else if (fieldType.equals(ecp.getEString())) {
				return new TextCellEditor(parent);
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
