package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;

public class DefaultChangeSetContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ChangeSetTableRoot changeSetRoot) {
			return changeSetRoot.getGroups().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ChangeSetTableGroup group) {
			return group.getRows().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ChangeSetTableRow row) {
			return row.getTableGroup();
		}
		if (element instanceof EObject eObject) {
			return eObject.eContainer();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof ChangeSetTableRoot //
				|| element instanceof ChangeSetTableGroup;
	}

}
