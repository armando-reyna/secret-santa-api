package com.airsoftware.secretsantaapi.service;

import com.airsoftware.secretsantaapi.exception.EmptyGiveToException;
import com.airsoftware.secretsantaapi.exception.NoMorePeopleException;
import com.airsoftware.secretsantaapi.model.SecretSantaGroup;
import com.airsoftware.secretsantaapi.model.SecretSantaPerson;
import com.airsoftware.secretsantaapi.repository.GroupRepository;
import com.airsoftware.secretsantaapi.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final PersonRepository personRepository;
    private final CustomMailSender customMailSender;

    public SecretSantaGroup save(SecretSantaGroup group) {
        return groupRepository.save(group);
    }

    public List<SecretSantaGroup> getAll() {
        return groupRepository.findAll();
    }

    public void fixRelation() {
        final List<SecretSantaGroup> secretSantaGroups = groupRepository.findAll();
        secretSantaGroups.forEach(secretSantaGroup -> secretSantaGroup.getMembers().forEach(secretSantaPerson -> {
            secretSantaPerson.setGroup(secretSantaGroup);
            personRepository.save(secretSantaPerson);
        }));
    }

    public void celebrate(List<SecretSantaGroup> secretSantaGroups) throws EmptyGiveToException {
        if (secretSantaGroups == null) {
            secretSantaGroups = groupRepository.findAll();
        }
        try {
            calculateCelebration(secretSantaGroups);
        } catch (NoMorePeopleException noMorePeopleException) {
            celebrate(secretSantaGroups);
        }
    }

    private void calculateCelebration(final List<SecretSantaGroup> originalSecretSantaGroups) throws NoMorePeopleException, EmptyGiveToException {

        final List<SecretSantaGroup> secretSantaGroups = new ArrayList<>(originalSecretSantaGroups);
        final List<SecretSantaPerson> allPeople = secretSantaGroups.stream()
                .flatMap(secretSantaGroup -> secretSantaGroup.getMembers().stream())
                .collect(Collectors.toList());
        Collections.shuffle(allPeople);
        final List<SecretSantaPerson> pendingPeople = new ArrayList<>(allPeople);

        for (SecretSantaPerson person : allPeople) {
            final SecretSantaPerson givesTo = findRandomPerson(person, pendingPeople);
            person.setGivesTo(givesTo);
            pendingPeople.remove(givesTo);
        }

        final List<SecretSantaGroup> savedSecretSantaGroups = groupRepository.findAll();
        for (SecretSantaGroup secretSantaGroup : savedSecretSantaGroups) {
            log.info("Group: {}", secretSantaGroup.getEmail());
            for (SecretSantaPerson secretSantaPerson : secretSantaGroup.getMembers()) {
                if (secretSantaPerson.getGivesTo() == null) {
                    throw new EmptyGiveToException("Give to is empty.");
                }
                log.info("\t{} gives to {}", secretSantaPerson.getName(), secretSantaPerson.getGivesTo().getName());
            }
            customMailSender.sendEmailToGroup(secretSantaGroup);
        }

    }

    private SecretSantaPerson findRandomPerson(final SecretSantaPerson secretSantaPerson, final List<SecretSantaPerson> pendingPeople) throws NoMorePeopleException {
        final List<SecretSantaPerson> filteredPeople = pendingPeople.stream()
                .filter(secretSantaPersonPotential -> !secretSantaPerson.getId().equals(secretSantaPersonPotential.getId()))
                .filter(secretSantaPersonPotential -> !secretSantaPerson.getGroup().getEmail().equals(secretSantaPersonPotential.getGroup().getEmail()))
                .collect(Collectors.toList());
        if (filteredPeople.size() == 0) {
            throw new NoMorePeopleException("No more people available");
        }
        return filteredPeople.get(getRandomNumber(0, filteredPeople.size() - 1));
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
