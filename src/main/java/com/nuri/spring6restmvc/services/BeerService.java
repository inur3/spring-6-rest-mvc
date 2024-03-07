package com.nuri.spring6restmvc.services;

import com.nuri.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    void updateBeerById(UUID beerId, Beer beer);

    List<Beer> listBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void deleteById(UUID beerId);

    /**
     * To patch a given value, check the incoming object from the request body. Check all properties, if a property
     * is not null, then update (patch) the existing object with this value.
     * @param beerId, id of beer object to be patched.
     * @param beer, beer object used to update existing object.
     *
     */
    void patchBeerById(UUID beerId, Beer beer);
}
