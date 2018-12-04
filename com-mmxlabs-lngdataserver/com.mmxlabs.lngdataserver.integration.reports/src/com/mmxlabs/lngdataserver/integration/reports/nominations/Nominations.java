package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class Nominations {
	final String slotId;
	String buyer;
	String seller;
	final String cn;
	final String type;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	final LocalDate date;
	final String nominationType;
	final String nominationComment;
	String nomination;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate nominateBy;
	boolean warn;
	
	public Nominations(final Slot slot, final NominationType nType) {
		this.slotId = slot.getName();
		final Contract contract = slot.getContract();
		final Cargo c = slot.getCargo();
		if (c != null) {
			for (Slot s : c.getSlots()) {
				if (s instanceof DischargeSlot) {
					DischargeSlot ds = (DischargeSlot)s;
					if (ds.getContract() != null) {
						this.buyer = ds.getContract().getName();
					}
					//override if the counter-party is set explicitly
					if (ds.getSlotOrDelegateCounterparty() != null) {
						this.buyer = ds.getSlotOrDelegateCounterparty();
					}
				}
			}
		}
		BaseLegalEntity entity = null;
		if (contract != null) {
			entity = contract.getEntity();
		}
		if (slot.getSlotOrDelegateEntity()!= null){
			entity = slot.getSlotOrDelegateEntity();
		}
		if (entity != null) {
			this.seller = entity.getName();
		}
		this.cn = slot.getSlotOrDelegateCN();
		this.type = slot instanceof LoadSlot ? "Buy" : "Sell"; //for now
		this.date = slot.getWindowStart();
		this.nomination = "";
		this.nominateBy = null;
		this.nominationComment = ""; //none yet
		
		switch (nType) {
		case SLOT_DATE_0:{
			this.nominationType = slot instanceof LoadSlot ? "Buy window" : "Sell window";
			this.nomination = Formatters.asLocalDateFormatter.render(slot.getWindowStartWithSlotOrPortTime().toLocalDate());
			this.nominateBy = slot.getSlotOrDelegateWindowNominationDate();
			}
			break;
		case SLOT_DATE_1:{
			this.nominationType = slot instanceof LoadSlot ? "Buy window" : "Sell window";
			this.nomination = Formatters.asLocalDateFormatter.render(slot.getWindowStartWithSlotOrPortTime().toLocalDate());
			this.nominateBy = LocalDate.now();
			}
			break;
		case SLOT_DATE_2:{
			this.nominationType = slot instanceof LoadSlot ? "Buy window" : "Sell window";
			this.nomination = Formatters.asLocalDateFormatter.render(slot.getWindowStartWithSlotOrPortTime().toLocalDate());
			this.nominateBy = LocalDate.now();
			}
			break;
		case VESSEL:{
			this.nominationType = "Vessel";
			if (c == null) break;
			VesselAssignmentType vat = c.getVesselAssignmentType();
			if (vat == null) break;
			if (vat instanceof VesselAvailability) {
				VesselAvailability va = (VesselAvailability) vat;
				Vessel v = va.getVessel();
				if (v == null) break;
				this.nomination = v.getName();
				this.nominateBy = LocalDate.now();
				break;
			} else if (vat instanceof CharterInMarket) {
				CharterInMarket cim = (CharterInMarket) vat;
				Vessel v = cim.getVessel();
				if (v == null) break;
				this.nomination = v.getName();
				this.nominateBy = LocalDate.now();
				break;
			} else if (vat instanceof CharterInMarketOverride) {
				CharterInMarketOverride cimo = (CharterInMarketOverride) vat;
				CharterInMarket cim = cimo.getCharterInMarket();
				if (cim == null) break;
				Vessel v = cim.getVessel();
				if (v == null) break;
				this.nomination = v.getName();
				this.nominateBy = LocalDate.now();
				break;
			}
			this.nomination = "";
			this.nominateBy = LocalDate.now();
			}
			break;
		case VOLUME:{
			this.nominationType = "Volume";
			this.nomination = getFormattedVolume(slot);
			this.nominateBy = LocalDate.now();
			}
			break;
		case PORT:{
			this.nominationType = "Port";
			final Port p = slot.getPort();
			if (p == null) break;
			this.nomination = p.getName();
			this.nominateBy = LocalDate.now();
		}
			break;
		default:
			this.nominationType = "";
			break;
		}
		if (this.nominateBy != null) {
			if (LocalDate.now().isAfter(this.nominateBy)) {
				this.warn = true;
			}
		}
	}
	
	public Nominations() {
		this.slotId = "";
		this.buyer = "";
		this.seller = "";
		this.cn = "";
		this.type = "";
		this.date = null;
		this.nomination = "";
		this.nominateBy = null;
		this.nominationType = "";
		this.nominationComment = "";
		this.warn = false;
	}
	
	private String getFormattedVolume(final Slot slot) {
		String volume = "";
		if (slot != null) {
			
			double minValue = slot.getSlotOrDelegateMinQuantity();
			double maxValue = slot.getSlotOrDelegateMaxQuantity();
			String vol = "";
			String minVol = "";
			String maxVol = "";
			String opt = "";
			final double operationalTolerance = slot.getSlotOrDelegateOperationalTolerance() * 100.0;
			if (operationalTolerance != 0.0) {
				opt = String.format(" +/-%.0f", operationalTolerance);
			}
			
			if (slot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.MMBTU) {
				minValue = minValue / 1_000_000.0;
				maxValue = maxValue / 1_000_000.0;
				if (minValue != 0) minVol = String.format("%.3f", minValue);
				if (maxValue != Integer.MAX_VALUE) maxVol = String.format("%.3f", maxValue);
			} else {
				if (minValue != 0) minVol = String.format("%.0f", minValue);
				if (maxValue != Integer.MAX_VALUE) maxVol = String.format("%.0f", maxValue);
				vol = String.format(" %s", slot.getSlotOrDelegateVolumeLimitsUnit());
			}
			
			if (minVol.equals("")) {
				volume = maxVol + vol + opt;
			} else if(maxVol.equals("")) {
				volume = minVol + vol + opt;
			} else if (!minVol.equals("") && !maxVol.equals("")){
				volume = minVol + "-" + maxVol + vol + opt;
			}
		}
		return volume;
	}
	
	public enum NominationType{
		SLOT_DATE_0, SLOT_DATE_1, SLOT_DATE_2, VESSEL, VOLUME, PORT
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getNomination() {
		return nomination;
	}

	public void setNomination(String nomination) {
		this.nomination = nomination;
	}

	public LocalDate getNominateBy() {
		return nominateBy;
	}

	public void setNominateBy(LocalDate nominateBy) {
		this.nominateBy = nominateBy;
	}

	public boolean isWarn() {
		return warn;
	}

	public void setWarn(boolean warn) {
		this.warn = warn;
	}

	public String getSlotId() {
		return slotId;
	}

	public String getCn() {
		return cn;
	}

	public String getType() {
		return type;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getNominationType() {
		return nominationType;
	}

	public String getNominationComment() {
		return nominationComment;
	}
}
