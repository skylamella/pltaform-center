package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.AccountDO;
import org.springframework.data.repository.Repository;

/**
 * @author lamella
 * @description AccountRepo TODO
 * @since 2024-02-02 21:11
 */
public interface AccountRepo extends Repository<AccountDO, String> {

    AccountDO getAccountById(String id);

    AccountDO getAccountByMailOrPhoneOrUsername(String mail, String phone, String username);
}
