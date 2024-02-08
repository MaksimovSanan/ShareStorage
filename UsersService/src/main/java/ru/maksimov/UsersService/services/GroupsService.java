package ru.maksimov.UsersService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maksimov.UsersService.models.Group;
import ru.maksimov.UsersService.repositories.GroupsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupsService {

    private final GroupsRepository groupsRepository;

    @Autowired
    public GroupsService(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    public Group createGroup(Group group) {
        return groupsRepository.save(group);
    }

    public List<Group> findAll() {
        return groupsRepository.findAll();
    }

    // maybe make returned value is Group and throw GroupNotFoundException
    public Optional<Group> findById(int id) {
        return groupsRepository.findById(id);
    }
}
