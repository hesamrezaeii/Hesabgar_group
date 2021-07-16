package ir.ac.aut.hesabgar_group.domain.repo;

import ir.ac.aut.hesabgar_group.domain.document.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserInfoRepo extends MongoRepository<UserInfo, String> {
    UserInfo getUserInfoByUserName(String userName);
    UserInfo getUserInfoByEmailAddress(String emailAddress);
    UserInfo getUserInfoByTelephoneNumber(String telephoneNumber);
    UserInfo getUserInfoById(String id);

}
