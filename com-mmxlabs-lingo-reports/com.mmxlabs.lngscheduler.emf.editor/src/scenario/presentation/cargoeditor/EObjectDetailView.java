/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import scenario.NamedObject;
import scenario.presentation.cargoeditor.detailview.BasicAttributeInlineEditor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

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
	private final Map<EClassifier, IInlineEditorFactory> editorFactories = new HashMap<EClassifier, IInlineEditorFactory>();
	private final List<IInlineEditor> editors = new ArrayList<IInlineEditor>();
	private final EditingDomain editingDomain;

	public EObjectDetailView(final Composite parent, int style,
			final EditingDomain editingDomain) {
		super(parent, style);
		this.editingDomain = editingDomain;
	}

	public interface IInlineEditorFactory {
		public IInlineEditor createEditor(final EMFPath path,
				final EStructuralFeature feature);
	}

	public interface IInlineEditor {
		public void setInput(final EObject object);

		public Control createControl(final Composite parent);
	}

	private class NonEditableEditor extends BasicAttributeInlineEditor {
		private Label label;
		public NonEditableEditor(EMFPath path, EStructuralFeature feature) {
			super(path, feature, null);
		}

		@Override
		public Control createControl(Composite parent) {
			return (label = new Label(parent, SWT.NONE));
		}

		@Override
		protected void updateDisplay(Object value) {
			if (value instanceof NamedObject) {
				label.setText(((NamedObject)value).getName());
			} else {
				label.setText(value == null ? "" : value.toString());
			}
		}
	}
	
	public void setEditorFactoryForClassifier(final EClassifier classifier,
			final IInlineEditorFactory factory) {
		editorFactories.put(classifier, factory);
	}

	public void initForEClass(final EClass objectClass) {
		int groupCount = 1;
		addGroupForEClass(objectClass, unmangle(objectClass.getName()),
				new EMFPath(true));
		for (final EReference reference : objectClass.getEAllContainments()) {
			if (reference.isMany())
				continue;
			addGroupForEClass(reference.getEReferenceType(),
					unmangle(reference.getName()), new EMFPath(true, reference));
			groupCount++;
		}

		this.setLayout(new GridLayout(groupCount, false));
	}

	private void addGroupForEClass(final EClass objectClass,
			final String groupName, final EMFPath path) {
		final Group group = new Group(this, SWT.NONE);

		group.setText(groupName + " Properties:");

		final GridData groupLayoutData = new GridData(SWT.FILL, SWT.FILL, true,
				true);

		group.setLayoutData(groupLayoutData);

		group.setLayout(new FillLayout());
		final ScrolledComposite sc = new ScrolledComposite(group, SWT.V_SCROLL);
		sc.setLayout(new FillLayout());
		final Composite controls = new Composite(sc, SWT.NONE);
		sc.setContent(controls);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(false);
		final GridLayout groupLayout = new GridLayout(2, false);
		controls.setLayout(groupLayout);
		for (final EStructuralFeature attribute : objectClass.getEAllStructuralFeatures()) {
			
			if (attribute instanceof EReference) {
				final EReference reference = (EReference)attribute;
				if (reference.isContainment()) continue;
				if (reference.isMany()) continue; // do something
			}
			
			// create label for this attribute
			final Label attributeLabel = new Label(controls, SWT.RIGHT);
			attributeLabel.setText(unmangle(attribute.getName()) + ": ");
			final GridData labelData = new GridData(SWT.RIGHT, SWT.CENTER, false,
					false);
			attributeLabel.setLayoutData(labelData);

			// create editor for this attribute
			// TODO implement this.
			final EClassifier attributeType = attribute.getEType();

			final IInlineEditorFactory attributeEditorFactory = editorFactories
					.get(attributeType);
			// final IInlineEditor attributeEditor = new TextInlineEditor(path,
			// attribute, editingDomain);
			final Control attributeControl;
			if (attributeEditorFactory == null) {
				NonEditableEditor blank = new NonEditableEditor(path, attribute);
				editors.add(blank);
				attributeControl = blank.createControl(controls);
			} else {
				final IInlineEditor attributeEditor = attributeEditorFactory
						.createEditor(path, attribute);
				editors.add(attributeEditor);
				attributeControl = attributeEditor.createControl(controls);
			}

			final GridData editorData = new GridData(SWT.FILL, SWT.FILL, true,
					false);

			attributeControl.setLayoutData(editorData);
		}
		controls.pack();
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

	public void setInput(final EObject object) {
		// release any reference to old input and handle new input
		for (final IInlineEditor editor : editors) {
			editor.setInput(object);
		}
	}
}
