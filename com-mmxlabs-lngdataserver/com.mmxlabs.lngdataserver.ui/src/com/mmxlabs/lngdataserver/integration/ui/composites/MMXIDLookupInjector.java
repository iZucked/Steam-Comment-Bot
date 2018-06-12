/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.composites;

import java.util.List;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.lngdataserver.integration.distances.DefaultPortProvider;
import com.mmxlabs.lngdataserver.integration.distances.LocationRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.IBackEndAvailableListener;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * Supplemental component helper adding a button to link a port to an upstream port.
 * 
 * @author Simon Goodall
 *
 */
public class MMXIDLookupInjector extends BaseComponentHelper {

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, null);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject rootObject, final EObject value) {
		return super.getExternalEditingRange(rootObject, value);

	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass displayedClass) {

		//

		detailComposite.addInlineEditor(new BasicAttributeInlineEditor(PortPackage.Literals.LOCATION__MMX_ID) {
			@Override
			public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

				// final Control control = super.createControl(parent, dbc, toolkit);

				/** Insert button to lookup timezone from lat/lon */

				final Button btn = new Button(parent, SWT.PUSH);
				btn.setText("Link location");
				btn.setToolTipText("Link to upstream ports");
				btn.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {

						final SelectionDialog d = new SelectionDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {

							private TableViewer viewer;

							private TableViewer getTableViewer() {
								return viewer;
							}

							@Override
							protected Control createDialogArea(final Composite container) {
								container.setLayoutData(new GridData(GridData.FILL_BOTH));
								final Location location = (Location) input;

								final Composite composite_p = (Composite) super.createDialogArea(container);
								Composite composite = new Composite(composite_p, SWT.BORDER);
								composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

								final GridLayout layout = new GridLayout(2, false);
								composite.setLayout(layout);
								// Composite composite = new Composite(container, SWT.NONE);
								try {
									final LocationRepository repo = new LocationRepository();
									final String version = "<not specified>";
									final List<Port> ports = repo.getPorts(version);
									final DefaultPortProvider portProvider = new DefaultPortProvider(version, ports);

									final Label l_note = new Label(composite, SWT.NONE);
									l_note.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

									final Label l = new Label(composite, SWT.NONE);
									l.setText("Search: ");
									final Text txt = new Text(composite, SWT.NONE);
									txt.addModifyListener(new ModifyListener() {

										@Override
										public void modifyText(final ModifyEvent e) {
											final TableViewer tableViewer = getTableViewer();
											if (tableViewer != null) {
												tableViewer.refresh();
											}
										}
									});
									txt.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

									if (location.getMmxId() == null || location.getMmxId().isEmpty()) {
										l_note.setText("Currently unlinked");
										txt.setText(location.getName());
									} else {
										final Port p = portProvider.getPortById(location.getMmxId());
										if (p != null) {
											txt.setText(p.getLocation().getName());
											l_note.setText("Linked to " + p.getLocation().getName());
										} else {
											l_note.setText("Currently unlinked");
											txt.setText(location.getName());
										}
									}

									viewer = new TableViewer(composite);
									viewer.getTable().setLayoutData(GridDataFactory.fillDefaults().span(2, 1).hint(40, 500).create());

									viewer.setContentProvider(new ArrayContentProvider());
									viewer.setLabelProvider(new ColumnLabelProvider() {
										@Override
										public String getText(final Object element) {
											if (element instanceof Port) {
												return ((Port) element).getLocation().getName() + " - " + ((Port) element).getLocation().getCountry();
											}
											return element.toString();

										};
									});
									viewer.setInput(ports);

									getTableViewer().setFilters(new ViewerFilter() {

										@Override
										public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
											if (txt.getText().isEmpty()) {
												return true;
											}
											if (element instanceof Port) {
												final Port port = (Port) element;
												final Location location = port.getLocation();
												final String name = location.getName();

												if (name.toLowerCase().contains(txt.getText().toLowerCase())) {
													return true;
												}
												if (txt.getText().toLowerCase().contains(name.toLowerCase())) {
													return true;
												}

											}

											return false;
										}
									});
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								return composite;
							}

							@Override
							protected void createButtonsForButtonBar(final Composite parent) {
								createButton(parent, IDialogConstants.OK_ID, "Link", true);
								createButton(parent, IDialogConstants.YES_TO_ALL_ID, "Link and update fields", false);
								createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
							}

							@Override
							protected void buttonPressed(final int buttonId) {
								if (IDialogConstants.OK_ID == buttonId) {
									final IStructuredSelection selection = viewer.getStructuredSelection();
									setResult(selection.toList());

									setReturnCode(IDialogConstants.OK_ID);
									close();
								} else if (IDialogConstants.YES_TO_ALL_ID == buttonId) {
									final IStructuredSelection selection = viewer.getStructuredSelection();
									setResult(selection.toList());

									setReturnCode(IDialogConstants.YES_TO_ALL_ID);
									close();
								}

								super.buttonPressed(buttonId);
							}
						};

						d.setTitle("Link port to...");
						final Location location = (Location) input;

						final int open = d.open();
						if (open == IDialogConstants.OK_ID) {
							final Object[] r = d.getResult();
							if (r != null) {
								if (r.length == 1) {
									final Port p = (Port) r[0];
									System.out.println(p.getLocation().getMmxId());
									location.setMmxId(p.getLocation().getMmxId());
								}
							}
						} else if (open == IDialogConstants.YES_TO_ALL_ID) {
							final Object[] r = d.getResult();
							if (r != null) {
								if (r.length == 1) {
									final Port p = (Port) r[0];

									location.setName(p.getName());
									location.setCountry(p.getLocation().getCountry());
									location.setLat(p.getLocation().getLat());
									location.setLon(p.getLocation().getLon());
									location.setTimeZone(p.getLocation().getTimeZone());
									location.setMmxId(p.getLocation().getMmxId());
								}
							}
						}

					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});

				btn.setEnabled(false);
				BackEndUrlProvider.INSTANCE.addAvailableListener(new IBackEndAvailableListener() {

					@Override
					public void backendAvailable() {
						RunnerHelper.asyncExec(() -> btn.setEnabled(true));
					}
				});

				return btn;
			}

			@Override
			protected void updateDisplay(final Object value) {

			}
		});

	}

	@Override
	public int getDisplayPriority() {
		return 10;
	}
}
