package com.readearth.wuxiairmonitor.object;

import java.io.Serializable;

/**
 * Hx1为时段，Hx2为等级，Hx3为主要污染物
 * 
 * @author Ukuleele
 */
@SuppressWarnings("serial")
public class ForecastItem implements Serializable, Comparable<ForecastItem> {

	// Hx1为时段，Hx2为等级，Hx3为主要污染物
	private String H11;
	private String H12;
	private String H13;
	private String H21;
	private String H22;
	private String H23;
	private String H31;
	private String H32;
	private String H33;

	private String H40;
	private String H50;
	private String H60;

	public String getH11() {
		return H11;
	}

	public void setH11(String h11) {
		H11 = h11;
	}

	public String getH12() {
		return H12;
	}

	public void setH12(String h12) {
		H12 = h12;
	}

	public String getH13() {
		return H13;
	}

	public void setH13(String h13) {
		H13 = h13;
	}

	public String getH21() {
		return H21;
	}

	public void setH21(String h21) {
		H21 = h21;
	}

	public String getH22() {
		return H22;
	}

	public void setH22(String h22) {
		H22 = h22;
	}

	public String getH23() {
		return H23;
	}

	public void setH23(String h23) {
		H23 = h23;
	}

	public String getH31() {
		return H31;
	}

	public void setH31(String h31) {
		H31 = h31;
	}

	public String getH32() {
		return H32;
	}

	public void setH32(String h32) {
		H32 = h32;
	}

	public String getH33() {
		return H33;
	}

	public void setH33(String h33) {
		H33 = h33;
	}

	public String getH40() {
		return H40;
	}

	public void setH40(String h40) {
		H40 = h40;
	}

	public String getH50() {
		return H50;
	}

	public void setH50(String h50) {
		H50 = h50;
	}

	public String getH60() {
		return H60;
	}

	public void setH60(String h60) {
		H60 = h60;
	}

	@Override
	public int compareTo(ForecastItem another) {
		// TODO 自动生成的方法存根
		
		return 0;
	}

}
