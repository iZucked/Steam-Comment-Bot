package scenario.presentation.cargoeditor.detailview;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.presentation.cargoeditor.widgets.DateAndTime;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class LocalDateInlineEditor extends UnsettableInlineEditor {
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
	protected Control createValueControl(Composite parent) {
		final DateAndTime dateAndTime = new DateAndTime(parent, SWT.NONE, false);
		this.dateAndTime = dateAndTime;
		
		final Listener listener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				doSetValue(dateAndTime.getValue().getTime());
			}
		};
		
		dateAndTime.addListener(SWT.Selection, listener);
		dateAndTime.addListener(SWT.DefaultSelection, listener);
		
		dateAndTime.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dateAndTime.removeListener(SWT.Selection, listener);
				dateAndTime.removeListener(SWT.DefaultSelection, listener);
			}
		});
		
		return dateAndTime;
	}
	
	private TimeZone getTimeZone() {
		final Port port = (Port) (portReference == null ? null : input.eGet(portReference));
		final TimeZone zone = TimeZone.getTimeZone(portReference == null
				|| port == null || port.getTimeZone() == null ? "UTC" : port
				.getTimeZone());
		return zone;
	}
	 
	private Calendar getCalendar(final Date utcDate) {
		final Calendar cal = Calendar.getInstance(getTimeZone());
		cal.setTime(utcDate);
		return cal;
	}

	
	@Override
	protected void updateControl() {
		if (input != null && portReference != null && input.eGet(portReference) != null) {
			dateAndTime.setTimeZone(getTimeZone()); 
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		// Value will be a Date, so we need to find a port and localize the
		// date. Same as in the other date editors.
		final Date utcDate = (Date) value;
		dateAndTime.setValue(getCalendar(utcDate));
	}

	@Override
	protected boolean updateOnChangeToFeature(Object changedFeature) {
		return super.updateOnChangeToFeature(changedFeature) ||
			changedFeature.equals(portReference); //update if port changes
	}

	
}
