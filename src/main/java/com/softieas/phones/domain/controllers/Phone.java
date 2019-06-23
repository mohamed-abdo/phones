package com.softieas.phones.domain.controllers;

import com.softieas.phones.domain.services.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/phone")
public class Phone {
    private final static Logger LOGGER = LoggerFactory.getLogger(Phone.class);

    private final PhoneService phoneService;

    public Phone(@Autowired PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/importFile")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    ResponseEntity importFile(@RequestBody MultipartFile file) throws IOException {
        if (Objects.isNull(file))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file not found");

        LOGGER.debug("received file :{}, size:{}", file.getName(), file.getSize());

        var resource = file.getResource();
        var phonesStream = this.phoneService.parseInputStream(resource.getInputStream());
        var response = this.phoneService.importData(phonesStream)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.of(Optional.of(response));
    }
}
