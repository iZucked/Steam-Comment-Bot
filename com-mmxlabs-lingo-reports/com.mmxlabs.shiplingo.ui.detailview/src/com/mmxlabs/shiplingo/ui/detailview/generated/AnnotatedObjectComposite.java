package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import scenario.ScenarioPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.editors.TextInlineEditor;

/**
 * A composite containing a form for editing AnnotatedObject instances. The
 * EClass hierarchy is implemented by the static methods at the bottom of the
 * class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class AnnotatedObjectComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public AnnotatedObjectComposite(final Composite container, final int style,
			final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public AnnotatedObjectComposite(final Composite container, final int style,
			final boolean validate) {
		this(container, style, "Annotated Object", validate);
	}

	public AnnotatedObjectComposite(final Composite container, final int style) {
		this(container, style, "Annotated Object", true);
	}

	/**
	 * Create the main contents
	 * 
	 * @generated
	 */
	protected void createContents(final Composite group) {
		final Composite mainGroup;

		if (group == null) {
			mainGroup = createGroup(this, mainGroupTitle);
		} else {
			mainGroup = group;
		}

		super.createContents(mainGroup);

		createFields(this, mainGroup);
	}

	/**
	 * @generated
	 */
	protected static void createFields(final AbstractDetailComposite composite,
			final Composite mainGroup) {
		createFieldsFromSupers(composite, mainGroup);
		createAnnotatedObjectFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of AnnotatedObject.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(
			final AbstractDetailComposite composite, final Composite mainGroup) {
	}

	/**
	 * Create fields belonging directly to AnnotatedObject
	 * 
	 * @generated
	 */
	protected static void createAnnotatedObjectFields(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		createNotesEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the notes feature on AnnotatedObject
	 * 
	 * @generated NO notes are handled differently, in a little box of their own
	 */
	protected static void createNotesEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, new TextInlineEditor(
				composite.getInputPath(),
				ScenarioPackage.eINSTANCE.getAnnotatedObject_Notes(),
				composite.getEditingDomain(), composite.getCommandProcessor(),
				SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				), "Notes").setLayoutData(new GridData(GridData.FILL_BOTH));
		
//		final Composite notesGroup = composite.createGroup(composite, "Notes");
//		
//		((GridLayout) notesGroup.getLayout()).numColumns = 1;
//		
//		final TextInlineEditor editor = new TextInlineEditor(
//				composite.getInputPath(),
//				ScenarioPackage.eINSTANCE.getAnnotatedObject_Notes(),
//				composite.getEditingDomain(), composite.getCommandProcessor(),
//				SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
//				);
//		
//		editor.createControl(notesGroup).setLayoutData(new GridData(GridData.FILL_BOTH));
//		composite.addEditor(editor);
	}
}
