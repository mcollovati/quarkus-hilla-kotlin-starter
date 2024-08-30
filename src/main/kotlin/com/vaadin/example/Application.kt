package com.vaadin.example

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import com.vaadin.hilla.EndpointController
import com.vaadin.hilla.parser.jackson.JacksonObjectMapperFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.inject.Named

@Theme("quarkus-hilla-starter")
class Application : AppShellConfigurator{

    @Produces
    @ApplicationScoped
    @Named(EndpointController.ENDPOINT_MAPPER_FACTORY_BEAN_QUALIFIER)
    fun objectMapperFactory(): JacksonObjectMapperFactory {
        class Factory : JacksonObjectMapperFactory.Json() {
            @Suppress("deprecation")
            override fun build(): ObjectMapper {
                // Emulate Spring default configuration
                return super.build()
                    .registerModules(kotlinModule())
                    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                    .configure(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false
                    )
            }
        }
        return Factory()
    }
}