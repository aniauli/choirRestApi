package restapi.choir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChoirMemberControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CHOIR_MEMBERS = "/choirmembers";

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void givenShowAllUrlControllerShowsAllMembers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(CHOIR_MEMBERS + "/showall")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenAddMemberUrlControllerAddsMember() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(CHOIR_MEMBERS + "/addmember")
                    .content(objectMapper.writeValueAsString(new ChoirMemberToAdd("testName", "testPhoneNumber")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertThat(NumberUtils.isCreatable(mvcResult.getResponse().getContentAsString())).isTrue();
    }

    @Test
    void givenUpdateMemberUrlWithValidIdControllerUpdatesMembersPhoneNumber() throws Exception {
        givenAddMemberUrlControllerAddsMember();

        mockMvc.perform(MockMvcRequestBuilders.put(CHOIR_MEMBERS + "/updatemember")
                    .content(objectMapper.writeValueAsString(
                        new ChoirMemberToUpdate(1, null, "testUpdatedPhoneNumber")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenUpdateMemberUrlWithValidIdControllerUpdatesMembersName() throws Exception {
        givenAddMemberUrlControllerAddsMember();

        mockMvc.perform(MockMvcRequestBuilders.put(CHOIR_MEMBERS + "/updatemember")
                .content(objectMapper.writeValueAsString(
                        new ChoirMemberToUpdate(1, "testUpdatedName", null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenUpdateMemberUrlWithInvalidIdControllerThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(CHOIR_MEMBERS + "/updatemember")
                    .content(objectMapper.writeValueAsString(
                            new ChoirMemberToUpdate(150, "testUpdatedName", "testUpdatedPhoneNumber")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenDeleteMemberUrlWithValidIdControllerDeletesMembersName() throws Exception {
        givenAddMemberUrlControllerAddsMember();
        givenAddMemberUrlControllerAddsMember();

        mockMvc.perform(MockMvcRequestBuilders.delete(CHOIR_MEMBERS + "/deletemember/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenDeleteMemberUrlWithInvalidIdControllerThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(CHOIR_MEMBERS + "/deletemember/20"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}