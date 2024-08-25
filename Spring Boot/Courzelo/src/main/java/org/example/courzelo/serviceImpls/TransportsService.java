package org.example.courzelo.serviceImpls;

import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Transports;
import org.example.courzelo.repositories.TransportsRepository;
import org.example.courzelo.services.ITransportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransportsService implements ITransportsService {

    @Autowired
    private TransportsRepository transportsRepository;
    @Override
    public List<Transports> retrieveAllTransports() {
        return transportsRepository.findAll() ;
    }

    @Override
    public Transports retrieveTransport(String BlocId) {
        return transportsRepository.findById(BlocId).get();
    }

    @Override
    public Transports addTransports(Transports c) {
        return transportsRepository.save(c);
    }

    @Override
    public void removeTransports(String BlocId) {
        transportsRepository.deleteById(BlocId);

    }

    @Override
    public Transports modifyTransports(Transports transports) {
        return null;
    }

    @Override
    public Long GetNumberOfTransports() {
        return transportsRepository.count();
    }
}
