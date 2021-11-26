package com.airsoftware.secretsantaapi.repository;

import com.airsoftware.secretsantaapi.model.SecretSantaPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<SecretSantaPerson, Long> {

}
