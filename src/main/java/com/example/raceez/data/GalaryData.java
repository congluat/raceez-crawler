package com.example.raceez.data;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
@Component
public class GalaryData {

	private Integer offset;
	private Integer limit;
	private String url = "https://raceez.com/api/result/finisher-pix/events/61d6ec5409d021449044d914?offset=" + offset
			+ "&limit=" + limit;

	public GalaryData() {
		this.offset = 0;
		this.limit = 20;
	}

	public String getNextUrl() {
		String url = "https://raceez.com/api/result/finisher-pix/events/61d6ec5409d021449044d914?offset=" + offset
				+ "&limit=" + limit;
		this.offset = this.offset + this.limit;
		return url;
	}
}
