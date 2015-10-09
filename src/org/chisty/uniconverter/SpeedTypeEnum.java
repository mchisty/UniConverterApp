package org.chisty.uniconverter;

import java.util.ArrayList;
import java.util.List;

public enum SpeedTypeEnum {
	MILES_PER_HR("Miles/Hour"), KM_PER_HR("Kilometers/Hour"), METER_PER_SEC("Meters/Second"), FT_PER_SEC("Feet/Second"), INCHES_PER_SEC(
			"Inches/Second");
	private String label;
	private final List<String> labelsList = new ArrayList<String>();

	private SpeedTypeEnum(String l) {
		this.label = l;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return name();
	}

	public String[] getAllLabels() {
		for (SpeedTypeEnum se : values()) {
			labelsList.add(se.getLabel());
		}
		return (String[]) labelsList.toArray();
	}

	public static SpeedTypeEnum getName(String name) {
		return valueOf(name);
	}
}
