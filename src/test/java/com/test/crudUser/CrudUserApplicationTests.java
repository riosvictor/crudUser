package com.test.crudUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.jayway.jsonpath.JsonPath;
import com.test.crudUser.dto.UserDTO;
import com.test.crudUser.endpoints.UserEndpoints;
import com.test.crudUser.entity.User;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SystemStubsExtension.class)
class CrudUserApplicationTests {

	private static final Logger log = Logger.getLogger(CrudUserApplication.class.getName());
	private String encodedString = Base64.getEncoder().encodeToString("banana:nanica123".getBytes());

	private LocalDateTime localDate = LocalDateTime.of(1991, 01, 01, 00, 00, 00);
	private ZonedDateTime zoneDate = localDate.atZone(ZoneId.of("America/Sao_Paulo"));


	private UserDTO userDTO = new UserDTO("12345678987", "Sr Teste da Silva", "Masculino"
			, "teste@teste.teste", new Date(zoneDate.toInstant().toEpochMilli()), "", "");

	private UserDTO changeUserDTO = new UserDTO("12345678987", "Mario da Silva", "Masculino"
			, "teste@teste.teste", new Date(zoneDate.toInstant().toEpochMilli()), "Lima", "Peru");

	@LocalServerPort
	private int port;

	@SystemStub
	private static EnvironmentVariables environmentVariables;

	@SystemStub
	private SystemProperties restoreSystemProperties;

	@Autowired
	private ObjectMapper mapperRequest;

	@Autowired
	private ObjectMapper mapperResponse;

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	static void setUp(){
		log.info("----------------------------------------------");
		log.info(">>>            INIT SUIT TESTES            <<<");
	}

	@Test
	@Order(1)
	public void getSource() throws Exception {
		mockMvc.perform(get("/source"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("frontend")))
				.andExpect(content().string(containsString("backend")));

		log.info("#1 - getSource");
	}

	@Test
	@Order(2)
	public void getUsersError() throws Exception {
		mockMvc.perform(get("/softplan"+UserEndpoints.USERS))
				.andExpect(status().isUnauthorized());

		log.info("#2 - getUsersError");
	}

	@Test
	@Order(3)
	public void getUsersSucess() throws Exception {
		ResultActions resultActions = mockMvc.perform
				(
						get("/softplan"+UserEndpoints.USERS)
								.header("Authorization", "Basic "+encodedString)

				)
				//.andDo(print())
				.andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		JSONArray jsonArray = JsonPath.read(contentAsString, "$");
		String count = String.valueOf(jsonArray.size());

		environmentVariables.set("COUNT_USERS", count);

		log.info("#3 - getUsersSucess :: "+count);
	}

	@Test
	@Order(4)
	public void createUser() throws Exception {
		mapperRequest.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapperRequest.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(userDTO);

		ResultActions resultActions = mockMvc.perform
				(
						post("/softplan"+UserEndpoints.USERS)
						.header("Authorization", "Basic "+encodedString)
						.content(requestJson)
						.contentType(MediaType.APPLICATION_JSON)

				)
				.andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		User user = mapperResponse.readValue(contentAsString, User.class);
		environmentVariables.set("USER_ID", user.getId());

		log.info("#4 - createUser :: "+user.getId());
	}

	@Test
	@Order(5)
	public void getUsersSucess2() throws Exception {
		mockMvc.perform
				(
						get("/softplan"+UserEndpoints.USERS)
								.header("Authorization", "Basic "+encodedString)

				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(
						Integer.parseInt(System.getenv("COUNT_USERS")) + 1
				)));

		log.info("#5 - getUsersSucess2 :: "+ (Integer.parseInt(System.getenv("COUNT_USERS")) + 1));
	}

	@Test
	@Order(6)
	public void alterUser() throws Exception {
		mapperRequest.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapperRequest.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(changeUserDTO);
		String id = System.getenv("USER_ID");

		mockMvc.perform
				(
						put("/softplan"+UserEndpoints.USERS+"/"+id)
								.header("Authorization", "Basic "+encodedString)
								.content(requestJson)
								.contentType(MediaType.APPLICATION_JSON)

				)
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.nome").value("Mario da Silva"))
				.andExpect(jsonPath("$.naturalidade").value("Lima"))
				.andExpect(jsonPath("$.nacionalidade").value("Peru"));

		log.info("#6 - alterUser:: "+ id);
	}

	@Test
	@Order(7)
	public void getUser() throws Exception {
		String id = System.getenv("USER_ID");

		mockMvc.perform
				(
						get("/softplan"+UserEndpoints.USERS+"/"+id)
								.header("Authorization", "Basic "+encodedString)
								.contentType(MediaType.APPLICATION_JSON)

				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Mario da Silva"))
				.andExpect(jsonPath("$.naturalidade").value("Lima"))
				.andExpect(jsonPath("$.nacionalidade").value("Peru"));

		log.info("#7 - getUser:: "+ id);
	}

	@Test
	@Order(8)
	public void deleteUser() throws Exception{
		String id = System.getenv("USER_ID");
		mockMvc.perform
				(
						delete("/softplan" + UserEndpoints.USERS+"/"+id)
								.header("Authorization", "Basic " + encodedString)

				)
				.andExpect(status().isOk());

		log.info("#8 - deleteUser :: "+ id);
	}

	@Test
	@Order(9)
	public void getUsersSucess3() throws Exception {
		mockMvc.perform
				(
						get("/softplan"+UserEndpoints.USERS)
								.header("Authorization", "Basic "+encodedString)

				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(
						Integer.parseInt(System.getenv("COUNT_USERS"))
				)));

		log.info("#9 - getUsersSucess3 :: "+ Integer.parseInt(System.getenv("COUNT_USERS")));
	}

}
