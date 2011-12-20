package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing TotalVolumeLimit instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class TotalVolumeLimitComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public TotalVolumeLimitComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public TotalVolumeLimitComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Total Volume Limit", validate);
	}

	public TotalVolumeLimitComposite(final Composite container, final int style) {
		this(container, style, "Total Volume Limit", true);
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
    createTotalVolumeLimitFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of TotalVolumeLimit.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to TotalVolumeLimit
	 * @generated
	 */
	protected static void createTotalVolumeLimitFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createPortsEditor(composite, mainGroup);
    createMaximumVolumeEditor(composite, mainGroup);
    createStartDateEditor(composite, mainGroup);
    createDurationEditor(composite, mainGroup);
    createRepeatingEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the ports feature on TotalVolumeLimit
	 * @generated
	 */
	protected static void createPortsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getTotalVolumeLimit_Ports()),
      "Ports");
  }
		
	/**
	 * Create an editor for the maximumVolume feature on TotalVolumeLimit
	 * @generated
	 */
	protected static void createMaximumVolumeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getTotalVolumeLimit_MaximumVolume()),
      "Maximum Volume");
  }
		
	/**
	 * Create an editor for the startDate feature on TotalVolumeLimit
	 * @generated
	 */
	protected static void createStartDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getTotalVolumeLimit_StartDate()),
      "Start Date");
  }
		
	/**
	 * Create an editor for the duration feature on TotalVolumeLimit
	 * @generated
	 */
	protected static void createDurationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getTotalVolumeLimit_Duration()),
      "Duration");
  }
		
	/**
	 * Create an editor for the repeating feature on TotalVolumeLimit
	 * @generated
	 */
	protected static void createRepeatingEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getTotalVolumeLimit_Repeating()),
      "Repeating");
  }
}
