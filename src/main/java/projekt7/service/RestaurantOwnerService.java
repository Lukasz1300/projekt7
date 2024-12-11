package projekt7.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import projekt7.entity.RestaurantOwner;
import projekt7.repository.RestaurantOwnerRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class RestaurantOwnerService {

    private final RestaurantOwnerRepository ownerRepository;

    public Optional<RestaurantOwner> findOwnerById(Long id) {
        return ownerRepository.findById(id);
    }

    public List<RestaurantOwner> findAllOwners() {
        return ownerRepository.findAll();  // Zwraca wszystkich właścicieli
    }


}
