package com.rvigo.logger.boot.starter

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/test")
    fun get() {
    }

    @GetMapping("/my/cool/path")
    fun getMyCoolPath() {
    }
}