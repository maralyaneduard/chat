package com.test.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.chat.controller.RoomController;
import com.test.chat.dto.RoomDto;
import com.test.chat.entity.Room;
import com.test.chat.mapper.RoomMapper;
import com.test.chat.service.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomService roomService;

    @Mock
    private RoomMapper roomMapper;

    @Before
    public void setup() {
        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    public void roomList() throws Exception {
        List<Room> roomList = new ArrayList<>();

        Room room1 = new Room();
        room1.setUsers(new HashSet<>());
        room1.setName("ROOM1");
        room1.setId(1);

        Room room2 = new Room();
        room2.setUsers(new HashSet<>());
        room2.setName("ROOM1");
        room2.setId(1);
        roomList.add(room1);
        roomList.add(room2);

        when(roomService.findAll()).thenReturn(roomList);

        this.mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/rooms/room-list"))
                .andExpect(model().attributeExists("rooms"));
        verify(roomService, times(1)).findAll();
    }

    @Test
    public void updateRoom() throws Exception {
        Room room1 = new Room();
        room1.setUsers(new HashSet<>());
        room1.setName("ROOM1");
        room1.setId(1);

        when(roomService.findById(1)).thenReturn(room1);
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room1);
        doNothing().when(roomService).updateRoom(any(Room.class));

        this.mockMvc.perform(post("/rooms/update")
                .param("_csrf", "value")
                .param("id", "1")
                .param("name", "name")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rooms"));

        verify(roomService, times(1)).findById(1);
        verify(roomService, times(1)).updateRoom(any());
    }

    @Test
    public void updateRoomUserNotFound() throws Exception {
        Room room1 = new Room();
        room1.setUsers(new HashSet<>());
        room1.setName("ROOM1");
        room1.setId(1);

        when(roomService.findById(1)).thenReturn(null);
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room1);
        doNothing().when(roomService).updateRoom(any(Room.class));

        this.mockMvc.perform(post("/rooms/update")
                .param("_csrf", "value")
                .param("id", "1")
                .param("name", "name")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/rooms/room-update"));

        verify(roomService, times(1)).findById(1);
        verify(roomService, times(0)).updateRoom(any());
        verify(roomMapper, times(0)).toRoom(any());
    }


    public static String asJsonString(final Object obj) {
        try {

            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
