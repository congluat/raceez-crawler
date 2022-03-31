package com.example.raceez.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagingData {

	private Integer total_item;
	private Integer total_page;
	private Integer current_page;
}
