package com.mmxlabs.models.ui.impl;

import java.util.LinkedList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;

/**
 * The default detail composite implementation; does not do anything about having child composites.
 * @author hinton
 *
 */
public class DefaultDetailComposite extends Composite implements IInlineEditorContainer, IDisplayComposite {
	private ICommandHandler commandHandler;
	private EClass displayedClass;
	private IInlineEditorWrapper wrapper = new IInlineEditorWrapper() {
		@Override
		public IInlineEditor wrap(IInlineEditor editor) {
			return editor;
		}
	};
	private GridLayout gridLayout;
	
	public DefaultDetailComposite(final Composite parent, final int style) {
		super(parent, style);
		this.gridLayout = new GridLayout(2, false);
		setLayout(gridLayout);
	}

	private final LinkedList<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	
	@Override
	public void addInlineEditor(IInlineEditor editor) {
		editor = wrapper.wrap(editor);
		if (editor != null) {
			editor.setCommandHandler(commandHandler);
			editors.add(editor);
		}
	}
	
	public void createControls() {
		for (final IInlineEditor editor : editors) {
			final Label label = new Label(this, SWT.NONE);
			label.setText(editor.getLabel());
			label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			final Control control = editor.createControl(this);
			control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));			
		}
	}
	
	/**
	 * Display the given EObject in this container.
	 * 
	 * Recreates the controls if the object's eClass is different to what we had before.
	 * 
	 * @param object
	 */
	public void display(final MMXRootObject root, final EObject object) {
		final EClass eClass = object.eClass();
		if (eClass != displayedClass) {			
			clear();
			initialize(eClass);
			createControls();
		}
		for (final IInlineEditor editor : editors) {
			editor.display(root, object);
		}
	}

	private void clear() {
		editors.clear();
		for (final Control c : getChildren()) c.dispose();
	}
	
	private void initialize(final EClass eClass) {
		this.displayedClass = eClass;
		final IComponentHelper helper = Activator.getDefault().getComponentHelperRegistry().getComponentHelper(displayedClass);
		helper.addEditorsToComposite(this);
	}

	@Override
	public Composite getComposite() {
		return this;
	}
}
