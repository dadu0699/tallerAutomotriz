package org.didierdominguez.controller;

import org.didierdominguez.bean.Service;
import org.didierdominguez.bean.SparePart;
import org.didierdominguez.list.SimpleList.SimpleList;
import org.didierdominguez.list.SimpleList.SimpleNode;
import org.didierdominguez.list.Stack.Stack;
import org.didierdominguez.list.Stack.StackNode;

public class ControllerService {
    private static ControllerService instance;
    private SimpleList serviceList;
    private int id;
    private boolean update;

    private ControllerService() {
        serviceList = new SimpleList();
        id = 0;
    }

    public static ControllerService getInstance() {
        if (instance == null) {
            instance = new ControllerService();
        }
        return instance;
    }

    public void createService(String name, String brand, String model, Double laborPrice) {
        if (searchService(name, brand, model) == null) {
            id++;
            Stack sparesStack = new Stack();
            Service service = new Service(id, name.trim().toUpperCase(), brand.trim().toUpperCase(), model.trim().toUpperCase(), sparesStack, laborPrice, laborPrice);
            serviceList.addLastNode(service);
            System.out.println("Service added successfully");
        } else {
            System.out.println("The service is already registered");
        }
    }

    public void createSparesStack(Integer id, SparePart sparePart) {
        Service service = searchService(id);
        if (service != null) {
            service.getSpares().push(sparePart);
            service.setTotal(service.getTotal() + sparePart.getPrice());
            System.out.println("Spare part added in service successfully");
        } else {
            System.out.println("Service was not found");
        }
    }

    public void updateService(Integer id, String name, String brand, String model, Double laborPrice) {
        Service service = searchService(id);
        update = false;
        if (service != null) {
            if (searchService(name, brand, model) == null || (service.getName().equalsIgnoreCase(name)
                    && service.getModel().equalsIgnoreCase(model)
                    && service.getBrand().equalsIgnoreCase(brand))) {
                service.setName(name);
                service.setBrand(brand);
                service.setModel(model);
                service.setLaborPrice(laborPrice);
                updateTotalService(id);
                update = true;
                System.out.println("Service updated successfully");
            } else {
                System.out.println("The service is already registered");
            }
        } else {
            System.out.println("Service was not found");
        }
    }

    public boolean updateService() {
        return update;
    }

    public void updateTotalService(Integer id) {
        Service service = searchService(id);
        SparePart sparePart;
        if (service != null) {
            StackNode auxiliaryNode = (StackNode) service.getSpares().getTop();
            double subtotal = service.getLaborPrice();
            while (auxiliaryNode != null) {
                sparePart = (SparePart) auxiliaryNode.getObject();
                subtotal += sparePart.getPrice();
                auxiliaryNode = auxiliaryNode.getNextNode();
            }
            service.setTotal(subtotal);
        }
    }

    public void updateTotalService() {
        SimpleNode auxiliaryNode = serviceList.getFirstNode();
        while (auxiliaryNode != null) {
            Service service = (Service) auxiliaryNode.getObject();
            updateTotalService(service.getId());
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
    }

    public void deleteService(Integer id) {
        Service service = searchService(id);
        if (service != null) {
            serviceList.deleteSpecificNode(service);
            System.out.println("Service deleted successfully");
        } else {
            System.out.println("Service was not found");
        }
    }

    public SimpleList getServiceList() {
        return serviceList;
    }

    private Service searchService(Integer id) {
        SimpleNode auxiliaryNode = getServiceList().getFirstNode();
        while (auxiliaryNode != null && !((Service) auxiliaryNode.getObject()).getId().equals(id)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (Service) auxiliaryNode.getObject();
        }
        return null;
    }

    public Service searchService(String name, String brand, String model) {
        SimpleNode auxiliaryNode = getServiceList().getFirstNode();
        while (auxiliaryNode != null
                && (!((Service) auxiliaryNode.getObject()).getName().equalsIgnoreCase(name.trim())
                || !((Service) auxiliaryNode.getObject()).getBrand().equalsIgnoreCase(brand.trim())
                || !((Service) auxiliaryNode.getObject()).getModel().equalsIgnoreCase(model.trim()))) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (Service) auxiliaryNode.getObject();
        }
        return null;
    }
}
