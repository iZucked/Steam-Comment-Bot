/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.AssignmentManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.ContractManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.cargo.ui.util.TimeWindowHelper;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortSingleReferenceManipulatorExtension;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.PriceAttributeManipulator;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.DESPurchaseDealTypeManipulator;
import com.mmxlabs.models.ui.editors.FOBSaleDealTypeManipulator;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.models.ui.tabular.columngeneration.EMFReportColumnManager;
import com.mmxlabs.models.ui.tabular.columngeneration.EmfBlockColumnFactory;
import com.mmxlabs.models.ui.tabular.columngeneration.IColumnFactory;
import com.mmxlabs.models.ui.tabular.columngeneration.SingleColumnFactoryBuilder;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.HoursSingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualEnumAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualSingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.util.emfpath.EMFMultiPath;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class TradesBasedColumnFactory implements ITradesColumnFactory {

	private static final String REPORT_TYPE = "TRADES_TABLE";

	private final class HasRestrictionsFormatter extends BaseFormatter {
		private static final String yesSymbol = "\u2713";

		@Override
		public String render(final Object object) {
			if (object instanceof Slot) {
				final Slot<?> slot = (Slot<?>) object;
				if (!slot.getRestrictedVessels().isEmpty()) {
					return yesSymbol;
				}
				if (slot.isLocked()) {
					return yesSymbol;
				}
				if (!slot.getSlotOrDelegateContractRestrictions().isEmpty()) {
					return yesSymbol;
				}
				if (!slot.getSlotOrDelegatePortRestrictions().isEmpty()) {
					return yesSymbol;
				}
				return "";
			}
			return "";
		}
	}

	private final class DivertibleFormatter extends BaseFormatter {
		private static final String yesSymbol = "\u2713";

		@Override
		public String render(final Object object) {
			if (object instanceof Slot) {
				final Slot<?> slot = (Slot<?>) object;
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					if (loadSlot.isDESPurchase()) {
						if (((LoadSlot) slot).getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
							return yesSymbol;
						}
					}

				}
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.isFOBSale()) {
						if (((DischargeSlot) slot).getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
							return yesSymbol;
						}
					}

				}
				return "";
			}
			return "";
		}
	}

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final IScenarioEditingLocation scenarioEditingLocation, final EClass eclass,
			final IAdaptable report) {

		final IReferenceValueProviderProvider referenceValueProvider = scenarioEditingLocation.getReferenceValueProviderCache();
		final ICommandHandler commandHandler = scenarioEditingLocation.getDefaultCommandHandler();

		switch (columnID) {
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-id": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "ID").withTooltip("The main ID for all except discharge slots")
							.withBlockType(LOAD_START_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Load ID")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualPortSingleReferenceManipulatorExtension(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider,
					scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Port").withTooltip("Load Port")
							.withBlockType(LOAD_PORT_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Load Port")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-cv": {
			final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getLoadSlot_CargoCV(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "CV").withTooltip("Load CV")
							.withBlockType(LOAD_PORT_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Load CV")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-vol": {
			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_VOLUME_GROUP, null, null, ColumnType.NORMAL, "Load Volume");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);

					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), scenarioEditingLocation.getDefaultCommandHandler());
						final ColumnHandler handler = blockManager.createColumn(block, "Volume") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asSummaryOnly() //
								.build();

						handler.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								handler.column.getColumn().getColumnGroup().setText("Volume");
							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								handler.column.getColumn().getColumnGroup().setText("");
							}
						});
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Min") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Max") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit(),
								scenarioEditingLocation.getDefaultCommandHandler(), e -> mapName((VolumeUnits) e));
						blockManager.createColumn(block, "Units") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_OperationalTolerance(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Op. Tol.") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					return null;
				}
			});

		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-date": {
			// Customise so we can sort by the discharge date if no load is present

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_WINDOW_GROUP, null, null, ColumnType.NORMAL, "Load date");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(),
								scenarioEditingLocation.getDefaultCommandHandler()) {
							@Override
							public String renderSetValue(final Object owner, final Object object) {
								String v = super.renderSetValue(owner, object);
								if (!v.isEmpty()) {
									final String suffix = TimeWindowHelper.getTimeWindowSuffix(owner);
									v = v + suffix;
								}
								if (!v.isEmpty()) {
									if (owner instanceof Slot) {
										final Slot<?> slot = (Slot<?>) owner;
										if (slot.getWindowFlex() != 0) {
											v = v + " *";
										}
									}
								}
								v = v + (v.isEmpty() ? "" : " ");
								v = v + TimeWindowHelper.getCPWindowSuffix(owner);
								return v;
							}
						};

						blockManager.createColumn(block, "Window") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.withMultiPathForSorting(new EMFMultiPath(true, new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()),
										new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()))) //
								.asSummaryOnly() //
								.withGroupExpandedLabel("Window") //
								.build();
					}
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Date") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.withMultiPathForSorting(new EMFMultiPath(true, new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()),
										new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()))) //
								.asDetailOnly() //
								.build();
					}
					{
						final HoursSingleReferenceManipulator rendMan = new HoursSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Time") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Size") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(),
								scenarioEditingLocation.getDefaultCommandHandler(), e -> mapName((TimePeriod) e));
						blockManager.createColumn(block, "Units") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Duration(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Duration") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.withTooltip("Visit duration in hours")
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlex(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Flex") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlexUnits(),
								scenarioEditingLocation.getDefaultCommandHandler(), e -> mapName((TimePeriod) e));
						blockManager.createColumn(block, "Units") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowCounterParty(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Counter Party") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					return null;
				}
			});

		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchasecontract": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_PRICING_GROUP, null, null, ColumnType.NORMAL, "Purchase contract");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ContractManipulator rendMan = new ContractManipulator(referenceValueProvider, scenarioEditingLocation.getDefaultCommandHandler());

						blockManager.createColumn(block, "Buy at") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asSummaryOnly() //
								.withGroupExpandedLabel("Buy at") //
								.build();
					}

					{
						final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Contract(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Contract") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asDetailOnly() //
								.build();
					}
					{
						final PriceAttributeManipulator rendMan = new PriceAttributeManipulator(CargoPackage.eINSTANCE.getSlot_PriceExpression(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Expression") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.withTooltip("Price expression") //
								.asDetailOnly() //
								.build();
					}
					return null;
				}
			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-entity": {
			final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Entity(), referenceValueProvider,
					scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Entity").withTooltip("Load entity")
							.withBlockType(LOAD_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Purchase Entity")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchasecounterparty": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Counterparty").withTooltip("Purchase counterparty")
							.withBlockType(LOAD_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Purchase Counterparty")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchase-cn": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__CN, scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "CN").withTooltip("Purchase CN")
							.withBlockType(LOAD_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Purchase CN")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-cancelled": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Cancelled(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Cancelled").withTooltip("Purchase is cancelled")
							.withBlockType(LOAD_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Cancelled")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-restrictions": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_EXTRA_GROUP, null, null, ColumnType.NORMAL, "Load restrictions");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);

					{
						final ICellRenderer rendMan = new HasRestrictionsFormatter();
						blockManager.createColumn(block, "Restrictions") //
								.withCellRenderer(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asSummaryOnly() //
								.withGroupExpandedLabel("Restrictions") //
								.build();
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Locked(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Keep open") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVessels(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name());

						final ColumnHandler createColumn = blockManager.createColumn(block, "Vessels") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsOverride(),
								scenarioEditingLocation.getDefaultCommandHandler());
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(),
								scenarioEditingLocation.getDefaultCommandHandler()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedVesselsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPorts(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, " Ports") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsOverride(),
								scenarioEditingLocation.getDefaultCommandHandler());
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(),
								scenarioEditingLocation.getDefaultCommandHandler()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}

					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContracts(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Contracts") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsOverride(),
								scenarioEditingLocation.getDefaultCommandHandler());
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(),
								scenarioEditingLocation.getDefaultCommandHandler()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}

					return null;
				}

			});
		}
			break;

		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-id": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "ID").withBlockType(DISCHARGE_START_GROUP)
							.withOrderKey(DISCHARGE_START_GROUP + "_1")
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Discharge ID")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualPortSingleReferenceManipulatorExtension(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider,
					scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Port").withBlockType(DISCHARGE_PORT_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Discharge Port")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-vol": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_VOLUME_GROUP, null, null, ColumnType.NORMAL, "Discharge volume");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);

					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Volume") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asSummaryOnly()
								.withGroupExpandedLabel("Volume") //
								.build();
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Min") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Max") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}

					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit(),
								scenarioEditingLocation.getDefaultCommandHandler(), e -> mapName((VolumeUnits) e));
						blockManager.createColumn(block, "Units") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_OperationalTolerance(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Op. Tol.") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					return null;
				}
			});
		}
			break;

		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-date": {
			// Customise so we can sort by the discharge date if no load is present

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_WINDOW_GROUP, null, null, ColumnType.NORMAL, "Discharge date");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(),
								scenarioEditingLocation.getDefaultCommandHandler()) {
							@Override
							public String renderSetValue(final Object owner, final Object object) {
								String v = super.renderSetValue(owner, object);
								if (!v.isEmpty()) {
									final String suffix = TimeWindowHelper.getTimeWindowSuffix(owner);
									v = v + suffix;
								}
								if (!v.isEmpty()) {
									if (owner instanceof Slot) {
										final Slot<?> slot = (Slot<?>) owner;
										if (slot.getWindowFlex() != 0) {
											v = v + " *";
										}
									}
								}
								v = v + (v.isEmpty() ? "" : " ");
								v = v + TimeWindowHelper.getCPWindowSuffix(owner);
								return v;
							}
						};
						blockManager.createColumn(block, "Window") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asSummaryOnly()
								.withGroupExpandedLabel("Window")
								.build();
					}
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Date") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final HoursSingleReferenceManipulator rendMan = new HoursSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Time") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Size") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(),
								scenarioEditingLocation.getDefaultCommandHandler(), e -> mapName((TimePeriod) e));
						blockManager.createColumn(block, "Units") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Duration(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Duration") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlex(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Flex") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlexUnits(),
								scenarioEditingLocation.getDefaultCommandHandler(), e -> mapName((TimePeriod) e));
						blockManager.createColumn(block, "Units") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowCounterParty(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Counter Party") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					return null;
				}
			});

		}

			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.salescontract": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_PRICING_GROUP, null, null, ColumnType.NORMAL, "Sales contract");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ContractManipulator rendMan = new ContractManipulator(referenceValueProvider, scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Sell at") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asSummaryOnly() //
								.withGroupExpandedLabel("Sell at") //
								.build();
					}

					{
						final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Contract(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Contract") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.eINSTANCE.getSlot_PriceExpression(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Expression") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					return null;
				}
			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-entity": {
			final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Entity(), referenceValueProvider,
					scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Entity").withBlockType(DISCHARGE_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Sales Entity")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.salescounterparty": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Counterparty").withBlockType(DISCHARGE_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Sales counterparty")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.sales-cn": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__CN, scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "CN").withBlockType(DISCHARGE_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Sales CN")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-cancelled": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Cancelled(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Cancelled").withTooltip("Sale is cancelled")
							.withBlockType(DISCHARGE_PRICING_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Cancelled")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
			break;
		}
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-restrictions": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_EXTRA_GROUP, null, null, ColumnType.NORMAL, "Discharge restrictions");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ICellRenderer rendMan = new HasRestrictionsFormatter();
						blockManager.createColumn(block, "Restrictions") //
								.withCellRenderer(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()) //
								.asSummaryOnly() //
								.withGroupExpandedLabel("Restrictions") //
								.build();
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Locked(), scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Keep open") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVessels(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name());

						blockManager.createColumn(block, "Vessels") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsOverride(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Override") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(),
								scenarioEditingLocation.getDefaultCommandHandler()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedVesselsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						blockManager.createColumn(block, "Permissive") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPorts(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						blockManager.createColumn(block, " Ports") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();

					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsOverride(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Override") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(),
								scenarioEditingLocation.getDefaultCommandHandler()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						blockManager.createColumn(block, "Permissive") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContracts(), referenceValueProvider,
								scenarioEditingLocation.getDefaultCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}

						};
						blockManager.createColumn(block, "Contracts") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsOverride(),
								scenarioEditingLocation.getDefaultCommandHandler());
						blockManager.createColumn(block, "Override") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(),
								scenarioEditingLocation.getDefaultCommandHandler()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						blockManager.createColumn(block, "Permissive") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}

					return null;
				}

			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.assignment": {

			final AssignmentManipulator rendMan = new AssignmentManipulator(scenarioEditingLocation);
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Assignment").withBlockType(CARGO_END_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Assignment")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow__GetAssignableObject())
							.build());
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.load-terminal": {
			columnManager.registerColumn(REPORT_TYPE, createWiringColumn(columnID, "L-Type", report, true, LOAD_END_GROUP, null, LOAD_END_GROUP + "_1"));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.discharge-terminal": {

			columnManager.registerColumn(REPORT_TYPE, createWiringColumn(columnID, "D-Type", report, false, DISCHARGE_START_GROUP, null, DISCHARGE_START_GROUP + "_0"));
			break;
		}
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.load-divertible": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_DIVERSION_GROUP, null, LOAD_DIVERSION_GROUP + "_0", ColumnType.NORMAL, "Load divertible");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ICellRenderer rendMan = new DivertibleFormatter();
						blockManager.createColumn(block, "Diversion") //
								.withCellRenderer(rendMan)
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()) //
								.asSummaryOnly() //
								.withGroupExpandedLabel("Diversion")
								.build();

					}
					{
						final DESPurchaseDealTypeManipulator rendMan = new DESPurchaseDealTypeManipulator(CargoPackage.eINSTANCE.getLoadSlot_DesPurchaseDealType(),
								scenarioEditingLocation.getDefaultCommandHandler()) {
							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof LoadSlot loadSlot) {
									return loadSlot.isDESPurchase();
								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof LoadSlot loadSlot) {
									if (loadSlot.isDESPurchase()) {
										return super.render(object);
									}
								}
								return "";
							}
						};
						blockManager.createColumn(block, "DES type") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();

					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction(),
								scenarioEditingLocation.getDefaultCommandHandler()) {
							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof LoadSlot loadSlot) {
									return loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE;

								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof LoadSlot loadSlot) {
									if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
										return super.render(object);

									}

								}
								return "";
							}
						};

						blockManager.createColumn(block, "Ship days") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
								.asDetailOnly() //
								.build();
					}

					return null;
				}

			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.discharge-divertible": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_DIVERSION_GROUP, null, DISCHARGE_DIVERSION_GROUP + "_0", ColumnType.NORMAL, "Discharge divertible");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ICellRenderer rendMan = new DivertibleFormatter();
						blockManager.createColumn(block, "Diversion") //
								.withCellRenderer(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()) //
								.asSummaryOnly() //
								.withGroupExpandedLabel("Diversion") //
								.build();

					}
					{
						final FOBSaleDealTypeManipulator rendMan = new FOBSaleDealTypeManipulator(CargoPackage.eINSTANCE.getDischargeSlot_FobSaleDealType(),
								scenarioEditingLocation.getDefaultCommandHandler()) {

							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof DischargeSlot dischargeSlot) {
									return dischargeSlot.isFOBSale();

								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof DischargeSlot dischargeSlot) {
									if (dischargeSlot.isFOBSale()) {
										return super.render(object);
									}

								}
								return "";
							}
						};

						blockManager.createColumn(block, "FOB type") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction(),
								scenarioEditingLocation.getDefaultCommandHandler()) {
							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof DischargeSlot dischargeSlot) {
									return dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST;
								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof DischargeSlot dischargeSlot) {
									if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
										return super.render(object);
									}

								}
								return "";
							}
						};

						blockManager.createColumn(block, "Ship days") //
								.withCellEditor(rendMan) //
								.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
								.asDetailOnly() //
								.build();
					}

					return null;
				}

			});
			break;
		}

		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-exposures": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ComputeExposure(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Exposure").withTooltip("Compute Exposures")
							.withBlockType(LOAD_END_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Exposure")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
			break;
		}
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-exposures": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ComputeExposure(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Exposure").withTooltip("Compute Exposures")
							.withBlockType(DISCHARGE_END_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Exposure")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
			break;
		}
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-hedge": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ComputeHedge(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Hedging").withTooltip("Generate Hedge")
							.withBlockType(LOAD_END_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Hedging")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot())
							.build());
			break;
		}
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-hedge": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ComputeHedge(), scenarioEditingLocation.getDefaultCommandHandler());
			columnManager.registerColumn(REPORT_TYPE,
					new SingleColumnFactoryBuilder(columnID, "Hedging").withTooltip("Generate Hedge")
							.withBlockType(DISCHARGE_END_GROUP)
							.withCellEditor(rendMan)
							.withBlockConfigurationName("Hedging")
							.withElementPath(CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot())
							.build());
			break;
		}

		}
	}

	private EmfBlockColumnFactory createWiringColumn(final String columnID, final String columnName, final IAdaptable report, final boolean isLoad, final String blockType, final String blockGroup,
			final String orderKey) {
		return new EmfBlockColumnFactory() {

			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				final ColumnBlock block = blockManager.createBlock(columnID, columnName, blockType, blockGroup, orderKey, ColumnType.NORMAL, null);

				return blockManager.configureHandler(block, new ColumnHandler(block, null, (ICellRenderer) null, (ICellManipulator) null, new ETypedElement[0], columnName, new IColumnFactory() {

					@Override
					public void destroy(final GridViewerColumn gvc) {
						gvc.getColumn().dispose();
					}

					@Override
					public GridViewerColumn createColumn(final ColumnHandler handler) {

						if (report == null) {
							return null;
						}

						final ScenarioTableViewer viewer = report.getAdapter(ScenarioTableViewer.class);

						final GridColumnGroup group = handler.block.getOrCreateColumnGroup(viewer.getGrid());

						final String title = handler.title;
						final ICellRenderer formatter = handler.getFormatter();
						final String tooltip = handler.getTooltip();

						final GridColumn col;
						if (group != null) {
							col = new GridColumn(group, SWT.NONE);
						} else {
							col = new GridColumn(viewer.getGrid(), SWT.NONE);
						}
						final GridViewerColumn column = new GridViewerColumn(viewer, col);

						column.getColumn().setText(title);
						column.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, formatter);

						// Set a default label provider
						column.setLabelProvider(new EObjectTableViewerColumnProvider(viewer, null, null) {
							@Override
							public String getText(final Object element) {
								return null;
							}
						});

						final GridColumn tc = column.getColumn();

						tc.setData(ColumnHandler.COLUMN_HANDLER, this);
						tc.setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, formatter);
						tc.setMoveable(true);
						if (tooltip != null) {
							column.getColumn().setHeaderTooltip(tooltip);
						}

						column.getColumn().setMinimumWidth(100);
						column.getColumn().setWidth(100);
						column.getColumn().setResizeable(false);
						column.getColumn().setVisible(false);

						// Create the custom rendering stuff
						final Grid grid = viewer.getGrid();
						final SlotTypePainter diagram = new SlotTypePainter(grid, column, isLoad);

						GridViewerHelper.configureLookAndFeel(column, GridViewerHelper.FLAGS_ROW_HOVER);
						return column;
					}
				}) {
					@Override
					public void setColumnFactory(final IColumnFactory columnFactory) {
						// Ignore standard column factory
					}

				}, false);
			}

		};
	}

	private static String mapName(final VolumeUnits units) {

		return switch (units) {
		case M3 -> "m³";
		case MMBTU -> "mmBtu";
		default -> units.getName();
		};
	}

	private static String mapName(final TimePeriod units) {

		return switch (units) {
		case DAYS -> "Days";
		case HOURS -> "Hours";
		case MONTHS -> "Months";
		default -> units.getName();
		};
	}
}
