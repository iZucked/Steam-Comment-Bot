/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.importer.FuelCurveImporter;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class VesselViewerPane_Editor extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(VesselViewerPane_Editor.class);

	// TODO: Make these colours a preference so they can be consistently used across various UI parts
	private final Color tcVessel = new Color(Display.getDefault(), 150, 210, 230);

	private final IScenarioEditingLocation jointModelEditor;

	public VesselViewerPane_Editor(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain));

		addTypicalColumn("Class", new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_VesselClass(), jointModelEditor.getReferenceValueProviderCache(), editingDomain));

		addTypicalColumn("Time Charter", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVessel_TimeCharterRate(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("Start Port",
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(),
						MMXCorePackage.eINSTANCE.getNamedObject_Name()), FleetPackage.eINSTANCE.getVessel_Availability());

		addTypicalColumn("Start After", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartAfter(), jointModelEditor.getEditingDomain()),
				FleetPackage.eINSTANCE.getVessel_Availability());

		addTypicalColumn("Start Before", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartBy(), jointModelEditor.getEditingDomain()),
				FleetPackage.eINSTANCE.getVessel_Availability());

		addTypicalColumn("End Port",
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(),
						MMXCorePackage.eINSTANCE.getNamedObject_Name()), FleetPackage.eINSTANCE.getVessel_Availability());

		addTypicalColumn("End After", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndAfter(), jointModelEditor.getEditingDomain()),
				FleetPackage.eINSTANCE.getVessel_Availability());

		addTypicalColumn("End By", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndBy(), jointModelEditor.getEditingDomain()),
				FleetPackage.eINSTANCE.getVessel_Availability());

		setTitle("Vessels", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	@Override
	protected Action createImportAction() {
		final Action def = super.createImportAction();
		def.setText("Import Vessels");

		return new AbstractMenuAction("Import CSV") {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
			}

			@Override
			protected void populate(final Menu menu) {
				addActionToMenu(def, menu);

				final Action importVesselClasses = new ImportAction(getJointModelEditorPart()) {
					{
						setText("Import Vessel Classes");
					}

					@Override
					protected void doImportStages(final DefaultImportContext context) {
						final EObject container = (EObject) viewer.getInput();
						final EReference containment = FleetPackage.eINSTANCE.getFleetModel_VesselClasses();

						final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(containment.getEReferenceType());
						// open file picker

						final String path = importHooksProvider.getImportFilePath();
						
						if (path == null)
							return;

						CSVReader reader = null;
						try {
							reader = new CSVReader(new File(path));
							final Collection<EObject> importedObjects = importer.importObjects(containment.getEReferenceType(), reader, context);
							context.run();
							importHooksProvider.getEditingDomain().getCommandStack().execute(mergeLists(container, containment, new ArrayList<EObject>(importedObjects)));
						} catch (final IOException e) {
							log.error(e.getMessage(), e);
						} finally {

							if (reader != null) {
								try {
									reader.close();
								} catch (final IOException e) {
								}
							}

						}
					}
				};
				addActionToMenu(importVesselClasses, menu);
				final Action importFuels = new ImportAction(getJointModelEditorPart()) {
					{
						setText("Import Base Fuels");
					}

					@Override
					protected void doImportStages(final DefaultImportContext context) {
						final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(FleetPackage.eINSTANCE.getBaseFuel());

						final String path = importHooksProvider.getImportFilePath();
						
						if (path == null)
							return;

						CSVReader reader = null;
						try {
							reader = new CSVReader(new File(path));
							final Collection<EObject> importedObjects = importer.importObjects(FleetPackage.eINSTANCE.getBaseFuel(), reader, context);
							context.run();
							importHooksProvider.getEditingDomain().getCommandStack()
									.execute(mergeLists(getScenarioViewer().getCurrentContainer(), FleetPackage.eINSTANCE.getFleetModel_BaseFuels(), new ArrayList<EObject>(importedObjects)));
						} catch (final IOException e) {
							log.error(e.getMessage(), e);
						} finally {

							if (reader != null) {
								try {
									reader.close();
								} catch (final IOException e) {
								}
							}

						}
					}
				};
				addActionToMenu(importFuels, menu);

				final Action importFuelCurves = new LockableAction("Import Consumption Curves") {
					{
						setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
					}

					@Override
					public void run() {
						final DefaultImportContext context = new DefaultImportContext();
						final FuelCurveImporter importer = new FuelCurveImporter();
						final FileDialog fileDialog = new FileDialog(part.getSite().getShell());
						fileDialog.setFilterExtensions(new String[] { "*.csv" });
						final String path = fileDialog.open();

						if (path == null)
							return;

						CSVReader reader = null;
						try {
							jointModelEditor.setDisableUpdates(true);
							jointModelEditor.setDisableCommandProviders(true);
							reader = new CSVReader(new File(path));
							final Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> consumptions = importer.readConsumptions(reader, context);
							final EditingDomain ed = jointModelEditor.getEditingDomain();
							final CompoundCommand command = new CompoundCommand();
							final FleetModel fleet = jointModelEditor.getRootObject().getSubModel(FleetModel.class);
							each_vessel: for (final String vesselName : consumptions.keySet()) {
								// find vessel class
								for (final VesselClass vc : fleet.getVesselClasses()) {
									if (vc.getName().equals(vesselName)) {
										final Pair<List<FuelConsumption>, List<FuelConsumption>> ladenAndBallastConsumptions = consumptions.get(vesselName).getSecond();
										// apply change
										if (vc.getLadenAttributes().getFuelConsumption().isEmpty() == false)
											command.append(DeleteCommand.create(ed, vc.getLadenAttributes().getFuelConsumption()));
										if (vc.getBallastAttributes().getFuelConsumption().isEmpty() == false)
											command.append(DeleteCommand.create(ed, vc.getBallastAttributes().getFuelConsumption()));
										if (ladenAndBallastConsumptions.getFirst().isEmpty() == false)
											command.append(AddCommand.create(ed, vc.getLadenAttributes(), FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumption(),
													ladenAndBallastConsumptions.getFirst()));
										if (ladenAndBallastConsumptions.getSecond().isEmpty() == false)
											command.append(AddCommand.create(ed, vc.getBallastAttributes(), FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumption(),
													ladenAndBallastConsumptions.getSecond()));
										continue each_vessel;
									}
								}
								// collect problem
								context.addProblem(consumptions.get(vesselName).getFirst());
							}

							ed.getCommandStack().execute(command);
						} catch (final IOException e) {
							context.addProblem(context.createProblem("IO Error: " + e.getMessage(), false, false, false));
						} finally {
							jointModelEditor.setDisableUpdates(false);
							jointModelEditor.setDisableCommandProviders(false);

							if (reader != null) {
								try {
									reader.close();
								} catch (final IOException e) {
								}
							}

						}
					}
				};

				addActionToMenu(importFuelCurves, menu);
			}
		};
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		final ScenarioTableViewer scenarioTableViewer = new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart()) {
			@Override
			public EObject getElementForNotificationTarget(final EObject source) {
				if (source instanceof VesselAvailability) {
					return source.eContainer();
				}

				return super.getElementForNotificationTarget(source);
			}
		};
		scenarioTableViewer.setColourProvider(new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {
				return null;
			}

			@Override
			public Color getBackground(final Object element) {

				if (element instanceof Vessel) {

					final Vessel vessel = (Vessel) element;
					if (vessel.isSetTimeCharterRate()) {
						return tcVessel;
					}
				}
				return null;
			}

		});
		return scenarioTableViewer;

	}

	@Override
	public void dispose() {

		tcVessel.dispose();

		super.dispose();
	}
}
