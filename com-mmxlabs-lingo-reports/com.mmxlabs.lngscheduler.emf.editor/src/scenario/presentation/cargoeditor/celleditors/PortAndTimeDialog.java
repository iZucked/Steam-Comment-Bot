package scenario.presentation.cargoeditor.celleditors;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import scenario.fleet.FleetFactory;
import scenario.fleet.PortAndTime;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class PortAndTimeDialog extends Dialog {
	
	protected PortAndTime result;
	protected Shell shlVesselLocationConstraint;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PortAndTimeDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public PortAndTime open(final PortAndTime input) {
		if (input == null) {
			result = FleetFactory.eINSTANCE.createPortAndTime();
		} else {
			result = EcoreUtil.copy(input);
		}
		createContents();
		shlVesselLocationConstraint.open();
		shlVesselLocationConstraint.layout();
		
		// center in parent window
		final Rectangle shellBounds = getParent().getBounds();
		final Point dialogSize = shlVesselLocationConstraint.getSize();

		shlVesselLocationConstraint.setLocation(shellBounds.x
				+ (shellBounds.width - dialogSize.x) / 2, shellBounds.y
				+ (shellBounds.height - dialogSize.y) / 2);

		
		Display display = getParent().getDisplay();
		while (!shlVesselLocationConstraint.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlVesselLocationConstraint = new Shell(getParent(), getStyle());
		shlVesselLocationConstraint.setSize(318, 144);
		shlVesselLocationConstraint.setText("Vessel Location Constraint");
		shlVesselLocationConstraint.setLayout(new GridLayout(4, false));
		
		Label lblFixPort = new Label(shlVesselLocationConstraint, SWT.NONE);
		lblFixPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFixPort.setText("Fix Port");
		
		Button btnFixedPort = new Button(shlVesselLocationConstraint, SWT.CHECK | SWT.RIGHT);
		
		Combo combo = new Combo(shlVesselLocationConstraint, SWT.NONE);
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_combo.widthHint = 269;
		combo.setLayoutData(gd_combo);
		
		Label lblFixEarliestTime = new Label(shlVesselLocationConstraint, SWT.NONE);
		lblFixEarliestTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFixEarliestTime.setText("Fix Earliest Time");
		
		Button btnFixedStartTime = new Button(shlVesselLocationConstraint, SWT.CHECK);
		
		DateTime dateTime = new DateTime(shlVesselLocationConstraint, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		DateTime dateTime_1 = new DateTime(shlVesselLocationConstraint, SWT.BORDER);
		dateTime_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFixLatestTime = new Label(shlVesselLocationConstraint, SWT.NONE);
		lblFixLatestTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFixLatestTime.setText("Fix Latest Time");
		
		Button button = new Button(shlVesselLocationConstraint, SWT.CHECK);
		
		DateTime dateTime_2 = new DateTime(shlVesselLocationConstraint, SWT.BORDER);
		dateTime_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		DateTime dateTime_3 = new DateTime(shlVesselLocationConstraint, SWT.BORDER);
		dateTime_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(shlVesselLocationConstraint, SWT.NONE);
		
		Composite composite = new Composite(shlVesselLocationConstraint, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 3, 1));
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shlVesselLocationConstraint.close();
			}
		});
		btnCancel.setText("Cancel");
		
		Button btnOK = new Button(composite, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlVesselLocationConstraint.close();
			}
		});
		btnOK.setText("OK");

	}
}
