package com.nuri.spring6restmvc.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nuri.spring6restmvc.configuration.DataLoader;
import com.nuri.spring6restmvc.entities.Beer;
import com.nuri.spring6restmvc.model.BeerStyle;
import com.nuri.spring6restmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // will only bring up spring data Jpa components.
@Import({DataLoader.class, BeerCsvServiceImpl.class, ObjectMapper.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByStyle() {
        Page<Beer> list = beerRepository.findAllByBeerStyle(BeerStyle.IPA, null);
        assertThat(list.getContent().size()).isEqualTo(547);
    }

    @Test
    void testGetBeerListByName() {
        Page<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);
        assertThat(list.getContent().size()).isEqualTo(336);
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () ->  {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("My Friend   klflllllllllllllllllsllslslllslslsllslsllsjsjsjsjsjsjsjsjjsjsjjsjsjsj")
                    .beerStyle(BeerStyle.GOSE)
                    .upc("234324222")
                    .price(new BigDecimal("11.99"))
                    .build());

            beerRepository.flush();
        });
    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My Friend")
                        .beerStyle(BeerStyle.GOSE)
                        .upc("234324222")
                        .price(new BigDecimal("11.99"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testBeerCount() throws IOException {
        // Load the beer repository with test data.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/beers.json")) {
            beerRepository.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Beer>>() {
            }));
        }

        // Get the saved entries back, and confirm that id is not null.
        List<Beer> beers = beerRepository.findAll();

        assertThat(beerRepository.count()).isGreaterThan(3L);
        assertThat(beers.get(0).getId()).isNotNull();
    }

}