package restfulapi.three.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import restfulapi.three.entity.Document;
import restfulapi.three.repository.DocumentRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    private final String uploadDir = "uploads/";

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document Not Found"));
    }

    public Document saveDocument(Document document, MultipartFile docFile, MultipartFile videoFile, MultipartFile imageFile) throws Exception {
        // Simpan file hanya jika ada
        String docFilePath = (docFile != null && !docFile.isEmpty()) ? saveFile(docFile, "docs") : null;
        String videoFilePath = (videoFile != null && !videoFile.isEmpty()) ? saveFile(videoFile, "videos") : null;
        String imageFilePath = (imageFile != null && !imageFile.isEmpty()) ? saveFile(imageFile, "images") : null;
    
        // Set file path ke entity hanya jika ada file
        document.setDocFilePath(docFilePath);
        document.setVideoFilePath(videoFilePath);
        document.setImageFilePath(imageFilePath);
    
        return documentRepository.save(document);
    }
    

    public Document updateDocument(Long id, Document updatedDocument) {
        Optional<Document> existingDocumentOpt = documentRepository.findById(id);
        if (existingDocumentOpt.isPresent()) {
            Document existingDocument = existingDocumentOpt.get();
            existingDocument.setTitle(updatedDocument.getTitle());
            existingDocument.setDescription(updatedDocument.getDescription());
            existingDocument.setAuthor(updatedDocument.getAuthor());
            return documentRepository.save(existingDocument);
        }
        throw new RuntimeException("Document not found");
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    private String saveFile(MultipartFile file, String subDir) throws Exception {
        // Abaikan jika file tidak diupload
        if (file == null || file.isEmpty()) {
            return null; // Return null jika file kosong atau tidak diupload
        }
    
        Path uploadPath = Paths.get(uploadDir + subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // Buat folder jika belum ada
        }
    
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
        return filePath.toString(); // Kembalikan path file yang tersimpan
    }
    
}
