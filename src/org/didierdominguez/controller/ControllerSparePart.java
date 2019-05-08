package org.didierdominguez.controller;

import org.didierdominguez.bean.SparePart;
import org.didierdominguez.list.SimpleList.SimpleList;
import org.didierdominguez.list.SimpleList.SimpleNode;

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

    public void createSparePart(String name, String brand, String model, Integer stock, Double price) {
        if (searchSparePart(name, brand, model) == null) {
            id++;
            SparePart sparePart = new SparePart(id, name.trim().toUpperCase(), brand.trim().toUpperCase(),
                    model.trim().toUpperCase(), stock, price);
            sparePartList.addLastNode(sparePart);
            System.out.println("Spare part added successfully");
        } else {
            System.out.println("The spare part is already registered");
        }
    }

    public void updateSparePart(Integer id, String name, String brand, String model, Integer stock, Double price) {
        SparePart sparePart = searchSparePart(id);
        update = false;
        if (sparePart != null) {
            if (searchSparePart(name, brand, model) == null || (sparePart.getName().equalsIgnoreCase(name)
                    && sparePart.getModel().equalsIgnoreCase(model)
                    && sparePart.getBrand().equalsIgnoreCase(brand))) {
                sparePart.setName(name);
                sparePart.setBrand(brand);
                sparePart.setModel(model);
                sparePart.setStock(stock);
                sparePart.setPrice(price);
                ControllerService.getInstance().updateTotalService();
                update = true;
                System.out.println("Spare part updated successfully");
            } else {
                System.out.println("The spare part is already registered");
            }
        } else {
            System.out.println("Spare part was not found");
        }
    }

    public boolean updateSparePart() {
        return update;
    }

    public void deleteSparePart(Integer id) {
        SparePart user = searchSparePart(id);
        if (user != null) {
            sparePartList.deleteSpecificNode(user);
            System.out.println("Spare part deleted successfully");
        } else {
            System.out.println("Spare part was not found");
        }
    }

    public SimpleList getSparePartList() {
        return sparePartList;
    }

    public SparePart searchSparePart(Integer id) {
        SimpleNode auxiliaryNode = getSparePartList().getFirstNode();
        while (auxiliaryNode != null && !((SparePart) auxiliaryNode.getObject()).getId().equals(id)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (SparePart) auxiliaryNode.getObject();
        }
        return null;
    }

    public SparePart searchSparePart(String name, String brand, String model) {
        SimpleNode auxiliaryNode = getSparePartList().getFirstNode();
        while (auxiliaryNode != null
                && (!((SparePart) auxiliaryNode.getObject()).getName().equalsIgnoreCase(name)
                || !((SparePart) auxiliaryNode.getObject()).getBrand().equalsIgnoreCase(brand)
                || !((SparePart) auxiliaryNode.getObject()).getModel().equalsIgnoreCase(model))) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (SparePart) auxiliaryNode.getObject();
        }
        return null;
    }
}
