package com.example.raceez.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {
	private String code;
	private String url_thumbnail;
	private String url_hd_watermark;
}
