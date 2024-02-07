package ru.maksimov.imageserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maksimov.imageserver.models.ItemImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemsImageRepository extends JpaRepository<ItemImage, Integer> {
    List<ItemImage> findByItemId(Integer itemId);
}
