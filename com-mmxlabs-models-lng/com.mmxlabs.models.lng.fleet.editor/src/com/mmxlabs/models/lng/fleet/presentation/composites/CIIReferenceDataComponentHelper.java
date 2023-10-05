/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.models.lng.fleet.CIIGradeBoundary;
import com.mmxlabs.models.lng.fleet.CIIReductionFactor;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * A component helper for CII Reference data
 *
 * @generated NOT
 */
public class CIIReferenceDataComponentHelper extends DefaultComponentHelper {

	private static final FleetPackage fp = FleetPackage.eINSTANCE;
	
	public CIIReferenceDataComponentHelper() {
		super(fp.getCIIReferenceData());
		
		addEditor(fp.getCIIReferenceData_FuelEmissions(), createFuelReferenceTable());
		addEditor(fp.getCIIReferenceData_ReductionFactors(), createReductionFactorsTable());
		addEditor(fp.getCIIReferenceData_CiiGradeBoundaries(), createCIIGradeBoundaryTable());
	}
	
	/**
	 * Create the editor for the Fuel Emissions 
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createFuelReferenceTable() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Fuel reference table by type");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(150);

			b.withComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {
					
					final FuelEmissionReference fer1 = (FuelEmissionReference) e1;
					final FuelEmissionReference fer2 = (FuelEmissionReference) e2;
					
					//
					final double v1 = fer1 == null ? -1 : fer1.getCf();
					final double v2 = fer2 == null ? 2 : fer2.getCf();
					//
					return Double.compare(v1, v2);
				}
			});

			b.buildColumn("Name", MMXCorePackage.Literals.NAMED_OBJECT__NAME)
					.withWidth(200)//
					.withRMMaker((ch, rvp) -> new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), ch))
					.build();

			b.buildColumn("ISO Reference", fp.getFuelEmissionReference_IsoReference()) //
					.withWidth(250) //
					.withRMMaker((ch, rvp) -> new StringAttributeManipulator(fp.getFuelEmissionReference_IsoReference(), ch)) //
					.build();

			b.buildColumn("CF (t-CO/t Fuel)", fp.getFuelEmissionReference_Cf()) //
					.withWidth(125) //
					.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getFuelEmissionReference_Cf(), ch)) //
					.build();
			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
				final FuelEmissionReference fer = FleetFactory.eINSTANCE.createFuelEmissionReference();
				final Command c = AddCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_FuelEmissions(), fer);
				ch.handleCommand(c, refData, fp.getCIIReferenceData_FuelEmissions());

			});
			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
				if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
					final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
					final Command c = RemoveCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_FuelEmissions(), ss.toList());
					ch.handleCommand(c, refData, fp.getCIIReferenceData_FuelEmissions());
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(fp.getCIIReferenceData_FuelEmissions());
		};
	}
		
		/**
		 * Create the editor for the Reduction Factors
		 *
		 * @generated NOT
		 */
		protected Function<EClass, IInlineEditor> createReductionFactorsTable() {
			return topClass -> {
				final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
				b.withShowHeaders(true);
				b.withLabel("Reduction factors");
				b.withContentProvider(new ArrayContentProvider());
				b.withHeightHint(150);

				b.withComparator(new ViewerComparator() {
					@Override
					public int compare(final Viewer viewer, final Object e1, final Object e2) {
						
						final CIIReductionFactor rf1 = (CIIReductionFactor) e1;
						final CIIReductionFactor rf2 = (CIIReductionFactor) e2;
						
						//
						final int v1 = rf1 == null ? -1 : rf1.getYear();
						final int v2 = rf2 == null ? 2 : rf2.getYear();
						//
						return Integer.compare(v1, v2);
					}
				});

				b.buildColumn("Year", fp.getCIIReductionFactor_Year())
						.withWidth(70)//
						.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIReductionFactor_Year(), ch))
						.build();
				
				b.buildColumn("%", fp.getCIIReductionFactor_Percentage()) //
				.withWidth(50) //
				.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIReductionFactor_Percentage(), ch)) //
				.build();
				
				b.buildColumn("Remark", fp.getCIIReductionFactor_Remark()) //
						.withWidth(350) //
						.withRMMaker((ch, rvp) -> new StringAttributeManipulator(fp.getCIIReductionFactor_Remark(), ch)) //
						.build();

				// Add action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
					final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
					final CIIReductionFactor fer = FleetFactory.eINSTANCE.createCIIReductionFactor();
					final Command c = AddCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_ReductionFactors(), fer);
					ch.handleCommand(c, refData, fp.getCIIReferenceData_ReductionFactors());

				});
				// Delete action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
					if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
						final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
						final Command c = RemoveCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_ReductionFactors(), ss.toList());
						ch.handleCommand(c, refData, fp.getCIIReferenceData_ReductionFactors());
					}
				}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

				return b.build(fp.getCIIReferenceData_ReductionFactors());
			};
	}
		
		/**
		 * Create the editor for the CII Grade boundaries
		 *
		 * @generated NOT
		 */
		protected Function<EClass, IInlineEditor> createCIIGradeBoundaryTable() {
			return topClass -> {
				final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
				b.withShowHeaders(true);
				b.withLabel("CII Grade boundaries");
				b.withContentProvider(new ArrayContentProvider());
				b.withHeightHint(100);

				b.withComparator(new ViewerComparator() {
					@Override
					public int compare(final Viewer viewer, final Object e1, final Object e2) {
						
						final CIIGradeBoundary gb1 = (CIIGradeBoundary) e1;
						final CIIGradeBoundary gb2 = (CIIGradeBoundary) e2;
						
						//
						final double v1 = gb1 == null ? -1 : gb1.getDwtUpperLimit();
						final double v2 = gb2 == null ? 2 : gb2.getDwtUpperLimit();
						//
						return Double.compare(v1, v2);
					}
				});

				b.buildColumn("Capacity", fp.getCIIGradeBoundary_DwtUpperLimit())
						.withWidth(100)//
						.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIGradeBoundary_DwtUpperLimit(), ch))
						.build();
				// made a flat data structure for now#
				// might need to make a proper one instead
				b.buildColumn("exp(d1) [A]", fp.getCIIGradeBoundary_GradeAValue()) //
				.withWidth(120) //
				.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIGradeBoundary_GradeAValue(), ch)) //
				.build();
				
				b.buildColumn("exp(d2) [B]", fp.getCIIGradeBoundary_GradeAValue()) //
				.withWidth(120) //
				.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIGradeBoundary_GradeBValue(), ch)) //
				.build();
				
				b.buildColumn("exp(d3) [C]", fp.getCIIGradeBoundary_GradeAValue()) //
				.withWidth(120) //
				.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIGradeBoundary_GradeCValue(), ch)) //
				.build();
				
				b.buildColumn("exp(d4) [D]", fp.getCIIGradeBoundary_GradeAValue()) //
				.withWidth(120) //
				.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIGradeBoundary_GradeDValue(), ch)) //
				.build();

				// Add action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
					final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
					final CIIGradeBoundary gbs = FleetFactory.eINSTANCE.createCIIGradeBoundary();
					final Command c = AddCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_CiiGradeBoundaries(), gbs);
					ch.handleCommand(c, refData, fp.getCIIReferenceData_CiiGradeBoundaries());

				});
				// Delete action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
					if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
						final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
						final Command c = RemoveCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_CiiGradeBoundaries(), ss.toList());
						ch.handleCommand(c, refData, fp.getCIIReferenceData_CiiGradeBoundaries());
					}
				}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

				return b.build(fp.getCIIReferenceData_CiiGradeBoundaries());
			};
	}
}