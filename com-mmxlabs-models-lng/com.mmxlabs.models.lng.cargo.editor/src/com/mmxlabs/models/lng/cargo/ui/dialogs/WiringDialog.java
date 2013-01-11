/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.dialogs;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * A dialog which contains a {@link WiringComposite} - all the logic happens in there.
 * 
 * 
 * Normal usage is to call {@link #open(List, EditingDomain)} with a list of cargoes to rewire. If the user presses the OK button the dialog will then execute a command to make the necessary changes.
 * 
 * @author Tom Hinton
 * 
 */
public class WiringDialog extends Dialog {
	/**
	 * @param parent
	 */
	public WiringDialog(final Shell parent) {
		super(parent);
	}

	public void open(final MMXRootObject rootObject, final List<Cargo> cargoes, final EditingDomain domain, final IReferenceValueProvider portProvider, final IReferenceValueProvider contractProvider) {
		final Shell shell = new Shell((SWT.DIALOG_TRIM & ~SWT.CLOSE) | SWT.APPLICATION_MODAL | SWT.RESIZE);
		shell.setSize(1000, 600);
		shell.setText("Redirection Wizard");
		final GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 6;
		shell.setLayout(gl_shell);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL |SWT.DOUBLE_BUFFERED);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setBounds(0, 0, 84, 84);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(true);

		final WiringComposite wiringComposite = new WiringComposite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(wiringComposite);
		scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setLayout(new FillLayout());


	    scrolledComposite.addControlListener(new ControlAdapter() {
	        public void controlResized(ControlEvent e) {
	          Rectangle r = scrolledComposite.getClientArea();
	          scrolledComposite.setMinSize(shell.computeSize(r.width,
	              SWT.DEFAULT));
	        }
	      });

		

		final Composite buttonsComposite = new Composite(shell, SWT.NONE);
		buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		buttonsComposite.setBounds(0, 0, 64, 64);
		buttonsComposite.setLayout(new GridLayout(3, false));

		((GridLayout) buttonsComposite.getLayout()).marginWidth = 0;

		final Button btnOptimise = new Button(buttonsComposite, SWT.NONE);
		btnOptimise.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnOptimise.setText("Add Cargo");

		btnOptimise.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				wiringComposite.addNewCargo(null, false);
			}
		});

		final Button btnOk = new Button(buttonsComposite, SWT.NONE);
		final GridData gd_btnOk = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		// gd_btnOk.widthHint = 50;
		gd_btnOk.grabExcessHorizontalSpace = true;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setText("OK");

		btnOk.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				final Command c = wiringComposite.createApplyCommand(domain, rootObject);
				if (c.canExecute()) {
					domain.getCommandStack().execute(c);
					shell.close();
				} else {
					throw new RuntimeException("Unable to execute wiring command");
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		final Button btnCancel = new Button(buttonsComposite, SWT.NONE);
		final GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		// gd_btnCancel.widthHint = 50;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancel");

		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				shell.close();
			}
		});

		wiringComposite.setPortProvider(portProvider);
		wiringComposite.setContractProvider(contractProvider);
		wiringComposite.setCargoes(cargoes);

		wiringComposite.addListener(SWT.Modify, new Listener() {
			{
				final Listener me = this;
				wiringComposite.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						wiringComposite.removeListener(SWT.Modify, me);
					}
				});
			}

			@Override
			public void handleEvent(final Event event) {
				btnOk.setEnabled(wiringComposite.isWiringFeasible());
			}
		});

		shell.layout();

		// center in parent window
		final Rectangle shellBounds = getParent().getBounds();
		final Point dialogSize = shell.getSize();

		shell.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2, shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

		shell.open();
		shell.setDefaultButton(btnOk);
		// run event loop and then close
		final Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
