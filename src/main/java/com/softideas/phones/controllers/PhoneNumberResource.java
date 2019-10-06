package com.softideas.phones.controllers;

import com.softideas.phones.models.PhoneNumber;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PhoneNumberResource implements ResourceAssembler<PhoneNumber, Resource<PhoneNumber>> {
    @Override
    public Resource<PhoneNumber> toResource(PhoneNumber phoneNumber) {
        return new Resource<>(phoneNumber,
                linkTo(methodOn(Phone.class).validate(phoneNumber.getNumber())).withSelfRel());
    }
}
