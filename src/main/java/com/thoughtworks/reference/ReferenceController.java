package com.thoughtworks.reference;

import com.thoughtworks.reference.dto.ReferenceDto;
import com.thoughtworks.reference.dto.ReferenceUploadDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
public class ReferenceController {

    @ResponseBody
    @RequestMapping(value = "/reference", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ReferenceUploadDto createReference(@RequestBody ReferenceDto referenceDto){
        return ReferenceUploadDto.builder()
                .destination("/samson")
                .referenceId(UUID.randomUUID().toString())
                .build();
    }

}
