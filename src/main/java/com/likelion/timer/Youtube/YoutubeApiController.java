
package com.likelion.timer.Youtube;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class YoutubeApiController {

	@Value("${youtube.api.key}")
	private String apiKey;  // API 키를 외부에서 주입받음

	private final RestTemplate restTemplate;

	public YoutubeApiController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(value="/api/v1/Youtube/keywordSearchData.do", method= RequestMethod.POST)
	public String keywordSearchData(@RequestParam("search") String search) {
		String apiUrl = "https://www.googleapis.com/youtube/v3/search";
		String url;

		try {
			url = apiUrl + "?key=" + URLEncoder.encode(apiKey, "UTF-8");
			url += "&part=snippet&type=video&maxResults=12&videoEmbeddable=true";
			url += "&q=" + URLEncoder.encode(search, "UTF-8");

			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			return response.getBody();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "Encoding error occurred";
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occurred";
		}
	}
}
