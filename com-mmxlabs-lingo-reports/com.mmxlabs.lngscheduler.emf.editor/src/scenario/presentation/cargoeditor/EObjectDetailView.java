/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A reflectively self-constructing detail view for EObjects. Displays a group
 * for each contained object, containing a vertical list of editors for each
 * attribute, much like {@link EObjectEditorViewerPane}.
 * 
 * Unlike {@link EObjectEditorViewerPane}, this only flattens out the
 * containment hierarchy to one level deep. This is because any more depth is
 * unsightly.
 * 
 * @author hinton
 * 
 */
public class EObjectDetailView extends Composite {
	public EObjectDetailView(final Composite parent, int style) {
		super(parent, style);
	}

	public void initForEClass(final EClass objectClass) {
		int groupCount = 1;
		addGroupForEClass(objectClass, unmangle(objectClass.getName()));
		for (final EReference reference : objectClass.getEAllContainments()) {
			if (reference.isMany())
				continue;
			addGroupForEClass(reference.getEReferenceType(),
					unmangle(reference.getName()));
			groupCount++;
		}
		
		this.setLayout(new GridLayout(groupCount, false));
	}

	private void addGroupForEClass(final EClass objectClass,
			final String groupName) {
		final Group group = new Group(this, SWT.NONE);
		
		group.setText(groupName + " Properties:");
		
		final GridData groupLayoutData = new GridData(SWT.FILL, SWT.FILL, true,
				true);

		group.setLayoutData(groupLayoutData);

		final GridLayout groupLayout = new GridLayout(2, false);
		group.setLayout(groupLayout);

		for (final EAttribute attribute : objectClass.getEAllAttributes()) {
			// create label for this attribute
			final Label attributeLabel = new Label(group, SWT.RIGHT);
			attributeLabel.setText(unmangle(attribute.getName()));
			final GridData labelData = new GridData(SWT.RIGHT, SWT.FILL, true,
					true);
			attributeLabel.setLayoutData(labelData);

			// create editor for this attribute
			// TODO implement this.
			final Text attributeEditor = new Text(group, SWT.NONE);
			final GridData editorData = new GridData(SWT.FILL, SWT.FILL, true,
					true);

			attributeEditor.setLayoutData(editorData);
		}

		for (final EReference reference : objectClass.getEAllReferences()) {
			// handle references appropriately.
			if (reference.isContainment()) {

			} else {
				if (reference.isMany()) {

				} else {

				}
			}
		}
	}

	private String unmangle(final String name) {
		final StringBuilder sb = new StringBuilder();
		boolean lastWasLower = true;
		boolean firstChar = true;
		for (final char c : name.toCharArray()) {
			if (firstChar) {
				sb.append(Character.toUpperCase(c));
				firstChar = false;
			} else {
				if (lastWasLower && Character.isUpperCase(c))
					sb.append(" ");
				lastWasLower = Character.isLowerCase(c);
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
