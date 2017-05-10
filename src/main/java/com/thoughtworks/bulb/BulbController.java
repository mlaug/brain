package com.thoughtworks.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.dto.BulbDto;
import com.thoughtworks.bulb.dto.BulbLinkDto;
import com.thoughtworks.bulb.repository.BulbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Slf4j
public class BulbController {

    @Autowired
    BulbRepository bulbRepository;

    @ResponseBody
    @RequestMapping(value = "/bulbs", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Bulb createBulb(@RequestBody BulbDto bulbDto){

        // Idempotent is key
        Bulb bulbByUuid = bulbRepository.findByUuid(bulbDto.getUuid());
        if ( bulbByUuid != null){
            this.log.info("Updating bulb with UUID {}", bulbDto.getUuid());
            bulbByUuid.setSummary(bulbDto.getSummary());
            bulbByUuid.setTitle(bulbDto.getTitle());
            return bulbRepository.save(bulbByUuid);
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

}
