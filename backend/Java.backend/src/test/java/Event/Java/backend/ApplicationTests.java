package Event.Java.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void eventsAreReturnedInFifoOrderAndCanBeRemoved() throws Exception {
		String firstId = createEvent("First event", "First description");
		String secondId = createEvent("Second event", "Second description");

		mockMvc.perform(get("/api/events"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(firstId))
				.andExpect(jsonPath("$[0].name").value("First event"))
				.andExpect(jsonPath("$[1].id").value(secondId))
				.andExpect(jsonPath("$[1].name").value("Second event"));

		mockMvc.perform(delete("/api/events/{id}", firstId))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/events"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(secondId));

		mockMvc.perform(delete("/api/events/{id}", secondId))
				.andExpect(status().isNoContent());
	}

	@Test
	void eventTitleIsRequiredAndEventDateIsNotPartOfTheRequest() throws Exception {
		mockMvc.perform(post("/api/events")
				.contentType("application/json")
				.content("""
						{"description":"Description without an event title"}
						"""))
				.andExpect(status().isBadRequest());

		MvcResult created = mockMvc.perform(post("/api/events")
				.contentType("application/json")
				.content("""
						{"name":"Event without a date","description":"No date is required"}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Event without a date"))
				.andExpect(jsonPath("$.description").value("No date is required"))
				.andExpect(jsonPath("$.createdAt").isNotEmpty())
				.andReturn();
		String id = extractId(created);
		mockMvc.perform(delete("/api/events/{id}", id)).andExpect(status().isNoContent());
	}

	private String createEvent(String name, String description) throws Exception {
		MvcResult result = mockMvc.perform(post("/api/events")
				.contentType("application/json")
				.content("""
						{"name":"%s","description":"%s"}
						""".formatted(name, description)))
				.andExpect(status().isCreated())
				.andReturn();
		return extractId(result);
	}

	private String extractId(MvcResult result) throws Exception {
		Matcher matcher = Pattern.compile("\"id\":\"([^\"]+)\"")
				.matcher(result.getResponse().getContentAsString());
		org.junit.jupiter.api.Assertions.assertTrue(matcher.find(), "Created event should have an id");
		return matcher.group(1);
	}
}
