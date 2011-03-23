package scenario.presentation.cargoeditor.detailview;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.presentation.cargoeditor.widgets.DateAndTime;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class LocalDateInlineEditor extends BasicAttributeInlineEditor {
	private DateAndTime dateAndTime;
	private final EReference portReference;

	public LocalDateInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain) {
		super(path, feature, editingDomain);
		final EClass container = feature.getEContainingClass();
		// check for associated port reference
		EReference portReference = null;
		for (final EReference ref : container.getEAllReferences()) {
			if (ref.isMany())
				continue;
			if (ref.getEReferenceType().equals(PortPackage.eINSTANCE.getPort())) {
				portReference = ref;
				break;
			}
		}
		this.portReference = portReference;
	}

	@Override
	public Control createControl(Composite parent) {
		final DateAndTime dateAndTime = new DateAndTime(parent, SWT.NONE, false);
		this.dateAndTime = dateAndTime;
		return dateAndTime;
	}
	
	private Calendar getCalendar(final Date utcDate) {
		final Port port = (Port) input.eGet(portReference);
		final TimeZone zone = TimeZone.getTimeZone(portReference == null
				|| port == null || port.getTimeZone() == null ? "UTC" : port
				.getTimeZone());
		
		final Calendar cal = Calendar.getInstance(zone);
		cal.setTime(utcDate);
		return cal;
	}

	@Override
	protected void updateDisplay(final Object value) {
		// Value will be a Date, so we need to find a port and localize the
		// date. Same as in the other date editors.
		final Date utcDate = (Date) value;
		dateAndTime.setValue(getCalendar(utcDate));
	}

}
