package com.lnu.coronacitybot.service.impl;

import com.lnu.coronacitybot.dao.UserDAO;
import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.entity.enums.SubscriptionRate;
import com.lnu.coronacitybot.model.Psid;
import com.lnu.coronacitybot.model.UserId;
import com.lnu.coronacitybot.model.incomming.ProfileInfo;
import com.lnu.coronacitybot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Value("${facebook.profile.url}")
    private String profileUrl;
    @Value("${facebook.token}")
    private String TOKEN;
    @Value("${facebook.unlink.url}")
    private String UNLINK_URL;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public User createIfNotExist(UserId chatId) {
        User user = userDAO.findByChatId(chatId.getId());
        if (user == null) {
            user = new User();
            user.setChatId(chatId.getId());
            user.setState(State.INITIAL);
            user.setIsSubscribed(Boolean.FALSE);
            ProfileInfo profileInfo = getProfileInfo(chatId.getId());
            user.setFirstName(profileInfo.getFirstName());
            user.setLastName(profileInfo.getLastName());
            if (!StringUtils.isEmpty(profileInfo.getLocale()))
                user.setLocale(profileInfo.getLocale());
            userDAO.save(user);
        }
        return user;
    }

    public void unlinkUserAccount(String id) {
        Psid psid = new Psid(id);
        restTemplate.postForObject(UNLINK_URL + TOKEN, psid, String.class);
    }

    public Optional<String> saveLoginAndPasswordAndGetAuthCode(String username, String password, String userID) {
        //process username and password and return authorization code if successful
        return Optional.empty();
    }

    private ProfileInfo getProfileInfo(String userId) {
        String url = profileUrl.replace("$user_id$", userId) + TOKEN;
        return restTemplate.getForObject(url, ProfileInfo.class);
    }

    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public void setState(User user, String state) {
        user.setState(state);
        save(user);
    }

    @Override
    public Set<User> getAllUsersWithActiveSubscription() {
        return userDAO.findAllByIsSubscribed(Boolean.TRUE);
    }

    @Override
    public void saveAll(Set<User> allUsersWithActiveSubscription) {
        userDAO.save(allUsersWithActiveSubscription);
    }
}
