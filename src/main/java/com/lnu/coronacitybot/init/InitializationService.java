package com.lnu.coronacitybot.init;

import com.lnu.coronacitybot.entity.enums.Postbacks;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.MessagesHolder;
import com.lnu.coronacitybot.model.outgoing.DomainWhiteList;
import com.lnu.coronacitybot.model.outgoing.buttons.GetStartedButton;
import com.lnu.coronacitybot.model.outgoing.buttons.StartedButtonRequest;
import com.lnu.coronacitybot.model.outgoing.menu.ContextMenuSettingRequest;
import com.lnu.coronacitybot.model.outgoing.menu.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class InitializationService {
    @Value("${refresh.welcome.message:true}")
    private Boolean refreshWelcomeMessage;
    @Value("${refresh.menu:true}")
    private Boolean refreshMenu;
    @Value("${facebook.welcome.message.url}")
    private String welcomeMessageUrl;
    @Value("${facebook.token}")
    private String token;
    @Value("${facebook.setwhitelisting.url}")
    private String WHITELISTING_URL;
    @Value("${facebook.set_started_button.url}")
    private String getStartedButtonURL;
    @Value("${system.base.url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MessagesHolder messagesHolder;


    @Transactional
    public void tryToRefreshWelcomeMessage() {
        StartedButtonRequest request = new StartedButtonRequest();
        request.setGetStarted(new GetStartedButton(Postbacks.INIT));
        restTemplate.postForObject(getStartedButtonURL + token, request,
                StartedButtonRequest.class);
    }

    private void setMenu() {
        if (refreshMenu) {
            List<MenuAction> actions = new ArrayList<>();
            //add menu items
            actions.add(MenuAction.createPayloadMenuAction("From the beginning",
                    Postbacks.INIT));
            ContextMenuSettingRequest request = ContextMenuSettingRequest.getFullRequest(actions);
            restTemplate.postForObject(welcomeMessageUrl + token, request, String.class);
        }
    }

    @PostConstruct
    public void init() {
        tryToRefreshWelcomeMessage();
        setMenu();
    }

    @PostConstruct
    public void setDomainWhiteList() {
        DomainWhiteList domainWhiteList = DomainWhiteList.createDomainWhiteListing();
        domainWhiteList.addDomain(baseUrl);
        domainWhiteList.setAddActionType();
        String s = restTemplate.postForObject(WHITELISTING_URL + token, domainWhiteList, String.class);

    }
}
