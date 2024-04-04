package org.bansikah.bookjunittest2;


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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    public void createBook_success() throws Exception {
        Book book = new Book(1L,1L, "Atomic Habits", "Mastering habits", 5 );
        String json = objectWriter.writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
        Mockito.verifyNoMoreInteractions(bookRepository);

    }

    @Test
    public void createBook2_success() throws Exception {
        Book record = Book.builder()
                .id(1L)
                .bookId(1L)
                .name("Atomic Habists")
                .summary("Mastering habits")
                .rating(5)
                .build();

        Mockito.when(bookRepository.save(record)).thenReturn(record);

        String content = objectMapper.writeValueAsString(record);

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));
       // perform.andExpect(status().isOk());

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()) // Check if "id" field exists
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void updateBook_success() throws Exception {
    Book updateBookRecord = Book.builder()
            .bookId(1L)
            .name("Updated book")
            .summary("Updated summary")
            .rating(1)
            .build();

    Mockito.when(bookRepository.findById(book1.getBookId())).thenReturn(Optional.of(book1));
    Mockito.when(bookRepository.save(updateBookRecord)).thenReturn(updateBookRecord);

    String updatedetContent = objectWriter.writeValueAsString(updateBookRecord);

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedetContent));


        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").exists()) // Check if "id" field exists
                .andExpect(jsonPath("$.bookId").isNotEmpty()) // Check if "id" is not null
                .andExpect(jsonPath("$.name").value("Updated book"));
    }

    @Test
    public void deleteBookById_success() throws Exception {
        // Define the ID of the book to be found
        Long expectedId = 1L;
        when(bookRepository.findById(expectedId)).thenReturn(Optional.of(book1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(expectedId);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void deleteBookById_notFound() throws Exception {
        // Define the ID of the book to be found
        Long expectedId = 1L;
        when(bookRepository.findById(expectedId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Mockito.verify(bookRepository, Mockito.times(0)).deleteById(expectedId);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }
}
