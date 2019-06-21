package com.softieas.phones.domain.services;

import com.softieas.phones.domain.models.PhoneSheet;
import com.softieas.phones.domain.models.UploadStats;

import java.util.stream.Stream;

public interface PhoneService {
    UploadStats uploadFile(Stream<PhoneSheet> phoneSheetStream);

}
