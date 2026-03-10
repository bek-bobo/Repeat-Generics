package com.example.demo.group;

import com.example.demo.core.repository.CoreRepository;
import com.example.demo.group.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupRepository extends CoreRepository<Group, UUID> {

    @EntityGraph(attributePaths = "users")
    Page<Group> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "users")
    Page<Group> findAll(Specification<Group> specification, Pageable pageable);
}
