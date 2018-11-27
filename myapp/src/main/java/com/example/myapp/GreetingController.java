package myapp;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.Iterable;
import java.util.Iterator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import com.microsoft.applicationinsights.TelemetryClient;

@Controller
public class GreetingController {

    @Autowired
    private GreetingRepository greetingRepository;
    @Autowired
    TelemetryClient telemetryClient; // This is for Application Insights

    @GetMapping("/greeting")
    public String greetingForm(@RequestParam(value="content", defaultValue="Default content") String content, Model model) {
        Greeting greeting = new Greeting();
        greeting.setContent(content);
        model.addAttribute("greeting", greeting);
        telemetryClient.trackTrace("Greeting Form page view.");
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        greetingRepository.save(greeting);
        Iterable<Greeting> greetings = greetingRepository.findAll();
        model.addAttribute("greetings", greetings);
        telemetryClient.trackTrace("Greeting Form submit.");
        return "result";
    }
}