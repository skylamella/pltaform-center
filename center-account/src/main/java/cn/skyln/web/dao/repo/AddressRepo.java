package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.AddressDO;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @author lamella
 * @description AddressRepo TODO
 * @since 2024-04-02 22:59
 */
public interface AddressRepo extends Repository<AddressDO, String> {

    AddressDO getAddressDOByIdAndUserId(String id, String userId);

    AddressDO getAddressDOByUserIdAndDefaultStatus(String userId, Integer defaultStatus);

    List<AddressDO> findAddressDOSByUserId(String userId);
}
