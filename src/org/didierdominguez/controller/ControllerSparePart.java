package org.didierdominguez.controller;

import org.didierdominguez.list.SimpleList.SimpleList;

public class ControllerSparePart {
    private static ControllerSparePart instance;
    private SimpleList sparePartList;
    private int id;
    private boolean update;

    private ControllerSparePart() {
        sparePartList = new SimpleList();
        id = 0;
    }

    public static ControllerSparePart getInstance() {
        if (instance == null) {
            instance = new ControllerSparePart();
        }
        return instance;
    }
}
