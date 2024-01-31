package ru.maksimov.webclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.NewRentContract;
import ru.maksimov.webclient.models.RentContract;
import ru.maksimov.webclient.models.User;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/rent")
public class RentController {
    private final RestTemplate restTemplate;

    @Autowired
    public RentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}")
    public String getContractInfo(@PathVariable("id") int id, Model model) {
        RentContract rentContract = restTemplate.getForObject("http://ITEMSSERVICE/rent/" + id, RentContract.class);
        model.addAttribute("rentContract", rentContract);
        return "contracts/contractInfo";
    }

    @PostMapping("/{id}/decide")
    public String decideContract(@PathVariable("id") int id,
                                 @ModelAttribute RentContract rentContract,
                                 Principal principal) {

        String updateContractUrl = "http://ITEMSSERVICE/rent/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RentContract> requestEntity = new HttpEntity<>(rentContract, headers);

        ResponseEntity<RentContract> responseEntity = restTemplate.exchange(
                updateContractUrl,
                HttpMethod.PATCH,
                requestEntity,
                RentContract.class); // Указываем ожидаемый тип ответа

        System.out.println("Contract was updated: " + responseEntity.getBody());

        return "redirect:/rent/" + id;
    }

    @PostMapping("/submitRentContract")
    public String submitRent(@ModelAttribute NewRentContract newRentContract, Principal principal){

        // MDA TRASH..
        newRentContract.setReservedFrom(
                LocalDateTime.parse(
                        newRentContract.getReservedFromDateFromForm() + "T" +
                                (newRentContract.getReservedFromTimeFromForm().isEmpty() ?
                                        "23:59":
                                        newRentContract.getReservedFromTimeFromForm()
                                )
                )
        );
        newRentContract.setReservedTo(
                LocalDateTime.parse(
                        newRentContract.getReservedToDateFromForm() + "T" +
                                (newRentContract.getReservedToTimeFromForm().isEmpty()?
                                        "23:59":
                                        newRentContract.getReservedToTimeFromForm()
                                )
                )
        );

        User user = restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);

        newRentContract.setBorrowerId(user.getId());

        String addContractUrl = "http://ITEMSSERVICE/rent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<NewRentContract> requestEntity = new HttpEntity<>(newRentContract, headers);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(addContractUrl, requestEntity, Void.class);

        System.out.println("New RentContract was added to db: " + newRentContract);
        return "redirect:/item/" + newRentContract.getRentalItemId();
    }
}
