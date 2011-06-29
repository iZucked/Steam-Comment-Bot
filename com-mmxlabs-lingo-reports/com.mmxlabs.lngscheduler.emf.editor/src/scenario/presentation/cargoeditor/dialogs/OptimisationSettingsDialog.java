/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.dialogs;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

import scenario.ScenarioPackage;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.Objective;
import scenario.optimiser.OptimiserPackage;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoPackage;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.BasicAttributeManipulator;
import scenario.presentation.cargoeditor.DialogFeatureManipulator;
import scenario.presentation.cargoeditor.EObjectEditorViewerPane;
import scenario.presentation.cargoeditor.NumericAttributeManipulator;
import scenario.presentation.cargoeditor.curveeditor.CurveDialog;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditor;
import scenario.presentation.cargoeditor.detailview.LocalDateInlineEditor;
import scenario.presentation.cargoeditor.detailview.NumberInlineEditor;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * A dialog for editing the optimisation's current settings.
 * 
 * This is not a reflective one, as it's quite a special case.
 * 
 * @author Tom Hinton
 * 
 */
public class OptimisationSettingsDialog extends Dialog {
	private static final int DEFAULT_CURVE = 0x10;
	private EditingDomain editingDomain;
	private ICommandProcessor processor = new ICommandProcessor() {
		@Override
		public void processCommand(final Command command, final EObject target,
				final EStructuralFeature feature) {
			editingDomain.getCommandStack().execute(command);
		}
	};

	final List<EObjectEditorViewerPane> viewerPanes = new LinkedList<EObjectEditorViewerPane>();
	final List<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	private LSOSettings currentSettings;
	private IWorkbenchPage page;
	private ScenarioEditor editor;

	public OptimisationSettingsDialog(final Shell parentShell,
			final EditingDomain editingDomain, final IWorkbenchPage page,
			final ScenarioEditor editor) {
		super(parentShell);
		this.editingDomain = editingDomain;
		this.page = page;
		this.editor = editor;
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Optimisation Settings Editor");
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite parentComposite = (Composite) super
				.createDialogArea(parent);

		final EMFPath noPath = new CompiledEMFPath(true);
		parentComposite.setLayout(new GridLayout(2, false));
		{
			final Group rangeGroup = new Group(parentComposite, SWT.NONE);
			rangeGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 2, 1));
			rangeGroup.setText("Optimisation Range");
			rangeGroup.setLayout(new GridLayout(5, false));

