/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.curveeditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Tom Hinton
 * 
 */
public class CurveDialog extends Dialog {
	/**
	 * @param parentShell
	 */
	public CurveDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);

		final CurveCanvas cc = new CurveCanvas(c, SWT.NONE);
		cc.setLayoutData(new GridData(GridData.FILL_BOTH));

		return c;
	}
	
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		final CurveDialog c = new CurveDialog(shell);
		c.open();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
	
	
}
