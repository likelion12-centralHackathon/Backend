package com.likelion.timer.Youtube;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// 특정 검색어에 대한 12개의 검색 목록을 가져오는 API
@RestController
public class YoutubeApiController {

	@Value("${youtube.api.key}")
	private String apiKey;  // API 키를 외부에서 주입받음

	@Autowired
	private final RestTemplate restTemplate;

	public YoutubeApiController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostMapping("/api/v1/Youtube/keywordSearchData.do")
	public String keywordSearchData(@RequestParam("search") String search) {
		String apiUrl = "https://www.googleapis.com/youtube/v3/search";
		String url;

		try {
			url = apiUrl + "?key=" + URLEncoder.encode(apiKey, "UTF-8");
			url += "&part=snippet&type=video&maxResults=12&videoEmbeddable=true";
			url += "&q=" + URLEncoder.encode(search, "UTF-8");
			url += "&order=relevance";

			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			return extractVideoDetails(response.getBody());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "Encoding error occurred";
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occurred";
		}
	}

	private String extractVideoDetails(String responseBody) {
		StringBuilder result = new StringBuilder();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(responseBody);
			JsonNode items = root.path("items");

			for (JsonNode item : items) {
				String videoId = item.path("id").path("videoId").asText();
				String title = item.path("snippet").path("title").asText();
				result.append("Title: ").append(title)
					.append("\nURL: https://www.youtube.com/watch?v=").append(videoId)
					.append("\n\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error processing the response";
		}
		return result.toString();
	}
}