/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;

import scenario.port.DistanceModel;
import scenario.port.PortPackage;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.detailview.editors.dialogs.DistanceEditorDialog;
import com.mmxlabs.shiplingo.ui.tableview.DialogFeatureManipulator;

/**
 * Editor for canals. Has a name column and a distance matrix column.
 * 
 * @author Tom Hinton
 * 
 */
public class CanalEVP extends NamedObjectEVP {
	public CanalEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addTypicalColumn("Distance Matrix", 
				new DialogFeatureManipulator(PortPackage.eINSTANCE.getCanal_DistanceModel(), part.getEditingDomain()) {
			
			@Override
			protected String renderValue(Object value) {
				return "Double-click to edit";
			}
			
			@Override
			protected Object openDialogBox(Control cellEditorWindow, Object object) {
				final DistanceModel dm = (DistanceModel) getValue(object);
				final DistanceEditorDialog ded = new DistanceEditorDialog(cellEditorWindow.getShell());
				
				if (ded.open(part, part.getEditingDomain(), dm) == Window.OK) return ded.getResult();
				
				return null;
			}
		});
	}
}
