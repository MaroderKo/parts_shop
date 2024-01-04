package com.autosale.shop.controller

import com.autosale.shop.model.JwtTokensDTO
import com.autosale.shop.model.User
import com.autosale.shop.service.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

internal class LoginControllerTest {
    private val service: AuthenticationService = Mockito.mock(AuthenticationService::class.java)
    private val controller: LoginController = LoginController(service)
    private val mock: MockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    private val mapper: ObjectMapper = ObjectMapper()

    @Test
    fun login() {
        val user = User(null, "admin", "admin", null)
        val tokens = JwtTokensDTO("access_token", "refresh_token")
        Mockito.`when`(service.loginByUserCredentials(user)).thenReturn(tokens)
        val result = mock.perform(
            MockMvcRequestBuilders.request(HttpMethod.POST, "/login")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(
                MockMvcResultMatchers.content()
                    .contentType(MediaType.APPLICATION_JSON)
            ).andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
        MatcherAssert.assertThat(result.response.contentAsString, `is`(mapper.writeValueAsString(tokens)))
    }

    @Test
    fun refreshTokens() {
        val tokens = JwtTokensDTO("access_token", "refresh_token")
        Mockito.`when`(service.loginByRefreshToken("token")).thenReturn(tokens)
        val result = mock.perform(
            MockMvcRequestBuilders.request(HttpMethod.POST, "/login/tokens/refresh")
                .content("{\"refreshToken\": \"token\"}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(
                MockMvcResultMatchers.content()
                    .contentType(MediaType.APPLICATION_JSON)
            ).andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
        MatcherAssert.assertThat(result.response.contentAsString, `is`(mapper.writeValueAsString(tokens)))
    }
}