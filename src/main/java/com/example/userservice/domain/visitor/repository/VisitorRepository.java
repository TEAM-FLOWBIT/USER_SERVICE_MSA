package com.example.userservice.domain.visitor.repository;

import com.example.userservice.domain.visitor.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor,Long> {

}
