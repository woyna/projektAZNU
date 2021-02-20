package org.bp.mediaorderingui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.bp.mediaorderingui.model.OrderMediaRequest;
import org.bp.mediaorderingui.model.OrderSummary;

@Controller
public class MediaOrderingController {
	
	@org.springframework.beans.factory.annotation.Value("${gateway}")
	private String gateway;
	
	String mediaURI = "http://" + gateway + "/api/media/order";

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@GetMapping("/")
	public String menuForm(Model model) {
		return "menu";
	}
	
	@GetMapping("/getOrder")
	public String getOrderForm(Model model) {
		model.addAttribute("order", new OrderSummary());
		return "getOrder";
	}

	@GetMapping("/orderMedia")
	public String orderMediaForm(Model model) {
		model.addAttribute("order", new OrderMediaRequest());
		return "orderMedia";
	}

	@PostMapping("/orderMedia")
	public String orderMedia(@ModelAttribute OrderMediaRequest omr, Model model) {
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			ResponseEntity<OrderSummary> re = restTemplate.postForEntity(
					mediaURI,
					omr,
					OrderSummary.class);
			
			model.addAttribute("orderSummary", re.getBody());
			return "result";
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			return "error";
		}
	}
	
	@PostMapping("/getOrder")
	public String getOrder(@ModelAttribute OrderSummary os, Model model) {
		RestTemplate restTemplate = new RestTemplate();
		String URI = mediaURI + "/" + os.getId();
		try {
			ResponseEntity<OrderSummary> re = restTemplate.getForEntity(URI, OrderSummary.class);
			model.addAttribute("orderSummary", re.getBody());
			return "result";
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			return "error";
		}
	}
}