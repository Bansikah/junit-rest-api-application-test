package org.bansikah.bookjunittest2;

import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper= new ObjectMapper();
    ObjectWriter objectWriter = new ObjectMapper().writer();

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    Book book1 = new Book(1L,1L, "Atomic Habits", "Mastering habits", 5 );
    Book book2 = new Book(2L,2L, "Influence People", "Impacting Lives", 4 );
    Book book3 = new Book(3L,3L, "Rich Dad Poor Dad", "Assets and Liabillites", 3 );

    //Ensure that the application does not start the Tomcat server and it tests only the controller class
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllBooks_success() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(book1, book2, book3));


        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get book by ID - Success")
    public void testGetBookById_Success() throws Exception {
        // Define the ID of the book to be found
        Long expectedId = 1L;
        when(bookRepository.findById(expectedId)).thenReturn(Optional.of(book1));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", expectedId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedId, book1.getBookId());
        assertEquals("Atomic Habits", book1.getName());
        assertEquals(5, book1.getRating());
        assertEquals("Mastering habits", book1.getSummary());
    }

}
