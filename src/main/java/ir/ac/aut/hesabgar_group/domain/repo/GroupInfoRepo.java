package ir.ac.aut.hesabgar_group.domain.repo;

import ir.ac.aut.hesabgar_group.domain.document.GroupInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupInfoRepo extends MongoRepository<GroupInfo, String> {
    GroupInfo getGroupInfoById(String id);
}
