package com.nuri.spring6restmvc.mappers;

import com.nuri.spring6restmvc.entities.Beer;
import com.nuri.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);

}
