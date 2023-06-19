package com.marciopd.recipesapi.persistence;

import com.marciopd.recipesapi.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    boolean existsByName(String name);

    List<TagEntity> findByOrderByNameAsc();

    Set<TagEntity> findByIdIsIn(Set<Long> ids);
}
