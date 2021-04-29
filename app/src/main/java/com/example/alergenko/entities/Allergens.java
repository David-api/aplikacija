package com.example.alergenko.entities;

public enum Allergens {
    JAJCA(70, "jajca"),
    ORESCKI(71, "oreščki"),
    GLUTEN(72, "gluten"),
    MLEKO(73, "mleko"),
    SOJA(74, "soja"),
    ARASIDI(75, "arašidi"),
    ZELENE(76, "zelena"),
    RIBE(77, "ribe"),
    RAKI(78, "raki"),
    GORCICNO_SEME(79, "gorčično seme"),
    SEZAM(80, "sezam"),
    ZVEPLO(81, "žveplo (SO2)"),
    VOLCJI_BOB(82, "volčji bob"),
    MEHKUZCI(83, "mehkužci"),
    NULL(84, "ne vsebuje alergenov");


    private int id;
    private String name;

    private Allergens(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
