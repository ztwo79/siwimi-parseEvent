package com.siwimi.webapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.ActivitySite;
import com.siwimi.webapi.parser.ParseWebsite;
import com.siwimi.webapi.repository.ActivityRepository;
import com.siwimi.webapi.repository.ActivitySiteRepository;
import com.siwimi.webapi.repository.LocationRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private ActivityRepository activityRep;
	
	@Autowired
	private LocationRepository locationRep;
	
	@Autowired
	private ActivitySiteRepository activitySiteRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<ActivitySite> sites = activitySiteRepository.findAll();		
		if (sites != null) {
			for (ActivitySite site : sites) {
				String classPath = "com.siwimi.webapi.parser." + site.getClassName();
				ParseWebsite parse = (ParseWebsite) ParseWebsite.class.getClassLoader().loadClass(classPath).newInstance();
				List<Activity> activities = parse.retrieve(site.getUrl());
				parse.saveActivity(activities, activityRep, locationRep);
			}
		}
		
		
	}

}
