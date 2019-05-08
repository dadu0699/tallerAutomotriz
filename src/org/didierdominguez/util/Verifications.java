package org.didierdominguez.util;

public class Verifications {
    private static Verifications instance;

    private Verifications() {
    }

    public static Verifications getInstance() {
        if (instance == null) {
            instance = new Verifications();
        }
        return instance;
    }

    public boolean isNumericInteger(String valor) {
        try {
            Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isNumericDouble(String valor) {
        try {
            Double.parseDouble(valor);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
