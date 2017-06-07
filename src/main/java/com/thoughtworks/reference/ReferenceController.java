package com.thoughtworks.reference;

import com.thoughtworks.bulb.repository.BulbRepository;
import com.thoughtworks.reference.domain.BulbReference;
import com.thoughtworks.reference.dto.BulbReferenceDto;
import com.thoughtworks.reference.repository.ReferenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(allowCredentials = "true")
@RestController
@Slf4j
public class ReferenceController {

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private BulbRepository bulbRepository;

    @ResponseBody
    @RequestMapping(value = "/{userid}/bulbs/{uuid}/references", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BulbReference createReference(
            @PathVariable("uuid") String uuid,
            @PathVariable("userid") String userId,
            @RequestBody BulbReferenceDto referenceDto
    ){

        Optional<BulbReference> bulbReference = Optional.ofNullable(referenceRepository.findByUuid(referenceDto.getUuid()));
        if ( !bulbReference.isPresent() ) {
            BulbReference reference = BulbReference
                    .builder()
                    .reference(referenceDto.getReference())
                    .uuid(referenceDto.getUuid())
                    .userId(userId)
                    .build();

            BulbReference savedReference = referenceRepository.save(reference);
            bulbRepository.addReference(uuid, reference.getUuid());
            return savedReference;
        }

        return bulbReference.get();

    }

}
