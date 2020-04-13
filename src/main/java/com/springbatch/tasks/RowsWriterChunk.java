package com.springbatch.tasks;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.springbatch.entity.KycCustomerData;

public class RowsWriterChunk implements ItemWriter<KycCustomerData> {

	@Override
	public void write(List<? extends KycCustomerData> items) throws Exception {
		//ItemWriter is mandatory for using Chunk oriented job approach

	}

}
