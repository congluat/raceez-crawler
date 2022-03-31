package com.example.raceez.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.raceez.data.GalaryData;
import com.example.raceez.data.ImageData;
import com.example.raceez.data.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class CrawlerService {

	private final RestTemplate restTemplate;

	private final GalaryData galaryData;

	@EventListener(ApplicationReadyEvent.class)
	public void process() throws MalformedURLException, IOException, InterruptedException {
		String folder = "/Users/luatnguyen/Downloads/dlut_raceez/";
		Integer page = 775;
		galaryData.setOffset(page*galaryData.getLimit());
		ObjectMapper mapper = new ObjectMapper();
		while (true) {
			
			
			String url = galaryData.getNextUrl();
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			ObjectNode node = (ObjectNode) mapper.readTree(response.getBody());
			ResponseData data = mapper.readValue(node.get("result").toString(), ResponseData.class);
			
			log.info("Downloading Page {}", data.getPaging().getCurrent_page());

			ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);
			CountDownLatch latch = new CountDownLatch(data.getData().size());
			for (ImageData img : data.getData()) {
				WORKER_THREAD_POOL.submit(() -> {
					try {
						try (InputStream in = new URL(img.getUrl_hd_watermark()).openStream()) {
							Files.copy(in, Paths.get(folder + img.getCode() + ".jpg"));
							log.info("Download image: {}", img.getCode());
						}
						latch.countDown();
					} catch (Exception e) {
						Thread.currentThread().interrupt();
					}
				});

			}

			latch.await(10, TimeUnit.SECONDS);

			if (data.getPaging().getCurrent_page().equals(data.getPaging().getTotal_page())) {
				break;
			}
		}
		log.info("Completed");
	}

}
