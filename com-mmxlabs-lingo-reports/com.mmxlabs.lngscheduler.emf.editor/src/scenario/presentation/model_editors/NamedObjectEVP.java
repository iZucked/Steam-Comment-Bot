/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import scenario.NamedObject;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.tableview.BasicAttributeManipulator;

/**
 * An editor for {@link NamedObject}s, which just has a name column,
 * and default behaviour otherwise.
 * 
 * @author Tom Hinton
 *
 */
public class NamedObjectEVP extends ScenarioObjectEditorViewerPane {
	public NamedObjectEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		BasicAttributeManipulator manipulator = new BasicAttributeManipulator(
				namedObjectName, part.getEditingDomain());
		addColumn("Name", manipulator, manipulator);
	}
}
