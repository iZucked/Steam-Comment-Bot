package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.ArrayList;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorPackage;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table;
import com.mmxlabs.models.lng.cargo.ui.editorpart.AssignmentManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.ContractManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
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
import com.mmxlabs.models.ui.tabular.manipulators.EnumAttributeManipulator;
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

	private final class TextualPortSingleReferenceManipulatorExtension extends TextualSingleReferenceManipulator {
		private TextualPortSingleReferenceManipulatorExtension(EReference field, IReferenceValueProviderProvider valueProviderProvider, EditingDomain editingDomain) {
			super(field, valueProviderProvider, editingDomain);
		}

		protected IContentProposalProvider createProposalProvider() {
			final IContentProposalProvider proposalProvider = new IContentProposalProvider() {

				@Override
				public IContentProposal[] getProposals(final String full_contents, final int position) {

					final int completeFrom = 0;

					final String contents = full_contents.substring(completeFrom, position);
					final ArrayList<ContentProposal> list = new ArrayList<>();
					for (int i = 0; i < names.size(); ++i) {
						final String proposal = names.get(i);
						if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
							final String c = proposal.substring(contents.length());
							String description = "";
							EObject eObject = valueList.get(i);
							if (eObject instanceof Port) {
								Port port = (Port) eObject;
								Location l = port.getLocation();
								if (l != null) {
									description = " - " + l.getCountry();
								}
							}

							list.add(new ContentProposal(c, proposal + description, null, c.length()));

						}
					}

					return list.toArray(new IContentProposal[list.size()]);
				}
			};
			return proposalProvider;
		}
	}

	private final class HasRestrictionsFormatter extends BaseFormatter {
		public String render(Object object) {

			if (object instanceof Slot) {
				Slot slot = (Slot) object;
				if (!slot.getAllowedVessels().isEmpty()) {
					return "Y";
				}
				if (slot.isLocked()) {
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
				return "N";
			}
			return "";
		}
	}

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final IReferenceValueProviderProvider referenceValueProvider, final EditingDomain editingDomain,
			final IScenarioEditingLocation editingLocation, final EClass eclass, final IAdaptable report) {
		switch (columnID) {
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-id": {
			final BasicAttributeManipulator rendMan = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "L-ID", "The main ID for all except discharge slots", ColumnType.NORMAL, LOAD_START_GROUP,
					DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualPortSingleReferenceManipulatorExtension(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "Port", "", ColumnType.NORMAL, LOAD_PORT_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-cv": {
			final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getLoadSlot_CargoCV(), editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "CV", "", ColumnType.NORMAL, LOAD_PORT_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-vol": {

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_VOLUME_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
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
							public void treeExpanded(TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Volume");
							}

							@Override
							public void treeCollapsed(TreeEvent e) {
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
								(e) -> mapName((VolumeUnits) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
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

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_WINDOW_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain) {
							@Override
							public String renderSetValue(final Object owner, final Object object) {
								final String v = super.renderSetValue(owner, object);
								if (v != "") {
									final String suffix = getTimeWindowSuffix(owner);
									return v + suffix;
								}
								return v;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Window", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());

						EMFMultiPath path = new EMFMultiPath(true, new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()),
								new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
						createColumn.column.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, path);
						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Window");

							}

							@Override
							public void treeCollapsed(TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("");

							}
						});
					}
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Date", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());

						EMFMultiPath path = new EMFMultiPath(true, new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()),
								new EMFPath(true, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
						createColumn.column.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, path);
						createColumn.column.getColumn().setSummary(false);
						createColumn.column.getColumn().setDetail(true);
					}
					{
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(), editingDomain);
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
								(e) -> mapName((TimePeriod) e));
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
					return null;
				}
			});

		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchasecontract": {

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
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
							public void treeExpanded(TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Buy at");

							}

							@Override
							public void treeCollapsed(TreeEvent e) {
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
						final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.eINSTANCE.getSlot_PriceExpression(), editingDomain);
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
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "Entity", "", ColumnType.NORMAL, LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.purchasecounterparty": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "Counterparty", null, ColumnType.NORMAL, LOAD_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-restrictions": {

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "Restrictions", LOAD_EXTRA_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
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
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Locked(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Keep open", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
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
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "D-ID", null, ColumnType.NORMAL, DISCHARGE_START_GROUP, DEFAULT_BLOCK_TYPE,
					DISCHARGE_START_GROUP + "_1", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-port": {
			final TextualSingleReferenceManipulator rendMan = new TextualPortSingleReferenceManipulatorExtension(CargoPackage.eINSTANCE.getSlot_Port(), referenceValueProvider, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "Port", "", ColumnType.NORMAL, DISCHARGE_PORT_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, rendMan,
					rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-vol": {

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_VOLUME_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
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
							public void treeExpanded(TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Volume");
							}

							@Override
							public void treeCollapsed(TreeEvent e) {
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
								(e) -> mapName((VolumeUnits) e));
						final ColumnHandler createColumn = blockManager.createColumn(block, "Units", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
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

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_WINDOW_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
					}
					block.setPlaceholder(true);
					block.setExpandable(true);
					block.setExpandByDefault(false);
					block.setForceGroup(true);
					{
						final LocalDateAttributeManipulator rendMan = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editingDomain) {
							@Override
							public String renderSetValue(final Object owner, final Object object) {
								final String v = super.renderSetValue(owner, object);
								if (v != "") {
									final String suffix = getTimeWindowSuffix(owner);
									return v + suffix;
								}
								return v;
							}
						};
						final ColumnHandler createColumn = blockManager.createColumn(block, "Window", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());

						createColumn.column.getColumn().setSummary(true);
						createColumn.column.getColumn().setDetail(false);

						createColumn.column.getColumn().getColumnGroup().addTreeListener(new TreeListener() {

							@Override
							public void treeExpanded(TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Window");

							}

							@Override
							public void treeCollapsed(TreeEvent e) {
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
						final NumericAttributeManipulator rendMan = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Time", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_LoadSlot());
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
								(e) -> mapName((TimePeriod) e));
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
					return null;
				}
			});

		}

			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.salescontract": {
			
			
			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "", DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
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
							public void treeExpanded(TreeEvent e) {
								createColumn.column.getColumn().getColumnGroup().setText("Sell at");

							}

							@Override
							public void treeCollapsed(TreeEvent e) {
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
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "Entity", "", ColumnType.NORMAL, DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.salescounterparty": {
			final StringAttributeManipulator rendMan = new StringAttributeManipulator(CargoPackage.Literals.SLOT__COUNTERPARTY, editingDomain);
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "Counterparty", null, ColumnType.NORMAL, DISCHARGE_PRICING_GROUP, DEFAULT_BLOCK_TYPE,
					DEFAULT_ORDER_KEY, rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.d-restrictions": {

			columnManager.registerColumn("TRADES_TABLE", new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(columnID);
					if (block == null) {
						block = blockManager.createBlock(columnID, "Restrictions", DISCHARGE_EXTRA_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY, ColumnType.NORMAL);
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
					{
						final BooleanAttributeManipulator rendMan = new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Locked(), editingDomain);
						final ColumnHandler createColumn = blockManager.createColumn(block, "Keep open", rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow_DischargeSlot());
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
			columnManager.registerColumn("TRADES_TABLE", new SimpleEmfBlockColumnFactory(columnID, "Assignment", null, ColumnType.NORMAL, CARGO_END_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY,
					rendMan, rendMan, CargoBulkEditorPackage.eINSTANCE.getRow__GetAssignableObject()));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.load-terminal": {
			columnManager.registerColumn("TRADES_TABLE", createWiringColumn(columnID, "L-Type", report, true, LOAD_END_GROUP, DEFAULT_BLOCK_TYPE, DEFAULT_ORDER_KEY));
		}
			break;
		case "com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.discharge-terminal": {

			columnManager.registerColumn("TRADES_TABLE", createWiringColumn(columnID, "D-Type", report, false, DISCHARGE_START_GROUP, DEFAULT_BLOCK_TYPE, DISCHARGE_START_GROUP + "_0"));
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

						final ScenarioTableViewer viewer = (ScenarioTableViewer) report.getAdapter(ScenarioTableViewer.class);
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
						diagram = new SlotTypePainter(grid, column, isLoad);

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

	protected String getTimeWindowSuffix(final Object owner) {
		if (owner instanceof Slot) {
			final Slot slot = (Slot) owner;
			final int size = slot.getSlotOrPortWindowSize();
			final TimePeriod units = slot.getSlotOrPortWindowSizeUnits();
			String suffix = "h";
			switch (units) {
			case DAYS:
				suffix = "d";
				break;
			case HOURS:
				suffix = "h";
				break;
			case MONTHS:
				suffix = "m";
				break;
			default:
				return "";

			}
			if (size > 0) {
				return String.format(" +%d%s", size, suffix);
			}

		}
		return "";
	}

	private static String mapName(VolumeUnits units) {

		switch (units) {
		case M3:
			return "m³";
		case MMBTU:
			return "mmBtu";
		}
		return units.getName();
	}

	private static String mapName(TimePeriod units) {

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
