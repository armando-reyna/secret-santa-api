package com.airsoftware.secretsantaapi.control;

import com.airsoftware.secretsantaapi.model.SecretSantaGroup;
import com.airsoftware.secretsantaapi.service.GroupService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class Controller {

    protected final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final GroupService groupService;

    @PostMapping("/registration")
    public SecretSantaGroup save(@RequestBody SecretSantaGroup secretSantaGroup) {
        return groupService.save(secretSantaGroup);
    }

    @GetMapping("/group")
    public List<SecretSantaGroup> getAll() {
        return groupService.getAll();
    }

    @PostMapping("/fix")
    public void fixRelation() {
        groupService.fixRelation();
    }

    @PostMapping("/celebrate")
    public void celebrate() throws Exception {
        groupService.celebrate(null);
    }

}
