/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Calendar;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public class RedirectionTradesTableContextMenu implements ITradesTableContextMenuExtension {

	@Override
	public void contributeToMenu(@NonNull final EditingDomain domain, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isSetContract()) {
				final Contract contract = loadSlot.getContract();

				if (contract.getPriceInfo() instanceof RedirectionPriceParameters) {

					final boolean swappable = true;
					if (swappable) {
						final IMenuListener listener = new IMenuListener() {

							@Override
							public void menuAboutToShow(final IMenuManager manager) {

								if (loadSlot.isDESPurchase()) {
									// Convert to FOB Purchase
									menuManager.add(new ConvertToFOBPurchase(domain, loadSlot));

								} else {
									// Convert to DES Purchase
									menuManager.add(new ConvertToDESPurchase(domain, loadSlot));
								}

							}
						};
						listener.menuAboutToShow(menuManager);
					}
				}
			}

		}
	}

	private static class ConvertToFOBPurchase extends Action {
		private final LoadSlot loadSlot;
		private final EditingDomain domain;

		public ConvertToFOBPurchase(final EditingDomain domain, final LoadSlot loadSlot) {
			super("Convert to FOB Purchase");
			this.domain = domain;
			this.loadSlot = loadSlot;
		}

		@Override
		public void run() {

			if (loadSlot.isSetContract()) {
				final Contract contract = loadSlot.getContract();

				if (contract.getPriceInfo() instanceof RedirectionPriceParameters) {
					final RedirectionPriceParameters redirectionPriceParameters = (RedirectionPriceParameters) contract.getPriceInfo();

					final CompoundCommand cmd = new CompoundCommand(getText());
					cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE, Boolean.FALSE));
					cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.SLOT__PORT, redirectionPriceParameters.getSourcePurchasePort()));

					for (final EObject obj : loadSlot.getExtensions()) {
						if (obj instanceof RedirectionContractOriginalDate) {
							final RedirectionContractOriginalDate redirectionContractOriginalDate = (RedirectionContractOriginalDate) obj;
							cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.SLOT__WINDOW_START, redirectionContractOriginalDate.getDate()));
							break;
						}
					}

					if (cmd.canExecute()) {
						domain.getCommandStack().execute(cmd);
					}
				}
			}

		}
	}

	private static class ConvertToDESPurchase extends Action {
		private final LoadSlot loadSlot;
		private final EditingDomain domain;

		public ConvertToDESPurchase(final EditingDomain domain, final LoadSlot loadSlot) {
			super("Convert to DES Purchase");
			this.domain = domain;
			this.loadSlot = loadSlot;
		}

		@Override
		public void run() {

			if (loadSlot.isSetContract()) {
				final Contract contract = loadSlot.getContract();

				if (contract.getPriceInfo() instanceof RedirectionPriceParameters) {
					final RedirectionPriceParameters redirectionPriceParameters = (RedirectionPriceParameters) contract.getPriceInfo();

					final CompoundCommand cmd = new CompoundCommand(getText());
					cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE, Boolean.TRUE));
					cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.LOAD_SLOT__CARGO_CV, loadSlot.getSlotOrPortCV()));

					final Cargo cargo = loadSlot.getCargo();
					if (cargo == null) {
						cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.SLOT__PORT, redirectionPriceParameters.getDesPurchasePort()));
						final Calendar cal = Calendar.getInstance();
						cal.setTime(loadSlot.getWindowStart());
						cal.add(Calendar.DAY_OF_YEAR, 60);
						cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.SLOT__WINDOW_START, cal.getTime()));

					} else {
						DischargeSlot dischargeSlot = null;
						for (final Slot slot : cargo.getSlots()) {
							if (slot instanceof DischargeSlot) {
								dischargeSlot = (DischargeSlot) slot;
							}
						}

						if (dischargeSlot != null) {
							cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.SLOT__PORT, dischargeSlot.getPort()));
							cmd.append(SetCommand.create(domain, loadSlot, CargoPackage.Literals.SLOT__WINDOW_START, dischargeSlot.getWindowStart()));
						}

					}

					if (cmd.canExecute()) {
						domain.getCommandStack().execute(cmd);
					}
				}
			}

		}
	}
}
