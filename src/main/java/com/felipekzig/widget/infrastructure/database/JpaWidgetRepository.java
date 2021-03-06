package com.felipekzig.widget.infrastructure.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.felipekzig.widget.domain.entity.Widget;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Profile("sql")
@Repository
public interface JpaWidgetRepository extends JpaRepository<Widget, UUID>, JpaSpecificationExecutor<Widget> {
    Optional<Widget> findByzIndex(Integer zIndex);

    List<Widget> findAll(Specification<Widget> spec);

    Page<Widget> findAll(Specification<Widget> spec, Pageable page);
}
