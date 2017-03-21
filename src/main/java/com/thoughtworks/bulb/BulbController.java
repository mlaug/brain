package com.thoughtworks.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.dto.BulbDto;
import com.thoughtworks.bulb.repository.BulbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class BulbController {

    @Autowired
    BulbRepository bulbRepository;

    @ResponseBody
    @RequestMapping(value = "/bulb", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Bulb createBulb(@RequestBody BulbDto bulbDto){
        Bulb bulb = Bulb.builder()
                .summary(bulbDto.getSummary())
                .title(bulbDto.getTitle())
                .build();
        bulbRepository.save(bulb);
        return bulb;
    }

}
