package com.nuri.spring6restmvc.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nuri.spring6restmvc.entities.Beer;
import com.nuri.spring6restmvc.entities.Customer;
import com.nuri.spring6restmvc.model.BeerCSVRecord;
import com.nuri.spring6restmvc.model.BeerStyle;
import com.nuri.spring6restmvc.repositories.BeerRepository;
import com.nuri.spring6restmvc.repositories.CustomerRepository;
import com.nuri.spring6restmvc.services.BeerCsvService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final ObjectMapper objectMapper;
    private final BeerCsvService beerCsvService;

    public DataLoader(CustomerRepository customerRepository, BeerRepository beerRepository, ObjectMapper objectMapper, BeerCsvService beerCsvService) {
        this.customerRepository = customerRepository;
        this.beerRepository = beerRepository;
        this.objectMapper = objectMapper;
        this.beerCsvService = beerCsvService;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadCustomers();
        loadCsvData();
        loadBeers();
    }

    private void loadCsvData() throws FileNotFoundException {
        if (beerRepository.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

            List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

            recs.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
    }

    private void loadCustomers() throws IOException {
        if (customerRepository.count() == 0) {
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/customers.json")) {
                customerRepository.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Customer>>() {
                }));
            }
        }
    }

    private void loadBeers() throws IOException {
        if (beerRepository.count() == 0) {
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/beers.json")) {
                beerRepository.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Beer>>() {
                }));
            }
        }
    }
}
