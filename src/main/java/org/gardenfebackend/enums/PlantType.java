package org.gardenfebackend.enums;

public enum PlantType {
    Flower("Цветок"),
    Berry("Ягода"),
    Vegetable("Овощ"),
    Tree("Дерево");

    private final String labelRu;

    PlantType(String labelRu) {
        this.labelRu = labelRu;
    }

    public String getLabelRu() {
        return labelRu;
    }
}

