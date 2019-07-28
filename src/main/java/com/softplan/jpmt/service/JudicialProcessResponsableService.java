package com.softplan.jpmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.jpa.repository.JudicialProcessResponsableRepository;

@Service
public class JudicialProcessResponsableService {

	@Autowired
	private JudicialProcessResponsableRepository judicialProcessResponsableRepository;

	public JudicialProcessResponsable getJudicialProcessResponsableByJudicialProcessId(long id) {
		JudicialProcessResponsable judicialProcessResponsable = judicialProcessResponsableRepository.getOne(id);
		return judicialProcessResponsable;
	}
	
	public JudicialProcessResponsable persistJudicialProcess(JudicialProcessResponsable judicialProcessResponsable) {
		
		return judicialProcessResponsableRepository.save(judicialProcessResponsable);
	}
	
//	public void deleteById(long id) {
//        // Retrieve the movie with this ID
//		JudicialProcess judicialProcess = judicialProcessRepository.getOne(id);
//        if (judicialProcess != null) {
//            try {
//               
//                judicialProcess.getResponsables().forEach(responsable -> {
//                    responsable.getJudicialProcess().remove(judicialProcess);
//                });
//
//                judicialProcessRepository.delete(judicialProcess);
//                
//            } catch (Exception e) {//TODO tirar este catch genérico
//                e.printStackTrace();
//            }
//        }
//    }
	

}
