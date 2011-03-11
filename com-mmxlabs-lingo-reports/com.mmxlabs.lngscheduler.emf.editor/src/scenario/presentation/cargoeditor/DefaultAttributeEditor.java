/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor;

import java.util.Calendar;
import java.util.Date;
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
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.presentation.cargoeditor.celleditors.DateTimeCellEditor;
import scenario.presentation.cargoeditor.celleditors.SpinnerCellEditor;

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
			return new DateFeatureManipulator(path, field, editingDomain);
		} else if (dataType.equals(EcorePackage.eINSTANCE.getEBoolean())) {
			return new BooleanFeatureManipulator(path, field, editingDomain);
		} else if (dataType.equals(EcorePackage.eINSTANCE.getEInt())
				|| dataType.equals(EcorePackage.eINSTANCE.getEFloat())) {
			return new NumberFeatureManipulator(path, field, editingDomain);
		} else {
			return new DefaultFeatureManipulator(path, field, editingDomain);
		}
	}

	private class NumberFeatureManipulator extends BaseFeatureManipulator {
		protected NumberFeatureManipulator(final List<EReference> path,
				final EStructuralFeature field,
				final EditingDomain editingDomain) {
			super(path, field, editingDomain);
		}

		@Override
		public String getStringValue(EObject row) {
			return getFieldValue(row).toString();
		}

		@Override
		public void setFromEditorValue(EObject row, Object value) {
			doSetCommand(getTarget(row), value);
		}

		@Override
		public boolean canModify(EObject row) {
			return true;
		}

		@Override
		public CellEditor createCellEditor(final Composite parent) {
			final SpinnerCellEditor editor = new SpinnerCellEditor(parent);

			if (!field.getEType().equals(EcorePackage.eINSTANCE.getEInt())) {
				editor.setDigits(3);
			} else {
				editor.setDigits(0);
			}

			editor.setMinimumValue((Integer) 0);
			editor.setMaximumValue((Integer) 1000000); // cannot use
														// Integer.MAX_VALUE
														// because of how
														// spinners work.

			return editor;
		}

		@Override
		public Object getEditorValue(EObject row) {
			return getFieldValue(row);
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
	private class DateFeatureManipulator extends BaseFeatureManipulator {
		private final EReference portReference;

		public DateFeatureManipulator(final List<EReference> path,
				final EStructuralFeature field,
				final EditingDomain editingDomain) {
			super(path, field, editingDomain);
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

		@Override
		public String getStringValue(final EObject o) {
			final EObject target = getTarget(o);
			final Calendar calendar = getCalendar(target);
			if (calendar == null)
				return "";

			return String.format("%02d/%02d/%d %02d:%02d %s", calendar
					.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.YEAR), calendar
							.get(Calendar.HOUR_OF_DAY), calendar
							.get(Calendar.MINUTE), calendar.getTimeZone()
							.getDisplayName(false, TimeZone.SHORT));
		}

		@Override
		public void setFromEditorValue(final EObject o, final Object value) {
			final EObject target = getTarget(o);
			final TimeZone tz = getTimeZone(target);
			final Calendar calendar = (Calendar) value;
			calendar.setTimeZone(tz);
			final Date newValue = calendar.getTime();

			doSetCommand(target, newValue);
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
	 * Format a boolean as Yes or No, and display a checkbox to edit (TODO could
	 * be a dropdown instead?)
	 * 
	 * @author hinton
	 * 
	 */
	private class BooleanFeatureManipulator extends BaseFeatureManipulator {
		private static final String CHECKED_KEY = "CHECKED";
		private static final String UNCHECKED_KEY = "UNCHECKED";

		private Image makeShot(Control control, boolean type) {
			// Hopefully no platform uses exactly this color
			// because we'll make it transparent in the image.
			Color greenScreen = new Color(control.getDisplay(), 222, 223, 224);

			Shell shell = new Shell(control.getShell(), SWT.NO_TRIM);

			// otherwise we have a default gray color
			shell.setBackground(greenScreen);

			Button button = new Button(shell, SWT.CHECK);
			button.setBackground(greenScreen);
			button.setSelection(type);

			// otherwise an image is located in a corner
			button.setLocation(1, 1);
			Point bsize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);

			// otherwise an image is stretched by width
			bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
			bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
			button.setSize(bsize);
			shell.setSize(bsize);

			shell.open();
			GC gc = new GC(shell);
			Image image = new Image(control.getDisplay(), bsize.x, bsize.y);
			gc.copyArea(image, 0, 0);
			gc.dispose();
			shell.close();

			ImageData imageData = image.getImageData();
			imageData.transparentPixel = imageData.palette.getPixel(greenScreen
					.getRGB());

			return new Image(control.getDisplay(), imageData);
		}

		private Image checkedImage;
		private Image uncheckedImage;

		protected BooleanFeatureManipulator(final List<EReference> path,
				final EStructuralFeature field,
				final EditingDomain editingDomain) {
			super(path, field, editingDomain);

		}

		@Override
		public String getStringValue(final EObject o) {
			// final Boolean value = (Boolean) getFieldValue(o);
			// return value.booleanValue() ? "Yes" : "No";
			return "";
		}

		@Override
		public void setFromEditorValue(final EObject o, final Object value) {
			doSetCommand(getTarget(o), value);
		}

		@Override
		public boolean canModify(final EObject row) {
			return true;
		}

		@Override
		public CellEditor createCellEditor(Composite parent) {
			if (JFaceResources.getImageRegistry().getDescriptor(CHECKED_KEY) == null) {
				JFaceResources.getImageRegistry().put(UNCHECKED_KEY,
						uncheckedImage = makeShot(parent, false));
				JFaceResources.getImageRegistry().put(CHECKED_KEY,
						checkedImage = makeShot(parent, true));
			} else {
				uncheckedImage = JFaceResources.getImageRegistry().get(
						UNCHECKED_KEY);
				checkedImage = JFaceResources.getImageRegistry().get(
						CHECKED_KEY);
			}
			// set images

			return new CheckboxCellEditor(parent);
		}

		@Override
		public Object getEditorValue(final EObject row) {
			return getFieldValue(row);
		}

		@Override
		public Image getImageValue(final EObject object, final Image columnImage) {
			final Boolean value = (Boolean) getFieldValue(object);

			if (value.booleanValue()) {
				return checkedImage;
			} else {
				return uncheckedImage;
			}
		}

	}

	/**
	 * Simple manipulator which should handle the basic EDataTypes
	 * 
	 * @author hinton
	 * 
	 */
	private class DefaultFeatureManipulator extends BaseFeatureManipulator {

		private final EDataType fieldType;
		private final EFactory factory;

		public DefaultFeatureManipulator(final List<EReference> path,
				final EStructuralFeature field,
				final EditingDomain editingDomain) {
			super(path, field, editingDomain);
			this.fieldType = ((EAttribute) field).getEAttributeType();
			this.factory = fieldType.getEPackage().getEFactoryInstance();
		}

		@Override
		public String getStringValue(EObject o) {
			return getFieldValue(o).toString();
		}

		@Override
		public void setFromEditorValue(final EObject o, final Object value) {
			assert value instanceof String;
			final String vString = (String) value;

			try {
				Object eValue = factory.createFromString(fieldType, vString);
				doSetCommand(getTarget(o), eValue);
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
							Integer.parseInt(s);
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