			final Label freezeLabel = new Label(rangeGroup, SWT.NONE);
			freezeLabel.setText("Freeze initial schedule for:");
			freezeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
					false, false));

			final NumberInlineEditor localEditor = new NumberInlineEditor(
					noPath,
					OptimiserPackage.eINSTANCE
							.getOptimisationSettings_FreezeDaysFromStart(),
					editingDomain, processor);

			editors.add(localEditor);

			final Control control = localEditor.createControl(rangeGroup);
			control.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
					false));

			final Label days = new Label(rangeGroup, SWT.NONE);
			days.setText("days");
			days.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));

			final Label rangeLabel = new Label(rangeGroup, SWT.NONE);
			rangeLabel.setText("Ignore elements after:");
			rangeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
					false));
			final LocalDateInlineEditor afterEditor = new LocalDateInlineEditor(
					noPath,
					OptimiserPackage.eINSTANCE
							.getOptimisationSettings_IgnoreElementsAfter(),
					editingDomain, processor);

			editors.add(afterEditor);

			final Control control2 = afterEditor.createControl(rangeGroup);
			control2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
					false));
		}

		{
			final Group constraintGroup = new Group(parentComposite, SWT.NONE);
			constraintGroup.setLayout(new FillLayout());
			constraintGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, true, 1, 1));
			constraintGroup.setText("Constraints");
			// this creates some problems, going to have to do something else.
			final EObjectEditorViewerPane evp = new EObjectEditorViewerPane(
					page, editor);
			evp.createControl(constraintGroup);

			final List<EReference> path = new LinkedList<EReference>();
			path.add(OptimiserPackage.eINSTANCE
					.getOptimisationSettings_Constraints());
			evp.init(path, editor.getAdapterFactory());

			final BasicAttributeManipulator id = new BasicAttributeManipulator(
					ScenarioPackage.eINSTANCE.getNamedObject_Name(),
					editingDomain);
			evp.addTypicalColumn("Name", id);

			// add enablement column (cell image?)

			parent.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					evp.dispose();
				}
			});

			evp.setTitle(" ");

			viewerPanes.add(evp);
		}
		{
			final Group constraintGroup = new Group(parentComposite, SWT.NONE);
			constraintGroup.setLayout(new FillLayout());
			constraintGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, true, 1, 1));
			constraintGroup.setText("Objectives");
			// this creates some problems, going to have to do something else.
			final EObjectEditorViewerPane evp = new EObjectEditorViewerPane(
					page, editor);
			evp.createControl(constraintGroup);

			final List<EReference> path = new LinkedList<EReference>();
			path.add(OptimiserPackage.eINSTANCE
					.getOptimisationSettings_Objectives());
			evp.init(path, editor.getAdapterFactory());

			final BasicAttributeManipulator id = new BasicAttributeManipulator(
					ScenarioPackage.eINSTANCE.getNamedObject_Name(),
					editingDomain);
			evp.addTypicalColumn("Name", id);

			final NumericAttributeManipulator weight = new NumericAttributeManipulator(
					OptimiserPackage.eINSTANCE.getObjective_Weight(),
					editingDomain);

			evp.addTypicalColumn("Weight", weight);

			final DialogFeatureManipulator curveDialogManipulator = new DialogFeatureManipulator(
					OptimiserPackage.eINSTANCE.getObjective_DiscountCurve(),
					editingDomain) {
				@Override
				protected String renderValue(Object value) {
					if (value == null) {
						return "Default";
					} else {
						return "Custom";
					}
				}

				@Override
				protected Object openDialogBox(Control cellEditorWindow,
						Object object) {
					final Objective objective = (Objective) object;
					if (objective.isSetDiscountCurve()) {
						final MessageDialog dialog = new MessageDialog(
								getParentShell(), "Use default curve", null,
								"Would you like to clear the custom discount curve for objective "
										+ objective.getName()
										+ ", or to edit it?", MessageDialog.QUESTION,
								new String[] {"Edit", "Clear"}, 0);
						
						if (dialog.open() == Window.OK) {
							final CurveDialog cd = new CurveDialog(getParentShell());
							if (cd.open(objective.getDiscountCurve()) == Window.OK) {
								return cd.createNewCurve();
							} else {
								return null;
							}
						} else {
							return SetCommand.UNSET_VALUE;
						}
					} else {
						final CurveDialog cd = new CurveDialog(getParentShell());
						if (cd.open(null) == Window.OK) {
							return cd.createNewCurve();
						} else {
							return null;
						}
					}
				}
			};

			evp.addTypicalColumn("Discount Curve", curveDialogManipulator);

			parent.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					evp.dispose();
				}
			});

			evp.setTitle(" ");

			viewerPanes.add(evp);
		}
		{
			final Group paramsGroup = new Group(parentComposite, SWT.NONE);

			paramsGroup.setLayout(new GridLayout(4, false));

			paramsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 2, 1));
			paramsGroup.setText("Optimiser Parameters");
			// iterations, alpha, etc.

			final Label itersLabel = new Label(paramsGroup, SWT.NONE);
			itersLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
					false, 1, 1));
			itersLabel.setText("Iterations:");

			final NumberInlineEditor itersEditor = new NumberInlineEditor(
					noPath,
					LsoPackage.eINSTANCE.getLSOSettings_NumberOfSteps(),
					editingDomain, processor);

			itersEditor.createControl(paramsGroup);

			editors.add(itersEditor);

			final CompiledEMFPath thresholderPath = new CompiledEMFPath(true,
					LsoPackage.eINSTANCE.getLSOSettings_ThresholderSettings());

			final Label alphaLabel = new Label(paramsGroup, SWT.NONE);
			alphaLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
					false, 1, 1));
			alphaLabel.setText("Cooling/epoch:");

			final NumberInlineEditor alphaEditor = new NumberInlineEditor(
					thresholderPath,
					LsoPackage.eINSTANCE.getThresholderSettings_Alpha(),
					editingDomain, processor);

			alphaEditor.createControl(paramsGroup);
			editors.add(alphaEditor);

			final Label iaLabel = new Label(paramsGroup, SWT.NONE);
			iaLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
					false, 1, 1));
			iaLabel.setText("Initial Acceptance:");

			final NumberInlineEditor iaEditor = new NumberInlineEditor(
					thresholderPath,
					LsoPackage.eINSTANCE
							.getThresholderSettings_InitialAcceptanceRate(),
					editingDomain, processor);

			iaEditor.createControl(paramsGroup);
			editors.add(iaEditor);

			final Label elLabel = new Label(paramsGroup, SWT.NONE);
			elLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
					false, 1, 1));
			elLabel.setText("Epoch Length:");

			final NumberInlineEditor elEditor = new NumberInlineEditor(
					thresholderPath,
					LsoPackage.eINSTANCE.getThresholderSettings_EpochLength(),
					editingDomain, processor);

			elEditor.createControl(paramsGroup);
			editors.add(elEditor);
		}

		setInput(currentSettings);// set the input again, if it was already set
									// and the controls need hooking up.

		return parentComposite;
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		final GridData parentData = (GridData) parent.getLayoutData();
		parentData.grabExcessHorizontalSpace = true;
		parentData.horizontalAlignment = SWT.FILL;

		final GridLayout parentLayout = (GridLayout) parent.getLayout();
		parentLayout.makeColumnsEqualWidth = false;

		final Button defaultCurveButton = createButton(parent, DEFAULT_CURVE,
				"Edit default curve", false);

		defaultCurveButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				true, false));

		// super.createButtonsForButtonBar(parent);

		final Button okButton = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);

		okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Button cancelButton = createButton(parent,
				IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL,
				false);

		cancelButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case DEFAULT_CURVE:
			final DiscountCurve currentCurve = currentSettings
					.getDefaultDiscountCurve();
			final CurveDialog curveDialog = new CurveDialog(getParentShell());
			if (curveDialog.open(currentCurve) == CurveDialog.OK) {
				final DiscountCurve newCurve = curveDialog.createNewCurve();
				currentSettings.setDefaultDiscountCurve(newCurve);
			}
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}

	public int open(final LSOSettings currentSettings) {
		setInput(currentSettings);
		return super.open();
	}

	public LSOSettings getOutput() {
		return currentSettings;
	}

	/**
	 * Takes a copy of the argument and then hooks it up to all the editors.
	 * 
	 * Will return this copy to the caller who can then replace the original, or
	 * do whatever else they fancy.
	 * 
	 * @param currentSettings
	 */
	private void setInput(final LSOSettings inputSettings) {
		if (this.currentSettings != inputSettings)
			this.currentSettings = EcoreUtil.copy(inputSettings);
		for (final IInlineEditor editor : editors) {
			editor.setInput(this.currentSettings);

		}
		for (final EObjectEditorViewerPane evp : viewerPanes) {
			evp.getViewer().setInput(this.currentSettings);
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
