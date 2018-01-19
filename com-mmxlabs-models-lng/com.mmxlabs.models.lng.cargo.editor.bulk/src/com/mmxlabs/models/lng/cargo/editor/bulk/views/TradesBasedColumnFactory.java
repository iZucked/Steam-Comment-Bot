package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.lang.instrument.IllegalClassFormatException;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorPackage;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.SortData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.AssignmentManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.ContractManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
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
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualSingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class TradesBasedColumnFactory implements ITradesColumnFactory {

	private final class HasRestrictionsFormatter extends BaseFormatter {
		public String render(Object object) {

			if (object instanceof Slot) {
				Slot slot = (Slot) object;
				if (!slot.getAllowedVessels().isEmpty()) {
					return "Y";
				}
				if (slot.isOverrideRestrictions()) {
					if (!slot.getRestrictedContracts().isEmpty()) {
						return "Y";
					}
					if (!slot.getRestrictedPorts().isEmpty()) {
						return "Y";
					}
				} else if (slot.getContract() != null) {
					Contract c = slot.getContract();
					if (!c.getRestrictedContracts().isEmpty()) {
						return "Y";
					}
					if (!c.getRestrictedPorts().isEmpty()) {
						return "Y";
					}
				}
			}
			return "N";
		}
	}

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final IReferenceValueProviderProvider referenceValueProvider, final EditingDomain editingDomain,
			final IScenarioEditingLocation editingLocation, final EClass eclass, final IAdaptable report) {
		switch (columnID) {
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-id": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("LOAD_ID", "L-ID", "The main ID for all except discharge slots", ColumnType.NORMAL, LOAD_START_GROUP,
					DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("LOAD_PORT", "Port", "", ColumnType.NORMAL, LOAD_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-vol": {
			final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("LOAD_VOL", "Vol", "", ColumnType.NORMAL, LOAD_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-date": {
			final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain) {
				@Override
				public Comparable<?> getComparable(final Object object) {

					if (object instanceof Row) {
						final Row rowData = (Row) object;
						if (rowData.getDischargeSlot() != null) {
							return rowData.getDischargeSlot().getWindowStart();
						}
					}

					return super.getComparable(object);
				}
			};
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("LOAD_DATE", "Date", "", ColumnType.NORMAL, LOAD_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-window-length": {
			final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("LOAD_WINDOW_SIZE", "Window Size", "", ColumnType.NORMAL, LOAD_MAIN_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchasecontract": {
			final ContractManipulator rendMan = new ContractManipulator(referenceValueProvider, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("PURCHASE_CONTRACT", "Purchase Contract", null, ColumnType.NORMAL, LOAD_MAIN_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchasecounterparty": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("PURCHASE_COUNTERPARTY", "Counterparty", null, ColumnType.NORMAL, LOAD_MAIN_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-restrictions": {

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID("L-Restrictions");
					if (block == null) {
						block = blockManager.createBlock("L-Restrictions", "Restrictions", LOAD_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);

					{
						ICellRenderer rendMan = new HasRestrictionsFormatter();
						final ColumnHandler createColumn = blockManager.createColumn(block, "", rendMan, (ICellManipulator) null, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(false);
						createColumn.column.getColumn().setSummary(true);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_AllowedVessels(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());

						final ColumnHandler createColumn = blockManager.createColumn(block, "Vessels", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_OverrideRestrictions(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedListsArePermissive(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPorts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());
						final ColumnHandler createColumn = blockManager.createColumn(block, " Ports", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContracts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());
						final ColumnHandler createColumn = blockManager.createColumn(block, "Contracts", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
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
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("DISCHARGE_ID", "D-ID", null, ColumnType.NORMAL, DISCHARGE_START_GROUP, DEFAULT_BLOCK_TYPE,
					DISCHARGE_START_GROUP + "_1", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("DISCHARGE_PORT", "Port", "", ColumnType.NORMAL, DISCHARGE_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-vol": {
			final VolumeAttributeManipulator rendMan = new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("DISCHARGE_VOL", "Vol", "", ColumnType.NORMAL, DISCHARGE_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-date": {
			final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain) {
				@Override
				public Comparable<?> getComparable(final Object object) {

					if (object instanceof Row) {
						final Row rowData = (Row) object;
						if (rowData.getDischargeSlot() != null) {
							return rowData.getDischargeSlot().getWindowStart();
						}
					}

					return super.getComparable(object);
				}
			};
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("DISCHARGE_DATE", "Date", "", ColumnType.NORMAL, DISCHARGE_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-window-length": {
			final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("DISCHARGE_WINDOW_SIZE", "Window Size", "", ColumnType.NORMAL, DISCHARGE_MAIN_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.salescontract": {
			final ContractManipulator rendMan = new ContractManipulator(referenceValueProvider, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("SALES_CONTRACT", "Sales Contract", null, ColumnType.NORMAL, DISCHARGE_MAIN_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.salescounterparty": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("SALES_COUNTERPARTY", "Counterparty", null, ColumnType.NORMAL, DISCHARGE_MAIN_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-restrictions": {

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID("D-Restrictions");
					if (block == null) {
						block = blockManager.createBlock("D-Restrictions", "Restrictions", DISCHARGE_MAIN_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						ICellRenderer rendMan = new HasRestrictionsFormatter();
						final ColumnHandler createColumn = blockManager.createColumn(block, "", rendMan, (ICellManipulator) null, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(false);
						createColumn.column.getColumn().setSummary(true);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_AllowedVessels(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());

						final ColumnHandler createColumn = blockManager.createColumn(block, "Vessels", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_OverrideRestrictions(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Override", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}

					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedListsArePermissive(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Permissive", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedPorts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());
						final ColumnHandler createColumn = blockManager.createColumn(block, " Ports", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);

					}
					{
						final MultipleReferenceManipulator rendMan = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_RestrictedContracts(), referenceValueProvider, editingDomain,
								MMXCorePackage.eINSTANCE.getNamedObject_Name());
						final ColumnHandler createColumn = blockManager.createColumn(block, "Contracts", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
						createColumn.column.getColumn().setDetail(true);
						createColumn.column.getColumn().setSummary(false);
					}
					return null;
				}

			});
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.assignment": {
			final AssignmentManipulator rendMan = new AssignmentManipulator(editingLocation);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory("ASSIGNMENT", "Assignment", null, ColumnType.NORMAL, CARGO_END_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow__GetAssignableObject()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.load-terminal": {
			columnManager.registerColumn("TRADES_TABLE", createWiringColumn("L-TERMINAL", "L-Type", report, true, LOAD_END_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.discharge-terminal": {

			columnManager.registerColumn("TRADES_TABLE", createWiringColumn("D-Terminal", "D-Type", report, false, DISCHARGE_START_GROUP, DEFAULT_BLOCK_TYPE, DISCHARGE_START_GROUP + "_0"));
		}
			break;
		}
	}

	private SimpleEmfBlockColumnFactory createWiringColumn(final String columnID, final String columnName, final IAdaptable report, final boolean isLoad, final String blockGroup,
			final String blockType, final String orderKey) {
		return new SimpleEmfBlockColumnFactory(columnID, columnName, null, ColumnType.NORMAL, blockGroup, blockType, orderKey, null, null, (ETypedElement[]) null) {

			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				final ColumnBlock block = blockManager.createBlock(blockID, blockDisplayName, blockType, blockGroup, orderKey, columnType);

				return blockManager.configureHandler(block, new ColumnHandler(block, (ICellRenderer) null, (ICellManipulator) null, new ETypedElement[0], blockDisplayName, new IColumnFactory() {

					SlotTypePainter diagram;

					@Override
					public void destroy(final GridViewerColumn gvc) {
						gvc.getColumn().dispose();
					}

					@Override
					public GridViewerColumn createColumn(final ColumnHandler handler) {

						if (report == null) {
							return null;
						}

						final GridTreeViewer viewer = (GridTreeViewer) report.getAdapter(GridTreeViewer.class);
						final Table table = (Table) report.getAdapter(Table.class);

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
						column.setLabelProvider(new CellLabelProvider() {

							@Override
							public void update(final ViewerCell cell) {
								// No normal cell update
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
						diagram = new SlotTypePainter(grid, column, isLoad);
						diagram.setTable(table);
						// Link the the sort state
						diagram.setSortData((SortData) report.getAdapter(SortData.class));

						GridViewerHelper.configureLookAndFeel(column);
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
}
