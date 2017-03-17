package com.thoughtworks.bulb;

import com.thoughtworks.bulb.dto.BulbDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController("/bulb")
public class BulbController {

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BulbDto createBulb(@RequestBody BulbDto bulb){
        return bulb;
    }

}
