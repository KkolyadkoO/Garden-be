package org.gardenfebackend.enums;

public enum GardenType {
    PLOT("Участок"),
    WINDOWSILL("Подоконник");

    private final String labelRu;

    GardenType(String labelRu) {
        this.labelRu = labelRu;
    }

    public String getLabelRu() {
        return labelRu;
    }
}

