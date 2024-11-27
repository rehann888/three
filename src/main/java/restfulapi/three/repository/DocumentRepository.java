package restfulapi.three.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restfulapi.three.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}


