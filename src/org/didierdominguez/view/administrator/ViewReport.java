package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.bean.SparePart;
import org.didierdominguez.controller.ControllerCustomer;
import org.didierdominguez.controller.ControllerSparePart;
import org.didierdominguez.list.CircularDoubleList.CircularDoubleNode;
import org.didierdominguez.list.SimpleList.SimpleNode;
import org.didierdominguez.util.ReportGenerator;
import org.didierdominguez.util.ScreenSize;

public class ViewReport extends Stage {
    private static ViewReport instance;
    private String colors;

    private ViewReport() {
        colors = "'#ffc785','#7189bf','#df7599','#72d6c9','#c6c9d0','#c44b6c','#218b81','#f27348','#2ad0ce','#38667e'";
    }

    public static ViewReport getInstance() {
        if (instance == null) {
            instance = new ViewReport();
        }
        return instance;
    }

    HBox getViewReport() {
        HBox hBox = new HBox();
        VBox vBoxButtons = new VBox();
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);
        gridPane.setMinWidth(x / 2);
        gridPane.setPrefSize(x, y);
        hBox.setPrefSize(x, y);

        Text textTitle = new Text("REPORTES");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        vBoxButtons.setPrefSize(x, y);

        JFXButton buttonCustomer = new JFXButton("CLIENTES");
        buttonCustomer.getStyleClass().addAll("panelButton", "primaryButton");
        buttonCustomer.setPrefSize(x, y);
        buttonCustomer.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCustomer.setOnAction(event -> {
            int gold = 0;
            int normal = 0;
            String content = "<!doctype html><html lang=\"es\"><head><!-- Required meta tags --><meta " +
                    "charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
                    "shrink-to-fit=no\"><!-- Bootstrap CSS --><link rel=\"stylesheet\" " +
                    "href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" " +
                    "href=\"https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css\"><title>customerReport</title>" +
                    "</head><body><div class=\"container\" align=\"center\"><br><h2>TABLA DE CLIENTES</h2><hr><table id=\"example\" " +
                    "class=\"display\" style=\"width:100%\"><thead><tr><th>ID</th><th>NOMBRE</th><th>USUARIO</th>" +
                    "<th>TIPO</th></tr></thead><tbody><tr>";

            CircularDoubleNode auxiliaryNode = ControllerCustomer.getInstance().getCustomerList().getNode();
            if (auxiliaryNode != null) {
                do {
                    content += "<td>"+((Customer) auxiliaryNode.getObject()).getId()+"</td>"+
                            "<td>"+((Customer) auxiliaryNode.getObject()).getName()+"</td>"
                            +"<td>"+((Customer) auxiliaryNode.getObject()).getUser().getUserName()+"</td>"
                            +"<td>"+(((Customer) auxiliaryNode.getObject()).getType() ? "ORO" : "NORMAL")+"</td></tr>";
                    if (((Customer) auxiliaryNode.getObject()).getType()) {
                        gold++;
                    } else {
                        normal++;
                    }
                    auxiliaryNode = auxiliaryNode.getPreviousNode();
                } while (auxiliaryNode != ControllerCustomer.getInstance().getCustomerList().getNode());
            }
            content += "</tbody></table></div><div class=\"container\" align=\"center\"><br><h2>GRÁFICA TIPO DE CLIENTES" +
                    "</h2><hr><br><div style=\"width:40%\"><canvas id=\"myChart\" width=\"100\" height=\"100\">" +
                    "</canvas><br></div></div><script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\"></script>" +
                    "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>" +
                    "<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>" +
                    "<script src=\"https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js\"></script>" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.min.js\"></script>" +
                    "<script>$(document).ready(function () {$('#example').DataTable({\"order\": [[0, \"desc\"]]});});" +
                    "</script><script>var ctx=document.getElementById('myChart').getContext('2d');var myPieChart=new Chart" +
                    "(ctx,{type:'pie',data:{labels:['ORO','NORMAL'],datasets: [{data: ["+ gold +","+ normal +"]," +
                    "backgroundColor:["+colors+"],borderColor:["+colors+"],borderWidth:1}]}," +
                    "options:{responsive:true}});</script></body></html>";
            ReportGenerator.getInstance().writeFile(content, "customerReport.html");
        });

