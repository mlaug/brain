package com.thoughtworks.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.dto.BulbDto;
import com.thoughtworks.bulb.dto.BulbLinkDto;
import com.thoughtworks.bulb.exceptions.BulbAccessDeniedException;
import com.thoughtworks.bulb.exceptions.BulbNotFound;
import com.thoughtworks.bulb.repository.BulbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(allowCredentials = "true")
@RestController
@Slf4j
public class BulbController {

    @Autowired
    BulbRepository bulbRepository;

    @ResponseBody
    @RequestMapping(value = "/{user}/bulbs/{uuid}", method = RequestMethod.GET)
    public Bulb getBulb(@PathVariable("uuid") String uuid) throws BulbNotFound {
        Optional<Bulb> bulb = Optional.ofNullable(bulbRepository.findByUuid(uuid));
        bulb.orElseThrow(BulbNotFound::new);
        return bulb.get();
    }

    @ExceptionHandler(BulbNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void bulbNotFound(){ }

    @ResponseBody
    @RequestMapping(value = "/{user}/bulbs", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Bulb createBulb(@RequestBody BulbDto bulbDto, @PathVariable("user") String userId) throws BulbAccessDeniedException {

        // Idempotent is key
        Optional<Bulb> bulbByUuid = Optional.ofNullable(bulbRepository.findByUuid(bulbDto.getUuid()));
        if ( bulbByUuid.isPresent() ){
            if ( !bulbByUuid.get().getUserId().equals(userId) ){
                throw new BulbAccessDeniedException("bulb does not belong to you, motherfucker");
            }
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
                    .userId(userId)
                    .build();
            return bulbRepository.save(bulb);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/{user}/bulbs", method = RequestMethod.GET)
    public Iterable<Bulb> getBulbs(){
        return bulbRepository.findAll();
    }

    @RequestMapping(value = "/{user}/bulbs/{uuid}", method = RequestMethod.PUT)
    public void linkBulbs(@RequestBody BulbLinkDto bulbLinkDto,
                          @PathVariable("uuid") String uuid,
                          @PathVariable("user") String userId) throws BulbAccessDeniedException {
        Bulb bulbOne = bulbRepository.findByUuid(uuid);
        Bulb bulbTwo = bulbRepository.findByUuid(bulbLinkDto.getLink());
        if ( !bulbOne.getUserId().equals(userId) || !bulbTwo.getUserId().equals(userId) )
            throw new BulbAccessDeniedException("Both bulbs don't belong to the same user");
        bulbRepository.linkBulbs(uuid, bulbLinkDto.getLink());
    }

    @RequestMapping(value = "/{user}/bulbs/{uuid}", method = RequestMethod.DELETE)
    public void deleteBulb(@PathVariable("uuid") String uuid,
                           @PathVariable("user") String userId) throws BulbAccessDeniedException {
        Optional<Bulb> bulbByUuid = Optional.ofNullable(bulbRepository.findByUuid(uuid));
        if(bulbByUuid.isPresent()){
            Bulb bulb = bulbByUuid.get();
            if ( !bulb.getUserId().equals(userId) ){
                throw new BulbAccessDeniedException("this ain't the bulb you are looking for");
            }
            bulbRepository.delete(bulb);
        }
    }

    @ExceptionHandler(BulbAccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAccessDenied(){

    }

}
