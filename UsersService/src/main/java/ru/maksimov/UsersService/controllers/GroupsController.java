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
import ru.maksimov.UsersService.util.exceptions.GroupNotFoundException;
import ru.maksimov.UsersService.util.exceptions.RequestNotFoundException;

import java.util.List;

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
        Group group = groupsService.findById(id);
        return ResponseEntity.ok(group);
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestParam(name = "userId") int userId,
                                             @RequestBody @Valid NewGroupDTO newGroupDTO,
                                             BindingResult bindingResult
                                              ) {
        Group group = modelMapper.map(newGroupDTO, Group.class);
        User owner = usersService.findById(userId);
        group.setOwner(owner);
        group.addMember(owner);
        try {
            group = groupsService.saveGroup(group);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(group);
    }


    @GetMapping("/{groupId}/requests")
    public ResponseEntity<List<RequestForMembership>> getRequests(@PathVariable("groupId") int groupId,
                                                                  @RequestParam(name = "visitorId") int visitorId,
                                                                  @RequestParam(name = "message", required = false) String message) {
        Group group = groupsService.findById(groupId);
        User visitor = usersService.findById(visitorId);

        if(group.getOwner().getId().equals(visitorId)){
            List<RequestForMembership> requests = requestsService.requestsForGroup(groupId);
            return ResponseEntity.ok(requests);
        }
        else {
            RequestForMembership requestForMembership = requestsService.sendRequest(group, visitor, message);
            return ResponseEntity.ok(List.of(requestForMembership));
        }
    }

    @GetMapping("/{groupId}/requests/{requestId}")
    public ResponseEntity<RequestForMembership> confirmRequest(@PathVariable("groupId") int groupId,
                                                               @PathVariable("requestId") int requestId,
                                                               @RequestParam(name = "visitorId") int visitorId) {
        List<RequestForMembership> requests = requestsService.requestsForGroup(groupId);
        RequestForMembership request = requestsService.findById(requestId);
        if(requests.contains(request)) {
            Group group = request.getGroup();
            User user = request.getUser();
            group.addMember(user);
            groupsService.saveGroup(group);
            user.addGroup(group);
            usersService.update(user.getId(), user);
            requestsService.deleteRequest(request);
            return ResponseEntity.ok(request);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleException(GroupNotFoundException groupNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleException(RequestNotFoundException requestNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
