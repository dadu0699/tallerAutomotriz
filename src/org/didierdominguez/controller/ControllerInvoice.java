package org.didierdominguez.controller;

import org.didierdominguez.bean.Invoice;
import org.didierdominguez.bean.Order;
import org.didierdominguez.list.SimpleList.SimpleList;
import org.didierdominguez.list.SimpleList.SimpleNode;

public class ControllerInvoice {
    private static ControllerInvoice instance;
    private SimpleList invoiceList;
    private int id;
    private boolean update;

    private ControllerInvoice() {
        invoiceList = new SimpleList();
        id = 0;
    }

    public static ControllerInvoice getInstance() {
        if (instance == null) {
            instance = new ControllerInvoice();
        }
        return instance;
    }

    public void createInvoice(Order order) {
        id++;
        Invoice invoice = new Invoice(id, order, false);
        invoiceList.addLastNode(invoice);
        System.out.println("Invoice added successfully");
    }

    public SimpleList getInvoiceList() {
        return invoiceList;
    }

    public void updateInvoice(Integer id) {
        Invoice invoice = searchInvoice(id);
        if (invoice != null) {
            invoice.setState(true);
            ControllerServiceFinished.getInstance().deleteFinishedList(invoice.getOrder());
        }
    }

    public boolean updateInvoice() {
        return update;
    }

    private Invoice searchInvoice(Integer id) {
        SimpleNode auxiliaryNode = getInvoiceList().getFirstNode();
        while (auxiliaryNode != null && !((Invoice) auxiliaryNode.getObject()).getId().equals(id)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (auxiliaryNode != null && auxiliaryNode.getObject() != null) {
            return (Invoice) auxiliaryNode.getObject();
        }
        return null;
    }
}
