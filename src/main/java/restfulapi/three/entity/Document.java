package restfulapi.three.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String author;

    @Column(nullable = true)
    private String docFilePath;

    @Column(nullable = true)
    private String videoFilePath;

    @Column(nullable = true)
    private String imageFilePath;

    @Column(length = 500)
    private String notes;

    private String category;

    private String tags;

    private String createdBy;

    private String lastModifiedBy;

    @Column(columnDefinition = "boolean default false")
    private boolean isArchived;
}

