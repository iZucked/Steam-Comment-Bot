/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor.celleditors;

import java.util.Calendar;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenario.presentation.cargoeditor.widgets.DatePopup;

/**
 * A CellEditor which displays a date & time picker control.
 * 
 * TODO trigger focus listeners to make changes happen
 * 
 * @author hinton
 * 
 */
public class DateTimeCellEditor extends CellEditor {
	private DatePopup popup;

	public DateTimeCellEditor() {
		super();
	}

	public DateTimeCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	public DateTimeCellEditor(Composite parent) {
		super(parent);
	}

	public void setLegalRange(final Calendar minimumValue,
			final Calendar maximumValue) {

	}

	@Override
	protected Control createControl(final Composite parent) {
		popup = new DatePopup(parent, SWT.NONE);
		popup.addSelectionListener(getSelectionListener());
		popup.addKeyListener(getKeyListener());
		popup.addDisposeListener(new DisposeListener() {			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				popup.removeSelectionListener(getSelectionListener());
				popup.removeKeyListener(getKeyListener());
				popup.removeFocusListener(getFocusListener());
			}
		});
		setValueValid(true);
		return popup;
	}

	private SelectionListener selectionListener = null;
	private SelectionListener getSelectionListener() {
		if (selectionListener == null) {
			selectionListener = new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					markDirty();			
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					handleDefaultSelection(e);
				}						
			};
		}
		return selectionListener;
	}
	
	private FocusListener focusListener = null;
	private FocusListener getFocusListener() {
		if (focusListener == null) {
			focusListener = new FocusListener() {
				@Override
				public void focusLost(final FocusEvent e) {
					DateTimeCellEditor.this.focusLost();
				}
				
				@Override
				public void focusGained(final FocusEvent e) {
				}
			};
		}
		return focusListener;
	}
	
	private KeyListener keyListener = null;
	private KeyListener getKeyListener() {
		if (keyListener == null) {
			keyListener = new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					 keyReleaseOccured(e);
				}
			};
		}
		return keyListener;
	}

	@Override
	protected void doSetFocus() {
		popup.setFocus();
		popup.addFocusListener(getFocusListener());
	}

	@Override
	public void deactivate() {
		if (popup != null && !popup.isDisposed()) {
			popup.removeFocusListener(getFocusListener());
		}
		super.deactivate();
	}

	@Override
	protected Object doGetValue() {
		return popup.getValue();
	}

	@Override
	protected void doSetValue(final Object value) {
		popup.setValue((Calendar) value);
	}

	@Override
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}
	
	   /**
     * Handles a default selection event from the text control by applying the editor
     * value and deactivating this cell editor.
     * 
     * @param event the selection event
     * 
     * @since 3.0
     */
    protected void handleDefaultSelection(SelectionEvent event) {
        fireApplyEditorValue();
        deactivate();
    }
    
    @Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\r') { // Return key
			return;
		}
		super.keyReleaseOccured(keyEvent);
	}
}
