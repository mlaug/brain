package com.thoughtworks.reference;

import com.thoughtworks.reference.domain.Reference;
import com.thoughtworks.reference.dto.ReferenceDto;
import com.thoughtworks.reference.dto.ReferenceUploadDto;
import com.thoughtworks.reference.repository.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
public class ReferenceController {

    @Autowired
    ReferenceRepository referenceRepository;

    @ResponseBody
    @RequestMapping(value = "/reference", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ReferenceUploadDto createReference(@RequestBody ReferenceDto referenceDto){
        Reference reference = Reference.builder()
                .uuid(UUID.randomUUID().toString())
                .title(referenceDto.getTitle())
                .stereotype(referenceDto.getStereotype())
                .build();

        referenceRepository.save(reference);

        return ReferenceUploadDto.builder()
                .destination("/samson")
                .referenceId(reference.getUuid())
                .build();
    }

}
