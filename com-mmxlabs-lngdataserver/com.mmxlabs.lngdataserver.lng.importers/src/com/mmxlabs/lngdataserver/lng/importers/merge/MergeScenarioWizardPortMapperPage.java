/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataser.lng.importers.merge.support.AddOption;
import com.mmxlabs.lngdataser.lng.importers.merge.support.IgnoreOption;
import com.mmxlabs.lngdataser.lng.importers.merge.support.MappingOption;
import com.mmxlabs.lngdataser.lng.importers.merge.support.MergeOption;
import com.mmxlabs.lngdataser.lng.importers.merge.support.MergePair;
import com.mmxlabs.lngdataser.lng.importers.merge.support.MergeRow;
import com.mmxlabs.lngdataser.lng.importers.merge.support.OverwriteExistingOption;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;

public class MergeScenarioWizardPortMapperPage extends WizardPage implements IScenarioDependent {

	protected TableViewer mergeTableViewer;
	private NamedObjectListGetter namedObjectGetter = s -> ScenarioModelUtil.findReferenceModel(s).getPortModel().getPorts();
	private ModelGetter modelGetter = ScenarioModelUtil::getPortModel;
	private EStructuralFeature feature = PortPackage.Literals.PORT_MODEL__PORTS;
	private List<TableColumn> tableColumns = new ArrayList<>();
	private final ObjectMapper mapper = new ObjectMapper();

	public MergeScenarioWizardPortMapperPage(String title) {
		super(title, title, null);
		mapper.registerModule(new EMFJacksonModule());
	}

	public ModelGetter getModelGetter() {
		return this.modelGetter;
	}

	public EStructuralFeature getFeature() {
		return this.feature;
	}

	private List<MergeRow<Port>> getMergeRows() {
		if (mergeTableViewer.getInput() instanceof List) {
			return (List<MergeRow<Port>>) mergeTableViewer.getInput();
		}
		return Collections.emptyList();
	}

