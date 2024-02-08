package ru.maksimov.UsersService.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maksimov.UsersService.dto.usersDto.NewGroupDTO;
import ru.maksimov.UsersService.models.Group;
import ru.maksimov.UsersService.models.RequestForMembership;
import ru.maksimov.UsersService.models.User;
import ru.maksimov.UsersService.services.GroupsService;
import ru.maksimov.UsersService.services.RequestsService;
import ru.maksimov.UsersService.services.UsersService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupsController {

    private final GroupsService groupsService;
    private final RequestsService requestsService;

    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public GroupsController(GroupsService groupsService, RequestsService requestsService, UsersService usersService, ModelMapper modelMapper) {
        this.groupsService = groupsService;
        this.requestsService = requestsService;
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<Group>> findAll() {
        List<Group> groups = groupsService.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> findById(@PathVariable("id") int id) {
        Optional<Group> group = groupsService.findById(id);
        if(group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(group.get());
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestParam(name = "userId") int userId,
                                             @RequestBody @Valid NewGroupDTO newGroupDTO,
                                             BindingResult bindingResult
                                              ) {
        Group group = modelMapper.map(newGroupDTO, Group.class);
        User user = new User();
        user.setId(userId);
        group.setOwner(user);
        try {
            group = groupsService.createGroup(group);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(group);
    }


    @GetMapping("/{id}/requests")
    public ResponseEntity<List<RequestForMembership>> getRequests(@PathVariable("id") int groupId,
                                                                  @RequestParam(name = "visitorId") int visitorId,
                                                                  @RequestParam(name = "message", required = false) String message) {
        Optional<Group> group = groupsService.findById(groupId);
        User visitor = usersService.findById(visitorId);

        // NEED TO THROWS EXCEPTIONS IN SERVICES!!!
        if(group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else {
            if(group.get().getOwner().getId().equals(visitorId)){
                List<RequestForMembership> requests = requestsService.requestsForGroup(groupId);
                return ResponseEntity.ok(requests);
            }
            else {
                RequestForMembership requestForMembership = requestsService.sendRequest(group.get(), visitor, message);
                return ResponseEntity.ok(List.of(requestForMembership));
            }
        }
    }
}
