package com.nuri.spring6restmvc.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nuri.spring6restmvc.repositories.BeerRepository;
import com.nuri.spring6restmvc.repositories.CustomerRepository;
import com.nuri.spring6restmvc.services.BeerCsvService;
import com.nuri.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.xml.crypto.Data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DataLoaderTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;


    BeerCsvService beerCsvService;


    DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        beerCsvService = new BeerCsvServiceImpl();
        dataLoader = new DataLoader(customerRepository, beerRepository,
                new ObjectMapper().registerModule( new JavaTimeModule()), beerCsvService);
    }

    @Test
    void testRunMethod() throws Exception {
        dataLoader.run(); // Or can provide run with null as an arg

        assertThat(beerRepository.count()).isGreaterThan(4L);
        assertThat(customerRepository.count()).isGreaterThan(1L);
    }
}