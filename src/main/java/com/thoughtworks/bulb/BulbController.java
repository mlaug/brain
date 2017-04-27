package com.thoughtworks.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.dto.BulbDto;
import com.thoughtworks.bulb.repository.BulbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
public class BulbController {

    @Autowired
    BulbRepository bulbRepository;

    @ResponseBody
    @RequestMapping(value = "/bulbs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Bulb createBulb(@RequestBody BulbDto bulbDto){
        Bulb bulb = Bulb.builder()
                .summary(bulbDto.getSummary())
                .title(bulbDto.getTitle())
                .uuid(bulbDto.getUuid())
                .build();

        // Idempotent
        Bulb bulbByUuid = bulbRepository.findByUuid(bulbDto.getUuid());
        if ( bulbByUuid != null){
            this.log.info("Not creating bulb with same UUID {} to ensure idemptotence", bulbDto.getUuid());
            return bulbByUuid;
        }

        Bulb savedBulb = bulbRepository.save(bulb);

        // TODO: refactor with optional, using jackson jdk8 features
        // TODO: possible without if case?
        if ( bulbDto.getParentBulbUuid() != null ){
            bulbRepository.linkParentToChild(bulbDto.getParentBulbUuid(), savedBulb.getUuid());
        }

        return savedBulb;
    }

    @ResponseBody
    @RequestMapping(value = "/bulbs", method = RequestMethod.GET)
    public Bulb getBulbs(){
        // TODO: currently we just getting one of the root elements
        List<Bulb> roots = bulbRepository.findRoots();
        // return bulbRepository.findOne(roots.get(roots.size() - 1).getId());
        return bulbRepository.findOne(0L);
    }

}