        JFXButton buttonMostUsedParts = new JFXButton("TOP 10, REPUESTOS MÁS USADOS");
        buttonMostUsedParts.getStyleClass().addAll("panelButton", "primaryButton");
        buttonMostUsedParts.setPrefSize(x, y);
        buttonMostUsedParts.setButtonType(JFXButton.ButtonType.FLAT);
        buttonMostUsedParts.setOnAction(event -> {
            String name = "";
            String used = "";
            int count = 0;
            String content = "<!doctype html><html lang=\"es\"><head><!-- Required meta tags --><meta " +
                    "charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
                    "shrink-to-fit=no\"><!-- Bootstrap CSS --><link rel=\"stylesheet\" " +
                    "href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" " +
                    "href=\"https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css\"><title>mostUsedPartsReport</title>" +
                    "</head><body><div class=\"container\" align=\"center\"><br><h2>TABLA DE REPUESTOS MÁS UTILIZADOS</h2><hr><table id=\"example\" " +
                    "class=\"display\" style=\"width:100%\"><thead><tr><th>ID</th><th>NOMBRE</th><th>MARCA</th>" +
                    "<th>MODELO</th><th>STOCK</th><th>PRECIO</th></tr></thead><tbody><tr>";

            SimpleNode auxiliaryNode = ControllerSparePart.getInstance().getSparePartListSortedByUse().getFirstNode();
            while (auxiliaryNode != null && count < 10) {
                content += "<td>"+((SparePart) auxiliaryNode.getObject()).getId()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getName()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getBrand()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getModel()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getStock()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getPrice()+"</td></tr>";
                name += "'"+((SparePart) auxiliaryNode.getObject()).getName() + "',";
                used += ((SparePart) auxiliaryNode.getObject()).getCount() + ",";
                auxiliaryNode = auxiliaryNode.getNextNode();
                count++;
            }
            content += "</tbody></table></div><div class=\"container\" align=\"center\"><br><h2>GRÁFICA REPUESTOS MÁS UTILIZADOS" +
                    "</h2><hr><br><div style=\"width:40%\"><canvas id=\"myChart\" width=\"100\" height=\"100\">" +
                    "</canvas><br></div></div><script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\"></script>" +
                    "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>" +
                    "<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>" +
                    "<script src=\"https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js\"></script>" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.min.js\"></script>" +
                    "<script>$(document).ready(function () {$('#example').DataTable({\"order\": [[0, \"asc\"]]});});" +
                    "</script><script>var ctx=document.getElementById('myChart').getContext('2d');var myPieChart=new Chart" +
                    "(ctx,{type:'bar',data:{labels:["+ name +"],datasets: [{data: ["+ used +"]," +
                    "backgroundColor:["+colors+"],borderColor:["+colors+"],borderWidth:1}]},options:{responsive:true}});</script></body></html>";
            ReportGenerator.getInstance().writeFile(content, "mostUsedPartsReport.html");

            String[] titles = { "ID", "NOMBRE", "MARCA", "MODELO", "STOCK", "PRECIO", "USOS"};
            try {
                ReportGenerator.getInstance().generatePDFMostUsedParts("mostUsedPartsReport.pdf", "TABLA DE REPUESTOS MÁS UTILIZADOS", titles);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        JFXButton buttonMoreExpensiveParts = new JFXButton("TOP 10, REPUESTOS MÁS CAROS ");
        buttonMoreExpensiveParts.getStyleClass().addAll("panelButton", "primaryButton");
        buttonMoreExpensiveParts.setPrefSize(x, y);
        buttonMoreExpensiveParts.setButtonType(JFXButton.ButtonType.FLAT);
        buttonMoreExpensiveParts.setOnAction(event -> {
            String name = "";
            String price = "";
            int count = 0;
            String content = "<!doctype html><html lang=\"es\"><head><!-- Required meta tags --><meta " +
                    "charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
                    "shrink-to-fit=no\"><!-- Bootstrap CSS --><link rel=\"stylesheet\" " +
                    "href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" " +
                    "href=\"https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css\"><title>sparePartsReport</title>" +
                    "</head><body><div class=\"container\" align=\"center\"><br><h2>TABLA DE REPUESTOS MÁS CAROS</h2><hr><table id=\"example\" " +
                    "class=\"display\" style=\"width:100%\"><thead><tr><th>ID</th><th>NOMBRE</th><th>MARCA</th>" +
                    "<th>MODELO</th><th>STOCK</th><th>PRECIO</th></tr></thead><tbody><tr>";

            SimpleNode auxiliaryNode = ControllerSparePart.getInstance().getSparePartListSortedByPrice().getFirstNode();
            while (auxiliaryNode != null && count < 10) {
                content += "<td>"+((SparePart) auxiliaryNode.getObject()).getId()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getName()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getBrand()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getModel()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getStock()+"</td>"
                        +"<td>"+((SparePart) auxiliaryNode.getObject()).getPrice()+"</td></tr>";
                name += "'"+((SparePart) auxiliaryNode.getObject()).getName() + "',";
                price += ((SparePart) auxiliaryNode.getObject()).getPrice() + ",";
                auxiliaryNode = auxiliaryNode.getNextNode();
                count++;
            }
            content += "</tbody></table></div><div class=\"container\" align=\"center\"><br><h2>GRÁFICA REPUESTOS MÁS CAROS" +
                    "</h2><hr><br><div style=\"width:40%\"><canvas id=\"myChart\" width=\"100\" height=\"100\">" +
                    "</canvas><br></div></div><script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\"></script>" +
                    "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>" +
                    "<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>" +
                    "<script src=\"https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js\"></script>" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.min.js\"></script>" +
                    "<script>$(document).ready(function () {$('#example').DataTable({\"order\": [[5, \"desc\"]]});});" +
                    "</script><script>var ctx=document.getElementById('myChart').getContext('2d');var myPieChart=new Chart" +
                    "(ctx,{type:'bar',data:{labels:["+ name +"],datasets: [{data: ["+ price +"]," +
                    "backgroundColor:["+colors+"],borderColor:["+colors+"],borderWidth:1}]},options:{responsive:true}});</script></body></html>";
            ReportGenerator.getInstance().writeFile(content, "sparePartsReport.html");
        });

        JFXButton buttonMostUsedServices = new JFXButton("TOP 10, SERVICIOS MÁS USADOS ");
        buttonMostUsedServices.getStyleClass().addAll("panelButton", "primaryButton");
        buttonMostUsedServices.setPrefSize(x, y);
        buttonMostUsedServices.setButtonType(JFXButton.ButtonType.FLAT);
        buttonMostUsedServices.setOnAction(event -> {
        });

        JFXButton buttonTopCars = new JFXButton("TOP 5, AUTOMÓVILES");
        buttonTopCars.getStyleClass().addAll("panelButton", "primaryButton");
        buttonTopCars.setPrefSize(x, y);
        buttonTopCars.setButtonType(JFXButton.ButtonType.FLAT);

        vBoxButtons.getChildren().addAll(buttonCustomer, buttonMostUsedParts, buttonMoreExpensiveParts,
                buttonMostUsedServices, buttonTopCars);
        gridPane.add(vBoxButtons, 0, 1);

        hBox.getChildren().add(gridPane);

        return hBox;
    }
}
