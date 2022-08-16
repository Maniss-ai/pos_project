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

        Double total_amount = 0.0;
        int number = 1;
        for(OrderItemData placeOrderData : placeOrderDataList) {
            OrderInvoicePojo orderInvoicePojo = new OrderInvoicePojo();

            orderInvoicePojo.setSNo(number++);
            orderInvoicePojo.setBarcode(placeOrderData.getBarcode());
            orderInvoicePojo.setQuantity(placeOrderData.getQuantity());
            orderInvoicePojo.setSelling_price(Precision.round(placeOrderData.getSelling_price(), 2));
            orderInvoicePojo.setBill_amount(Precision.round(placeOrderData.getQuantity() * placeOrderData.getSelling_price(), 2));

            total_amount += orderInvoicePojo.getBill_amount();

            orderInvoicePojoList.add(orderInvoicePojo);
        }

        total_amount = Precision.round(total_amount, 2);

        JAXBContext contextObj = JAXBContext.newInstance(OrderInvoice.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        OrderInvoice orderInvoice = new OrderInvoice(1, orderInvoicePojoList, orderPojo.getTime().toString(), orderPojo.getOrder_id(), total_amount);
        StringWriter stringWriter = new StringWriter();
        marshallerObj.marshal(orderInvoice, stringWriter);

        String xmlContent = stringWriter.toString();
        System.out.println("\n\n\n :::::DATA:::::DATA:::::DATA:::::\n\n\n");
        System.out.println(xmlContent);
        System.out.println("\n\n\n :::::DATA:::::DATA:::::DATA:::::\n\n\n");

        marshallerObj.marshal(orderInvoice, Files.newOutputStream(Paths.get("src/main/resources/xml/invoice.xml")));
        GenerateInvoice.convertToPDF();
    }
}
