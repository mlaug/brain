package com.thoughtworks.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.dto.BulbDto;
import com.thoughtworks.bulb.dto.BulbLinkDto;
import com.thoughtworks.bulb.exceptions.BulbNotFound;
import com.thoughtworks.bulb.repository.BulbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@Slf4j
public class BulbController {

    @Autowired
    BulbRepository bulbRepository;

    @ResponseBody
    @RequestMapping(value = "/bulbs/{uuid}", method = RequestMethod.GET)
    public Bulb getBulb(@PathVariable("uuid") String uuid) throws BulbNotFound {
        Optional<Bulb> bulb = Optional.ofNullable(bulbRepository.findByUuid(uuid));
        bulb.orElseThrow(BulbNotFound::new);
        return bulb.get();
    }

    @ExceptionHandler(BulbNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void bulbNotFound(){ }

    @ResponseBody
    @RequestMapping(value = "/bulbs", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Bulb createBulb(@RequestBody BulbDto bulbDto){

        // Idempotent is key
        Optional<Bulb> bulbByUuid = Optional.ofNullable(bulbRepository.findByUuid(bulbDto.getUuid()));
        if ( bulbByUuid.isPresent() ){
            this.log.info("Updating bulb with UUID {}", bulbDto.getUuid());
            bulbByUuid.get().setSummary(bulbDto.getSummary());
            bulbByUuid.get().setTitle(bulbDto.getTitle());
            return bulbRepository.save(bulbByUuid.get());
        }
        else {
            this.log.info("Updating bulb with UUID {}", bulbDto.getUuid());
            Bulb bulb = Bulb.builder()
                    .summary(bulbDto.getSummary())
                    .title(bulbDto.getTitle())
                    .uuid(bulbDto.getUuid())
                    .build();
            return bulbRepository.save(bulb);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/bulbs", method = RequestMethod.GET)
    public Iterable<Bulb> getBulbs(){
        return bulbRepository.findAll();
    }

    @RequestMapping(value = "/bulbs/{uuid}", method = RequestMethod.PUT)
    public void linkBulbs(@RequestBody BulbLinkDto bulbLinkDto, @PathVariable("uuid") String uuid){
        bulbRepository.linkBulbs(uuid, bulbLinkDto.getLink());
    }

    @RequestMapping(value = "/bulbs/{uuid}", method = RequestMethod.DELETE)
    public void deleteBulb(@PathVariable("uuid") String uuid){
        Optional<Bulb> bulbByUuid = Optional.ofNullable(bulbRepository.findByUuid(uuid));
        bulbByUuid.ifPresent(bulb -> bulbRepository.delete(bulb));
    }


}
