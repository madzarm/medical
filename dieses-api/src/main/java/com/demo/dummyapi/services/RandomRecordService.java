package com.demo.dummyapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.demo.dummyapi.entity.Record;
import org.springframework.stereotype.Service;

@Service
public class RandomRecordService{
	List<String> DIESES = List.of("Acquired immunodeficiency syndrome (AIDS)", "Chlamydia infection", "COVID-19", "Coxsackievirus", "Ebola haemorrhagic fever", "Epidemic louse-borne typhus", "Gonorrhoea", "Hepatitis", "Human papillomavirus infection", "Invasive Haemophilus influenzae disease", "Louse-borne typhus", "Malaria", "Measles", "Pneumococcal disease", "Rabies", "Smallpox", "Syphilis", "Tetanus", "Tuberculosis", "Mononucleosis", "Zika virus disease");


	public List<Record> generateRecord() {
		List<Record> table = new ArrayList<>();
		for(int i = 0; i<10000; i++) {
			List diseases = new ArrayList();
			String name;
			for(int j = 0; j<(ThreadLocalRandom.current().nextInt(0,5)); j++) {
				name = DIESES.get(ThreadLocalRandom.current().nextInt(0, DIESES.size()));
				if (diseases.contains(name))
					System.out.println("duplicate");
				else
					diseases.add(name);
			}
			String listString = String.join(", ", diseases);
			var result = new Record(listString);
			result.setDiseases(listString);

			table.add(result);
		}
		System.out.println(table.size());
		return table;

	}

}
