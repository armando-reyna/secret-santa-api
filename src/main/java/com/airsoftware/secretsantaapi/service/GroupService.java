package com.airsoftware.secretsantaapi.service;

import com.airsoftware.secretsantaapi.model.SecretSantaGroup;
import com.airsoftware.secretsantaapi.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GroupService {

    protected final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    public SecretSantaGroup save(SecretSantaGroup group) {
        return groupRepository.save(group);
    }

    public List<SecretSantaGroup> getAll() {
        return groupRepository.findAll();
    }

}
