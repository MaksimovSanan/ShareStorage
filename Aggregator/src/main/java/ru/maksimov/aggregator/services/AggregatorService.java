package ru.maksimov.aggregator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.aggregator.models.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;


@Service
public class AggregatorService {

    private final RestTemplate restTemplate;

    @Autowired
    public AggregatorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserInfo getUserInfo(int userId, int visitorId){

        // TODO
        // FIX FIX FIX THAT
        User user;
        try {
            user = restTemplate.getForObject("http://USERSSERVICE/users/" + userId, User.class);

            // upload user photo
            try {
                ResponseEntity<byte[]> response = restTemplate.getForEntity("http://IMAGESERVER/user-image/" + userId, byte[].class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    byte[] avatarBytes = response.getBody();
                    user.setAvatar(avatarBytes);
                } else {
                    user.setAvatar(null);
                }
            } catch(Exception e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            return null;
        }
//
//        // TODO UserNotFoundPage
//        if(user == null) {
//            return null;
//        }


        String role = userId == visitorId? "owner" : "guest";


        List<Item> items = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(
                "http://ITEMSSERVICE/items?ownerId=" + userId, Item[].class)
        )).toList();

        List<RentContract> rentContracts = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(
                "http://ITEMSSERVICE/rent?borrowerId=" + userId + "&ownerId=" + userId, RentContract[].class)
        )).toList();


        return new UserInfo(role, user, items, rentContracts);
    }

    public ItemInfo getItemInfo(Integer itemId, Integer visitorId) {

        ItemInfo itemInfo = new ItemInfo();

        // TODO
        // FIX FIX FIX THAT
        Item item;
        try {
            item = restTemplate.getForObject("http://ITEMSSERVICE/items/" + itemId, Item.class);
        } catch(Exception e) {
            return null;
        }

//        if(item == null) {
//            return null;
//        }

        // get images for item
        try {
            ResponseEntity<List<byte[]>> response = restTemplate.exchange(
                    "http://IMAGESERVER/item-image/" + itemId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<byte[]>>() {});

            if (response.getStatusCode() == HttpStatus.OK) {
                item.setPictures(response.getBody());
            } else {
                item.setPictures(null);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        itemInfo.setItem(item);


        // get rentContracts for item
        List<RentContract> rentContracts = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(
                "http://ITEMSSERVICE/rent?itemId=" + itemId, RentContract[].class)
        )).toList();
        itemInfo.setRentContracts(rentContracts);

        if(item.getOwnerId() == visitorId) {
            itemInfo.setAccess("owner");
        } else {
            itemInfo.setAccess("guest");
            if(rentContracts.stream().anyMatch(rentContract -> {
                return visitorId.equals(rentContract.getBorrowerId());
            })) {
                itemInfo.getItem().setStatus(300);
            }
        }

        return itemInfo;
    }

    public GroupInfo getGroupInfo(Integer groupId, Integer visitorId) {
        Group group;
        User visitor;
        try {
            group = restTemplate.getForObject("http://USERSSERVICE/groups/" + groupId, Group.class);
            // upload group photo
            try {
                ResponseEntity<byte[]> response = restTemplate.getForEntity("http://IMAGESERVER/group-image/" + groupId, byte[].class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    byte[] avatarBytes = response.getBody();
                    group.setAvatar(avatarBytes);
                } else {
                    group.setAvatar(null);
                }
            } catch(Exception e) {
                System.out.println(e);
            }
            visitor =  restTemplate.getForObject("http://USERSSERVICE/users/" + visitorId, User.class);
        } catch(Exception e) {
            return null;
        }
        String role;
        if(visitor.getId().equals(group.getOwner().getId())) {
            role = "owner";
        }
        else if (visitor.getGroupsMember().stream().filter(simpleGroupDTO -> simpleGroupDTO.getId().equals(group.getId()))
                .findAny().isPresent()){
            role = "member";
        }
        else if (visitor.getRequestsForMembership().stream()
                .filter(requestForMembership -> requestForMembership.getGroupId().equals(group.getId()))
                .findAny().isPresent()) {
            role = "guestWithRequest";
        }
        else {
            role = "guest";
        }

        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?groupId=" + groupId, Item[].class))
                .toList();

        return new GroupInfo(role, group, items);
    }
}
