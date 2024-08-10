package org.example.courzelo.serviceImpls.Forum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Forum.SubforumRepository;
import org.example.courzelo.services.Forum.ISubForumService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubForumServiceImpl implements ISubForumService {
    private final SubforumRepository repository;
    @Override
    public List<SubForum> getSubForums() {
        return repository.findAll();
    }

    @Override
    public List<SubForum> getUserSubs(User user) {
        return repository.findByUser(user);
    }

    @Override
    public void deleteSubForum(String id) {
        repository.deleteById(id);
    }

    @Override
    public SubForum getSubForum(String id) {
        return repository.findById(id).orElseThrow(()->new RuntimeException("Error SubServiceImp"));
    }

    @Override
    public SubForum saveSub(SubForum SubForum) {
        return repository.save(SubForum);
    }

    @Override
    public SubForum updateSubForum(SubForum SubForum) {
        return repository.save(SubForum);
    }

    @Override
    public SubForum updateSubForum1(String id, String name, String description) {
        SubForum sub = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sub not found"));
        sub.setName(name);
        sub.setDescription(description);

        return repository.save(sub);
    }
}
