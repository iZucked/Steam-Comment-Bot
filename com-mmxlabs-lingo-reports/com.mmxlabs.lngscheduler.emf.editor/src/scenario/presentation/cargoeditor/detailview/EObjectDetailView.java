/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import scenario.presentation.EditorUtils;
import scenario.presentation.cargoeditor.EObjectEditorViewerPane;
import scenario.provider.LngEditPlugin;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
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
	private final ICommandProcessor processor;
	
	private final IBatchValidator validator;
	private EObject input;
	


	public EObjectDetailView(final Composite parent, final int style,
			final ICommandProcessor processor, final EditingDomain editingDomain) {
		super(parent, style);
		// Wrap in processor which triggers validation on object change
		this.processor = new ICommandProcessor() {

			@Override
			public void processCommand(final Command command, final EObject target,
					final EStructuralFeature feature) {
				
				processor .processCommand(command, target, feature);
				
				final IStatus status = validator.validate(input);
				
				for (final IInlineEditor e : editors) {
					e.processValidation(status);
				}
			}
		};
		
		validator = ModelValidationService.getInstance().newValidator(
				EvaluationMode.BATCH);

		// include live constraints, also, in batch validation
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS,
				true);
	}

	/**
	 * This is an interface for objects which should create an
	 * {@link IInlineEditor} for a given path to target + feature to edit +
	 * command processor for processing the final commands
	 * 
	 * @author Tom Hinton
	 * 
	 */
	public interface IInlineEditorFactory {
		public IInlineEditor createEditor(final EMFPath path,
				final EStructuralFeature feature,
				final ICommandProcessor commandProcessor);
	}

	public interface IMultiInlineEditorFactory extends IInlineEditorFactory {
		public IInlineEditor createMultiEditor(final EMFPath path,
				final EStructuralFeature feature,
				final ICommandProcessor commandProcessor);
	}

	/**
	 * This is an interface for an inline editor. It is given an input, and
	 * should generate commands and send them to the appropriate command
	 * processor when stuff happens.
	 * 
	 * @author Tom Hinton
	 * 
	 */
	public interface IInlineEditor {
		public void setInput(final EObject object);

		public Control createControl(final Composite parent);
		
		void processValidation(IStatus status);
	}

	public interface ICommandProcessor {
		public void processCommand(final Command command, final EObject target,
				final EStructuralFeature feature);
	}

	// private class NonEditableEditor extends BasicAttributeInlineEditor {
	// private Label label;
	//
	// public NonEditableEditor(EMFPath path, EStructuralFeature feature) {
	// super(path, feature, null);
	// }
	//
	// @Override
	// public Control createControl(Composite parent) {
	// return (label = new Label(parent, SWT.NONE));
	// }
	//
	// @Override
	// protected void updateDisplay(Object value) {
	// if (value instanceof NamedObject) {
	// label.setText(((NamedObject) value).getName());
	// } else {
	// label.setText(value == null ? "" : value.toString());
	// }
	// }
	// }

	public void setEditorFactoryForClassifier(final EClassifier classifier,
			final IInlineEditorFactory factory) {
		editorFactories.put(classifier, factory);
	}

	public void initForEClass(final EClass objectClass) {
		int groupCount = 1;
		addGroupForEClass(objectClass, EditorUtils.unmangle(objectClass.getName()),
				new CompiledEMFPath(true));
		for (final EReference reference : objectClass.getEAllContainments()) {
			if (reference.isMany()) {
				continue;
			}
			String groupName = nameByFeature.get(reference);
			if (groupName == null) {
				groupName = EditorUtils.unmangle(reference.getName());
			}

			addGroupForEClass(reference.getEReferenceType(), groupName,
					new CompiledEMFPath(true, reference));
			groupCount++;
		}

		this.setLayout(new GridLayout(groupCount, true));
	}

	private void addGroupForEClass(final EClass objectClass,
			final String groupName, final EMFPath path) {
		final Group group = new Group(this, SWT.NONE);

		group.setText(groupName);

		final GridData groupLayoutData = new GridData(SWT.FILL, SWT.FILL, true,
				true);

		group.setLayoutData(groupLayoutData);

		// group.setLayout(new FillLayout());
		// final ScrolledComposite sc = new ScrolledComposite(group,
		// SWT.V_SCROLL);
		// sc.setLayout(new FillLayout());
		// final Composite controls = new Composite(sc, SWT.NONE);
		// sc.setContent(controls);
		// sc.setExpandHorizontal(true);
		// sc.setExpandVertical(false);

		final Composite controls = group;
		final GridLayout groupLayout = new GridLayout(2, false);
		controls.setLayout(groupLayout);
		groupLayout.horizontalSpacing = 10;

		for (final EStructuralFeature attribute : objectClass
				.getEAllStructuralFeatures()) {

			// create editor for this attribute
			if (attribute instanceof EReference) {
				final EReference reference = (EReference) attribute;
				if (reference.isContainment()
						&& !(editorFactoriesByFeature.containsKey(reference)))
					continue;
			}

			final EClassifier attributeType = attribute.getEType();

			final IInlineEditorFactory attributeEditorFactory = editorFactoriesByFeature
					.containsKey(attribute) ? editorFactoriesByFeature
					.get(attribute) : editorFactories.get(attributeType);

			if (attribute.isMany()
					&& !(attributeEditorFactory instanceof IMultiInlineEditorFactory))
				continue;

			// final IInlineEditor attributeEditor = new TextInlineEditor(path,
			// attribute, editingDomain);
			final Control attributeControl;
			if (attributeEditorFactory == null) {
				continue;
				// skip over non-editable fields
				// NonEditableEditor blank = new NonEditableEditor(path,
				// attribute);
				// editors.add(blank);
				// attributeControl = blank.createControl(controls);
			} else {
				final IInlineEditor attributeEditor;
				if (attribute.isMany()) {
					attributeEditor = ((IMultiInlineEditorFactory) attributeEditorFactory)
							.createMultiEditor(path, attribute, processor);
				} else {
					attributeEditor = attributeEditorFactory.createEditor(path,
							attribute, processor);
				}

				// create label for this attribute

				final Label attributeLabel = new Label(controls, SWT.RIGHT);
				// Check supplied mappings
				String attributeName = nameByFeature.get(attribute);
				// Try using LNG Edit Plugin information
				if (attributeName == null) {
					// Construct key in form used by plugin.properties
					// final String key = "_UI_" + objectClass.getName() +"_" +
					// attribute.getName() + "_feature";
					final String key = "_UI_"
							+ attribute.getEContainingClass().getName() + "_"
							+ attribute.getName() + "_feature";
					// TODO: Pass in an instance of ResourceLocator rather than
					// direct dependence on LngEditPlugin
					attributeName = LngEditPlugin.getPlugin().getString(key);
				}
				// Ok, so nothing provided, try and deduce name
				if (attributeName == null)
					attributeName = EditorUtils.unmangle(attribute.getName());
				attributeLabel.setText(attributeName + ": ");
				final GridData labelData = new GridData(SWT.RIGHT, SWT.CENTER,
						false, false);
				attributeLabel.setLayoutData(labelData);

				editors.add(attributeEditor);
				attributeControl = attributeEditor.createControl(controls);
			}

			final GridData editorData = new GridData(SWT.FILL, SWT.FILL, true,
					false);

			attributeControl.setLayoutData(editorData);
		}
		controls.pack();
	}


	public void setInput(final EObject object) {
		
		this.input = object;
		
		// release any reference to old input and handle new input
		for (final IInlineEditor editor : editors) {
			try {
				editor.setInput(object);
			} catch (final Exception ex) {
				System.err.println("Error setting input on " + editor);
				ex.printStackTrace();
			}
		}

		// Set initial validation status
		final IStatus status = (input == null) ? Status.OK_STATUS : validator.validate(input);
		for (final IInlineEditor e : editors) {
			e.processValidation(status);
		}
	}

	private final Map<EStructuralFeature, IInlineEditorFactory> editorFactoriesByFeature = new HashMap<EStructuralFeature, IInlineEditorFactory>();

	public void setEditorFactoryForFeature(final EStructuralFeature feature,
			final IInlineEditorFactory factory) {
		editorFactoriesByFeature.put(feature, factory);
	}

	private final Map<EStructuralFeature, String> nameByFeature = new HashMap<EStructuralFeature, String>();

	public void setNameForFeature(final EStructuralFeature key,
			final String value) {
		nameByFeature.put(key, value);
	}
}
