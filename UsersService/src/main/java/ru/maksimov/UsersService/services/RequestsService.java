package ru.maksimov.UsersService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maksimov.UsersService.models.Group;
import ru.maksimov.UsersService.models.RequestForMembership;
import ru.maksimov.UsersService.models.User;
import ru.maksimov.UsersService.repositories.RequestsRepository;

import java.util.List;

@Service
public class RequestsService {

    private final RequestsRepository requestsRepository;

    @Autowired
    public RequestsService(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    public List<RequestForMembership> findAll() {
        return requestsRepository.findAll();
    }

    public List<RequestForMembership> requestsForGroup(int groupId) {
        return requestsRepository.findAllByGroupId(groupId);
    }

    public List<RequestForMembership> requestsForUser(int userId) {
        return requestsRepository.findAllByUserId(userId);
    }

    public RequestForMembership sendRequest(Group group, User user, String message) {
        RequestForMembership requestForMembership = new RequestForMembership(group, user, message);
        requestsRepository.save(requestForMembership);
        return requestForMembership;
    }

}
