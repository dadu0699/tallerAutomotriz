package org.didierdominguez.controller;

import org.didierdominguez.bean.Car;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.list.CircularSimpleList.CircularSimpleList;
import org.didierdominguez.list.CircularSimpleList.CircularSimpleNode;

public class ControllerCar {
    private static ControllerCar instance;
    private CircularSimpleList carList;
    private boolean update;

    private ControllerCar() {
        carList = new CircularSimpleList();
    }

    public static ControllerCar getInstance() {
        if (instance == null) {
            instance = new ControllerCar();
        }
        return instance;
    }

    public void createCar(String id, String brand, String model, String picture, Customer customer) {
        if (searchCar(id) == null) {
            Car car = new Car(id.toUpperCase().trim(), brand.toUpperCase().trim(),
                    model.toUpperCase().trim(), picture, customer);
            carList.addNode(car);
            System.out.println("Car added successfully");
        } else {
            System.out.println("The car is already registered");
        }
    }

    public void updateCar(String id, String brand, String model, String picture) {
        Car car = searchCar(id);
        update = false;
        if (car != null) {
            if (searchCar(id) == null || car.getId().equalsIgnoreCase(id)) {
                car.setId(id);
                car.setBrand(brand);
                car.setModel(model);
                car.setPicture(picture);
                update = true;
                System.out.println("Car updated successfully");
            } else {
                System.out.println("The car is already registered");
            }
        } else {
            System.out.println("Car was not found");
        }
    }

    public boolean updateCar() {
        return update;
    }

    public void deleteCar(String id) {
        Car car = searchCar(id);
        if (car != null) {
            carList.deleteSpecificNode(car);
            System.out.println("Car deleted successfully");
        } else {
            System.out.println("Car was not found");
        }
    }

    public CircularSimpleList getCarList() {
        return carList;
    }

    private Car searchCar(String id) {
        CircularSimpleNode auxiliaryNode = getCarList().getNode();
        if (auxiliaryNode != null) {
            do {
                if (((Car) auxiliaryNode.getObject()).getId().equalsIgnoreCase(id.trim())) {
                    return (Car) auxiliaryNode.getObject();
                } else {
                    auxiliaryNode = auxiliaryNode.getNextNode();
                }
            } while (auxiliaryNode != getCarList().getNode());
        }
        return null;
    }

    public Car searchCar(String brand, String model) {
        CircularSimpleNode auxiliaryNode = getCarList().getNode();
        if (auxiliaryNode != null) {
            do {
                if (((Car) auxiliaryNode.getObject()).getBrand().equalsIgnoreCase(brand.trim())
                        && ((Car) auxiliaryNode.getObject()).getModel().equalsIgnoreCase(model.trim())) {
                    return (Car) auxiliaryNode.getObject();
                } else {
                    auxiliaryNode = auxiliaryNode.getNextNode();
                }
            } while (auxiliaryNode != getCarList().getNode());
        }
        return null;
    }
}
