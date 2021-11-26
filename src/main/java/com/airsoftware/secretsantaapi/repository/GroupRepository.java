package com.airsoftware.secretsantaapi.repository;

import com.airsoftware.secretsantaapi.model.SecretSantaGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<SecretSantaGroup, String> {

}
