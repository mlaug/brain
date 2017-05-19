package com.thoughtworks.reference;

import com.thoughtworks.bulb.repository.BulbRepository;
import com.thoughtworks.reference.domain.BulbReference;
import com.thoughtworks.reference.dto.BulbReferenceDto;
import com.thoughtworks.reference.repository.ReferenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Slf4j
public class ReferenceController {

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private BulbRepository bulbRepository;

    @ResponseBody
    @RequestMapping(value = "/bulbs/{uuid}/references", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BulbReference createReference(
            @PathVariable("uuid") String uuid,
            @RequestBody BulbReferenceDto referenceDto
    ){

        BulbReference reference = BulbReference
                .builder()
                .reference(referenceDto.getReference())
                .uuid(referenceDto.getUuid())
                .build();

        BulbReference savedReference = referenceRepository.save(reference);
        bulbRepository.addReference(uuid, reference.getUuid());

        return savedReference;
    }

    @ResponseBody
    @RequestMapping(value = "/reference/{refId}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImagePreviewOfReference(@RequestParam("refId") Long refId){
        BulbReference reference = referenceRepository.findOne(refId);
        return null;
    }

}
