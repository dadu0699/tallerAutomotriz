package org.didierdominguez.util;

import org.didierdominguez.controller.ControllerEmployee;

public class Inserts {
    private static Inserts instance;

    private Inserts() {
    }

    public static Inserts getInstance() {
        if (instance == null) {
            instance = new Inserts();
        }
        return instance;
    }

    public void insertData() {
        // EMPLOYEE
        ControllerEmployee.getInstance().createEmployee("EMPLEADO1", "ADMINISTRADOR", "EMP1", "admin", true);
        ControllerEmployee.getInstance().createEmployee("EMPLEADO2", "ADMINISTRADOR", "EMP2", "admin", true);
        ControllerEmployee.getInstance().createEmployee("EMPLEADO3", "ADMINISTRADOR", "EMP3", "admin", true);
        ControllerEmployee.getInstance().createEmployee("EMPLEADO4", "MECÁNICO", "EMP4", "admin", false);
        ControllerEmployee.getInstance().createEmployee("EMPLEADO5", "MECÁNICO", "EMP5", "admin", false);
        ControllerEmployee.getInstance().createEmployee("EMPLEADO6", "MECÁNICO", "EMP6", "admin", false);
        ControllerEmployee.getInstance().createEmployee("EMPLEADO7", "RECEPTOR/PAGADOR", "EMP7", "admin", false);
        ControllerEmployee.getInstance().createEmployee("EMPLEADO8", "RECEPTOR/PAGADOR", "EMP8", "admin", false);
    }
}
