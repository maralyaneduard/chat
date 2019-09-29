package com.test.chat;

import com.test.chat.entity.Room;
import com.test.chat.entity.User;
import com.test.chat.repository.MessageRepository;
import com.test.chat.repository.RoomRepository;
import com.test.chat.service.RoomService;
import com.test.chat.service.impl.RoomServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RoomRepository roomRepository;

    @Before
    public void setup() {
        // Process mock annotations
        MockitoAnnotations.initMocks(this);
        ((RoomServiceImpl)this.roomService).setRoomRepository(roomRepository);
        ((RoomServiceImpl)this.roomService).setMessageRepository(messageRepository);
    }

    @Test
    public void clearRoom() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("mail@mail.ru");
        user1.setName("name1");
        User user2 = new User();
        user2.setId(2);
        user2.setEmail("mail@mail.ru");
        user2.setName("name2");

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        Room room1 = new Room();
        room1.setUsers(users);
        room1.setName("ROOM1");
        room1.setId(1);

        when(roomRepository.getOne(any())).thenReturn(room1);
        Mockito.doNothing().when(messageRepository).removeByRoom(any(Room.class));
        roomService.clearRoom(1);

        Assert.assertEquals(0, room1.getUsers().size());
        Mockito.verify(roomRepository, Mockito.times(1)).getOne(1);
    }

    @Test
    public void clearRoomWrongUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("mail@mail.ru");
        user1.setName("name1");
        User user2 = new User();
        user2.setId(2);
        user2.setEmail("mail@mail.ru");
        user2.setName("name2");

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        Room room1 = new Room();
        room1.setUsers(users);
        room1.setName("ROOM1");
        room1.setId(1);

        when(roomRepository.getOne(any())).thenReturn(null);
        Mockito.doNothing().when(messageRepository).removeByRoom(any(Room.class));
        roomService.clearRoom(1);

        Assert.assertEquals(2, room1.getUsers().size());
        Mockito.verify(roomRepository, Mockito.times(1)).getOne(1);
        Mockito.verify(messageRepository, Mockito.times(0)).removeByRoom(any(Room.class));
    }
}
