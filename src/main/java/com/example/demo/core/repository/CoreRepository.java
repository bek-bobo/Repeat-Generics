package com.example.demo.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CoreRepository<Entity,ID> extends JpaRepository<Entity,ID>, JpaSpecificationExecutor<Entity> {
}
