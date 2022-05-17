package com.technominds.digitalsahungrav2;

public class village_overview {
    String key,values;

    public village_overview(){

    }

    public village_overview(String key, String values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
