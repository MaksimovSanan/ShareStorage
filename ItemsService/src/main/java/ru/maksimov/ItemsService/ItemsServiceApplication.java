package ru.maksimov.ItemsService;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import ru.maksimov.ItemsService.dto.itemDto.NewItemDTO;
import ru.maksimov.ItemsService.dto.rentContractDto.NewRentContractDTO;
import ru.maksimov.ItemsService.models.RentContract;
import ru.maksimov.ItemsService.models.RentalItem;

@SpringBootApplication
@EnableDiscoveryClient
public class ItemsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemsServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		modelMapper.typeMap(NewRentContractDTO.class, RentContract.class)
				.addMappings(mapper -> {
					mapper.map(NewRentContractDTO::getRentalItemId, RentContract::setRentalItem);
				});

		modelMapper.typeMap(NewItemDTO.class, RentalItem.class)
				.addMappings(mapper -> mapper.skip(RentalItem::setId));


		return modelMapper;
	}
}
