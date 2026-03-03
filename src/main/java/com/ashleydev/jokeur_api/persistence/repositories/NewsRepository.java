package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.NewsEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
  Optional<NewsEntity> findByLink(String link);

  Page<NewsEntity> findAllByOrderByPublishedAtDesc(Pageable pageable);
}
