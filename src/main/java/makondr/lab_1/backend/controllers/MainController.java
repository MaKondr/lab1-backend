package makondr.lab_1.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import makondr.lab_1.backend.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class MainController {
    private static final String path = "data.txt";
    private int counter = 4;
    private List<Map<String, String>> messages = new ArrayList<>() {{
        add(new HashMap<>() {{
            put("id", "1");
            put("message", "First message");
        }});
        add(new HashMap<>() {{
            put("id", "2");
            put("message", "Second message");
        }});
        add(new HashMap<>() {{
            put("id", "3");
            put("message", "Third message");
        }});
    }};

    @GetMapping
    public List<Map<String, String>> getMessages() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getMessage(@PathVariable String id) {
        return getMessageById(id);
    }

    @PostMapping
    public Map<String, String> addMessage(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        writeToJson(messages);
        return message;
    }

    private Map<String, String> getMessageById(String id) {
        return messages.stream()
                .filter(m -> m.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    private void writeToJson(List<Map<String, String>> messages) {
        try {
            new ObjectMapper().writeValue(new File(path), messages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
