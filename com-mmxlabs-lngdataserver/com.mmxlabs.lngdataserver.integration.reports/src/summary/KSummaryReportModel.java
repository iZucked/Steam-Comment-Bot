/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package summary;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */


/**
 * @author josephpallamidessi
 *
 */
public class KSummaryReportModel {
	String counterparty;
	int cargoes;
	int delivered;
	double tonnes;
	double avgPrice;
	double avgCargoSize;
	String sizeUnit;
	String currency;
	boolean isADP = false;
	
	public String getCounterparty() {
		return counterparty;
	}
	public void setCounterparty(String counterparty) {
		this.counterparty = counterparty;
	}
	public int getCargoes() {
		return cargoes;
	}
	public void setCargoes(int cargoes) {
		this.cargoes = cargoes;
	}
	public int getDelivered() {
		return delivered;
	}
	public void setDelivered(int delivered) {
		this.delivered = delivered;
	}
	public double getTonnes() {
		return tonnes;
	}
	public void setTonnes(double tonnes) {
		this.tonnes = tonnes;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	public double getAvgCargoSize() {
		return avgCargoSize;
	}
	public void setAvgCargoSize(double avgCargoSize) {
		this.avgCargoSize = avgCargoSize;
	}
	public String getSizeUnit() {
		return sizeUnit;
	}
	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public boolean isADP() {
		return isADP;
	}
	public void setADP(boolean isADP) {
		this.isADP = isADP;
	}
	
}
