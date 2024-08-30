package com.vaadin.example

import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.hilla.BrowserCallable

@BrowserCallable
@AnonymousAllowed
class HelloEndpoint {

    fun sayHello(name: String) = "Hello ${name.ifEmpty { "stranger" }}"
}