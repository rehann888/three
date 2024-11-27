package restfulapi.three.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import restfulapi.three.entity.Document;
import restfulapi.three.repository.DocumentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    private final String uploadDir = "uploads/";

    public List<Document> getAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        if (documents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No documents available");
        }
        return documents;
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document Not Found with ID: " + id));
    }

    public Document saveDocument(Document document, MultipartFile docFile, MultipartFile videoFile, MultipartFile imageFile) {
        try {
            String docFilePath = (docFile != null && !docFile.isEmpty()) ? saveFile(docFile, "docs") : null;
            String videoFilePath = (videoFile != null && !videoFile.isEmpty()) ? saveFile(videoFile, "videos") : null;
            String imageFilePath = (imageFile != null && !imageFile.isEmpty()) ? saveFile(imageFile, "images") : null;

            document.setDocFilePath(docFilePath);
            document.setVideoFilePath(videoFilePath);
            document.setImageFilePath(imageFilePath);

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed: " + e.getMessage());
        }
    }

    public Document updateDocument(Long id, Document updatedDocument) {
        return documentRepository.findById(id).map(existingDocument -> {
            existingDocument.setTitle(updatedDocument.getTitle());
            existingDocument.setDescription(updatedDocument.getDescription());
            existingDocument.setAuthor(updatedDocument.getAuthor());
            return documentRepository.save(existingDocument);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document Not Found with ID: " + id));
    }

    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document Not Found with ID: " + id);
        }
        try {
            documentRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete document: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty or null");
        }

        Path uploadPath = Paths.get(uploadDir + subDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create upload directory: " + e.getMessage());
            }
        }

        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save file: " + e.getMessage());
        }

        return filePath.toString();
    }
}


