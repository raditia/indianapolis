package com.gdn.controller;

import com.gdn.BajinganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/bajingan")
public class BajinganController {
    @Autowired
    private BajinganService bajinganService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void executeBajingan() {
        bajinganService.executeBajingan();
    }
}
