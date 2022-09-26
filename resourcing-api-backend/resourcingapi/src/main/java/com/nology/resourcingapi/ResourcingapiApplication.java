package com.nology.resourcingapi;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.entity.Temp;

@SpringBootApplication
public class ResourcingapiApplication {

//	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		SpringApplication.run(ResourcingapiApplication.class, args);
		
//		Job job = new Job();
//		job.setId(101L);
//		job.setAssigned(false);
//		job.setName("cleaning");
//		job.setStartDate(new Date(1532516399000L));
//		job.setEndDate(new Date(1532516499000L));
////		job.setTemp(null);
//		
//		Temp t = new Temp();
//		t.setId(201L);
//		t.setFirstName("Tom");
//		t.setLastName("Sawyer");
////		t.setJobs((List<Job>) job);
//		
//		Configuration config = new Configuration().configure().addAnnotatedClass(Temp.class).addAnnotatedClass(Job.class);
//		ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
//		SessionFactory sf = config.buildSessionFactory(registry);
//		Session session = sf.openSession();
//		
//		session.beginTransaction();
//		
//		session.save(job);
//		session.save(t);
//		
//		session.getTransaction().commit();
	}

}
