package cl.edvasqueza.pdfgenerator.controller;

import cl.edvasqueza.pdfgenerator.GeneratePdfService;
import cl.edvasqueza.pdfgenerator.controller.dto.GeneratePdfWithDataRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
class PdfGeneratorController {

    @Resource
    private GeneratePdfService generatePdfService;

    @PostMapping(
            value = "/pdf",
            produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    ResponseEntity<StreamingResponseBody> getPdfStream(@Valid @RequestBody GeneratePdfWithDataRequest request) {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> pdfData = mapper.convertValue(request.getData(), HashMap.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("content-disposition", "attachment; filename=file.pdf");

        StreamingResponseBody responseBody = outputStream -> {
            generatePdfService.generatePdf(request.getTemplate(), request.getPassword() ,pdfData, outputStream);
        };
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .headers(headers)
                .body(responseBody);
    }

    @PostMapping(
            value = "/pdf",
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    ResponseEntity<String> getPdfBase64(@Valid @RequestBody GeneratePdfWithDataRequest request) {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> pdfData = mapper.convertValue(request.getData(), HashMap.class);

        ByteArrayOutputStream outputStream = generatePdfService.generatePdf(request.getTemplate(), request.getPassword(), pdfData);
        byte[] pdfBytes = outputStream.toByteArray();
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(base64Pdf);
    }
}