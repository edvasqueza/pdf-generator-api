package cl.edvasqueza.pdfgenerator;

import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Service
public class GeneratePdfService {

    private static final Logger logger = LogManager.getLogger(GeneratePdfService.class);

    public ByteArrayOutputStream generatePdf(String template, String password, Map<String, Object> data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generatePdf(template, password, data, outputStream);
        return outputStream;
    }

    public void generatePdf(String template, String password, Map<String, Object> data, OutputStream outputStream) {
        InputStream templateStream = getClass().getResourceAsStream(template);
        if(templateStream == null) {
            logger.info("Template {} not found", template);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Template not found");
        }
        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, new JREmptyDataSource());

            JRPdfExporter exporter = getPdfExporter(password, outputStream, jasperPrint);
            exporter.exportReport();

        } catch (JRException e) {
            logger.error("Error generating pdf file", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error generating pdf file", e);
        }
    }

    private static JRPdfExporter getPdfExporter(String password, OutputStream outputStream, JasperPrint jasperPrint) {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        if(password != null && !password.trim().isEmpty()) {
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            configuration.setEncrypted(true);
            configuration.set128BitKey(true);
            configuration.setUserPassword(password);
            //configuration.setOwnerPassword("reports");
            configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_MODIFY_CONTENTS | PdfWriter.ALLOW_FILL_IN);
            exporter.setConfiguration(configuration);
        }
        return exporter;
    }

}