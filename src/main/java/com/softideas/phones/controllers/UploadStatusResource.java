package com.softideas.phones.controllers;

import com.softideas.phones.models.UploadStats;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UploadStatusResource implements ResourceAssembler<UploadStats, Resource<UploadStats>> {
    @Override
    public Resource<UploadStats> toResource(UploadStats uploadStats) {
        return new Resource<>(uploadStats, linkTo(methodOn(Phone.class).getFileStatus(uploadStats.getFileRef())).withSelfRel());
    }
}
