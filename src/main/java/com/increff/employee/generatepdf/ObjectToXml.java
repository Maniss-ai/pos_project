package com.increff.employee.generatepdf;

import com.increff.employee.model.PlaceOrderData;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.PlaceOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ObjectToXml {
    @Autowired
    private static PlaceOrderService placeOrderService;

    public static void generateXmlString(List<PlaceOrderData> placeOrderDataList, OrderPojo orderPojo) throws Exception {
        // convert placeOrderPojoList -> OrderInvoicePojoList
        ArrayList<OrderInvoicePojo> orderInvoicePojoList = new ArrayList<>();

        int total_amount = 0;
        for(PlaceOrderData placeOrderData : placeOrderDataList) {
            OrderInvoicePojo orderInvoicePojo = new OrderInvoicePojo();

            orderInvoicePojo.setBarcode(placeOrderData.getBarcode());
            orderInvoicePojo.setQuantity(placeOrderData.getQuantity());
            orderInvoicePojo.setSelling_price(placeOrderData.getSelling_price());
            orderInvoicePojo.setBill_amount(placeOrderData.getQuantity() * placeOrderData.getSelling_price());

            total_amount += orderInvoicePojo.getBill_amount();

            orderInvoicePojoList.add(orderInvoicePojo);
        }

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
