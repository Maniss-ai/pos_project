package com.increff.employee.util;

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
        File xsltFile = new File(PATH_TO_XSL);
        StreamSource xmlSource = new StreamSource(new File(PATH_TO_XML));

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        OutputStream out;

        out = Files.newOutputStream(Paths.get(PATH_TO_PDF));

        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }

}
