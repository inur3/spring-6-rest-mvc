package com.nuri.spring6restmvc.services;

import com.nuri.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {

    public List<BeerCSVRecord> convertCSV(File csvFile);

}
