# PdfGeneratorApi

The PDF Generation API is a Rest service that allows users to dynamically generate PDF documents based on provided templates and data.
The API is built using Spring Boot and provides endpoints for generating PDFs with customizable content.

## Features

- Flexible PDF Generation: Generate PDF documents using customizable templates (Jasper) and dynamic data inputs.
- Content Customization: Populate PDF templates with dynamic data, enabling personalized and data-driven PDF generation.
- Content Delivery Options: Choose between receiving the generated PDF as a streamed response or a Base64-encoded string, based on the client's preference.
- Content Negotiation: The API serves content in the desired format based on the client's Accept header, making it easy to integrate with various client applications.

## Endpoints

**Generate PDF**

    POST /api/pdf

This endpoint allows clients to generate PDF documents using provided data inputs and template references.

Request:

```javascript
{
    "template": "/templates/template1/sample-report.jrxml", // Template path
    "password": "pass", // Optional password
    "data": {
        "name": "Roberto Valenzuela Gonzalez"
    } // JSON data to populate the template
}
```
Response:

- If the client's Accept header indicates text/plain, a Base64-encoded PDF is returned.
- If the client's Accept header indicates application/pdf, a PDF document is streamed as the response.

## Run the project

Requirements:
- Java 11

Run `./mvnw clean install`

Run `./mvnw spring-boot:run`

## Future Enhancements

Caching and Compression: Incorporate caching strategies and content compression techniques to further optimize data transfer and reduce network latency.
