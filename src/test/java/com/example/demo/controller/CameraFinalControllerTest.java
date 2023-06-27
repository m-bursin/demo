package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.converter.CameraSourceTokenToCameraFinalConverter;
import com.example.demo.service.CameraService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CameraController.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DemoApplication.class)
public class CameraFinalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CameraService cameraService;

    @SpyBean
    CameraSourceTokenToCameraFinalConverter converter;

    @Test
    public void getCameras() throws Exception {
        mockMvc.perform(get("/get-cameras"))
                .andExpect(status().isOk());
    }
}