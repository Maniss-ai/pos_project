package com.increff.employee.generatepdf;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenerateInvoice {
    private static final String PATH_TO_XSL = "src/main/resources/xsl/invoice.xsl";
    private static final String PATH_TO_XML = "src/main/resources/xml/invoice.xml";
    private static final String PATH_TO_PDF = "src/main/resources/pdf/invoice.pdf";

    public static void convertToPDF() throws IOException, FOPException, TransformerException {
        // the XSL FO file
        File xsltFile = new File(PATH_TO_XSL);
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File(PATH_TO_XML));

        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        OutputStream out;

        out = Files.newOutputStream(Paths.get(PATH_TO_PDF));

        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }

    // todo check uses
    public static void generatePdf(HttpServletResponse response) {
        try {
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            //Set Up a buffer to obtain the content length
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(PATH_TO_XSL));
            //Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            //Setup input
            Source src = new StreamSource(new File(PATH_TO_XML));

            //Start the transformation and rendering process
            transformer.transform(src, res);

            //Prepare response
            response.setContentType("application/pdf");
            response.setContentLength(out.size());

            //Send content to Browser
            response.getOutputStream().write(out.toByteArray());
            response.getOutputStream().flush();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
