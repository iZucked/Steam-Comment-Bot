/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */
package com.mmxlabs.shiplingo.ui.detailview.base;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import scenario.ScenarioPackage;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.shiplingo.ui.detailview.editors.EENumInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.ICommandProcessor;
import com.mmxlabs.shiplingo.ui.detailview.editors.IInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.LocalDateInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.MultiReferenceInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.NumberInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.ReferenceInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.TextInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.ValueListInlineEditor;

/**
 * A base class for all the object editing composites generated with Acceleo.
 * 
 * Provides functionality to create groups, controls and so on.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractDetailComposite extends Composite {
	/**
	 * Provides value providers for objects of a given EClass
	 */
	protected IValueProviderProvider valueProviderProvider = null;
	/**
	 * Processes editing commands
	 */
	protected ICommandProcessor commandProcessor;
	/**
	 * Where editing commands are applied
	 */
	protected EditingDomain editingDomain;
	/**
	 * The path used to dereference features viewed in this composite
	 * 
	 * TODO consider not needing this, for example by generating the appropriate
	 * path following code in a version of setInput() produced by MTL
	 */
	protected EMFPath inputPath = new CompiledEMFPath(true);
	/**
	 * The current input
	 */
	protected EObject input = null;

	/**
	 * Used to wrap all inline editors, so that extra controls can be introduced
	 * on editors if desired.
	 */
	protected IInlineEditorWrapper editorWrapper = IInlineEditorWrapper.IDENTITY;

	/**
	 * All the editors in this composite. See also
	 * {@link #addEditor(IInlineEditor)}.
	 */
	private final List<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	/**
	 * All the composite editors for sub-objects in this composite. See
	 * {@link #addSubEditor(AbstractDetailComposite)}.
	 */
	private final List<AbstractDetailComposite> subEditors = new LinkedList<AbstractDetailComposite>();

	private final GridLayout myLayout = new GridLayout(1, true);

	private final IBatchValidator validator;

	private boolean currentStateValid = true;

	private Runnable postValidationRunnable = null;

	/**
	 * Construct a new detail composite within the parent composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public AbstractDetailComposite(final Composite parent, int style,
			final boolean shouldValidate) {
		super(parent, style);
		if (shouldValidate) {
			validator = ModelValidationService.getInstance().newValidator(
					EvaluationMode.BATCH);

			validator.setOption(
					IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
		} else {
			validator = null;
		}
	}

	/**
	 * Trigger a validation, and tell the {@link #postValidationRunnable} that
	 * validation has happened.
	 */
	protected void validate() {
		if (validator == null)
			return;

		final IStatus status = input == null ? Status.OK_STATUS : validator
				.validate(input);

		processValidation(status);

		currentStateValid = status.matches(Status.ERROR) == false;

		if (postValidationRunnable != null) {
			postValidationRunnable.run();
		}
	}

	/**
	 * Update validation markers in this and children of this.
	 * 
	 * @param status
	 */
	private void processValidation(IStatus status) {
		for (final IInlineEditor e : editors) {
			e.processValidation(status);
		}

		for (final AbstractDetailComposite s : subEditors) {
			s.processValidation(status);
		}
	}

	public Point getPreferredSize(final int width) {
		return new Point(width, getPreferredHeight(width));
	}

	@Override
	public void setOrientation(final int swtOrientation) {
		if ((swtOrientation & SWT.VERTICAL) != 0) {
			myLayout.numColumns = 1;
		} else {
			myLayout.numColumns = 1 + subEditors.size();
		}
	}

	public int getPreferredWidth() {
		int result = 0;
		for (final Control c : getChildren()) {
			final Point p = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			result = Math.max(result, p.x);
		}

		return result * (1 + subEditors.size());
	}

	public int getPreferredHeight(final int width) {
		int heightSum = 0;
		int heightMax = 0;
		boolean tallMode = false;
		for (final Control c : getChildren()) {
			final Point p = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			if (p.x + 10 >= width) {
				tallMode = true;
			}

			heightSum += p.y + 32;
			heightMax = Math.max(heightMax, p.y + 32);
		}
		if (tallMode) {
			myLayout.numColumns = 1;
		} else {
			myLayout.numColumns = 1 + subEditors.size();
		}
		return tallMode ? heightSum : heightMax;
	}

	/**
	 * Call this to create the controls, then setInput to hook up.
	 * 
	 * @param valueProviderProvider
	 * @param editingDomain
	 * @param commandProcessor
	 */
	public void init(final IValueProviderProvider valueProviderProvider,
			final EditingDomain editingDomain,
			final ICommandProcessor commandProcessor,
			final IInlineEditorWrapper editorWrapper) {
		this.editorWrapper = editorWrapper;
		this.valueProviderProvider = valueProviderProvider;
		this.editingDomain = editingDomain;
		this.commandProcessor = validator == null ? commandProcessor
				: (new ICommandProcessor() {
					@Override
					public void processCommand(final Command command,
							final EObject target,
							final EStructuralFeature feature) {

						commandProcessor.processCommand(command, target,
								feature);

						validate();
					}
				});
		createContents(null);
		for (final AbstractDetailComposite adc : subEditors) {
			adc.init(valueProviderProvider, editingDomain,
					this.commandProcessor, editorWrapper);
		}
	}

	public boolean isCurrentStateValid() {
		return currentStateValid;
	}

	/**
	 * Display and edit the given input in this control.
	 * 
	 * @param input
	 */
	public void setInput(final EObject input) {
		this.input = input;
		for (final IInlineEditor editor : editors) {
			editor.setInput(input);
		}
		for (final AbstractDetailComposite sub : subEditors) {
			sub.setInput(input);
		}
		validate();
	}

	/**
	 * Set a path used to dereference the input object passed to
	 * {@link #setInput(EObject)}.
	 * 
	 * @param path
	 *            the new path to follow.
	 */
	public void setPath(final EMFPath path) {
		this.inputPath = path;
	}

	protected void createContents(final Composite group) {
		myLayout.marginHeight = 0;
		myLayout.marginWidth = 0;
		setLayout(myLayout);

		createExtraContents(group);
	}

	/**
	 * Subclasses can override this to create extra editors without messing with
	 * the main generated method
	 * 
	 * @param group
	 */
	protected void createExtraContents(final Composite group) {

	}

	/**
	 * Create a standard group in the given container, and return it
	 * 
	 * @param container
	 * @param label
	 * @return
	 */
	public Composite createGroup(final Composite container, final String label) {
		final Group g = new Group(container, getStyle());
		g.setText(label);

		final GridData groupLayoutData = new GridData(SWT.FILL, SWT.FILL, true,
				true);

		g.setLayoutData(groupLayoutData);
		final GridLayout groupLayout = new GridLayout(2, false);
		g.setLayout(groupLayout);
		groupLayout.horizontalSpacing = 10;
		return g;
	}

	/**
	 * Use the given editor to create a control in the given container, and also
	 * passes it to {@link #addEditor(IInlineEditor)} so that it is updated with
	 * new values.
	 * 
	 * NOTE this method is public but is not intended for general use by
	 * clients. It's public so that subclasses in another package can call it
	 * from static methods.
	 * 
	 * @param container
	 * @param feature
	 * @param label
	 */
	public Control createEditorControl(final Composite container,
			IInlineEditor editor, final String label) {
		if (editor != null) {
			editor = editorWrapper.wrap(editor);
			if (editor == null)
				return null; // wrapper may filter out this editor.
			// create label & control
			final Label labelControl = new Label(container, SWT.NONE);
			labelControl.setText(label);
			final Control control = editor.createControl(container);

			labelControl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
					false, false));
			control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			addEditor(editor);
			return control;
		}
		return null;
	}

	/**
	 * Add the given editor to the list of editors, so it is updated for new
	 * inputs.
	 * 
	 * @param editor
	 */
	public void addEditor(final IInlineEditor editor) {
		editors.add(editor);
	}

	/**
	 * Add the given composite to the list of sub-composites, again so it is
	 * updated for new inputs.
	 * 
	 * NOTE this method is public but is not intended for general use by
	 * clients. It's public so that subclasses in another package can call it
	 * from static methods.
	 * 
	 * @param composite
	 */
	public void addSubEditor(final AbstractDetailComposite composite) {
		addSubEditor(composite, true);

	}

	/**
	 * Create an IIlineEditor for the given feature. This is switching on the
	 * runtime type information provided by EMF, and so is a bit slower than
	 * might be ideal. In principle the switch could be happening at generation
	 * time in the MTL file, but I don't know enough MTL to do that at the mo.
	 * 
	 * 
	 * NOTE this method is public but is not intended for general use by
	 * clients. It's public so that subclasses in another package can call it
	 * from static methods.
	 * 
	 * @param feature
	 * @return
	 */
	public IInlineEditor createEditor(final EStructuralFeature feature) {
		final IInlineEditor editor;
		final EClassifier classifier = feature.getEType();
		if (classifier instanceof EClass) {
			assert feature instanceof EReference : "You can't have an eclass as an attribute!";
			final EReference reference = (EReference) feature;
			// lookup value provider
			final IReferenceValueProvider valueProvider = valueProviderProvider
					.getValueProvider((EClass) classifier);
			if (valueProvider == null)
				return null;
			if (reference.isMany()) {
				editor = new MultiReferenceInlineEditor(inputPath, feature,
						editingDomain, commandProcessor, valueProvider);
			} else {
				editor = new ReferenceInlineEditor(inputPath, feature,
						editingDomain, commandProcessor, valueProvider);
			}
		} else {
			final EDataType dataType = (EDataType) classifier;
			if (dataType == EcorePackage.eINSTANCE.getEString()) {
				editor = new TextInlineEditor(inputPath, feature,
						editingDomain, commandProcessor);
			} else if (dataType == EcorePackage.eINSTANCE.getEDate()
					|| dataType == ScenarioPackage.eINSTANCE
							.getDateAndOptionalTime()) {
				editor = new LocalDateInlineEditor(inputPath, feature,
						editingDomain, commandProcessor);
			} else if (dataType == EcorePackage.eINSTANCE.getEInt()
					|| dataType == EcorePackage.eINSTANCE.getEFloat()
					|| dataType == EcorePackage.eINSTANCE.getELong()
					|| dataType == EcorePackage.eINSTANCE.getEDouble()
					|| dataType == ScenarioPackage.eINSTANCE.getPercentage()) {
				editor = new NumberInlineEditor(inputPath, feature,
						editingDomain, commandProcessor);
			} else if (dataType.getInstanceClass().isEnum()) {
				editor = new EENumInlineEditor(inputPath, (EAttribute) feature,
						editingDomain, commandProcessor);
			} else if (dataType == EcorePackage.eINSTANCE.getEBoolean()) {
				editor = new ValueListInlineEditor(inputPath, feature, editingDomain, commandProcessor, 
						CollectionsUtil.makeArrayList(new Pair<String, Object>("Yes", true), new Pair<String, Object>("No", false))
						);
			} else {
				editor = null;
			}
		}
		return editor;
	}

	/**
	 * Set a runnable to run after each validation completes.
	 * 
	 * @param postValidationRunnable2
	 */
	public void setPostValidationRunnable(final Runnable postValidationRunnable) {
		this.postValidationRunnable = postValidationRunnable;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public ICommandProcessor getCommandProcessor() {
		return commandProcessor;
	}

	public IValueProviderProvider getValueProviderProvider() {
		return valueProviderProvider;
	}

	public EMFPath getInputPath() {
		return inputPath;
	}

	public void addSubEditor(AbstractDetailComposite composite, boolean expandColumns) {
		subEditors.add(composite);
		if (expandColumns)
			myLayout.numColumns++;// = subEditors.size() + 1;
	}
}
