# PDF Generator API

The PDF Generator API is a REST service designed to dynamically create PDF documents using templates and data provided by users. Built with Spring Boot, this API offers endpoints for generating customizable PDFs.

## Features

- **Flexible PDF Generation**: Create PDF documents by leveraging customizable templates (Jasper) and dynamic data inputs.
- **Content Customization**: Populate PDF templates with dynamic data, enabling personalized and data-driven PDF generation.
- **Content Delivery Options**: Select between receiving the generated PDF as a streamed response or a Base64-encoded string, based on client preferences.
- **Content Negotiation**: The API serves content in the desired format, as indicated by the client's Accept header, facilitating seamless integration with diverse client applications.
- **Compression**: The response can be compressed based on the request's Accept-Encoding header.

## Endpoints

### Generate PDF

**Endpoint**: `POST /api/pdf`

This endpoint enables clients to generate PDF documents using provided data inputs and template references.

**Request**:

```javascript
{
    "template": "/templates/template1/sample-report.jrxml", // Template path
    "password": "pass", // Optional password
    "data": {
        "name": "Roberto Valenzuela Gonzalez"
    } // JSON data to populate the template
}
```
**Response**:

- If the client's Accept header specifies text/plain, a Base64-encoded PDF is returned.
- If the client's Accept header specifies application/pdf, a PDF document is streamed as the response.

## Getting Started

Requirements:

- Java 11

To run the project:

- Clone the repository.
- Navigate to the project directory.
- Run the following commands:

```shell
./mvnw clean install
./mvnw spring-boot:run
```

This will build and run the project, making the API accessible.

## Future Enhancements

- Retrieve templates from a blob store using cloud providers.
