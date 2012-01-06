/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.debug;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.management.timer.Timer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.shiplingo.importer.importers.ImportUI;

import scenario.market.Index;
import scenario.market.MarketFactory;
import scenario.market.MarketPackage;
import scenario.market.StepwisePrice;
import scenario.presentation.cargoeditor.handlers.ScenarioModifyingAction;

/**
 * Generate some synthetic index data
 * 
 * @author hinton
 * 
 */
public class GenerateIndicesAction extends ScenarioModifyingAction implements ISelectionChangedListener {
	public GenerateIndicesAction() {
		super();
		setText("Generate");
		setToolTipText("Synthesize random walk data for a curve");
	}

	@Override
	public void run() {
		final EditingDomain domain = getEditingDomain(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		if (domain != null) {
			final Index target = (Index) ((IStructuredSelection) getLastSelection()).getFirstElement();

			final SimpleDateFormat consistentDate = new SimpleDateFormat("dd/MM/yyyy");

			final InputDialog input = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Generate Index", "Enter the new start date (dd/mm/yyyy)", "",
					new IInputValidator() {
						@Override
						public String isValid(String newText) {
							try {
								consistentDate.parse(newText);
							} catch (ParseException e) {
								return newText + " is not a date of the form dd/mm/yyyy";
							}

							return null;
						}
					});

			if (input.open() == Window.OK) {

				final InputDialog input2 = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Generate Index", "What is the starting price ($/mmbtu)", "5",
						new IInputValidator() {
							@Override
							public String isValid(String newText) {
								try {
									Double.parseDouble(newText);
								} catch (NumberFormatException e) {
									return newText + " is not a number";
								}
								;
								return null;
							}
						});

				if (input2.open() == Window.OK) {
					final InputDialog input3 = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Generate Index", "How many prices should be generated?", "52",
							new IInputValidator() {
								@Override
								public String isValid(String newText) {
									try {
										Integer.parseInt(newText);
									} catch (NumberFormatException e) {
										return newText + " is not a number";
									}
									;
									return null;
								}
							});

					if (input3.open() == Window.OK) {
						final InputDialog input4 = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Generate Index", "How many days should be between each price?",
								"7", new IInputValidator() {
									@Override
									public String isValid(String newText) {
										try {
											Integer.parseInt(newText);
										} catch (NumberFormatException e) {
											return newText + " is not a number";
										}
										;
										return null;
									}
								});

						if (input4.open() == Window.OK) {
							final InputDialog input5 = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Generate Index", "Sigma 1 price move", "0.1",
									new IInputValidator() {
										@Override
										public String isValid(String newText) {
											try {
												Double.parseDouble(newText);
											} catch (NumberFormatException e) {
												return newText + " is not a number";
											}
											;
											return null;
										}
									});
							if (input5.open() == Window.OK) {
								try {
									final Date date = consistentDate.parse(input.getValue());
									final double start = Double.parseDouble(input2.getValue());
									final double sigma1 = Double.parseDouble(input5.getValue());
									final int count = Integer.parseInt(input3.getValue());
									final int separation = Integer.parseInt(input4.getValue());

									try {
										ImportUI.beginImport();

										final CompoundCommand cc = new CompoundCommand();

										cc.append(DeleteCommand.create(domain, target.getPriceCurve().getPrices()));

										double currentValue = start;
										Date currentDate = date;
										final Random random = new Random(System.currentTimeMillis());
										final long sep = Timer.ONE_DAY * separation;

										for (int i = 0; i < count; i++) {
											final StepwisePrice price = MarketFactory.eINSTANCE.createStepwisePrice();
											price.setDate(currentDate);
											price.setPriceFromDate((float) currentValue);
											currentDate = new Date(currentDate.getTime() + sep);
											currentValue += random.nextGaussian() * sigma1;
											currentValue = Math.max(1, currentValue); // set a floor of 1 dollar
											cc.append(AddCommand.create(domain, target.getPriceCurve(), MarketPackage.eINSTANCE.getStepwisePriceCurve_Prices(), price));
										}

										domain.getCommandStack().execute(cc);
									} finally {
										ImportUI.endImport();
									}
								} catch (ParseException e) {

								}
							}
						}
					}
				}

			}

		}
	}

	@Override
	protected boolean isApplicableToSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).size() == 1) {
				if (((IStructuredSelection) selection).getFirstElement() instanceof Index) {
					return true;
				}
			}
		}
		return false;
	}

}
