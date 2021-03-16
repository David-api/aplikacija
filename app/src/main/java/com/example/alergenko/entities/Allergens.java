package com.example.alergenko.entities;

public enum Allergens {
    JAJCA(70, "jajca"),
    ORESCKI(70, "oreščki"),
    GLUTEN(70, "gluten"),
    MLEKO(70, "mleko"),
    SOJA(70, "soja"),
    ARASIDI(70, "arasidi"),
    ZELENA(70, "zelena"),
    RIBE(70, "ribe"),
    RAKI(70, "raki"),
    GORCICNO_SEME(70, "gorčično seme"),
    SEZAM(70, "sezam"),
    ZVEPLO(70, "žveplo (SO2)"),
    VOLCJI_BOB(70, "volčji bob"),
    MEHKUZCI(70, "mehkužci");


    public int id;
    public String name;

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