	private Pair<NamedObjectListGetter, List<MergeMapping>> getMergeMappings() {
		List<MergeMapping> mm = Collections.emptyList();
		if (mergeTableViewer.getInput() instanceof List) {
			mm = (List<MergeMapping>) mergeTableViewer.getInput();
		}
		return Pair.of(namedObjectGetter, mm);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE | SWT.BORDER);
		mergeTableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		container.setLayout(new FillLayout());
		setControl(container);
		dialogChanged();
	}

	protected void dialogChanged() {
		updateStatus(null);
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	public void update(String targetName, String sourceName, LNGScenarioModel target, LNGScenarioModel source) {
		if (source != null && target != null) {

			final List<Port> sourcePorts = ScenarioModelUtil.findReferenceModel(source).getPortModel().getPorts();
			final List<Port> targetPorts = new ArrayList<>(ScenarioModelUtil.findReferenceModel(target).getPortModel().getPorts());
			targetPorts.sort((p1, p2) -> ScenarioElementNameHelper.getName(p1, "<Unknown>").compareTo(ScenarioElementNameHelper.getName(p2, "<Unknown>")));

			final List<MappingOption<Port>> targetMappingOptions = new ArrayList<>(targetPorts.size());
			{
				int i = 0;
				for (final Port port : targetPorts) {
					targetMappingOptions.add(new MappingOption<>(port, i++));
				}
			}

			final AddOption addOption = new AddOption();
			final IgnoreOption ignoreOption = new IgnoreOption();
			final OverwriteExistingOption overwriteExistingOption = new OverwriteExistingOption();
			final Map<Port, MappingOption<Port>> targetPortToMappingOptions = targetMappingOptions.stream().collect(Collectors.toMap(MappingOption::getElement, Function.identity()));

			final List<MergePair<Port>> mergePairs = createMergePairs(sourcePorts, targetPorts, addOption, targetPortToMappingOptions);
			final List<MergeRow<Port>> mergeRows = createMergeRows(mergePairs);

			Table table = mergeTableViewer.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			mergeTableViewer.setContentProvider(ArrayContentProvider.getInstance().getInstance());

			mergeTableViewer.setComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, Object e1, Object e2) {
					final MergeRow<Port> row1 = (MergeRow<Port>) e1;
					final MergeRow<Port> row2 = (MergeRow<Port>) e2;
					final String row1Name = ScenarioElementNameHelper.getName(row1.getMergePair().getFrom(), "<Unknown>");
					final String row2Name = ScenarioElementNameHelper.getName(row2.getMergePair().getFrom(), "<Unknown>");
					return row1Name.compareTo(row2Name);
				}
			});

			String[] titles = { "Source:" + sourceName, "Target:" + targetName, "Info" };

			for (int i = 0; i < titles.length; i++) {

				// Only create new column if none already present.
				TableColumn column = this.tableColumns.size() > i ? this.tableColumns.get(i) : null;
				boolean newCol = false;

				if (column == null) {
					column = new TableColumn(table, SWT.NONE);
					column.setWidth(200);
					newCol = true;
					this.tableColumns.add(column);
				}
				column.setText(titles[i]);

				if (i == 1 && newCol) {
					TableViewerColumn viewerColumn = new TableViewerColumn(this.mergeTableViewer, column);
					viewerColumn.setLabelProvider(new ColumnLabelProvider());
					viewerColumn.setEditingSupport(new EditingSupport(mergeTableViewer) {

						String[] valuesAddIgnore = new String[2 + targetMappingOptions.size()];
						{
							valuesAddIgnore[0] = addOption.toString();
							valuesAddIgnore[1] = ignoreOption.toString();
							int i = 2;
							for (final MappingOption<Port> mappingOption : targetMappingOptions) {
								valuesAddIgnore[i++] = mappingOption.toString();
							}
						}
						String[] valuesOverwrite = new String[1 + targetMappingOptions.size()];
						{
							valuesOverwrite[0] = overwriteExistingOption.toString();
							int i = 1;
							for (final MappingOption<Port> mappingOption : targetMappingOptions) {
								valuesOverwrite[i++] = mappingOption.toString();
							}
						}

						boolean isMappable(Object element) {
							return (element instanceof MergeRow) && ((MergeRow) element).hasDefaultMapping();
						}

						String[] getValues(Object element) {
							if (isMappable(element)) {
								return this.valuesOverwrite;
							} else {
								return this.valuesAddIgnore;
							}
						}

						@Override
						protected CellEditor getCellEditor(Object element) {
							String[] values = getValues(element);
							return new ComboBoxCellEditor(table, values);
						}

						@Override
						protected boolean canEdit(Object element) {
							return true;
						}

						@Override
						protected Object getValue(Object element) {
							if (element instanceof MergeRow) {
								return ((MergeRow) element).getIndex();
							}
							return null;
						}

						@Override
						protected void setValue(Object element, Object valueIndex) {
							if (valueIndex instanceof Integer) {
								int index = (Integer) valueIndex;
								if (index < 0)
									return;
								final String values[] = getValues(element);
								if (index >= values.length) {
									return;
								}
								final MergeRow<Port> mergeRow = (MergeRow<Port>) element;
								final MergePair<Port> mergePair = mergeRow.getMergePair();
								if (isMappable(element)) {
									if (index == 0) {
										mergePair.setTo(overwriteExistingOption);
										getViewer().update(element, null);
									} else {
										--index;
										final MergeOption mergeOption = targetMappingOptions.get(index);
										mergePair.setTo(mergeOption);
										getViewer().update(element, null);
									}
								} else {
									if (index < 2) {
										if (index == 0) {
											mergePair.setTo(addOption);
										} else {
											mergePair.setTo(ignoreOption);
										}
										getViewer().update(element, null);
									} else {
										final MergeOption mergeOption = targetMappingOptions.get(index - 2);
										mergePair.setTo(mergeOption);
										getViewer().update(element, null);
									}
								}
							}
						}
					});
				}
			}

			mergeTableViewer.setLabelProvider(new ITableLabelProvider() {

				@Override
				public void addListener(ILabelProviderListener listener) {
				}

				@Override
				public void dispose() {
				}

				@Override
				public boolean isLabelProperty(Object element, String property) {
					return false;
				}

				@Override
				public void removeListener(ILabelProviderListener listener) {
				}

				@Override
				public Image getColumnImage(Object element, int columnIndex) {
					return null;
				}

				@Override
				public String getColumnText(Object element, int columnIndex) {
					if (element instanceof MergeRow) {
						final MergeRow<Port> row = (MergeRow<Port>) element;
						switch (columnIndex) {
						case 0:
							return ScenarioElementNameHelper.getName(row.getMergePair().getFrom(), "<Unknown>");
						case 1:
							return row.getMergePair().getTo().toString();
						case 2:
							return row.getInfo();
						}
					}
					return null;
				}

			});

			mergeTableViewer.setInput(mergeRows);
		}
	}

	private List<MergePair<Port>> createMergePairs(final List<Port> sourcePorts, final List<Port> targetPorts, @NonNull final AddOption addOption,
			@NonNull final Map<Port, MappingOption<Port>> mappingOptionsMap) {
		final Map<String, Port> targetMmxIdMap = targetPorts.stream().filter(p -> p.mmxID() != null).collect(Collectors.toMap(p -> p.mmxID(), Function.identity()));
		final List<MergePair<Port>> mergePairs = new ArrayList<>();
		for (final Port sourcePort : sourcePorts) {
			final Port suggestedTargetPort = targetMmxIdMap.get(sourcePort.mmxID());
			final MergeOption mergeOption;
			if (suggestedTargetPort != null) {
				mergeOption = mappingOptionsMap.get(suggestedTargetPort);
				if (mergeOption == null) {
					throw new IllegalStateException("All target ports should be a mapping option.");
				}
			} else {
				mergeOption = addOption;
			}
			mergePairs.add(new MergePair<>(sourcePort, mergeOption));
		}

		return mergePairs;
	}

	private List<MergeRow<Port>> createMergeRows(final List<MergePair<Port>> mergePairs) {

		return mergePairs.stream().map(mergePair -> {
			final String info;
			if (mergePair.getTo() instanceof MappingOption) {
				final MappingOption<Port> mappingOption = (MappingOption<Port>) mergePair.getTo();
				final Port sourcePort = mergePair.getFrom();
				final Port targetPort = mappingOption.getElement();

				if (!isJSONEqual(sourcePort, targetPort)) {
					info = "Source different from target version.";
				} else {
					info = "";
				}
			} else {
				info = "Not present in target.";
			}
			return new MergeRow<>(mergePair, info);
		}).collect(Collectors.toCollection(ArrayList::new));
	}

	protected List<String> getDiff(LNGScenarioModel sm, LNGScenarioModel tm) {
		List<String> diffs = new ArrayList<>();

		if (sm != null && tm != null) {
			final List<Port> portsS = ScenarioModelUtil.findReferenceModel(sm).getPortModel().getPorts();
			final List<Port> portsT = ScenarioModelUtil.findReferenceModel(sm).getPortModel().getPorts();

			for (final Port o : portsS) {
				final String mmxIdS = o.mmxID();
				if (mmxIdS != null) {
					Optional<Port> oT = portsT.stream().filter(ot -> ot.mmxID() != null && ot.mmxID().equals(mmxIdS)).findFirst();

					if (oT.isPresent()) {
						if (!isJSONEqual(o, oT.get())) {
							diffs.add("Source different from target version.");
						} else {
							diffs.add("");
						}
					} else {
						diffs.add("Not present in target.");
					}
				} else {
					diffs.add("Not present in target.");
				}
			}
		}

		return diffs;
	}

	protected String getName(Object o) {
		if (o instanceof NamedObject) {
			String name = ((NamedObject) o).getName();
			return name;
		} else {
			return "Unknown";
		}
	}

	protected List<? extends EObject> getEObjects(LNGScenarioModel sm) {
		List<? extends NamedObject> emfObjects = namedObjectGetter.getNamedObjects(sm);
		return emfObjects;
	}

	private boolean isJSONEqual(EObject o1, EObject o2) {
		try {
			String o1Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o1);
			o1Str = filterIDs(o1Str);
			String o2Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o2);
			o2Str = filterIDs(o2Str);
			boolean equal = o1Str.equals(o2Str);

			if (!equal) {
				System.out.println("o1Str = " + o1.toString() + "o2Str = " + o2.toString());
				String[] o1Lines = o1Str.split("\n");
				String[] o2Lines = o2Str.split("\n");
				if (o1Lines.length != o2Lines.length) {
					System.out.println("o1 lines = " + o1Lines.length + " o2 lines = " + o2Lines.length);
				}

				for (int i = 0; i < Math.max(o1Lines.length, o2Lines.length); i++) {
					if (i < o1Lines.length) {
						if (!o2Str.contains(o1Lines[i])) {
							System.out.println("Diff " + i + "(1):\t" + o1Lines[i]);
						}
					}
					if (i < o2Lines.length) {
						if (!o1Str.contains(o2Lines[i])) {
							System.out.println("Diff " + i + "(2):\t" + o2Lines[i]);
						}
					}
				}
			}
			return equal;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return false;
	}

	private String filterIDs(String o1Str) {
		StringBuilder filtered = new StringBuilder();
		String[] lines = o1Str.split(",");
		for (String line : lines) {
			if (line.contains("\"uuid\"") || line.contains("@lookupID") || line.contains("globalId")) {
				System.out.println("Filtered line: " + line);
			} else {
				filtered.append(line);
			}
		}
		return filtered.toString();
	}

	protected List<String> getItemNames(LNGScenarioModel sm) {
		if (sm == null) {
			return Collections.emptyList();
		}
		return ScenarioModelUtil.findReferenceModel(sm).getPortModel().getPorts().stream().map(p -> ScenarioElementNameHelper.getName(p, "<Unknown>")).collect(Collectors.toCollection(ArrayList::new));
	}

	public void merge(CompoundCommand cmd, MergeHelper mergeHelper) throws Exception {
		final List<MergeRow<Port>> mergeRows = getMergeRows();
		mergeHelper.merge(cmd, mergeRows);
	}
}
