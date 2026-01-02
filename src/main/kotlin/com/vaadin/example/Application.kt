package com.vaadin.example

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import com.vaadin.hilla.EndpointController
import com.vaadin.hilla.parser.jackson.JacksonObjectMapperFactory
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.inject.Named
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.MapperFeature
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule

@Theme("quarkus-hilla-starter")
class Application : AppShellConfigurator {

    @Produces
    @ApplicationScoped
    @Named(EndpointController.ENDPOINT_MAPPER_FACTORY_BEAN_QUALIFIER)
    fun objectMapperFactory(): JacksonObjectMapperFactory {
        class Factory : JacksonObjectMapperFactory.Json() {
            @Suppress("deprecation")
            override fun build(): ObjectMapper {
                // Emulate Spring default configuration
                val mapper = super.build() as JsonMapper;
                return mapper.rebuild().addModule(kotlinModule())
                    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                    .configure(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false
                    ).build()
            }
        }
        return Factory()
    }
}