package ru.maksimov.UsersService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maksimov.UsersService.models.Group;
import ru.maksimov.UsersService.repositories.GroupsRepository;
import ru.maksimov.UsersService.util.exceptions.GroupNotFoundException;

import java.util.List;

@Service
public class GroupsService {

    private final GroupsRepository groupsRepository;

    @Autowired
    public GroupsService(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    public Group saveGroup(Group group) {
        return groupsRepository.save(group);
    }

    public List<Group> findAll() {
        return groupsRepository.findAll();
    }

    // maybe make returned value is Group and throw GroupNotFoundException
    public Group findById(int id) {
        return groupsRepository.findById(id).orElseThrow(GroupNotFoundException::new);
    }
}
