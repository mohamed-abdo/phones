package com.softideas.phones.domain.controllers;

import com.softideas.phones.domain.models.PhoneNumber;
import com.softideas.phones.domain.models.PhoneSheet;
import com.softideas.phones.domain.services.PhoneService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/phones")
public class Phone {
    private final static Logger LOGGER = LoggerFactory.getLogger(Phone.class);

    private final PhoneService phoneService;
    private final UploadStatusResource uploadStatusResource;
    private final PhoneNumberResource phoneNumberResource;


    public Phone(@Autowired PhoneService phoneService, @Autowired UploadStatusResource uploadStatusResource, @Autowired PhoneNumberResource phoneNumberResource) {
        this.phoneService = phoneService;
        this.uploadStatusResource = uploadStatusResource;
        this.phoneNumberResource = phoneNumberResource;
    }

    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity ping() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/importFile")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    HttpEntity<?> importFile(@RequestBody MultipartFile file) throws IOException, URISyntaxException {
        if (Objects.isNull(file))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file not found");

        LOGGER.debug("received file :{}, size:{}", file.getName(), file.getSize());

        var resource = file.getResource();
        var fileChecksum = DigestUtils.md2Hex(resource.getInputStream());
        var phonesStream = this.phoneService.parseInputStream(resource.getInputStream());
        var uploadStatsResource = this.phoneService.importData(fileChecksum, phonesStream)
                .map(this.uploadStatusResource::toResource)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (uploadStatsResource.getContent().isNewFile())
            return ResponseEntity.created(new URI(uploadStatsResource.getId().getHref())).body(uploadStatsResource);
        else
            return ResponseEntity.ok(uploadStatsResource);
    }

    @GetMapping("/file/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    HttpEntity<?> getFileStatus(@PathVariable UUID fileId) {
        LOGGER.debug("get file stats :{}", fileId);
        if (Objects.isNull(fileId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fileId is missing");
        var stats = phoneService.getImportedFileStats(fileId)
                .map(this.uploadStatusResource::toResource)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/validate/{number}")
    public @ResponseBody
    HttpEntity<?> validate(@PathVariable String number) {
        LOGGER.debug("validate phone number:{}", number);
        if (Objects.isNull(number))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "number is missing");
        var normalizedNumber = phoneService.tryToFixNumber(new PhoneSheet("-1", number));
        var phoneNumber = PhoneNumber
                .builder()
                .number(normalizedNumber.getNumber())
                .fixedNumber(normalizedNumber.getFixedNumber())
                .phoneNumberStatus(normalizedNumber.getPhoneNumberStatus())
                .rejectionReason(normalizedNumber.getRejectionReason())
                .fixer(Optional.ofNullable(normalizedNumber.getPhoneNumberFixer()).map(Enum::name).orElse("n/a"))
                .build();
        return ResponseEntity.ok(phoneNumberResource.toResource(phoneNumber));
    }
}
