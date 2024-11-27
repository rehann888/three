package restfulapi.three.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import restfulapi.three.dto.DocumentRequest;
import restfulapi.three.dto.WebResponse;
import restfulapi.three.entity.Document;
import restfulapi.three.security.service.UserDetailImplementation;
import restfulapi.three.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Document Management API", description = "Endpoints for managing documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    @PreAuthorize("isAuthenticated()") 
    @Operation(summary = "Get all documents", description = "Fetch a list of all available documents.")
    public ResponseEntity<WebResponse<List<Document>>> getAllDocuments(@AuthenticationPrincipal UserDetailImplementation user) {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(WebResponse.<List<Document>>builder()
                .statusCode(200)
                .message("Documents fetched successfully")
                .data(documents)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") 
    @Operation(summary = "Get document by ID", description = "Fetch a single document by its unique ID.")
    public ResponseEntity<WebResponse<Document>> getDocumentById(
            @AuthenticationPrincipal UserDetailImplementation user,
            @Parameter(description = "ID of the document to fetch", required = true) @PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        return ResponseEntity.ok(WebResponse.<Document>builder()
                .statusCode(200)
                .message("Document fetched successfully")
                .data(document)
                .build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()") 
    @Operation(summary = "Create a new document", description = "Upload and create a new document.")
    public ResponseEntity<WebResponse<Document>> createDocument(
            @AuthenticationPrincipal UserDetailImplementation user,
            @ModelAttribute DocumentRequest documentRequest,
            @Parameter(description = "Document file to upload") @RequestParam(value = "docFile", required = false) MultipartFile docFile,
            @Parameter(description = "Video file to upload") @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            @Parameter(description = "Image file to upload") @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws Exception {

        
        if (docFile != null && docFile.isEmpty()) {
            throw new RuntimeException("Document file is empty");
        }

        if (videoFile != null && videoFile.isEmpty()) {
            throw new RuntimeException("Video file is empty");
        }

        if (imageFile != null && imageFile.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        Document document = new Document();
        document.setTitle(documentRequest.getTitle());
        document.setDescription(documentRequest.getDescription());
        document.setAuthor(documentRequest.getAuthor());
        document.setNotes(documentRequest.getNotes());
        document.setCategory(documentRequest.getCategory());
        document.setTags(documentRequest.getTags());
        document.setCreatedBy(documentRequest.getCreatedBy());
        document.setArchived(documentRequest.isArchived());

        Document savedDocument = documentService.saveDocument(document, docFile, videoFile, imageFile);

        return ResponseEntity.ok(WebResponse.<Document>builder()
                .statusCode(201)
                .message("Document created successfully")
                .data(savedDocument)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()") 
    @Operation(summary = "Update document", description = "Update an existing document by ID.")
    public ResponseEntity<WebResponse<Document>> updateDocument(
            @AuthenticationPrincipal UserDetailImplementation user,
            @Parameter(description = "ID of the document to update", required = true) @PathVariable Long id,
            @RequestBody DocumentRequest documentRequest) {

        Document document = new Document();
        document.setTitle(documentRequest.getTitle());
        document.setDescription(documentRequest.getDescription());
        document.setAuthor(documentRequest.getAuthor());
        document.setNotes(documentRequest.getNotes());
        document.setCategory(documentRequest.getCategory());
        document.setTags(documentRequest.getTags());
        document.setCreatedBy(documentRequest.getCreatedBy());
        document.setArchived(documentRequest.isArchived());

        Document updatedDocument = documentService.updateDocument(id, document);

        return ResponseEntity.ok(WebResponse.<Document>builder()
                .statusCode(200)
                .message("Document updated successfully")
                .data(updatedDocument)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()") 
    @Operation(summary = "Delete document", description = "Delete an existing document by ID.")
    public ResponseEntity<WebResponse<Void>> deleteDocument(
            @AuthenticationPrincipal UserDetailImplementation user,
            @Parameter(description = "ID of the document to delete", required = true) @PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(WebResponse.<Void>builder()
                .statusCode(200)
                .message("Document deleted successfully")
                .data(null)
                .build());
    }
}
