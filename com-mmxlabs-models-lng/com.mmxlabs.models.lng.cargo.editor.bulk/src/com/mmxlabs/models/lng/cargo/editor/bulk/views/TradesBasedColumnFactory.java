/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import com.mmxlabs.models.ui.tabular.columngeneration.SimpleEmfBlockColumnFactory;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.EnumAttributeManipulator;
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
						if (((LoadSlot)slot).getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
							return yesSymbol;
						}
					}

				}
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.isFOBSale()) {
						if (((DischargeSlot)slot).getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
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
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final IReferenceValueProviderProvider referenceValueProvider, final EditingDomain editingDomain,
			final IScenarioEditingLocation scenarioEditingLocation, final EClass eclass, final IAdaptable report) {
		switch (columnID) {
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-id": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "L-ID", "The main ID for all except discharge slots", ColumnType.NORMAL, LOAD_START_GROUP,
					DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan, rendMan, "Load ID", CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualPortSingleReferenceManipulatorExtension(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Port", "Load Port", ColumnType.NORMAL, LOAD_PORT_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, "Load Port", CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-cv": {
			final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getLoadSlot_CargoCV(), editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "CV", "Load CV", ColumnType.NORMAL, LOAD_PORT_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, "Load CV", CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-vol": {
			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_VOLUME_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Load Volume");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);

					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Volume", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Volume");
							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");
							}
						});
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Min", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Max", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit(), editingDomain,
								e -> mapName((VolumeUnits) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_OperationalTolerance(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Op. Tol.", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
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
						block = blockManager.createBlock(columnID, "", LOAD_WINDOW_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Load date");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain) {
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
						final ColumnHandler createColumn = blockManager.createColumn(block, "Window", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());

						final EMFMultiPath path = new EMFMultiPath(true, new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()),
								new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
						createColumn.column.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, path);
						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Window");

							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});
					}
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Date", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());

						final EMFMultiPath path = new EMFMultiPath(true, new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()),
								new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
						createColumn.column.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, path);
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final HoursSingleReferenceManipulator rendMan = new HoursSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Time", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Size", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(), editingDomain,
								e -> mapName((TimePeriod) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Duration(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Duration", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.setTooltip("Visit duration in hours");
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlex(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Flex", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlexUnits(), editingDomain,
								e -> mapName((TimePeriod) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowCounterParty(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Counter Party", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
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
						block = blockManager.createBlock(columnID, "", LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Purchase contract");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ContractManipulator rendMan = new ContractManipulator(referenceValueProvider, editingDomain);

						final ColumnHandler createColumn = blockManager.createColumn(block, "Buy at", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());

						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Buy at");

							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});
					}

					{
						final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Contract(), referenceValueProvider, editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Contract", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final PriceAttributeManipulator rendMan = new PriceAttributeManipulator(CargoPackage.eINSTANCE.getSlot_PriceExpression(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Expression", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.setTooltip("Price expression");
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					return null;
				}
			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-entity": {
			final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Entity(), referenceValueProvider, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Entity", "Load entity", ColumnType.NORMAL, LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, "Purchase Entity", CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchasecounterparty": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Counterparty", "Purchase counterparty", ColumnType.NORMAL, LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, "Purchase Counterparty", CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchase-cn": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__CN, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "CN", "Purchase CN", ColumnType.NORMAL, LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, "Purchase CN", CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-cancelled": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Cancelled(), editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Cancelled", "Purchase is cancelled", ColumnType.NORMAL, LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, "Cancelled", CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-restrictions": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_EXTRA_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Load restrictions");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);

					{
						final ICellRenderer rendMan = new HasRestrictionsFormatter();
						final ColumnHandler createColumn = blockManager.createColumn(block, "Restrictions", rendMan, (ICellManipulator) null, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(false);
						createColumn.column.getColumn().setSummary(true);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Restrictions");
							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});

					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Locked(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Keep open", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVessels(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());

						final ColumnHandler createColumn = blockManager.createColumn(block, "Vessels", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsOverride(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(), editingDomain) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedVesselsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPorts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, " Ports", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}
					
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsOverride(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(), editingDomain) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContracts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Contracts", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsOverride(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(), editingDomain) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					return null;
				}

			});
		}
			break;

		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-id": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "D-ID", null, ColumnType.NORMAL, DISCHARGE_START_GROUP, DEFAULT_BLOCK_TYPE,
					DISCHARGE_START_GROUP + "_1", rendMan, rendMan, "Discharge ID", CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualPortSingleReferenceManipulatorExtension(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Port", "", ColumnType.NORMAL, DISCHARGE_PORT_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, "Discharge Port", CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-vol": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_VOLUME_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Discharge volume");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);

					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Volume", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Volume");
							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Min", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Max", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}

					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit(), editingDomain,
								e -> mapName((VolumeUnits) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_OperationalTolerance(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Op. Tol.", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
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
						block = blockManager.createBlock(columnID, "", DISCHARGE_WINDOW_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Discharge date");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain) {
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
						final ColumnHandler createColumn = blockManager.createColumn(block, "Window", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());

						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Window");

							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});
					}
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Date", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final HoursSingleReferenceManipulator rendMan = new HoursSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Time", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Size", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(), editingDomain,
								e -> mapName((TimePeriod) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Duration(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Duration", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.setTooltip("Visit duration in hours");
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlex(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Flex", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final TextualEnumAttributeManipulator rendMan = new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowFlexUnits(), editingDomain,
								e -> mapName((TimePeriod) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowCounterParty(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Counter Party", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
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
						block = blockManager.createBlock(columnID, "", DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Sales contract");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ContractManipulator rendMan = new ContractManipulator(referenceValueProvider, editingDomain);

						final ColumnHandler createColumn = blockManager.createColumn(block, "Sell at", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());

						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Sell at");

							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});
					}

					{
						final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Contract(), referenceValueProvider, editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Contract", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.eINSTANCE.getSlot_PriceExpression(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Expression", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.setTooltip("Price expression");
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					return null;
				}
			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-entity": {
			final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Entity(), referenceValueProvider, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Entity", "", ColumnType.NORMAL, DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, "Sales Entity", CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.salescounterparty": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Counterparty", null, ColumnType.NORMAL, DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, "Sales counterparty", CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.sales-cn": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__CN, editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "CN", null, ColumnType.NORMAL, DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, "Sales CN", CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-cancelled": {
			final BooleanFlagAttributeManipulator rendMan = new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Cancelled(), editingDomain);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Cancelled", "Sale is cancelled", ColumnType.NORMAL, DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, "Cancelled", CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-restrictions": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_EXTRA_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL, "Discharge restrictions");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ICellRenderer rendMan = new HasRestrictionsFormatter();
						final ColumnHandler createColumn = blockManager.createColumn(block, "Restrictions", rendMan, (ICellManipulator) null, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(false);
						createColumn.column.getColumn().setSummary(true);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Restrictions");

							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Locked(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Keep open", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVessels(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());

						final ColumnHandler createColumn = blockManager.createColumn(block, "Vessels", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsOverride(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(), editingDomain) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedVesselsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPorts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, " Ports", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsOverride(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(), editingDomain) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedPortsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContracts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}

						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Contracts", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsOverride(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(), editingDomain) {

							@Override
							public boolean canEdit(final Object object) {
								if (object instanceof Slot) {
									final Slot<?> slot = (Slot<?>) object;
									return slot.isRestrictedContractsOverride() && super.canEdit(object);
								}
								return false;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					return null;
				}

			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.assignment": {

			final AssignmentManipulator rendMan = new AssignmentManipulator(scenarioEditingLocation);
			columnManager.registerColumn(REPORT_TYPE, new SimpleEmfBlockColumnFactory(columnID, "Assignment", null, ColumnType.NORMAL, CARGO_END_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, "Assignment", CargoBulkEditorPackage.eINSTANCE.getRow__GetAssignableObject()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.load-terminal": {
			columnManager.registerColumn(REPORT_TYPE, createWiringColumn(columnID, "L-Type", report, true, LOAD_END_GROUP, DEFAULT_BLOCK_TYPE, LOAD_END_GROUP + "_1"));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.discharge-terminal": {

			columnManager.registerColumn(REPORT_TYPE, createWiringColumn(columnID, "D-Type", report, false, DISCHARGE_START_GROUP, DEFAULT_BLOCK_TYPE, DISCHARGE_START_GROUP + "_0"));
			break;
		}
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.load-divertible": {

			columnManager.registerColumn(REPORT_TYPE, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_DIVERSION_GROUP, DEFAULT_BLOCK_TYPE, LOAD_DIVERSION_GROUP + "_0", ColumnType.NORMAL, "Load divertible");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ICellRenderer rendMan = new DivertibleFormatter();
						final ColumnHandler createColumn = blockManager.createColumn(block, "Diversion", rendMan, (ICellManipulator) null, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(false);
						createColumn.column.getColumn().setSummary(true);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Diversion");

							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});

					}
					{
						final DESPurchaseDealTypeManipulator rendMan = new DESPurchaseDealTypeManipulator(CargoPackage.eINSTANCE.getLoadSlot_DesPurchaseDealType(), editingDomain) {
							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) object;
									return loadSlot.isDESPurchase();

								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) object;
									if (loadSlot.isDESPurchase()) {
										return super.render(object);

									}

								}
								return "";
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "DES type", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction(), editingDomain) {
							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) object;
									return loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE;

								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) object;
									if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
										return super.render(object);

									}

								}
								return "";
							}
						};

						final ColumnHandler createColumn = blockManager.createColumn(block, "Ship days", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
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
						block = blockManager.createBlock(columnID, "", DISCHARGE_DIVERSION_GROUP, DEFAULT_BLOCK_TYPE, DISCHARGE_DIVERSION_GROUP + "_0", ColumnType.NORMAL, "Discharge divertible");
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final ICellRenderer rendMan = new DivertibleFormatter();
						final ColumnHandler createColumn = blockManager.createColumn(block, "Diversion", rendMan, (ICellManipulator) null, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(false);
						createColumn.column.getColumn().setSummary(true);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Diversion");

							}

							@Override
							public void treeCollapsed(final TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});

					}
					{
						final FOBSaleDealTypeManipulator rendMan = new FOBSaleDealTypeManipulator(CargoPackage.eINSTANCE.getDischargeSlot_FobSaleDealType(), editingDomain) {

							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof DischargeSlot) {
									final DischargeSlot dischargeSlot = (DischargeSlot) object;
									return dischargeSlot.isFOBSale();

								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof DischargeSlot) {
									final DischargeSlot dischargeSlot = (DischargeSlot) object;
									if (dischargeSlot.isFOBSale()) {
										return super.render(object);
									}

								}
								return "";
							}
						};

						final ColumnHandler createColumn = blockManager.createColumn(block, "FOB type", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction(), editingDomain) {
							@Override
							public boolean canEdit(final Object object) {

								if (object instanceof DischargeSlot) {
									final DischargeSlot dischargeSlot = (DischargeSlot) object;
									return dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST;
								}
								return false;
							}

							@Override
							public String render(final Object object) {
								if (object instanceof DischargeSlot) {
									final DischargeSlot dischargeSlot = (DischargeSlot) object;
									if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
										return super.render(object);
									}

								}
								return "";
							}
						};

						final ColumnHandler createColumn = blockManager.createColumn(block, "Ship days", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					return null;
				}

			});
		}
			break;

		}
	}
	
	private SimpleEmfBlockColumnFactory createWiringColumn(final String columnID, final String columnName, final IAdaptable report, final boolean isLoad, final String blockGroup,
			final String blockType, final String orderKey) {
		return new SimpleEmfBlockColumnFactory(columnID, columnName, null, ColumnType.NORMAL, blockGroup, blockType, orderKey, null, null, null, (ETypedElement[]) null) {

			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				final ColumnBlock block = blockManager.createBlock(blockID, blockDisplayName, blockType, blockGroup, orderKey, columnType, blockDisplayName);

				return blockManager.configureHandler(block, new ColumnHandler(block, (ICellRenderer) null, (ICellManipulator) null, new ETypedElement[0], blockDisplayName, new IColumnFactory() {

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

		switch (units) {
		case M3:
			return "m³";
		case MMBTU:
			return "mmBtu";
		}
		return units.getName();
	}

	private static String mapName(final TimePeriod units) {

		switch (units) {
		case DAYS:
			return "Days";
		case HOURS:
			return "Hours";
		case MONTHS:
			return "Months";
		default:
			break;

		}
		return units.getName();
	}
}
