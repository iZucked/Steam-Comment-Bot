/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.wiringeditor;

import java.util.List;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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

import scenario.cargo.Cargo;

/**
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

	public void open(final List<Cargo> cargos, final EditingDomain domain) {
		final Shell shell = new Shell((SWT.DIALOG_TRIM & ~SWT.CLOSE)
				| SWT.APPLICATION_MODAL | SWT.RESIZE);
		shell.setSize(500, 300);
		shell.setText("Wiring Editor");
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 6;
		shell.setLayout(gl_shell);

		ScrolledComposite scrolledComposite = new ScrolledComposite(shell,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		scrolledComposite.setBounds(0, 0, 84, 84);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		final WiringComposite wiringComposite = new WiringComposite(
				scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(wiringComposite);
		scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		scrolledComposite.setLayout(new FillLayout());

		Composite buttonsComposite = new Composite(shell, SWT.NONE);
		buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		buttonsComposite.setBounds(0, 0, 64, 64);
		buttonsComposite.setLayout(new GridLayout(3, false));
		
		((GridLayout)buttonsComposite.getLayout()).marginWidth = 0;
		
		Button btnOptimise = new Button(buttonsComposite, SWT.NONE);
		btnOptimise.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnOptimise.setText("Optimise");

		final Button btnOk = new Button(buttonsComposite, SWT.NONE);
		GridData gd_btnOk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOk.widthHint = 50;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setText("OK");

		btnOk.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				domain.getCommandStack().execute(wiringComposite.createApplyCommand(domain));
				
				shell.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		Button btnCancel = new Button(buttonsComposite, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 50;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancel");

		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});		
		
		wiringComposite.setCargos(cargos);

		wiringComposite.addListener(SWT.Modify, new Listener() {
			{
				final Listener me = this;
				wiringComposite.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
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

		shell.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x)
				/ 2, shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

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
