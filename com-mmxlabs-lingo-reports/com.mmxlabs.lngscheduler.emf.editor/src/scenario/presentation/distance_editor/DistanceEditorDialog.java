/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.distance_editor;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import scenario.port.DistanceModel;
import scenario.presentation.ScenarioEditor;

/**
 * A dialog for containing a {@link DistanceLineViewer}. It's easier to have it
 * in a dialog at the moment so it doesn't have to adapt the portmodel &c. to
 * update.
 * 
 * @author Tom Hinton
 * 
 */
public class DistanceEditorDialog extends Dialog {
	private DistanceLineViewer viewer;
	private DistanceModel distanceModel;
	private ScenarioEditor part;

	public DistanceEditorDialog(Shell parentShell) {
		super(parentShell);
	}

	public DistanceEditorDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);

		this.viewer = new DistanceLineViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);

		viewer.init(part);

		final Table table = viewer.getTable();
		final TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		viewer.setInput(distanceModel);

		viewer.refresh();
		
		return c;
	}
	
	

	@Override
	public void create() {
		super.create();
		getShell().setSize(1024,768);
		getShell().setText("Distance Matrix Editor");
		
		//center shell
		
		final TableColumn[] columns = viewer.getTable().getColumns();
		for (final TableColumn c : columns) {
			c.pack();
		}
	}

	public DistanceModel getResult() {
		return distanceModel;
	}
	
	public int open(final ScenarioEditor part, final DistanceModel dm) {
		this.part = part;
		this.distanceModel = EcoreUtil.copy(dm);

		return super.open();
	}

	@Override
	protected boolean isResizable() {
		return false;
	}
	
}
