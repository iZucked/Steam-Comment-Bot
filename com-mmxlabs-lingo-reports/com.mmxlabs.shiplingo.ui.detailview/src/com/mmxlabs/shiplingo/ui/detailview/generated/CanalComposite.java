package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.port.DistanceModel;
import scenario.port.PortPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.editors.DialogInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.dialogs.DistanceEditorDialog;

/**
 * A composite containing a form for editing Canal instances
 * 
 * @generated
 */
public class CanalComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public CanalComposite(final Composite container, final int style,
			final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public CanalComposite(final Composite container, final int style,
			final boolean validate) {
		this(container, style, "Canal", validate);
	}

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public CanalComposite(final Composite container, final int style) {
		this(container, style, "Canal", true);
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
		createCanalFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Canal.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		UUIDObjectComposite.createFields(composite, mainGroup);
		NamedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to Canal
	 * 
	 * @generated
	 */
	protected static void createCanalFields(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		createDistanceModelEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the distanceModel feature on Canal
	 * 
	 * @generated NO custom editor
	 */
	protected static void createDistanceModelEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		// final DistanceModelComposite sub =
		// new DistanceModelComposite(composite, composite.getStyle(),
		// "Distance Model", false);
		// sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true));
		// sub.setPath(new CompiledEMFPath(composite.getInputPath(),
		// PortPackage.eINSTANCE.getCanal_DistanceModel()));
		// composite.addSubEditor(sub);

		composite.createEditorControl(
				mainGroup,
				new DialogInlineEditor(composite.getInputPath(),
						PortPackage.eINSTANCE.getCanal_DistanceModel(),
						composite.getEditingDomain(), composite
								.getCommandProcessor()) {
					@Override
					protected String render(Object value) {
						return "Distance Matrix";
					}

					@Override
					protected Object displayDialog(final Object currentValue) {
						final DistanceModel dm = (DistanceModel) currentValue;
						final DistanceEditorDialog ded = new DistanceEditorDialog(
								getShell());

						if (ded.open(composite.getValueProviderProvider(),
								composite.getEditingDomain(), dm) == Window.OK)
							return ded.getResult();

						return null;
					}
				}, "Distance Matrix");
	}
}
