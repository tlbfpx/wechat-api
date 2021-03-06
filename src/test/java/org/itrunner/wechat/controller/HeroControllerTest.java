package org.itrunner.wechat.controller;

import org.itrunner.wechat.base.WithMockOAuth2User;
import org.itrunner.wechat.domain.Hero;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.itrunner.wechat.util.JsonUtils.asJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HeroControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockOAuth2User
    public void crudSuccess() throws Exception {
        Hero hero = new Hero();
        hero.setName("Jack");

        // add hero
        mvc.perform(post("/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json("{'id':11, 'name':'Jack', 'createBy':'123456789'}"));

        // update hero
        hero.setId(11l);
        hero.setName("Jacky");
        mvc.perform(put("/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json("{'name':'Jacky'}"));

        // find heroes by name
        mvc.perform(get("/heroes/?name=m").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // get hero by id
        mvc.perform(get("/heroes/11").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json("{'name':'Jacky'}"));

        // delete hero successfully
        mvc.perform(delete("/heroes/11").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // delete hero
        mvc.perform(delete("/heroes/9999")).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockOAuth2User
    void addHeroValidationFailed() throws Exception {
        Hero hero = new Hero();
        mvc.perform(post("/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}
