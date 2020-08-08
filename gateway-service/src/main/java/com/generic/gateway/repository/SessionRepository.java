package com.generic.gateway.repository;

import com.generic.gateway.domain.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, String> {
}
