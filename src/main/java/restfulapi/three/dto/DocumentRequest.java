package restfulapi.three.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String author;

    private String notes;

    private String category;

    private String tags;

    private String createdBy;

    private boolean isArchived;
}



