/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.ScenarioPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Scenario instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ScenarioComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ScenarioComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ScenarioComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Scenario", validate);
	}

	public ScenarioComposite(final Composite container, final int style) {
		this(container, style, "Scenario", true);
	}

	/**
	 * Create the main contents
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
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFieldsFromSupers(composite, mainGroup);
    createScenarioFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Scenario.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      AnnotatedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to Scenario
	 * @generated
	 */
	protected static void createScenarioFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createVersionEditor(composite, mainGroup);
    createNameEditor(composite, mainGroup);
    createFleetModelEditor(composite, mainGroup);
    createScheduleModelEditor(composite, mainGroup);
    createPortModelEditor(composite, mainGroup);
    createDistanceModelEditor(composite, mainGroup);
    createCanalModelEditor(composite, mainGroup);
    createCargoModelEditor(composite, mainGroup);
    createContractModelEditor(composite, mainGroup);
    createMarketModelEditor(composite, mainGroup);
    createOptimisationEditor(composite, mainGroup);
    createContainedModelsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the version feature on Scenario
	 * @generated
	 */
	protected static void createVersionEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ScenarioPackage.eINSTANCE.getScenario_Version()),
      "Version");
  }
		
	/**
	 * Create an editor for the name feature on Scenario
	 * @generated
	 */
	protected static void createNameEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ScenarioPackage.eINSTANCE.getScenario_Name()),
      "Name");
  }
		
	/**
	 * Create an editor for the fleetModel feature on Scenario
	 * @generated
	 */
	protected static void createFleetModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final FleetModelComposite sub = 
      new FleetModelComposite(composite, composite.getStyle(), 
        "Fleet Model", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), ScenarioPackage.eINSTANCE.getScenario_FleetModel()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the scheduleModel feature on Scenario
	 * @generated
	 */
	protected static void createScheduleModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final ScheduleModelComposite sub = 
      new ScheduleModelComposite(composite, composite.getStyle(), 
        "Schedule Model", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), ScenarioPackage.eINSTANCE.getScenario_ScheduleModel()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the portModel feature on Scenario
	 * @generated
	 */
	protected static void createPortModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ScenarioPackage.eINSTANCE.getScenario_PortModel()),
      "Port Model");
  }
		
	/**
	 * Create an editor for the distanceModel feature on Scenario
	 * @generated
	 */
	protected static void createDistanceModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ScenarioPackage.eINSTANCE.getScenario_DistanceModel()),
      "Distance Model");
  }
		
	/**
	 * Create an editor for the canalModel feature on Scenario
	 * @generated
	 */
	protected static void createCanalModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ScenarioPackage.eINSTANCE.getScenario_CanalModel()),
      "Canal Model");
  }
		
	/**
	 * Create an editor for the cargoModel feature on Scenario
	 * @generated
	 */
	protected static void createCargoModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final CargoModelComposite sub = 
      new CargoModelComposite(composite, composite.getStyle(), 
        "Cargo Model", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), ScenarioPackage.eINSTANCE.getScenario_CargoModel()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the contractModel feature on Scenario
	 * @generated
	 */
	protected static void createContractModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final ContractModelComposite sub = 
      new ContractModelComposite(composite, composite.getStyle(), 
        "Contract Model", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), ScenarioPackage.eINSTANCE.getScenario_ContractModel()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the marketModel feature on Scenario
	 * @generated
	 */
	protected static void createMarketModelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final MarketModelComposite sub = 
      new MarketModelComposite(composite, composite.getStyle(), 
        "Market Model", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), ScenarioPackage.eINSTANCE.getScenario_MarketModel()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the optimisation feature on Scenario
	 * @generated
	 */
	protected static void createOptimisationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final OptimisationComposite sub = 
      new OptimisationComposite(composite, composite.getStyle(), 
        "Optimisation", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), ScenarioPackage.eINSTANCE.getScenario_Optimisation()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the containedModels feature on Scenario
	 * @generated
	 */
	protected static void createContainedModelsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ScenarioPackage.eINSTANCE.getScenario_ContainedModels()),
      "Contained Models");
  }
}
