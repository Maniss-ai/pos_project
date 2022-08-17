package com.increff.employee.generatepdf;

import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.OrderItemService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ObjectToXml {
    @Autowired
    private static OrderItemService orderItemService;

    public static void generateXmlString(List<OrderItemData> placeOrderDataList, OrderPojo orderPojo) throws Exception {
        // convert placeOrderPojoList -> OrderInvoicePojoList
        ArrayList<OrderInvoicePojo> orderInvoicePojoList = new ArrayList<>();

        Double totalAmount = 0.0;
        Integer number = 1;
        for(OrderItemData placeOrderData : placeOrderDataList) {
            OrderInvoicePojo orderInvoicePojo = new OrderInvoicePojo();

            orderInvoicePojo.setSNo(number++);
            orderInvoicePojo.setBarcode(placeOrderData.getBarcode());
            orderInvoicePojo.setQuantity(placeOrderData.getQuantity());
            orderInvoicePojo.setSellingPrice(Precision.round(placeOrderData.getSellingPrice(), 2));
            orderInvoicePojo.setBillAmount(Precision.round(placeOrderData.getQuantity() * placeOrderData.getSellingPrice(), 2));

            totalAmount += orderInvoicePojo.getBillAmount();

            orderInvoicePojoList.add(orderInvoicePojo);
        }

        totalAmount = Precision.round(totalAmount, 2);

        JAXBContext contextObj = JAXBContext.newInstance(OrderInvoice.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        OrderInvoice orderInvoice = new OrderInvoice(1, orderInvoicePojoList, orderPojo.getTime().toString(), orderPojo.getOrderId(), totalAmount);
        StringWriter stringWriter = new StringWriter();
        marshallerObj.marshal(orderInvoice, stringWriter);

        String xmlContent = stringWriter.toString();

        marshallerObj.marshal(orderInvoice, Files.newOutputStream(Paths.get("src/main/resources/xml/invoice.xml")));
        GenerateInvoice.convertToPDF();
    }
}
