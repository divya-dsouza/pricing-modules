package org.divyad.controller;

import lombok.extern.slf4j.Slf4j;
import org.divyad.domain.PricingModel;
import org.divyad.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
public class PriceController {

    @Autowired
    PriceService priceService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("File") MultipartFile file) {
        log.info("Entering upload of PriceController");
        return priceService.upload(file);
    }

    @GetMapping(value = "/find/{field}")
    public List<PricingModel> find(@PathVariable(name = "field") String field,
                                   @RequestParam(name = "value") String value) {
        log.info("Entering upload of PriceController");
        return priceService.find(field, value);
    }
}
