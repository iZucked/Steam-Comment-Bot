/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor;

import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A CellEditor which displays a date & time picker control. 
 * 
 * @author hinton
 *
 */
public class DateTimeCellEditor extends CellEditor {
	private DatePopup popup;
	private Object value;
	
	public DateTimeCellEditor() {
		super();
	}

	public DateTimeCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	public DateTimeCellEditor(Composite parent) {
		super(parent);
	}

	public void setDisplayTimezone(final String timezone) {
		
	}
	
	public void setLegalRange(final Date minimumValue, final Date maximumValue) {
		
	}
	
	@Override
	protected Control createControl(final Composite parent) {
		return popup = new DatePopup(parent, SWT.NONE);
	}
	
	@Override
	protected void doSetFocus() {
		popup.setFocus();
	}

	@Override
	protected Object doGetValue() {
		return value;
	}
	
	@Override
	protected void doSetValue(final Object value) {
		this.value = value;
	}

	@Override
	protected boolean dependsOnExternalFocusListener() {
		return false; 
	}
}
