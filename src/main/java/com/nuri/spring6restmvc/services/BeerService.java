package com.nuri.spring6restmvc.services;

import com.nuri.spring6restmvc.model.BeerDTO;
import com.nuri.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                            Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Boolean deleteById(UUID beerId);

    /**
     * To patch a given value, check the incoming object from the request body. Check all properties, if a property
     * is not null, then update (patch) the existing object with this value.
     *
     * @param beerId, id of beer object to be patched.
     * @param beer,   beer object used to update existing object.
     * @return
     */
    Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer);
}
